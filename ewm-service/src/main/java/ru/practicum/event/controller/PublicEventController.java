package ru.practicum.event.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.dto.EventDto;
import ru.practicum.event.service.EventService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/events")
@ResponseStatus(HttpStatus.OK)
@Validated
public class PublicEventController {
	private final EventService eventService;
	private static final String formatDate = "yyyy-MM-dd HH:mm:ss";

	@GetMapping
	public List<EventDto> getEvents(@RequestParam(name = "text", required = false) String text,
	                                @RequestParam(name = "categories", required = false) Set<Long> categories,
	                                @RequestParam(name = "paid", required = false) boolean paid,
	                                @RequestParam(name = "rangeStart", required = false) @DateTimeFormat(pattern = formatDate) LocalDateTime rangeStart,
	                                @RequestParam(name = "rangeEnd", required = false) @DateTimeFormat(pattern = formatDate) LocalDateTime rangeEnd,
	                                @RequestParam(name = "onlyAvailable", required = false) boolean onlyAvailable,
	                                @RequestParam(name = "sort", defaultValue = "EVENT_DATE") String sort,
	                                @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
	                                @Positive @RequestParam(name = "size", defaultValue = "10") Integer size,
	                                HttpServletRequest request) {
		return eventService.getEventsWithParametersWithText(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size, request);
	}

	@GetMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	public EventDto getUserEvent(@PathVariable long id, HttpServletRequest request) {
		return eventService.getEvent(id, request);
	}
}