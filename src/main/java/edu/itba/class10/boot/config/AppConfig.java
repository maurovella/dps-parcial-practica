package edu.itba.class10.boot.config;

import edu.itba.class10.domain.common.entities.config.ExchangeApiConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class AppConfig {

	@Bean
	@ConfigurationProperties("exchange-api")
	public ExchangeApiConfig exchangeApiConfig() {
		return ExchangeApiConfig.builder().build();
	}

}
