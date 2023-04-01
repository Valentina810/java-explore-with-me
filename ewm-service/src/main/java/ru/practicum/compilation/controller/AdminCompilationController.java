package ru.practicum.compilation.controller;

import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.compilation.dto.CompilationCreateDto;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.CompilationUpdateDto;
import ru.practicum.compilation.service.CompilationService;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/admin/compilations")
@Validated
public class AdminCompilationController {
	private final CompilationService compilationService;

	public AdminCompilationController(CompilationService compilationService) {
		this.compilationService = compilationService;
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public CompilationDto addCompilation(@Valid @RequestBody CompilationCreateDto compilationCreateDto) {
		return compilationService.addCompilation(compilationCreateDto);
	}

	@DeleteMapping("/{compId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteCompilation(@PathVariable long compId) {
		compilationService.deleteCompilation(compId);
	}

	@PatchMapping("/{compId}")
	@ResponseStatus(HttpStatus.OK)
	public CompilationDto updateCompilation(@PathVariable long compId,
	                                        @Valid @RequestBody CompilationUpdateDto compilationUpdateDto) {
		return compilationService.updateCompilation(compId, compilationUpdateDto);
	}
}