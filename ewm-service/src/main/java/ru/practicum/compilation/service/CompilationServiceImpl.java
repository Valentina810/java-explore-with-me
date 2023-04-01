package ru.practicum.compilation.service;

import lombok.extern.java.Log;
import org.springframework.stereotype.Service;
import ru.practicum.compilation.dao.CompilationRepository;
import ru.practicum.compilation.dto.CompilationCreateDto;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.mapper.MapperCompilation;
import ru.practicum.compilation.model.Compilation;
import ru.practicum.event.dao.EventRepository;
import ru.practicum.event.model.Event;
import ru.practicum.exception.NotFoundException;

import java.util.List;

@Service
@Log
public class CompilationServiceImpl implements CompilationService {
	private final EventRepository eventRepository;
	private final CompilationRepository compilationRepository;

	public CompilationServiceImpl(EventRepository eventRepository, CompilationRepository compilationRepository) {
		this.eventRepository = eventRepository;
		this.compilationRepository = compilationRepository;
	}

	@Override
	public CompilationDto addCompilation(CompilationCreateDto compilationCreateDto) {
		CompilationDto compilationDto;
		if (compilationCreateDto.getEvents().isEmpty()) {
			compilationDto = MapperCompilation.toCompilationDto(compilationRepository.save(MapperCompilation.toCompilation(compilationCreateDto)));
		} else {
			List<Event> events = eventRepository.getEvents(compilationCreateDto.getEvents());
			if (compilationCreateDto.getEvents().size() != events.size()) {
				throw new NotFoundException("Невозможно создать подборку: в подборке указан id несуществующего события");
			}
			compilationDto = MapperCompilation.toCompilationDto(compilationRepository.save(MapperCompilation.toCompilation(compilationCreateDto, events)));
		}
		log.info("Добавлена подборка " + compilationDto);
		return compilationDto;
	}

	@Override
	public void deleteCompilation(long compId) {
		compilationRepository.findById(compId).orElseThrow(() ->
				new NotFoundException(String.format("Подборка не удалена: подборка c id %d не найдена!", compId)));
		compilationRepository.deleteById(compId);
		log.info("Удалена подборка c id " + compId);
	}

	@Override
	public CompilationDto updateCompilation(long compId, CompilationCreateDto compilationCreateDto) {
		Compilation saveCompilation = compilationRepository.findById(compId).orElseThrow(() ->
				new NotFoundException(String.format("Подборка не обновлена: подборка c id %d не найдена!", compId)));
		saveCompilation.setTitle(compilationCreateDto.getTitle());
		saveCompilation.setPinned(compilationCreateDto.getPinned());
		saveCompilation.setEvents(eventRepository.getEvents(compilationCreateDto.getEvents()));
		Compilation compilation = compilationRepository.save(saveCompilation);
		log.info("Обновлена подбока подборка c id " + compilation);
		return MapperCompilation.toCompilationDto(compilation);
	}
}