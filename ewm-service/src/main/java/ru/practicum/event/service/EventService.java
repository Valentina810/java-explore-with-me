package ru.practicum.event.service;

import ru.practicum.event.dto.EventCreateDto;
import ru.practicum.event.dto.EventDto;
import ru.practicum.event.dto.EventUpdateDto;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface EventService {
	EventDto addEvent(long userId, EventCreateDto eventCreateDto);

	List<EventDto> getUserEvents(long userId, Integer from, Integer size);

	EventDto getUserEvent(long userId, long eventId);

	EventDto updateEventUser(long userId, long eventId, EventUpdateDto eventUpdateDto);

	EventDto updateEventAdmin(long eventId, EventUpdateDto eventUpdateDto);

	List<EventDto> getUserEventsWithParameters(Set<Long> users, Set<String> states,
	                                           Set<Long> categories, LocalDateTime rangeStart, LocalDateTime rangeEnd, Integer from, Integer size);

	List<EventDto> getEventsWithParametersWithText(String text, Set<Long> categories, boolean paid, LocalDateTime rangeStart,
	                                               LocalDateTime rangeEnd, boolean onlyAvailable, String sort, Integer from, Integer size, HttpServletRequest request);

	EventDto getEvent(long eventId, HttpServletRequest request);
}