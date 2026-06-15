package edu.itba.class10.domain.model;

import edu.itba.class10.domain.entity.money.Currency;
import lombok.Builder;

import java.util.List;

@Builder
public record LatestExchangeRateRequest(Currency baseCurrency, List<Currency> targetCurrencies) {
}
