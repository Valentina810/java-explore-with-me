package ru.practicum.stat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.client.BaseClient;
import ru.practicum.dto.StatCreateDto;

import java.util.Set;

@Service
public class StatsClient extends BaseClient {
	@Autowired
	public StatsClient(@Value("${stat-server.url}") String serverUrl, RestTemplateBuilder builder) {
		super(
				builder
						.uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
						.requestFactory(HttpComponentsClientHttpRequestFactory::new)
						.build()
		);
	}

	public ResponseEntity<Object> saveStat(StatCreateDto statCreateDto) {
		return post("/hit", statCreateDto);
	}

	public ResponseEntity<Object> getStats(String start, String end, Set<String> uris, boolean unique) {
		StringBuilder path = new StringBuilder("/stats?");
		path.append("start=").append(start);
		path.append("&end=").append(end).append("&");
		if (!uris.isEmpty()) {
			uris.forEach(e -> path.append("uris=").append(e).append("&"));
		}
		path.append("unique=").append(unique);
		return get(path.toString());
	}
}
