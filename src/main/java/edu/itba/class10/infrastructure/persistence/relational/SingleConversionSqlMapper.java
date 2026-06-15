package edu.itba.class10.infrastructure.persistence.relational;

import edu.itba.class10.domain.entity.money.Currency;
import edu.itba.class10.domain.entity.money.MoneyAmount;
import edu.itba.class10.domain.persistence.SingleConversionEntity;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

@Component
public class SingleConversionSqlMapper {

	public SingleConversionSqlEntity toSql(SingleConversionEntity entity) {
		final var sqlEntity = new SingleConversionSqlEntity();
		sqlEntity.setDate(Timestamp.valueOf(entity.date().atStartOfDay()));
		sqlEntity.setFromCurrency(entity.from().currency().toString());
		sqlEntity.setFromAmount(entity.from().amount());
		sqlEntity.setToCurrency(entity.to().currency().toString());
		sqlEntity.setToAmount(entity.to().amount());
		return sqlEntity;
	}

	public SingleConversionEntity toDomain(SingleConversionSqlEntity singleConversionSqlEntity) {
		return new SingleConversionEntity(singleConversionSqlEntity.getDate().toLocalDateTime().toLocalDate(),
				MoneyAmount.create(Currency.valueOf(singleConversionSqlEntity.getFromCurrency()),
						singleConversionSqlEntity.getFromAmount()),
				MoneyAmount.create(Currency.valueOf(singleConversionSqlEntity.getToCurrency()),
						singleConversionSqlEntity.getToAmount()));
	}
}
