package edu.itba.class10.application;

import edu.itba.class10.domain.model.ExchangeRateResponse;
import edu.itba.class10.domain.model.HistoricalExchangeRateRequest;
import edu.itba.class10.domain.model.HistoricalExchangeRateResponse;
import edu.itba.class10.domain.model.LatestExchangeRateRequest;
import edu.itba.class10.domain.provider.ExchangeRateProvider;
import org.springframework.stereotype.Service;

@Service
public class FixerExchangeRateProvider implements ExchangeRateProvider {

	@Override
	public ExchangeRateResponse getExchangeRate(final LatestExchangeRateRequest request) {
		// segundo proveedor (fixer.io)
		return ExchangeRateResponse.empty();
	}

	@Override
	public HistoricalExchangeRateResponse getHistoricalExchangeRate(final HistoricalExchangeRateRequest request) {
		return HistoricalExchangeRateResponse.empty();
	}
}
