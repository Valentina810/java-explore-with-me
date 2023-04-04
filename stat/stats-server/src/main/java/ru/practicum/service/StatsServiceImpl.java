package ru.practicum.service;

import lombok.extern.java.Log;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dao.StatsRepository;
import ru.practicum.dto.StatCreateDto;
import ru.practicum.dto.StatDto;
import ru.practicum.dto.ViewStatDto;
import ru.practicum.mapper.MapperStat;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service
@Log
public class StatsServiceImpl implements StatsService {
	private final StatsRepository statsRepository;

	public StatsServiceImpl(StatsRepository statsRepository) {
		this.statsRepository = statsRepository;
	}

	@Override
	@Transactional(isolation = Isolation.SERIALIZABLE)
	public StatDto saveStat(StatCreateDto statCreateDto) {
		StatDto statDto = MapperStat.toStatDto(statsRepository.save(MapperStat.toStat(statCreateDto)));
		log.info("Сохранены данные о статистике " + statDto);
		return statDto;
	}

	@Override
	@Transactional(readOnly = true)
	public List<ViewStatDto> getStats(String start, String end, Set<String> uris, boolean unique) {
		LocalDateTime startDate = MapperStat.stringToLocalDateTime(start);
		LocalDateTime endDate = MapperStat.stringToLocalDateTime(end);
		List<ViewStatDto> statDtos;
		if ((uris == null) || (uris.isEmpty())) {
			statDtos = unique ? statsRepository.getStatsUniqueTrue(startDate, endDate) :
					statsRepository.getStatsUniqueFalse(startDate, endDate);
		} else {
			statDtos = unique ? statsRepository.getStatsUniqueTrueWithUris(startDate, endDate, uris) :
					statsRepository.getStatsUniqueFalseWithUris(startDate, endDate, uris);
		}

		log.info("Получены данные о статистике " + statDtos);
		return statDtos;
	}
}
