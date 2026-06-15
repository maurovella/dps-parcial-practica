package edu.itba.class10.boot.persistence;

import edu.itba.class10.domain.entity.money.Currency;
import edu.itba.class10.domain.entity.money.MoneyAmount;
import edu.itba.class10.domain.persistence.SingleConversionEntity;
import edu.itba.class10.infrastructure.persistence.relational.SingleConversionSqlEntity;
import edu.itba.class10.infrastructure.persistence.relational.SqlExchangePersistence;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@DataJpaTest
@ActiveProfiles("sql")
class SqlConversionRepositoryTest {

	@Autowired
	private SqlExchangePersistence sqlExchangePersistence;

	@Test
	void savesConversionEntity() {
		final var entity = new SingleConversionSqlEntity();
		entity.setFromAmount(BigDecimal.valueOf(100));
		entity.setFromCurrency("USD");
		entity.setToAmount(BigDecimal.valueOf(90));
		entity.setToCurrency("EUR");
		entity.setDate(Timestamp.valueOf(LocalDateTime.now()));

		final var fromAmount = MoneyAmount.create(Currency.USD, BigDecimal.valueOf(100));
		final var toAmount = MoneyAmount.create(Currency.EUR, BigDecimal.valueOf(90));
		final var singleConversionEntity = new SingleConversionEntity(LocalDate.of(2025, 10, 28), fromAmount, toAmount);
		this.sqlExchangePersistence.save(singleConversionEntity);

		final var persisted = this.sqlExchangePersistence.findById(1L);
		assertThat(persisted.isPresent(), is(true));
		assertThat(persisted.get(), is(singleConversionEntity));
	}
}
