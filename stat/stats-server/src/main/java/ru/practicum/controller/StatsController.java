package ru.practicum.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.StatCreateDto;
import ru.practicum.dto.StatDto;
import ru.practicum.dto.ViewStatDto;
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
	public List<ViewStatDto> getStats(@RequestParam(name = "start") String start,
	                                  @RequestParam(name = "end") String end,
	                                  @RequestParam(name = "uris", required = false) Set<String> uris,
	                                  @RequestParam(name = "unique", defaultValue = "false") boolean unique) {
		return statsServiceImpl.getStats(start, end, uris, unique);
	}
}
