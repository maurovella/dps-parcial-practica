package edu.itba.class10.api.controller;

import edu.itba.class10.ExchangeApi;
import edu.itba.class10.api.controller.mapper.SingleConversionMapper;
import edu.itba.class10.application.HttpExchangeRateProvider;
import edu.itba.class10.converter.api.domain.ConvertedAmount;
import edu.itba.class10.converter.api.domain.SingleConversionRequest;
import edu.itba.class10.domain.entity.money.Currency;
import edu.itba.class10.domain.model.LatestExchangeRateRequest;
import edu.itba.class10.domain.usecases.exchangerate.CurrencyConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ExchangeController implements ExchangeApi {

	private final CurrencyConverter currencyConverter;
	private final HttpExchangeRateProvider httpExchangeRateProvider;
	private final SingleConversionMapper singleConversionMapper = new SingleConversionMapper();

	@Override
	@RequestMapping("v1/exchange/convert")
	public ResponseEntity<ConvertedAmount> convertToSingleCurrency(final SingleConversionRequest request) {
		final var moneyAmount = this.singleConversionMapper.toMoneyAmount(request);
		final var requestedCurrency = this.singleConversionMapper.toCurrency(request);
		final var result = this.currencyConverter.convert(moneyAmount, requestedCurrency);
		final var convertedAmount = this.singleConversionMapper.toConvertedAmount(result);
		return ResponseEntity.ok(convertedAmount);
	}

	@RequestMapping("v1/exchange/convert-bulk")
	public ResponseEntity<List<ConvertedAmount>> convertBulk(@RequestParam final String from,
			@RequestParam final double amount, @RequestParam final List<String> to) {
		try {
			final Currency base = Currency.valueOf(from);
			final List<Currency> targets = to.stream().map(Currency::valueOf).toList();
			final var rates = this.httpExchangeRateProvider.getExchangeRate(
					LatestExchangeRateRequest.builder().baseCurrency(base).targetCurrencies(targets).build());
			final List<ConvertedAmount> out = new ArrayList<>();
			for (final Currency c : targets) {
				final double rate = rates.getExchange(c);
				final double converted = amount * rate;
				out.add(new ConvertedAmount()
						.currency(edu.itba.class10.converter.api.domain.Currency.valueOf(c.name()))
						.amount(converted));
			}
			return ResponseEntity.ok(out);
		} catch (final Exception e) {
			return ResponseEntity.ok(new ArrayList<>());
		}
	}

}
