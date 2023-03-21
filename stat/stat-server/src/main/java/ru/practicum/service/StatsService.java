package ru.practicum.service;

import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.StatCreateDto;
import ru.practicum.dto.StatDto;
import ru.practicum.dto.ViewStatDto;

import java.util.List;
import java.util.Set;

public interface StatsService {
	@Transactional(isolation = Isolation.SERIALIZABLE)
	StatDto saveStat(StatCreateDto statCreateDto);

	@Transactional(readOnly = true)
	List<ViewStatDto> getStats(String start, String end, Set<String> uris, boolean unique);
}