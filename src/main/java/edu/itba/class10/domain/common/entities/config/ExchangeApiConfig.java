package edu.itba.class10.domain.common.entities.config;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ExchangeApiConfig {

	private List<ExchangeApiConfigItem> httpProviders;

	public ExchangeApiConfigItem defaultConfig() {
		return this.httpProviders.stream().findFirst().orElseThrow(MissingExchangeApiConfigException::new);
	}
}
