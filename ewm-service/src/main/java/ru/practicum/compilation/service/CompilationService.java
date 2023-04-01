package ru.practicum.compilation.service;

import ru.practicum.compilation.dto.CompilationCreateDto;
import ru.practicum.compilation.dto.CompilationDto;

public interface CompilationService {
	CompilationDto addCompilation(CompilationCreateDto compilationCreateDto);

	void deleteCompilation(long compId);

	CompilationDto updateCompilation(long compId, CompilationCreateDto compilationCreateDto);
}