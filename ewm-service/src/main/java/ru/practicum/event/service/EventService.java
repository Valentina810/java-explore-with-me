package ru.practicum.event.service;

import ru.practicum.event.dto.EventCreateDto;
import ru.practicum.event.dto.EventDto;

public interface EventService {
	EventDto addEvent(long userId, EventCreateDto eventCreateDto);
}