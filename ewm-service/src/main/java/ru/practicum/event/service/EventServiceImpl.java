package ru.practicum.event.service;

import lombok.extern.java.Log;
import org.springframework.stereotype.Service;
import ru.practicum.category.dao.CategoryRepository;
import ru.practicum.event.dao.EventRepository;
import ru.practicum.event.dao.LocationRepository;
import ru.practicum.event.dao.StateRepository;
import ru.practicum.event.dto.EventCreateDto;
import ru.practicum.event.dto.EventDto;
import ru.practicum.event.mapper.MapperEvent;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.Location;
import ru.practicum.exception.NotFoundException;
import ru.practicum.user.dao.UserRepository;

import java.util.Optional;

@Service
@Log
public class EventServiceImpl implements EventService {
	private final UserRepository userRepository;
	private final EventRepository eventRepository;
	private final LocationRepository locationRepository;
	private final StateRepository stateRepository;
	private final CategoryRepository categoryRepository;

	public EventServiceImpl(UserRepository userRepository, EventRepository eventRepository, LocationRepository locationRepository, StateRepository stateRepository, CategoryRepository categoryRepository) {
		this.userRepository = userRepository;
		this.eventRepository = eventRepository;
		this.locationRepository = locationRepository;
		this.stateRepository = stateRepository;
		this.categoryRepository = categoryRepository;
	}

	@Override
	public EventDto addEvent(long userId, EventCreateDto eventCreateDto) {
		Event event = MapperEvent.toEvent(eventCreateDto);
		event.setInitiator(userRepository.findById(userId).orElseThrow(() ->
				new NotFoundException(String.format("Событие не было добавлено: пользователь с id %d не найден!", userId))));
		Optional<Location> location
				= locationRepository.findByLatAndLon(eventCreateDto.getLocation().getLat(),
				eventCreateDto.getLocation().getLon());
		if (location.isPresent()) {
			event.setLocation(location.get());
		} else {
			event.setLocation(locationRepository.save(Location.builder()
					.lat(eventCreateDto.getLocation().getLat())
					.lon(eventCreateDto.getLocation().getLon()).build()));
		}
		event.setCategory(categoryRepository.findById(eventCreateDto.getCategory()).orElseThrow(() ->
				new NotFoundException(String.format("Событие не было добавлено: категория с id %d не найдена!", eventCreateDto.getCategory()))));
		event.setState(stateRepository.findByName("PENDING"));
		Event save = eventRepository.save(event);
		return MapperEvent.toEventDto(save);
	}
}