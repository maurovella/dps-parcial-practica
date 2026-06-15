package edu.itba.class10.infrastructure.exchangerate.http;

import com.google.gson.Gson;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import edu.itba.class10.http.base.HttpClient;
import edu.itba.class10.http.base.HttpRequest;
import edu.itba.class10.http.base.HttpResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UniRestHttpClient implements HttpClient {

	@Override
	public <T> HttpResponse<T> get(final HttpRequest<T> request) {
		log.info("GET using Unirest request to: {}", request.endpoint());
		try {
			final var jsonResponse = Unirest.get(request.endpoint()).queryString(request.params())
					.headers(request.headers()).asJson();
			if (jsonResponse.getStatus() != 200) {
				return HttpResponse.error(request.onError(), jsonResponse.getStatusText());
			}
			final var typedResponse = new Gson().fromJson(jsonResponse.getBody().toString(), request.responseType());
			return HttpResponse.<T>builder().data(typedResponse).statusCode(jsonResponse.getStatus())
					.statusMessage(jsonResponse.getStatusText()).build();
		} catch (final UnirestException e) {
			return HttpResponse.error(request.onError(), e.getMessage());
		}
	}
}
