package edu.itba.class10;

import edu.itba.class10.application.usecase.CurrencyConverterImpl;
import edu.itba.class10.domain.entity.money.MoneyAmount;
import edu.itba.class10.domain.model.ExchangeRateResponse;
import edu.itba.class10.domain.persistence.ExchangePersistence;
import edu.itba.class10.domain.provider.ExchangeRateProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static edu.itba.class10.domain.entity.money.Currency.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CurrencyConverterTest {

	@Mock
	private ExchangeRateProvider mockExchangeRateProvider;

	@Spy
	private ExchangePersistence exchangePersistence;

	@Test
	void givenTheRequestFailsThenTheConversionShouldBeZero() {
		// Given
		when(this.mockExchangeRateProvider.getExchangeRate(any())).thenReturn(ExchangeRateResponse.empty());
		final var converter = new CurrencyConverterImpl(this.mockExchangeRateProvider, this.exchangePersistence);

		// When
		final var result = converter.convert(MoneyAmount.create(EUR, 100), USD);

		// Then
		final var expected = MoneyAmount.create(USD, 0);
		assertThat(result, is(expected));
	}

	@Test
	void givenAValidConversionRateThenConversionShouldSucceed() {
		// Given
		when(this.mockExchangeRateProvider.getExchangeRate(any()))
				.thenReturn(new ExchangeRateResponse(Map.of(USD, 102.0)));
		final var converter = new CurrencyConverterImpl(this.mockExchangeRateProvider, this.exchangePersistence);

		// When
		final var result = converter.convert(MoneyAmount.create(EUR, 100), USD);

		// Then
		final var expected = MoneyAmount.create(USD, 10200);
		verify(this.exchangePersistence, times(1)).save(any());
		assertThat(result, is(expected));
	}

	@Test
	void givenAValidConversionRateThenConversionShouldSucceedForTwoCurrencies() {
		// Given
		when(this.mockExchangeRateProvider.getExchangeRate(any()))
				.thenReturn(new ExchangeRateResponse(Map.of(USD, 102.0, JPY, 765.0)));
		final var converter = new CurrencyConverterImpl(this.mockExchangeRateProvider, this.exchangePersistence);

		// When
		final var result = converter.convert(MoneyAmount.create(EUR, 100), List.of(USD, JPY));

		// Then
		final var expected = List.of(MoneyAmount.create(USD, 10200), MoneyAmount.create(JPY, 76500));
		assertThat(result, hasSize(2));
		assertThat(result, containsInAnyOrder(expected.toArray()));
	}

}