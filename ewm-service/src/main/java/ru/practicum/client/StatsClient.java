package ru.practicum.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import lombok.extern.java.Log;
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
@Log
public class StatsClient extends BaseClient {
	@Autowired
	public StatsClient(@Value("${stats-server.url}") String serverUrl, RestTemplateBuilder builder) {
		super(
				builder
						.uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
						.requestFactory(HttpComponentsClientHttpRequestFactory::new)
						.build()
		);
	}

	ObjectMapper mapper = new ObjectMapper();

	public StatDto saveStat(StatCreateDto statCreateDto) {
		ResponseEntity<Object> response = post("/hit", statCreateDto);
		StatDto statDto;
		try {
			statDto = mapper.readValue(new Gson().toJson(response.getBody()), StatDto.class);
		} catch (JsonProcessingException e) {
			throw new DeserializationException("Объект StatDto невозможно десериализовать из тела ответа " + response.getBody().toString());
		}
		return statDto;
	}

	public List<ViewStatDto> getStats(String start, String end, Set<String> uris, boolean unique) {
		StringBuilder path = new StringBuilder("/stats?");
		if (start != null) {
			path.append("start=").append(start);
		}
		if (end != null) {
			path.append("&end=").append(end).append("&");
		}
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
			throw new DeserializationException("Объект StatDto невозможно десериализовать из тела ответа " + response.getBody().toString());
		}
		return statDtos;
	}
}