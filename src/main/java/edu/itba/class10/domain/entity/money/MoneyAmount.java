package edu.itba.class10.domain.entity.money;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Getter
@EqualsAndHashCode
@Accessors(fluent = true)
@ToString
public class MoneyAmount {
	private final Currency currency;
	private final BigDecimal amount;

	private MoneyAmount(final Currency currency, final BigDecimal amount) {
		this.currency = currency;
		this.amount = amount.setScale(2, RoundingMode.HALF_UP);
	}

	public static MoneyAmount create(final Currency currency, final double amount) {
		return new MoneyAmount(currency, BigDecimal.valueOf(amount));
	}

	public static MoneyAmount create(Currency currency, BigDecimal amount) {
		return new MoneyAmount(currency, amount);
	}
}
