package edu.itba.class10.http.base;

import lombok.Builder;
import org.apache.http.HttpStatus;

@Builder
public record HttpResponse<T>(T data, int statusCode, String statusMessage) {
	public static <T> HttpResponse<T> error(final T data, final String message) {
		return new HttpResponse<>(data, HttpStatus.SC_INTERNAL_SERVER_ERROR, message);
	}
}
