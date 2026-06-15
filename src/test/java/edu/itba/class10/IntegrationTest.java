package edu.itba.class10;

import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import edu.itba.class10.domain.common.entities.config.ExchangeApiConfig;
import edu.itba.class10.domain.common.entities.config.ExchangeApiConfigItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static org.mockito.Mockito.when;

public abstract class IntegrationTest {

	@RegisterExtension
	protected static final WireMockExtension wireMock = WireMockExtension.newInstance()
			.options(options().dynamicPort().usingFilesUnderClasspath("wiremock")).build();

	@MockitoBean
	protected ExchangeApiConfig exchangeApiConfig;

	@BeforeEach
	void setUp() {
		wireMock.resetAll();
		final var exchangeApiConfigItem = ExchangeApiConfigItem.builder().name("mock").baseUrl(wireMock.baseUrl())
				.apiKey("dummyApiKey").build();
		when(this.exchangeApiConfig.defaultConfig()).thenReturn(exchangeApiConfigItem);
	}

}
