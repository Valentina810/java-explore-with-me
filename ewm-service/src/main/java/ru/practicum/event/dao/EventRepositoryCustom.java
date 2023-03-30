package ru.practicum.event.dao;

import ru.practicum.event.model.Event;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface EventRepositoryCustom {
	List<Event> searchBy(Set<Long> users, Set<Long> states,
	                     Set<Long> categories, LocalDateTime rangeStart,
	                     LocalDateTime rangeEnd, Integer from, Integer size);
}