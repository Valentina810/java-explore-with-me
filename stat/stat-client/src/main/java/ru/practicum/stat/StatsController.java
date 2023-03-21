package ru.practicum.stat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.StatCreateDto;

import java.util.Set;

@RestController
public class StatsController {

	private final StatsClient statsClient;

	@Autowired
	public StatsController(StatsClient statsClient) {
		this.statsClient = statsClient;
	}

	@PostMapping("/hit")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<Object> createStat(@RequestBody StatCreateDto statCreateDto) {
		return statsClient.saveStat(statCreateDto);
	}

	@GetMapping("/stats")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<Object> getStats(@RequestParam(name = "start") String start,
	                                       @RequestParam(name = "end") String end,
	                                       @RequestParam(name = "uris") Set<String> uris,
	                                       @RequestParam(name = "unique", defaultValue = "false") boolean unique) {
		return statsClient.getStats(start, end, uris, unique);
	}
}