package ru.practicum.compilation.service;

import lombok.extern.java.Log;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.compilation.dao.CompilationRepository;
import ru.practicum.compilation.dto.CompilationCreateDto;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.CompilationUpdateDto;
import ru.practicum.compilation.mapper.MapperCompilation;
import ru.practicum.compilation.model.Compilation;
import ru.practicum.event.dao.EventRepository;
import ru.practicum.event.model.Event;
import ru.practicum.exception.NotFoundException;

import java.util.ArrayList;
import java.util.List;

@Service
@Log
@Transactional(isolation = Isolation.SERIALIZABLE)
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
	public CompilationDto updateCompilation(long compId, CompilationUpdateDto compilationUpdateDto) {
		Compilation saveCompilation = compilationRepository.findById(compId).orElseThrow(() ->
				new NotFoundException(String.format("Подборка не обновлена: подборка c id %d не найдена!", compId)));
		if (compilationUpdateDto.getTitle() != null) {
			saveCompilation.setTitle(compilationUpdateDto.getTitle());
		}
		if (compilationUpdateDto.getPinned() != null) {
			saveCompilation.setPinned(compilationUpdateDto.getPinned());
		}
		if (compilationUpdateDto.getEvents() != null) {
			saveCompilation.setEvents(eventRepository.getEvents(compilationUpdateDto.getEvents()));
		}
		Compilation compilation = compilationRepository.save(saveCompilation);
		log.info("Обновлена подборка c id " + compilation);
		return MapperCompilation.toCompilationDto(compilation);
	}

	@Override
	public List<CompilationDto> getCompilations(Boolean pinned, Integer from, Integer size) {
		List<CompilationDto> compilationDtos = new ArrayList<>();
		if (pinned != null) {
			List<Compilation> byPinned = compilationRepository.findByPinned(pinned, PageRequest.of(from / size, size));
			byPinned
					.forEach(e -> compilationDtos.add(MapperCompilation.toCompilationDto(e)));
		} else {
			compilationRepository.findAll(PageRequest.of(from / size, size))
					.forEach(e -> compilationDtos.add(MapperCompilation.toCompilationDto(e)));
		}
		log.info("Получен список подборок " + compilationDtos);
		return compilationDtos;
	}

	@Override
	public CompilationDto getCompilation(long compId) {
		Compilation compilation = compilationRepository.findById(compId).orElseThrow(() ->
				new NotFoundException(String.format("Подборка c id %d не найдена!", compId)));
		log.info("Получена подборка " + compilation);
		return MapperCompilation.toCompilationDto(compilation);
	}
}