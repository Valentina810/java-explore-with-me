package ru.practicum.service;

import ru.practicum.dto.StatCreateDto;
import ru.practicum.dto.StatDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dao.StatsRepository;
import ru.practicum.mapper.MapperStat;
import ru.practicum.model.ViewStat;

import java.util.List;
import java.util.Set;

@Service
public class StatsServiceImpl {
	private final StatsRepository statsRepository;

	public StatsServiceImpl(StatsRepository statsRepository) {
		this.statsRepository = statsRepository;
	}

	@Transactional(isolation = Isolation.SERIALIZABLE)
	public StatDto saveStat(StatCreateDto statCreateDto) {
		return MapperStat.toStatDto(statsRepository.save(MapperStat.toStat(statCreateDto)));
	}

	@Transactional(readOnly = true)
	public List<ViewStat> getStats(String start, String end, Set<String> uris, boolean unique) {
		if (unique) {
			return statsRepository.getStatsUniqueTrue(MapperStat.stringToLocalDateTime(start),
					MapperStat.stringToLocalDateTime(end), uris);
		} else return statsRepository.getStatsUniqueFalse(MapperStat.stringToLocalDateTime(start),
				MapperStat.stringToLocalDateTime(end), uris);
	}
}
