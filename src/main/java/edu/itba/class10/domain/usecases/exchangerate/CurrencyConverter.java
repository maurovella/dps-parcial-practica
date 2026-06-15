package edu.itba.class10.domain.usecases.exchangerate;

import edu.itba.class10.domain.entity.money.Currency;
import edu.itba.class10.domain.entity.money.MoneyAmount;

import java.util.List;

public interface CurrencyConverter {
	MoneyAmount convert(final MoneyAmount moneyAmount, final Currency toCurrency);

	List<MoneyAmount> convert(final MoneyAmount moneyAmount, final List<Currency> targetCurrencies);
}
