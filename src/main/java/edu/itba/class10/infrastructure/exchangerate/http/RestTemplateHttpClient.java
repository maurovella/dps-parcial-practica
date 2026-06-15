package edu.itba.class10.infrastructure.exchangerate.http;

import edu.itba.class10.http.base.HttpClient;
import edu.itba.class10.http.base.HttpRequest;
import edu.itba.class10.http.base.HttpResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
public class RestTemplateHttpClient implements HttpClient {

	private final RestTemplate restTemplate;

	public RestTemplateHttpClient(final RestTemplateBuilder restTemplateBuilder) {
		log.info("Using RestTemplate as HTTP client");
		this.restTemplate = restTemplateBuilder.build();
	}

	@Override
	public <T> HttpResponse<T> get(final HttpRequest<T> request) {
		final var headers = new HttpHeaders(MultiValueMap.fromSingleValue(request.headers()));
		final var requestEntity = new HttpEntity<>(headers);
		final var url = UriComponentsBuilder.fromUriString(request.endpoint())
				.queryParams(MultiValueMap.fromSingleValue(request.stringParams())).encode().toUriString();

		try {
			final var responseEntity = this.restTemplate.exchange(url, HttpMethod.GET, requestEntity,
					request.responseType());
			if (responseEntity.getStatusCode().isError()) {
				return HttpResponse.error(request.onError(), responseEntity.getStatusCode().toString());
			}
			return HttpResponse.<T>builder().data(responseEntity.getBody())
					.statusMessage(responseEntity.getStatusCode().toString()).build();
		} catch (final RestClientResponseException e) {
			return HttpResponse.error(request.onError(), e.getMessage());
		}
	}
}
