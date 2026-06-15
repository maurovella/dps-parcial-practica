# dps-parcial-practica

Repositorio de práctica para el parcial de **Desarrollo Profesional de Software**.

El código base es un **conversor de monedas** (exchange) implementado con
**arquitectura hexagonal / Clean Architecture** sobre Spring Boot. Sirve como
línea base limpia: cada **Pull Request** abierto sobre este repo introduce, a
propósito, una serie de violaciones a SOLID, a la arquitectura y/o a Clean Code.

## Cómo practicar

1. Andá a la pestaña **Pull Requests** del repo en GitHub.
2. Abrí un PR y leé su descripción (lo que el PR *dice* que hace).
3. Revisá el **diff** como si fueras el reviewer: identificá qué principios
   rompe, en qué capa y cómo lo corregirías.
4. Escribí tu review (podés comentar línea por línea en GitHub).
5. Recién después, compará con el solucionario (`.parcial/SOLUCIONES.md`,
   que queda solo en tu copia local y no se publica).

## Arquitectura de la línea base

```
edu.itba.class10
├── domain/                 # Núcleo. NO depende de frameworks ni de infra.
│   ├── entity/money/        #   Value objects: MoneyAmount, Currency
│   ├── model/               #   Modelos de request/response del dominio
│   ├── usecases/            #   PUERTOS de entrada (interfaces): CurrencyConverter, GetHistoricalRates
│   ├── provider/            #   PUERTO de salida: ExchangeRateProvider
│   └── persistence/         #   PUERTO de salida: ExchangePersistence
├── application/            # Casos de uso (implementan los puertos de entrada)
│   ├── usecase/             #   CurrencyConverterImpl, GetHistoricalRatesImpl
│   └── HttpExchangeRateProvider
├── infrastructure/         # ADAPTADORES de salida (implementan puertos de salida)
│   └── persistence/         #   SQL (JPA) y NoSQL (Mongo), HTTP clients
├── api/                    # ADAPTADOR de entrada (driving): controllers + mappers
└── boot/                   # Composition root: wiring y configuración de Spring
```

**Regla de dependencias:** las flechas apuntan siempre hacia adentro.
`api` y `infrastructure` dependen de `application` y `domain`; `application`
depende de `domain`; **`domain` no depende de nadie**.

> Nota: el repo es para revisar PRs. Compilar requiere JDK 25 y Maven.
