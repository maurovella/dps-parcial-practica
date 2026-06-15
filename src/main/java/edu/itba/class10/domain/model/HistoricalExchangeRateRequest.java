package edu.itba.class10.domain.model;

import edu.itba.class10.domain.entity.money.Currency;
import lombok.Builder;

import java.time.LocalDate;
import java.util.List;

@Builder
public record HistoricalExchangeRateRequest(LocalDate date, Currency baseCurrency, List<Currency> targetCurrencies) {
}
