package ru.practicum.event.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.dto.EventCreateDto;
import ru.practicum.event.dto.EventDto;
import ru.practicum.event.service.EventService;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/users")
@Validated
public class PrivateEventController {

	private final EventService eventService;

	@Autowired
	public PrivateEventController(EventService eventService) {
		this.eventService = eventService;
	}

	@PostMapping("/{userId}/events")
	@ResponseStatus(HttpStatus.CREATED)
	public EventDto addEvent(@PathVariable long userId,
	                         @Valid @RequestBody EventCreateDto eventCreateDto) {
		return eventService.addEvent(userId, eventCreateDto);
	}
}