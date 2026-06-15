package edu.itba.class10.application;

import edu.itba.class10.domain.common.entities.config.ExchangeApiConfig;
import edu.itba.class10.domain.entity.money.Currency;
import edu.itba.class10.domain.model.ExchangeRateResponse;
import edu.itba.class10.domain.model.HistoricalExchangeRateRequest;
import edu.itba.class10.domain.model.HistoricalExchangeRateResponse;
import edu.itba.class10.domain.model.LatestExchangeRateRequest;
import edu.itba.class10.domain.provider.ExchangeRateProvider;
import edu.itba.class10.http.base.HttpClient;
import edu.itba.class10.http.base.HttpRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HttpExchangeRateProvider implements ExchangeRateProvider {

	private final HttpClient httpClient;

	private final ExchangeApiConfig exchangeApiConfiguration;

	@Override
	public ExchangeRateResponse getExchangeRate(final LatestExchangeRateRequest request) {
		final var baseUrl = this.exchangeApiConfiguration.defaultConfig().baseUrl();
		final var currencies = request.targetCurrencies().stream().map(Currency::toString)
				.collect(Collectors.joining(","));
		final var response = this.httpClient
				.get(HttpRequest.<ExchangeRateResponse>builder().endpoint(baseUrl).headers(this.getHeaders())
						.params(Map.of("base_currency", request.baseCurrency().toString(), "currencies", currencies))
						.responseType(ExchangeRateResponse.class).onError(ExchangeRateResponse.empty()).build());

		return response.data();
	}

	private Map<String, String> getHeaders() {
		final var apiKey = this.exchangeApiConfiguration.defaultConfig().apiKey();
		return Map.of("accept", "application/json", "apikey", apiKey);
	}

	@Override
	public HistoricalExchangeRateResponse getHistoricalExchangeRate(final HistoricalExchangeRateRequest request) {
		final var baseUrl = this.exchangeApiConfiguration.defaultConfig().baseUrl();
		final var currencies = request.targetCurrencies().stream().map(Currency::toString)
				.collect(Collectors.joining(","));
		final var response = this.httpClient
				.get(HttpRequest.<HistoricalExchangeRateResponse>builder().endpoint(baseUrl).headers(this.getHeaders())
						.params(Map.of("base_currency", request.baseCurrency().toString(), "currencies", currencies))
						.responseType(HistoricalExchangeRateResponse.class)
						.onError(HistoricalExchangeRateResponse.empty()).build());

		return response.data();
	}

}
