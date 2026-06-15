package edu.itba.class10.domain.model;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record HistoricalRate(LocalDate date, ExchangeRateResponse exchangeRateResponse) {

	public static HistoricalRate empty() {
		return new HistoricalRate(LocalDate.now(), ExchangeRateResponse.empty());
	}
}
