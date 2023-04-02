package ru.practicum.event.service;

import lombok.extern.java.Log;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.category.dao.CategoryRepository;
import ru.practicum.event.dao.EventRepository;
import ru.practicum.event.dao.EventRepositoryCustom;
import ru.practicum.event.dao.LocationRepository;
import ru.practicum.event.dao.StateRepository;
import ru.practicum.event.dto.EventCreateDto;
import ru.practicum.event.dto.EventDto;
import ru.practicum.event.dto.EventUpdateDto;
import ru.practicum.event.mapper.MapperEvent;
import ru.practicum.event.model.*;
import ru.practicum.exception.BadDataException;
import ru.practicum.exception.ConditionsAreNotMetException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.user.dao.UserRepository;

import java.time.LocalDateTime;
import java.util.*;

@Service
@Log
public class EventServiceImpl implements EventService {
	private final UserRepository userRepository;
	private final EventRepository eventRepository;
	private final EventRepositoryCustom eventRepositoryCustom;
	private final LocationRepository locationRepository;
	private final StateRepository stateRepository;
	private final CategoryRepository categoryRepository;

	public EventServiceImpl(UserRepository userRepository, EventRepository eventRepository, EventRepositoryCustom eventRepositoryCustom, LocationRepository locationRepository, StateRepository stateRepository, CategoryRepository categoryRepository) {
		this.userRepository = userRepository;
		this.eventRepository = eventRepository;
		this.eventRepositoryCustom = eventRepositoryCustom;
		this.locationRepository = locationRepository;
		this.stateRepository = stateRepository;
		this.categoryRepository = categoryRepository;
	}

	@Override
	@Transactional(isolation = Isolation.SERIALIZABLE)
	public EventDto addEvent(long userId, EventCreateDto eventCreateDto) {
		if (MapperEvent.stringToLocalDateTime(eventCreateDto.getEventDate()).isAfter(LocalDateTime.now().plusHours(2))) {
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
			Event saveEvent = eventRepository.save(event);
			EventDto eventDto = MapperEvent.toEventDto(saveEvent);
			log.info("Создано событие " + eventDto);
			return eventDto;
		} else
			throw new ConditionsAreNotMetException("Событие не было добавлено: дата начала события должна быть не ранее, чем через два часа от текущего момента");
	}

	@Override
	@Transactional(readOnly = true)
	public List<EventDto> getUserEvents(long userId, Integer from, Integer size) {
		List<EventDto> eventDtos = new ArrayList<>();
		eventRepository.findByInitiatorId(userId, PageRequest.of(from / size, size))
				.forEach(e -> eventDtos.add(MapperEvent.toEventDto(e)));
		log.info("Получен список событий, добавленных пользователем с id " + userId);
		log.info("Список событий:" + eventDtos);
		return eventDtos;
	}

	@Override
	public EventDto getUserEvent(long userId, long eventId) {
		userRepository.findById(userId).orElseThrow(() ->
				new NotFoundException(String.format("Событие не найдено: пользователь с id %d не найден!", userId)));
		Event event = eventRepository.findById(eventId).orElseThrow(() ->
				new NotFoundException(String.format("Событие c id %d не найдено!", userId)));
		EventDto eventDto = MapperEvent.toEventDto(event);
		log.info("Получено событие:" + eventDto);
		return eventDto;
	}

	@Override
	@Transactional(isolation = Isolation.SERIALIZABLE)
	public EventDto updateEventUser(long userId, long eventId, EventUpdateDto eventUpdateDto) {
		userRepository.findById(userId).orElseThrow(() ->
				new NotFoundException(String.format("Пользователь с id %d не найден!", userId)));
		Event event = eventRepository.findById(eventId).orElseThrow(() ->
				new NotFoundException(String.format("Событие c id %d не найдено!", eventId)));
		if (!event.getInitiator().getId().equals(userId)) {
			throw new ConditionsAreNotMetException("Событие не было изменено: только организатор события может изменять данные события");
		}
		if (event.getState().getName().equals(StateEvent.PUBLISHED.name())) {
			throw new ConditionsAreNotMetException("Событие не было изменено: событие уже опубликовано");
		}

		if ((eventUpdateDto.getStateAction() != null) && (!event.getState().getName().equals(eventUpdateDto.getStateAction()))) {
			if (event.getState().getName().equals(StateEvent.CANCELED.name()) ||
					event.getState().getName().equals(StateEvent.PENDING.name()) ||
					event.getState().getName().equals(StateEvent.REJECTED.name())) {
				if (eventUpdateDto.getStateAction().equals(StateActionUser.SEND_TO_REVIEW.name())) {
					event.setState(stateRepository.findByName(StateEvent.PENDING.name()));
				} else if (eventUpdateDto.getStateAction().equals(StateActionUser.CANCEL_REVIEW.name())) {
					event.setState(stateRepository.findByName(StateEvent.CANCELED.name()));
				}
			} else
				throw new BadDataException("Невозможно обновить событие: изменить можно только отмененные события или события в состоянии ожидания модерации");
		}

		if (eventUpdateDto.getEventDate() != null) {
			if (!MapperEvent.stringToLocalDateTime(eventUpdateDto.getEventDate()).isAfter(LocalDateTime.now().plusHours(2))) {
				throw new ConditionsAreNotMetException("Событие не было добавлено: дата начала события должна быть не ранее, чем через два часа от текущего момента");
			} else event.setEventDate(MapperEvent.stringToLocalDateTime(eventUpdateDto.getEventDate()));
		}

		updateFields(eventUpdateDto, event);
		EventDto eventDto = MapperEvent.toEventDto(eventRepository.save(event));
		log.info("Обновлено событие:" + eventDto);
		return eventDto;
	}

	@Override
	@Transactional(isolation = Isolation.SERIALIZABLE)
	public EventDto updateEventAdmin(long eventId, EventUpdateDto eventUpdateDto) {
		Event event = eventRepository.findById(eventId).orElseThrow(() ->
				new NotFoundException(String.format("Событие c id %d не найдено!", eventId)));

		if ((eventUpdateDto.getStateAction() != null) && (!event.getState().getName().equals(eventUpdateDto.getStateAction()))) {
			if (StateActionAdmin.PUBLISH_EVENT.name().equals(eventUpdateDto.getStateAction())) {
				if (event.getState().getName().equals(StateEvent.PENDING.name())) {
					event.setState(stateRepository.findByName(StateEvent.PUBLISHED.name()));
				} else
					throw new ConditionsAreNotMetException("Событие не было изменено: событие можно публиковать, только если оно в состоянии ожидания публикации");
			}
			if (StateActionAdmin.REJECT_EVENT.name().equals(eventUpdateDto.getStateAction())) {
				if (!event.getState().getName().equals(StateEvent.PUBLISHED.name())) {
					event.setState(stateRepository.findByName(StateEvent.REJECTED.name()));
				} else
					throw new ConditionsAreNotMetException("Событие не было изменено: событие можно отклонить, только если оно еще не опубликовано");
			}
		}
		if (eventUpdateDto.getEventDate() != null) {
			if (!MapperEvent.stringToLocalDateTime(eventUpdateDto.getEventDate()).isAfter(LocalDateTime.now().plusHours(1))) {
				throw new ConditionsAreNotMetException("Событие не было изменено: дата начала изменяемого события должна быть не ранее чем за час от даты публикации");
			} else event.setEventDate(MapperEvent.stringToLocalDateTime(eventUpdateDto.getEventDate()));
		}

		updateFields(eventUpdateDto, event);
		EventDto eventDto = MapperEvent.toEventDto(eventRepository.save(event));
		log.info("Обновлено событие:" + eventDto);
		return eventDto;
	}

	private void updateFields(EventUpdateDto eventUpdateDto, Event event) {
		if (eventUpdateDto.getTitle() != null) {
			event.setTitle(eventUpdateDto.getTitle());
		}

		if (eventUpdateDto.getDescription() != null) {
			event.setDescription(eventUpdateDto.getDescription());
		}

		if (eventUpdateDto.getAnnotation() != null) {
			event.setAnnotation(eventUpdateDto.getAnnotation());
		}

		if (eventUpdateDto.getCategory() != null) {
			event.setCategory(categoryRepository.findById(eventUpdateDto.getCategory()).orElseThrow(() ->
					new NotFoundException(String.format("Событие не было добавлено: категория с id %d не найдена!", eventUpdateDto.getCategory()))));
		}

		if (eventUpdateDto.getLocation() != null) {
			Optional<Location> location
					= locationRepository.findByLatAndLon(eventUpdateDto.getLocation().getLat(),
					eventUpdateDto.getLocation().getLon());
			if (location.isPresent()) {
				event.setLocation(location.get());
			} else {
				event.setLocation(locationRepository.save(Location.builder()
						.lat(eventUpdateDto.getLocation().getLat())
						.lon(eventUpdateDto.getLocation().getLon()).build()));
			}
		}

		if (eventUpdateDto.getPaid() != null) {
			event.setPaid(eventUpdateDto.getPaid());
		}

		if (eventUpdateDto.getParticipantLimit() != null) {
			event.setParticipantLimit(eventUpdateDto.getParticipantLimit());
		}

		if (eventUpdateDto.getRequestModeration() != null) {
			event.setRequestModeration(eventUpdateDto.getRequestModeration());
		}
	}

	@Override
	@Transactional(readOnly = true)
	public List<EventDto> getUserEventsWithParameters(Set<Long> users, Set<String> states,
	                                                  Set<Long> categories, String rangeStart,
	                                                  String rangeEnd, Integer from, Integer size) {
		Set<Long> idsStates = new HashSet<>();
		stateRepository.findByNames(states).stream().mapToLong(e -> e.getId()).forEach(a -> idsStates.add(a));
		List<Event> events = eventRepositoryCustom.searchByCriteria(users, idsStates, categories,
				MapperEvent.stringToLocalDateTime(rangeStart), MapperEvent.stringToLocalDateTime(rangeEnd), from, size);
		List<EventDto> eventDtos = new ArrayList<>();
		events.forEach(e -> eventDtos.add(MapperEvent.toEventDto(e)));
		log.info("Получен список событий:" + eventDtos);
		return eventDtos;
	}

	@Override
	@Transactional(readOnly = true)
	public List<EventDto> getEventsWithParametersWithText(String text, Set<Long> categories,
	                                                      boolean paid, String rangeStart,
	                                                      String rangeEnd, boolean onlyAvailable,
	                                                      String sort, Integer from, Integer size) {
		List<Event> events = eventRepositoryCustom.searchByCriteria(text, categories, paid,
				MapperEvent.stringToLocalDateTime(rangeStart), MapperEvent.stringToLocalDateTime(rangeEnd),
				onlyAvailable, sort, stateRepository.findByName(StateEvent.PUBLISHED.name()).getId(), from, size);
		List<EventDto> eventDtos = new ArrayList<>();
		events.forEach(e -> eventDtos.add(MapperEvent.toEventDto(e)));
		log.info("Получен список событий:" + eventDtos);
		return eventDtos;
	}

	@Override
	@Transactional(readOnly = true)
	public EventDto getEvent(long eventId) {
		Event event = eventRepository.findById(eventId).orElseThrow(() ->
				new NotFoundException(String.format("Событие c id %d не найдено!", eventId)));
		EventDto eventDto = MapperEvent.toEventDto(event);
		log.info("Получено событие:" + eventDto);
		return eventDto;
	}
}