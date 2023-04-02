package ru.practicum.event.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.dto.EventDto;
import ru.practicum.event.service.EventService;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping(path = "/events")
@ResponseStatus(HttpStatus.OK)
@Validated
public class PublicEventController {
	private final EventService eventService;

	@Autowired
	public PublicEventController(EventService eventService) {
		this.eventService = eventService;
	}

	@GetMapping
	public List<EventDto> getEvents(@NotBlank @RequestParam(name = "text") String text,
	                                @RequestParam(name = "categories") Set<Long> categories,
	                                @RequestParam(name = "paid") boolean paid,
	                                @RequestParam(name = "rangeStart", required = false) String rangeStart,
	                                @RequestParam(name = "rangeEnd", required = false) String rangeEnd,
	                                @RequestParam(name = "onlyAvailable", required = false) boolean onlyAvailable,
	                                @RequestParam(name = "sort", defaultValue = "EVENT_DATE") String sort,
	                                @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
	                                @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
		return eventService.getEventsWithParametersWithText(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size);
	}

	@GetMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	public EventDto getUserEvent(@PathVariable long id) {
		return eventService.getEvent(id);
	}
}