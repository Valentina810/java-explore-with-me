package ru.practicum.request.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.request.dto.RequestDto;
import ru.practicum.request.service.RequestService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users/{userId}/requests")
@ResponseStatus(HttpStatus.OK)
public class PrivateRequestController {

	private final RequestService requestService;

	@GetMapping
	public List<RequestDto> getRequestUser(@PathVariable long userId) {
		return requestService.getRequestUser(userId);
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public RequestDto addRequest(@PathVariable long userId,
	                             @RequestParam long eventId) {
		return requestService.addRequest(userId, eventId);
	}

	@PatchMapping("/{requestId}/cancel")
	public RequestDto updateRequest(@PathVariable long userId,
	                                @PathVariable long requestId) {
		return requestService.updateRequest(userId, requestId);
	}
}