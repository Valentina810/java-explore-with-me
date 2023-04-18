package ru.practicum.client;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.StatCreateDto;
import ru.practicum.dto.StatDto;
import ru.practicum.dto.ViewStatDto;

import java.util.List;
import java.util.Set;

@RestController
@RequiredArgsConstructor
public class StatsController {

	private final StatsClient statsClient;

	@PostMapping("/hit")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<StatDto> createStat(@RequestBody StatCreateDto statCreateDto) {
		return new ResponseEntity<>(statsClient.saveStat(statCreateDto), HttpStatus.CREATED);
	}

	@GetMapping("/stats")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<List<ViewStatDto>> getStats(@RequestParam(name = "start") String start,
	                                                  @RequestParam(name = "end") String end,
	                                                  @RequestParam(name = "uris", required = false) Set<String> uris,
	                                                  @RequestParam(name = "unique", defaultValue = "false") boolean unique) {
		return ResponseEntity.ok(statsClient.getStats(start, end, uris, unique));
	}
}