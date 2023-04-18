package ru.practicum.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.event.dao.EventRepository;
import ru.practicum.event.dao.StateRepository;
import ru.practicum.event.dto.EventRequestStatusUpdateRequestDto;
import ru.practicum.event.dto.EventRequestStatusUpdateResult;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.State;
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
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RequestServiceImpl implements RequestService {
	private final EventRepository eventRepository;
	private final UserRepository userRepository;
	private final RequestRepository requestRepository;
	private final StateRepository stateRepository;

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
		if (requestRepository.findByEventIdAndRequesterId(eventId, userId).isPresent()) {
			throw new ConditionsAreNotMetException("Запрос на участие не был добавлен: нельзя добавить повторный запрос");
		}
		Request request = Request.builder().event(event).requester(user).created(LocalDateTime.now()).status(stateRepository.findByName(StateRequest.PENDING.name())).build();
		if (!event.getRequestModeration()) {
			request.setStatus(stateRepository.findByName(StateRequest.CONFIRMED.name()));
			event.setConfirmedRequests(event.getConfirmedRequests() + 1);
			eventRepository.save(event);
		}
		RequestDto requestDto = MapperRequest.toRequestDto(requestRepository.save(request));
		log.info("Добавлен запрос {}", requestDto);
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
			log.info("Обновлен запрос {}", requestDto);
			return requestDto;
		} else {
			throw new NotFoundException(String.format("Запрос на участие не был отменен: запрос с id %d не найден!", requestId));
		}
	}

	@Override
	@Transactional(readOnly = true)
	public List<RequestDto> getRequestUser(long userId) {
		List<RequestDto> requests = requestRepository.findByRequesterId(userId)
				.stream().map(MapperRequest::toRequestDto).collect(Collectors.toList());
		log.info("Получены данные о запросах пользователя {}", requests);
		return requests;
	}

	@Override
	@Transactional(readOnly = true)
	public List<RequestDto> getRequestsForEventTheUser(long userId, long eventId) {
		Event event = eventRepository.findById(eventId)
				.orElseThrow(() -> new NotFoundException(String.format("Невозможно получить информацию о заявках на участие в событии: событие с id %d не найдено!", eventId)));
		if (event.getInitiator().getId().equals(userId)) {
			List<RequestDto> events = requestRepository.findByEventId(eventId)
					.stream().map(MapperRequest::toRequestDto).collect(Collectors.toList());
			log.info("Получены данные о запросах на участие в событии {}", events);
			return events;
		} else
			throw new NotFoundException(String.format("Невозможно получить информацию о заявках на участие в событии: пользователь с id %d не является организатором события!", eventId));
	}

	@Override
	@Transactional(isolation = Isolation.SERIALIZABLE)
	public EventRequestStatusUpdateResult updateStatusRequests(long userId, long eventId, EventRequestStatusUpdateRequestDto eventRequestStatusUpdateRequestDto) {
		userRepository.findById(userId).orElseThrow(() -> new NotFoundException(String.format("Изменение статуса заявок на участие не выполнено: пользователь с id %d не найден!", userId)));
		Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException(String.format("Изменение статуса заявок на участие не выполнено: событие с id %d не найдено!", eventId)));
		List<RequestDto> confirmedRequests = new ArrayList<>();
		List<RequestDto> rejectedRequests = new ArrayList<>();

		Collections.sort(eventRequestStatusUpdateRequestDto.getRequestIds());
		HashMap<String, State> states = new HashMap<>();
		stateRepository.findAll().forEach(e -> states.put(e.getName(), e));
		List<Request> requests = requestRepository.getRequests(eventRequestStatusUpdateRequestDto.getRequestIds());
		requests.forEach(e -> {
			if (!e.getStatus().getName().equals(StateRequest.PENDING.name())) {
				throw new ConditionsAreNotMetException("Невозможно подтвердить заявку: статус можно изменить только у заявок, находящихся в состоянии ожидания");
			}
		});
		if ((event.getParticipantLimit().equals(0)) || !event.getRequestModeration()) {
			requests.forEach(e -> confirmedRequests.add(MapperRequest.toRequestDto(e)));
		} else {
			requests.forEach(e -> {
				if (event.getParticipantLimit() > event.getConfirmedRequests()) {
					if (eventRequestStatusUpdateRequestDto.getStatus().equals(StateRequest.CONFIRMED.name())) {
						event.setConfirmedRequests(event.getConfirmedRequests() + 1);
						eventRepository.save(event);
						e.setStatus(states.get(StateRequest.CONFIRMED.name()));
						requestRepository.save(e);
						confirmedRequests.add(MapperRequest.toRequestDto(e));
					} else if (eventRequestStatusUpdateRequestDto.getStatus().equals(StateRequest.REJECTED.name())) {
						e.setStatus(states.get(StateRequest.REJECTED.name()));
						requestRepository.save(e);
						rejectedRequests.add(MapperRequest.toRequestDto(e));
					}
				} else
					throw new ConditionsAreNotMetException("Невозможно подтвердить заявку: уже достигнут лимит по заявкам на данное событие");
			});
		}
		EventRequestStatusUpdateResult eventRequestStatusUpdateResult = EventRequestStatusUpdateResult.builder().confirmedRequests(confirmedRequests).rejectedRequests(rejectedRequests).build();
		log.info("Обновлен статус заявок на участие {}", eventRequestStatusUpdateResult);
		return eventRequestStatusUpdateResult;
	}

}