package ru.practicum.compilation.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.compilation.dto.CompilationCreateDto;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.model.Compilation;
import ru.practicum.event.model.Event;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MapperCompilation {
	public static Compilation toCompilation(CompilationCreateDto compilationCreateDto) {
		return Compilation.builder()
				.title(compilationCreateDto.getTitle())
				.pinned(compilationCreateDto.getPinned())
				.build();
	}

	public static Compilation toCompilation(CompilationCreateDto compilationCreateDto, List<Event> events) {
		return Compilation.builder()
				.title(compilationCreateDto.getTitle())
				.pinned(compilationCreateDto.getPinned())
				.events(events)
				.build();
	}

	public static CompilationDto toCompilationDto(Compilation compilation) {
		return CompilationDto.builder()
				.id(compilation.getId())
				.title(compilation.getTitle())
				.pinned(compilation.getPinned())
				.events(compilation.getEvents())
				.build();
	}
}