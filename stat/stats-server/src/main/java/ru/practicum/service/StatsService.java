package ru.practicum.service;

import ru.practicum.dto.StatCreateDto;
import ru.practicum.dto.StatDto;
import ru.practicum.dto.ViewStatDto;

import java.util.List;
import java.util.Set;

public interface StatsService {
	StatDto saveStat(StatCreateDto statCreateDto);

	List<ViewStatDto> getStats(String start, String end, Set<String> uris, boolean unique);
}