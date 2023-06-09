package ru.practicum.client;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

public class BaseClient {
	protected final RestTemplate rest;

	public BaseClient(RestTemplate rest) {
		this.rest = rest;
	}

	protected ResponseEntity<Object> get(String path) {
		return makeAndSendRequest(HttpMethod.GET, path, "");
	}

	protected <T> ResponseEntity<Object> post(String path, T body) {
		return makeAndSendRequest(HttpMethod.POST, path, body);
	}

	private <T> ResponseEntity<Object> makeAndSendRequest(HttpMethod method, String path, T body) {
		HttpEntity<T> requestEntity = new HttpEntity<>(body);
		ResponseEntity<Object> serverResponse;
		try {
			serverResponse = rest.exchange(path, method, requestEntity, Object.class);
		} catch (HttpStatusCodeException e) {
			return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsByteArray());
		}
		return prepareResponse(serverResponse);
	}

	private static ResponseEntity<Object> prepareResponse(ResponseEntity<Object> response) {
		if (response.getStatusCode().is2xxSuccessful()) {
			return response;
		}
		ResponseEntity.BodyBuilder responseBuilder = ResponseEntity.status(response.getStatusCode());

		if (response.hasBody()) {
			return responseBuilder.body(response.getBody());
		}
		return responseBuilder.build();
	}
}