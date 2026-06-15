package edu.itba.class10.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.util.Map;

@Getter
@AllArgsConstructor
public class HistoricalExchangeRateResponse {

	private Map<String, ExchangeRateResponse> data;

	public static HistoricalExchangeRateResponse empty() {
		return new HistoricalExchangeRateResponse(Map.of());
	}

	public HistoricalRate toHistoricalRate() {
		final var rates = this.data.entrySet().stream().findFirst()
				.orElse(Map.entry("1970-01-01", ExchangeRateResponse.empty()));
		final var date = LocalDate.parse(rates.getKey());
		return HistoricalRate.builder().date(date).exchangeRateResponse(rates.getValue()).build();
	}
}
