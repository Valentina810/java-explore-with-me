package ru.practicum.event.dao;

import ru.practicum.event.model.Event;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface EventRepositoryCustom {
	List<Event> searchByCriteria(Set<Long> users, Set<Long> states,
	                             Set<Long> categories, LocalDateTime rangeStart,
	                             LocalDateTime rangeEnd, Integer from, Integer size);

	List<Event> searchByCriteria(String text, Set<Long> categories,
	                             Boolean paid, LocalDateTime stringToLocalDateTime,
	                             LocalDateTime rangeStart, Boolean rangeEnd,
	                             String sort, Long state, Integer from, Integer size);
}