package ru.practicum.compilation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.service.CompilationService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/compilations")
@Validated
@ResponseStatus(HttpStatus.OK)
public class PublicCompilationController {
	private final CompilationService compilationService;

	@GetMapping
	public List<CompilationDto> getCompilations(@RequestParam(name = "pinned", required = false) Boolean pinned,
	                                            @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
	                                            @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
		return compilationService.getCompilations(pinned, from, size);
	}

	@GetMapping("/{compId}")
	public CompilationDto getCompilation(@PathVariable long compId) {
		return compilationService.getCompilation(compId);
	}
}