package ru.practicum.compilation.service;

import ru.practicum.compilation.dto.CompilationCreateDto;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.CompilationUpdateDto;

import java.util.List;

public interface CompilationService {
	CompilationDto addCompilation(CompilationCreateDto compilationCreateDto);

	void deleteCompilation(long compId);

	CompilationDto updateCompilation(long compId, CompilationUpdateDto compilationUpdateDto);

	List<CompilationDto> getCompilations(Boolean pinned, Integer from, Integer size);

	CompilationDto getCompilation(long compId);
}