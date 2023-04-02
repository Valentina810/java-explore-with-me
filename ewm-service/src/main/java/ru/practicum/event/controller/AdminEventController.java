package ru.practicum.event.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.dto.EventDto;
import ru.practicum.event.dto.EventUpdateDto;
import ru.practicum.event.service.EventService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping(path = "/admin/events")
@ResponseStatus(HttpStatus.OK)
@Validated
public class AdminEventController {
	private final EventService eventService;

	@Autowired
	public AdminEventController(EventService eventService) {
		this.eventService = eventService;
	}

	@GetMapping
	public List<EventDto> getEvents(@RequestParam(name = "users") Set<Long> users,
	                                @RequestParam(name = "states", required = false) Set<String> states,
	                                @RequestParam(name = "categories", required = false) Set<Long> categories,
	                                @RequestParam(name = "rangeStart", required = false) String rangeStart,
	                                @RequestParam(name = "rangeEnd", required = false) String rangeEnd,
	                                @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
	                                @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
		return eventService.getUserEventsWithParameters(users, states, categories, rangeStart, rangeEnd, from, size);
	}

	@PatchMapping("/{eventId}")
	public EventDto updateEvent(@PathVariable long eventId,
	                            @RequestBody EventUpdateDto eventUpdateDto) {
		return eventService.updateEventAdmin(eventId, eventUpdateDto);
	}
}
