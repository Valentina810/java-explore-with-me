package ru.practicum.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.dto.StatCreateDto;
import ru.practicum.dto.StatDto;
import ru.practicum.dto.ViewStatDto;
import ru.practicum.exception.DeserializationException;

import java.util.List;
import java.util.Set;

@Service
public class StatsClient extends BaseClient {
	private final ObjectMapper mapper;
	private static final String errorMessage = "Объект StatDto невозможно десериализовать из тела ответа ";

	@Autowired
	public StatsClient(@Value("${stats-server.url}") String serverUrl, RestTemplateBuilder builder) {
		super(
				builder
						.uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
						.requestFactory(HttpComponentsClientHttpRequestFactory::new)
						.build()
		);
		mapper = new ObjectMapper();
	}

	public StatDto saveStat(StatCreateDto statCreateDto) {
		ResponseEntity<Object> response = post("/hit", statCreateDto);
		StatDto statDto;
		try {
			statDto = mapper.readValue(new Gson().toJson(response.getBody()), StatDto.class);
		} catch (JsonProcessingException e) {
			throw new DeserializationException(response.getBody() == null ? errorMessage : errorMessage + response.getBody().toString());
		}
		return statDto;
	}

	public List<ViewStatDto> getStats(String start, String end, Set<String> uris, boolean unique) {
		StringBuilder path = new StringBuilder("/stats?");
		path.append("start=").append(start);
		path.append("&end=").append(end).append("&");
		if ((uris != null) && (!uris.isEmpty())) {
			uris.forEach(e -> path.append("uris=").append(e).append("&"));
		}
		path.append("unique=").append(unique);
		ResponseEntity<Object> response = get(path.toString());
		List<ViewStatDto> statDtos;
		try {
			statDtos = mapper.readValue(new Gson().toJson(response.getBody()), new TypeReference<List<ViewStatDto>>() {
			});
		} catch (JsonProcessingException e) {
			throw new DeserializationException(response.getBody() == null ? errorMessage : errorMessage + response.getBody().toString());
		}
		return statDtos;
	}
}
