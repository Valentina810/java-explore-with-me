package ru.practicum.controller;

import ru.practicum.dto.StatCreateDto;
import ru.practicum.dto.StatDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.model.ViewStat;
import ru.practicum.service.StatsServiceImpl;

import java.util.List;
import java.util.Set;

@RestController
public class StatsController {

	private final StatsServiceImpl statsServiceImpl;

	public StatsController(StatsServiceImpl statsServiceImpl) {
		this.statsServiceImpl = statsServiceImpl;
	}

	@PostMapping("/hit")
	@ResponseStatus(HttpStatus.CREATED)
	public StatDto createStat(@RequestBody StatCreateDto statCreateDto) {
		return statsServiceImpl.saveStat(statCreateDto);
	}

	@GetMapping("/stats")
	@ResponseStatus(HttpStatus.OK)
	public List<ViewStat> getStats(@RequestParam(name = "start") String start,
	                               @RequestParam(name = "end") String end,
	                               @RequestParam(name = "uris") Set<String> uris,
	                               @RequestParam(name = "unique", defaultValue = "false") boolean unique) {
		return statsServiceImpl.getStats(start, end, uris, unique);
	}
}
