package ru.practicum.event.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.client.StatsClient;
import ru.practicum.dto.StatCreateDto;
import ru.practicum.dto.StatDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.HashSet;

@Service
@RequiredArgsConstructor
@Slf4j
public class StatEventServiceImpl implements StatEventService {
	private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	private final StatsClient statsClient;

	public Long save(String app, String uri, String ip) {
		StatDto statDto = statsClient.saveStat(StatCreateDto.builder()
				.app(app)
				.uri(uri)
				.ip(ip)
				.timestamp(LocalDateTime.now().format(formatter))
				.build());
		log.info("Отправлены данные в модуль статистики {}", statDto);
		return statsClient.getStats(LocalDateTime.now().minusYears(1).format(formatter),
				LocalDateTime.now().plusHours(1).format(formatter),
				new HashSet<>(Collections.singletonList(uri)),
				false).iterator().next().getHits();
	}
}