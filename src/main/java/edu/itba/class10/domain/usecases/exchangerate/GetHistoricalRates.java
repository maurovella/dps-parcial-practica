package edu.itba.class10.domain.usecases.exchangerate;

import edu.itba.class10.domain.entity.money.Currency;
import edu.itba.class10.domain.entity.money.MoneyAmount;
import edu.itba.class10.domain.model.HistoricalRate;

import java.time.LocalDate;
import java.util.List;

public interface GetHistoricalRates {
	HistoricalRate getHistoricalRate(MoneyAmount moneyAmount, List<Currency> targetCurrencies, LocalDate date);
}
