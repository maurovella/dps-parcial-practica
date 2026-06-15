package edu.itba.class10.domain.model;

import edu.itba.class10.domain.entity.money.Currency;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExchangeRateResponse {

	private Map<Currency, Double> data;

	public static ExchangeRateResponse empty() {
		return new ExchangeRateResponse(Map.of()) {
			@Override
			public double getExchange(Currency toCurrency) {
				return 0;
			}
		};
	}

	public double getExchange(final Currency toCurrency) {
		return this.data.getOrDefault(toCurrency, 0.0);
	}
}
