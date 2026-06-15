package edu.itba.class10.application.usecase;

import edu.itba.class10.domain.entity.money.Currency;
import edu.itba.class10.domain.entity.money.MoneyAmount;
import edu.itba.class10.domain.model.HistoricalExchangeRateRequest;
import edu.itba.class10.domain.model.HistoricalRate;
import edu.itba.class10.domain.provider.ExchangeRateProvider;
import edu.itba.class10.domain.usecases.exchangerate.GetHistoricalRates;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
public class GetHistoricalRatesImpl implements GetHistoricalRates {

	private final ExchangeRateProvider exchangeRateProvider;

	@Override
	public HistoricalRate getHistoricalRate(final MoneyAmount moneyAmount, final List<Currency> targetCurrencies,
			final LocalDate date) {
		if (targetCurrencies.isEmpty()) {
			return HistoricalRate.empty();
		}

		final var historicalExchangeRateResponse = this.exchangeRateProvider
				.getHistoricalExchangeRate(HistoricalExchangeRateRequest.builder().baseCurrency(moneyAmount.currency())
						.targetCurrencies(targetCurrencies).date(date).build());

		return historicalExchangeRateResponse.toHistoricalRate();
	}
}
