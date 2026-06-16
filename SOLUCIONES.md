# Solucionario — práctica de review de PRs

> **Spoiler.** Leé esto recién después de hacer tu propio review de cada PR.
> Para cada PR vas a encontrar: lo que el PR dice que hace, los principios que
> rompe (SOLID / arquitectura hexagonal-clean / Clean Code) y cómo se corrige.

## Cómo abordar un review (checklist mental)

1. **¿Qué dice que hace vs. qué hace?** Leé la descripción y después el diff.
2. **¿En qué capa toca?** (`domain` / `application` / `infrastructure` / `api` / `boot`).
3. **Regla de dependencias:** ¿alguna flecha apunta hacia afuera? (sobre todo: ¿`domain` empezó a depender de un framework?).
4. **SOLID:** SRP (¿una sola razón para cambiar?), OCP (¿para extender hay que modificar?), LSP, ISP, DIP (¿depende de abstracciones o de concretos?).
5. **Clean Code:** nombres, números mágicos, métodos largos, duplicación (DRY), manejo de errores, código muerto, inmutabilidad, concurrencia.
6. **Comportamiento:** ¿cambia algún efecto observable (side-effects, status HTTP, precisión)?

| PR | Tema aparente | Dificultad | Familias que rompe |
|----|---------------|-----------|--------------------|
| 01 | Cache | Media | SRP, DIP, Clean Code, bug de comportamiento |
| 02 | Endpoint bulk | Media-alta | Arquitectura, DIP, SRP, DRY, manejo de errores |
| 03 | JPA en dominio | Alta | Regla de dependencias, DDD/VO, SRP |
| 04 | Multi-proveedor | Alta | OCP, DIP, ubicación de responsabilidad |
| 05 | Fee/descuento | Media | SRP, OCP, fuga de abstracción, Clean Code |

---

## PR #01 — `pr-01-cache-conversion`
**Dice:** cachea conversiones para no pegarle siempre a la API.
**Toca:** `application/usecase/CurrencyConverterImpl.java`.

### Qué rompe
- **SRP:** el caso de uso ahora, además de orquestar la conversión, gestiona una caché. Dos razones para cambiar.
- **DIP / arquitectura:** la caché es un `HashMap static` hardcodeado dentro del *application layer*. La estrategia de caché es un detalle de infraestructura; no debería incrustarse en el caso de uso ni acoplarse a una implementación concreta. Correspondería un **puerto de caché** inyectado o un **decorator** del puerto `CurrencyConverter`.
- **Concurrencia / Clean Code:** `static` mutable compartido en un bean *singleton* de Spring. `HashMap` **no es thread-safe** (bajo concurrencia puede corromperse). **Sin evicción ni TTL** ⇒ crece para siempre (*memory leak*). La **clave por concatenación de strings** (`""+...`) es frágil/ambigua. El comentario explica el "qué", no el "porqué".
- **Bug de comportamiento (lo más grave):** en *cache hit* retorna **sin** ejecutar `exchangePersistence.save(...)` ⇒ se pierde el registro de la conversión. Además, las **tasas cambian con el tiempo** y este cache no expira: devuelve cotizaciones viejas (*stale*) indefinidamente — inaceptable para un conversor.
- **Testing:** estado `static` compartido entre tests ⇒ tests acoplados y *flaky*.

### Cómo se corrige
Sacar la caché del caso de uso. Opciones: (a) un **decorator** `CachingCurrencyConverter implements CurrencyConverter` que envuelve al real; (b) cachear las **tasas** a nivel del `ExchangeRateProvider` con **expiración corta**. Usar una estructura *thread-safe* con TTL, y **no** romper el efecto de persistencia.

---

## PR #02 — `pr-02-bulk-fat-controller`
**Dice:** agrega `/v1/exchange/convert-bulk` para convertir a varias monedas.
**Toca:** `api/controller/ExchangeController.java`.

### Qué rompe
- **Arquitectura hexagonal:** el *controller* (adaptador de entrada) mete **lógica de negocio** (`amount * rate`, armado del request al proveedor) y habla **directo con el provider**, salteando el caso de uso `CurrencyConverter`. La regla de negocio debe vivir en `domain`/`application`; el controller sólo adapta HTTP ↔ dominio.
- **DIP:** inyecta la **clase concreta** `HttpExchangeRateProvider` (de `application`) en vez del puerto / caso de uso. La capa de entrada queda acoplada a una implementación.
- **DRY:** duplica la conversión (multiplicar por la tasa, armar `LatestExchangeRateRequest`) que **ya existe** en `CurrencyConverterImpl.convert(MoneyAmount, List<Currency>)`.
- **SRP:** el controller ahora valida, orquesta, calcula y mapea.
- **Manejo de errores:** `catch (Exception)` genérico que **traga todo** y devuelve **200 OK con lista vacía**. Oculta fallas, status incorrecto, imposible distinguir "sin resultados" de "error".
- **Precisión:** opera en `double`; el dominio usa `BigDecimal`.

### Cómo se corrige
El caso bulk debe ir en el **caso de uso** (que ya tiene `convert(..., List<Currency>)`). El controller sólo mapea request→dominio, llama a `currencyConverter` y mapea la respuesta. Errores: dejar que los maneje el *exception handler* global con status apropiados. No inyectar el provider concreto.

---

## PR #03 — `pr-03-jpa-en-dominio`
**Dice:** persistir reutilizando las entidades de dominio anotadas con JPA (menos mappers).
**Toca:** `domain/entity/money/MoneyAmount.java`, `domain/persistence/SingleConversionEntity.java`.

### Qué rompe
- **Regla de dependencias (Clean/Hexagonal):** el `domain` ahora importa `jakarta.persistence.*`. El **núcleo pasa a depender de un framework de infraestructura** ⇒ se invierte la dirección de las dependencias. Es *la* violación central de clean/hexagonal.
- **DDD / Value Object:** `MoneyAmount` deja de ser **inmutable**: gana `id`, constructor vacío y *setters* (`@Data`) que JPA exige. Se **pierde la invariante** del constructor original (`amount.setScale(2, HALF_UP)`). Y darle `@Id` a un *value object* es una contradicción: los VO se comparan **por valor**, no por identidad.
- **SRP:** la entidad de dominio mezcla reglas de negocio con **detalles de mapeo** (tablas, columnas, estrategias de generación, cascadas).
- **Modelo dudoso:** `@ManyToOne` a `MoneyAmount` trata un VO como entidad referenciable; semánticamente sería `@Embeddable`, no una tabla aparte.
- **Duplicación/colisión:** ya existen `SingleConversionSqlEntity` + mapper en `infrastructure`. Ahora hay **dos modelos de persistencia** para lo mismo. El PR "ahorra" un mapper rompiendo justamente la separación que protege al dominio.

### Cómo se corrige
El dominio queda **libre de frameworks**. El mapeo vive en `infrastructure` (como ya estaba: `SqlEntity` + mapper). Si molesta el *boilerplate*, usar un mapper declarativo (MapStruct) en infra — **nunca** anotar el dominio. `MoneyAmount` sigue siendo un VO inmutable con su invariante de escala.

---

## PR #04 — `pr-04-multi-provider`
**Dice:** soporta múltiples proveedores eligiendo por `EXCHANGE_PROVIDER`.
**Toca:** `application/usecase/CurrencyConverterImpl.java`, nuevo `application/FixerExchangeRateProvider.java`.

### Qué rompe
- **OCP:** para agregar/cambiar un proveedor hay que **modificar** `CurrencyConverterImpl` (el `if/else` de `resolveProvider`). El diseño original **ya cumplía OCP**: el caso de uso dependía del puerto `ExchangeRateProvider` y el proveedor se elegía por *wiring*. Este PR retrocede.
- **DIP:** el caso de uso ahora conoce **implementaciones concretas** (`instanceof HttpExchangeRateProvider`, `instanceof FixerExchangeRateProvider`). El `application` no debe conocer adaptadores concretos.
- **Polimorfismo roto:** reemplaza polimorfismo (ya disponible vía el puerto) por un **switch por tipo** — ejemplo de manual de violación de OCP.
- **Responsabilidad mal ubicada:** la **selección del adaptador** es responsabilidad del *composition root* (`boot`/config), no del caso de uso. Leer `System.getenv` dentro de la lógica mezcla configuración con dominio y mata la testeabilidad.
- **Clean Code:** condicionales repetidos, strings mágicos (`"fixer"`, `"currencyapi"`).

### Cómo se corrige
El caso de uso depende de **un** `ExchangeRateProvider` (la abstracción). La implementación se elige en `boot` con `@Profile` / `@ConditionalOnProperty` / `@Primary`, o un `@Bean` que devuelva el provider según config. Agregar un proveedor = **nueva clase** que implementa el puerto + wiring, **sin tocar** el caso de uso.

---

## PR #05 — `pr-05-fee-descuento`
**Dice:** aplica fee/descuentos según el tipo de cliente.
**Toca:** `application/usecase/CurrencyConverterImpl.java`, `domain/usecases/exchangerate/CurrencyConverter.java`.

### Qué rompe
- **SRP:** el caso de uso de **conversión** ahora también calcula **pricing comercial** (fees/descuentos). Dos reglas de negocio distintas; cambiar la política de precios obliga a tocar la conversión.
- **OCP:** agregar un tipo de cliente nuevo obliga a editar el `if/else` encadenado. Debería ser polimorfismo/estrategia (`PricingPolicy`) o reglas configurables.
- **Fuga de abstracción / Primitive Obsession:** `String userType` en el **puerto del dominio**. Cualquier string compila, pero `"vip"`/`"GOLD"` caen al `else` silencioso (cobran fee de NORMAL). Debería ser un tipo del dominio (`enum CustomerType` o un VO).
- **DRY:** re-implementa el *fetch* de la tasa y la conversión que ya existen en `convert(List)`/`makeConversion`.
- **Clean Code:** números mágicos (`0.05`, `0.02`, `0.03`, `10000`); nombres sin sentido (`x`, `tmp`, `res`); método largo con `if` anidados; **código muerto** comentado; **`double` para dinero** (pérdida de precisión, debería ser `BigDecimal`).
- **Bug latente:** `userType` desconocido cae al `else` y cobra fee de NORMAL en silencio.

### Cómo se corrige
Separar el *pricing* en su propio componente (estrategia por `CustomerType`), **tipar** el cliente con enum/VO, **reutilizar** la conversión existente, usar `BigDecimal`, nombrar las constantes y borrar el código muerto.
