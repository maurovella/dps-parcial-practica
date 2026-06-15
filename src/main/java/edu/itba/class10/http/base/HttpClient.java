package edu.itba.class10.http.base;

public interface HttpClient {
	<T> HttpResponse<T> get(HttpRequest<T> request);
}
