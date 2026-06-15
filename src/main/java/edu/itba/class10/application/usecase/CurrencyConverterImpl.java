package edu.itba.class10.application.usecase;

import edu.itba.class10.domain.entity.money.Currency;
import edu.itba.class10.domain.entity.money.MoneyAmount;
import edu.itba.class10.domain.model.LatestExchangeRateRequest;
import edu.itba.class10.domain.persistence.ExchangePersistence;
import edu.itba.class10.domain.persistence.SingleConversionEntity;
import edu.itba.class10.domain.provider.ExchangeRateProvider;
import edu.itba.class10.domain.usecases.exchangerate.CurrencyConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CurrencyConverterImpl implements CurrencyConverter {
	private final ExchangeRateProvider exchangeRateProvider;
	private final ExchangePersistence exchangePersistence;

	public MoneyAmount convert(final MoneyAmount moneyAmount, final Currency toCurrency) {
		final var result = this.convert(moneyAmount, List.of(toCurrency)).getFirst();
		this.exchangePersistence.save(this.createSingleConversionEntity(moneyAmount, result));
		return result;
	}

	private SingleConversionEntity createSingleConversionEntity(final MoneyAmount from, MoneyAmount to) {
		return new SingleConversionEntity(LocalDate.now(), from, to);
	}

	public List<MoneyAmount> convert(final MoneyAmount moneyAmount, final List<Currency> targetCurrencies) {
		if (targetCurrencies.isEmpty()) {
			return Collections.emptyList();
		}

		final var exchangeRates = this.exchangeRateProvider.getExchangeRate(LatestExchangeRateRequest.builder()
				.baseCurrency(moneyAmount.currency()).targetCurrencies(targetCurrencies).build());
		return targetCurrencies.stream().map(toCurrency -> this.makeConversion(exchangeRates.getExchange(toCurrency),
				moneyAmount.amount(), toCurrency)).toList();
	}

	private MoneyAmount makeConversion(final double exchangeRate, final BigDecimal amount, final Currency toCurrency) {
		final var convertedAmount = amount.multiply(BigDecimal.valueOf(exchangeRate));
		return MoneyAmount.create(toCurrency, convertedAmount);
	}
}
