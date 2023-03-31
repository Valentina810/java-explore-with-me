package ru.practicum.request.service;

import lombok.extern.java.Log;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.event.dao.EventRepository;
import ru.practicum.event.dao.StateRepository;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.StateEvent;
import ru.practicum.exception.ConditionsAreNotMetException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.request.dao.RequestRepository;
import ru.practicum.request.dto.RequestDto;
import ru.practicum.request.mapper.MapperRequest;
import ru.practicum.request.model.Request;
import ru.practicum.request.model.StateRequest;
import ru.practicum.user.dao.UserRepository;
import ru.practicum.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Log
public class RequestServiceImpl implements RequestService {
	private final EventRepository eventRepository;
	private final UserRepository userRepository;
	private final RequestRepository requestRepository;
	private final StateRepository stateRepository;

	public RequestServiceImpl(EventRepository eventRepository, UserRepository userRepository, RequestRepository requestRepository, StateRepository stateRepository) {
		this.eventRepository = eventRepository;
		this.userRepository = userRepository;
		this.requestRepository = requestRepository;
		this.stateRepository = stateRepository;
	}

	@Override
	@Transactional(isolation = Isolation.SERIALIZABLE)
	public RequestDto addRequest(long userId, long eventId) {
		User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException(String.format("Запрос на участие не был добавлен: пользователь с id %d не найден!", userId)));
		Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException(String.format("Запрос на участие не был добавлен: событие с id %d не найдено!", eventId)));
		if (event.getInitiator().getId().equals(userId)) {
			throw new ConditionsAreNotMetException("Запрос на участие не был добавлен: инициатор события не может добавить запрос на участие в своём событии");
		}
		if (!event.getState().getName().equals(StateEvent.PUBLISHED.name())) {
			throw new ConditionsAreNotMetException("Запрос на участие не был добавлен: нельзя участвовать в неопубликованном событии");
		}
		if (Long.valueOf(event.getParticipantLimit()).equals(event.getConfirmedRequests())) {
			throw new ConditionsAreNotMetException("Запрос на участие не был добавлен: у события достигнут лимит запросов на участие");
		}
		if (requestRepository.findByEventIdAndRequesterId(userId, eventId).isPresent()) {
			throw new ConditionsAreNotMetException("Запрос на участие не был добавлен: нельзя добавить повторный запрос");
		}
		Request request = Request.builder()
				.event(event)
				.requester(user)
				.created(LocalDateTime.now())
				.status(stateRepository.findByName(StateRequest.PENDING.name()))
				.build();
		if (!event.getRequestModeration()) {
			request.setStatus(stateRepository.findByName(StateRequest.CONFIRMED.name()));
			event.setConfirmedRequests(event.getConfirmedRequests() + 1);
			eventRepository.save(event);
		}
		RequestDto requestDto = MapperRequest.toRequestDto(requestRepository.save(request));
		log.info("Добавлен запрос " + requestDto);
		return requestDto;
	}

	@Override
	@Transactional(isolation = Isolation.SERIALIZABLE)
	public RequestDto updateRequest(long userId, long requestId) {
		userRepository.findById(userId).orElseThrow(() -> new NotFoundException(String.format("Запрос на участие не был отменен: пользователь с id %d не найден!", userId)));
		Optional<Request> request = requestRepository.findById(requestId);
		if (request.isPresent() && (request.get().getRequester().getId().equals(userId))) {
			Request requestSave = request.get();
			Long eventId = requestSave.getEvent().getId();
			Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException(String.format("Запрос на участие не был отменен: событие с id %d не найдено!", eventId)));
			event.setConfirmedRequests(event.getConfirmedRequests() - 1);
			eventRepository.save(event);
			requestSave.setStatus(stateRepository.findByName(StateRequest.CANCELED.name()));
			RequestDto requestDto = MapperRequest.toRequestDto(requestRepository.save(requestSave));
			log.info("Обновлен запрос " + requestDto);
			return requestDto;
		} else {
			throw new NotFoundException(String.format("Запрос на участие не был отменен: запрос с id %d не найден!", requestId));
		}
	}

	@Override
	@Transactional(readOnly = true)
	public List<RequestDto> getRequestUser(long userId) {
		List<RequestDto> requests = new ArrayList<>();
		requestRepository.findByRequesterId(userId).forEach(e -> requests.add(MapperRequest.toRequestDto(e)));
		log.info("Получены данные о запросах пользователя " + requests);
		return requests;
	}
}