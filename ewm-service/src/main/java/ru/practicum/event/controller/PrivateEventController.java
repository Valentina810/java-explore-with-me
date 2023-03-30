package ru.practicum.event.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.dto.EventCreateDto;
import ru.practicum.event.dto.EventDto;
import ru.practicum.event.dto.EventRequestStatusUpdateRequestDto;
import ru.practicum.event.dto.EventUpdateDto;
import ru.practicum.event.service.EventService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping(path = "/users/{userId}/events")
@Validated
public class PrivateEventController {

	private final EventService eventService;

	@Autowired
	public PrivateEventController(EventService eventService) {
		this.eventService = eventService;
	}

	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	public List<EventDto> getUserEvents(@PathVariable long userId,
	                                    @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
	                                    @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
		return eventService.getUserEvents(userId, from, size);
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public EventDto addEvent(@PathVariable long userId,
	                         @Valid @RequestBody EventCreateDto eventCreateDto) {
		return eventService.addEvent(userId, eventCreateDto);
	}

	@GetMapping("/{eventId}")
	@ResponseStatus(HttpStatus.OK)
	public EventDto getUserEvent(@PathVariable long userId,
	                             @PathVariable long eventId) {
		return eventService.getUserEvent(userId, eventId);
	}

	@PatchMapping("/{eventId}")
	@ResponseStatus(HttpStatus.OK)
	public EventDto updateEvent(@PathVariable long userId,
	                            @PathVariable long eventId,
	                            @Valid @RequestBody EventUpdateDto eventUpdateDto) {
		return eventService.updateEventUser(userId, eventId, eventUpdateDto);
	}

	@GetMapping("/{eventId}/requests")
	@ResponseStatus(HttpStatus.OK)
	public EventDto getRequestsEventTheUser(@PathVariable long userId,
	                                        @PathVariable long eventId) {
		return null;
	}

	@PatchMapping("/{eventId}/requests")
	@ResponseStatus(HttpStatus.OK)
	public EventDto getUpdateStatusRequest(@PathVariable long userId,
	                                       @PathVariable long eventId,
	                                       @Valid @RequestBody EventRequestStatusUpdateRequestDto eventRequestStatusUpdateRequestDto) {
		return null;
	}
}