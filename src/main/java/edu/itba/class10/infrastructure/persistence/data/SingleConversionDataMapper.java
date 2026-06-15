package edu.itba.class10.infrastructure.persistence.data;

import edu.itba.class10.domain.persistence.SingleConversionEntity;
import org.springframework.stereotype.Component;

@Component
public class SingleConversionDataMapper {
	public SingleConversionDocument toDocument(SingleConversionEntity entity) {
		return new SingleConversionDocument(null, entity.date().toString(), entity.from().currency().toString(),
				entity.to().currency().toString(), entity.from().amount().doubleValue(),
				entity.to().amount().doubleValue());
	}
}
