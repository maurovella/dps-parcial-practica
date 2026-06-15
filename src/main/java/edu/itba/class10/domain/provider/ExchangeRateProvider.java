package edu.itba.class10.domain.provider;

import edu.itba.class10.domain.model.ExchangeRateResponse;
import edu.itba.class10.domain.model.HistoricalExchangeRateRequest;
import edu.itba.class10.domain.model.HistoricalExchangeRateResponse;
import edu.itba.class10.domain.model.LatestExchangeRateRequest;

public interface ExchangeRateProvider {

	ExchangeRateResponse getExchangeRate(LatestExchangeRateRequest request);

	HistoricalExchangeRateResponse getHistoricalExchangeRate(HistoricalExchangeRateRequest request);
}
