package edu.itba.class10.boot.config;

import edu.itba.class10.http.base.HttpClient;
import edu.itba.class10.infrastructure.exchangerate.http.RestTemplateHttpClient;
import edu.itba.class10.infrastructure.exchangerate.http.UniRestHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HttpClientConfig {

	@Autowired
	private RestTemplateBuilder restTemplateBuilder;

	@Bean
	@RefreshScope
	public HttpClient httpClient(@Value("${http.client:unirest}") String client) {
		if ("unirest".equalsIgnoreCase(client)) {
			return new UniRestHttpClient();
		} else if ("resttemplate".equalsIgnoreCase(client)) {
			return new RestTemplateHttpClient(this.restTemplateBuilder);
		}
		throw new IllegalArgumentException("Unsupported HTTP client: " + client);
	}
}
