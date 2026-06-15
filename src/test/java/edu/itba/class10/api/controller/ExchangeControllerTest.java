package edu.itba.class10.api.controller;

import edu.itba.class10.converter.api.domain.Currency;
import edu.itba.class10.converter.api.domain.SingleConversionRequest;
import edu.itba.class10.domain.entity.money.MoneyAmount;
import edu.itba.class10.domain.usecases.exchangerate.CurrencyConverter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ExchangeControllerTest {

	@Mock
	private CurrencyConverter currencyConverter;

	@Test
	void testSingleRequest() {
		// Given
		final var requestedCurrency = edu.itba.class10.domain.entity.money.Currency.EUR;
		final var expectedAmount = MoneyAmount.create(requestedCurrency, BigDecimal.valueOf(87.0));
		final var requestedAmount = MoneyAmount.create(edu.itba.class10.domain.entity.money.Currency.USD,
				BigDecimal.valueOf(100));

		final var request = new SingleConversionRequest().to(Currency.EUR).from(Currency.USD).amount(100.0);
		final var controller = new ExchangeController(this.currencyConverter);

		when(this.currencyConverter.convert(requestedAmount, requestedCurrency)).thenReturn(expectedAmount);

		// When
		final var result = controller.convertToSingleCurrency(request);

		// Then
		Assertions.assertEquals(expectedAmount.amount().doubleValue(), result.getBody().getAmount());
		Assertions.assertEquals(expectedAmount.currency().name(), result.getBody().getCurrency().name());
	}
}
