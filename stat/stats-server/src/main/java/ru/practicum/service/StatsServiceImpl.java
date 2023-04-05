package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@RequiredArgsConstructor
@Slf4j
public class StatsServiceImpl implements StatsService {
	private final StatsRepository statsRepository;

	@Override
	@Transactional(isolation = Isolation.SERIALIZABLE)
	public StatDto saveStat(StatCreateDto statCreateDto) {
		StatDto statDto = MapperStat.toStatDto(statsRepository.save(MapperStat.toStat(statCreateDto)));
		log.info("Сохранены данные о статистике {}", statDto);
		return statDto;
	}

	@Override
	@Transactional(readOnly = true)
	public List<ViewStatDto> getStats(LocalDateTime start, LocalDateTime end, Set<String> uris, boolean unique) {
		List<ViewStatDto> statDtos;
		if ((uris == null) || (uris.isEmpty())) {
			statDtos = unique ? statsRepository.getStatsUniqueTrue(start, end) :
					statsRepository.getStatsUniqueFalse(start, end);
		} else {
			statDtos = unique ? statsRepository.getStatsUniqueTrueWithUris(start, end, uris) :
					statsRepository.getStatsUniqueFalseWithUris(start, end, uris);
		}

		log.info("Получены данные о статистике {}", statDtos);
		return statDtos;
	}
}
