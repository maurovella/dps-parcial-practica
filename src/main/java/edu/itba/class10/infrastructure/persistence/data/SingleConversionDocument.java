package edu.itba.class10.infrastructure.persistence.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Data
@AllArgsConstructor
@Document(collection = "single_conversions")
public class SingleConversionDocument {
	@MongoId
	private String id;
	@Indexed
	private String date;
	private String baseCurrency;
	private String targetCurrency;
	private double originalAmount;
	private double convertedAmount;
}
