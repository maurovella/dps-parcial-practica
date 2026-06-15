package edu.itba.class10.infrastructure.persistence.relational;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "single_conversion")
public class SingleConversionSqlEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column
	private Timestamp date;

	@Column
	private String fromCurrency;

	@Column
	private BigDecimal fromAmount;

	@Column
	private String toCurrency;

	@Column
	private BigDecimal toAmount;
}
