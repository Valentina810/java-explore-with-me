package ru.practicum.request.service;

import ru.practicum.request.dto.RequestDto;

import java.util.List;

public interface RequestService {
	RequestDto addRequest(long userId, long eventId);

	RequestDto updateRequest(long userId, long requestId);

	List<RequestDto> getRequestUser(long userId);
}