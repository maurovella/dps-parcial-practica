package edu.itba.class10;

import edu.itba.class10.application.HttpExchangeRateProvider;
import edu.itba.class10.application.usecase.CurrencyConverterImpl;
import edu.itba.class10.domain.entity.money.Currency;
import edu.itba.class10.domain.entity.money.MoneyAmount;
import edu.itba.class10.domain.usecases.exchangerate.CurrencyConverter;
import edu.itba.class10.infrastructure.exchangerate.http.UniRestHttpClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {CurrencyConverterImpl.class, HttpExchangeRateProvider.class, UniRestHttpClient.class})
class CurrencyConverterIT extends IntegrationTest {

	@Autowired
	private CurrencyConverter currencyConverter;

	@Test
	void givenTheRequestFailsThenTheConversionShouldBeZero() {

		final var moneyAmount = MoneyAmount.create(Currency.EUR, 100);
		final var result = this.currencyConverter.convert(moneyAmount, Currency.USD);
		assertThat(result, is(MoneyAmount.create(Currency.USD, 10200)));
	}
}
