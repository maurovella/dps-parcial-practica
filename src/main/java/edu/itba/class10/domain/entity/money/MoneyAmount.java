package edu.itba.class10.domain.entity.money;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@Accessors(fluent = true)
@Entity
@Table(name = "money_amount")
public class MoneyAmount {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Enumerated(EnumType.STRING)
	@Column
	private Currency currency;

	@Column
	private BigDecimal amount;

	public static MoneyAmount create(final Currency currency, final double amount) {
		final MoneyAmount moneyAmount = new MoneyAmount();
		moneyAmount.setCurrency(currency);
		moneyAmount.setAmount(BigDecimal.valueOf(amount));
		return moneyAmount;
	}

	public static MoneyAmount create(final Currency currency, final BigDecimal amount) {
		final MoneyAmount moneyAmount = new MoneyAmount();
		moneyAmount.setCurrency(currency);
		moneyAmount.setAmount(amount);
		return moneyAmount;
	}
}
