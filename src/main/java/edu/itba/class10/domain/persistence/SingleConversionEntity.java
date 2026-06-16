package edu.itba.class10.domain.persistence;

import edu.itba.class10.domain.entity.money.MoneyAmount;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDate;

@Data
@Accessors(fluent = true)
@Entity
@Table(name = "conversion_log")
public class SingleConversionEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private LocalDate date;

	@ManyToOne(cascade = CascadeType.ALL)
	private MoneyAmount from;

	@ManyToOne(cascade = CascadeType.ALL)
	private MoneyAmount to;

	public SingleConversionEntity() {
	}

	public SingleConversionEntity(final LocalDate date, final MoneyAmount from, final MoneyAmount to) {
		this.date = date;
		this.from = from;
		this.to = to;
	}
}
