package ru.practicum.service;

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
public class StatsServiceImpl implements StatsService {
	private final StatsRepository statsRepository;

	public StatsServiceImpl(StatsRepository statsRepository) {
		this.statsRepository = statsRepository;
	}

	@Override
	@Transactional(isolation = Isolation.SERIALIZABLE)
	public StatDto saveStat(StatCreateDto statCreateDto) {
		return MapperStat.toStatDto(statsRepository.save(MapperStat.toStat(statCreateDto)));
	}

	@Override
	@Transactional(readOnly = true)
	public List<ViewStatDto> getStats(String start, String end, Set<String> uris, boolean unique) {
		LocalDateTime startDate = MapperStat.stringToLocalDateTime(start);
		LocalDateTime endDate = MapperStat.stringToLocalDateTime(end);
		if (unique) {
			return statsRepository.getStatsUniqueTrue(startDate, endDate, uris);
		} else return statsRepository.getStatsUniqueFalse(startDate, endDate, uris);
	}
}
