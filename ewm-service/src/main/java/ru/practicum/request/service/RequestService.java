package ru.practicum.request.service;

import ru.practicum.event.dto.EventRequestStatusUpdateRequestDto;
import ru.practicum.event.dto.EventRequestStatusUpdateResult;
import ru.practicum.request.dto.RequestDto;

import java.util.List;

public interface RequestService {
	RequestDto addRequest(long userId, long eventId);

	RequestDto updateRequest(long userId, long requestId);

	List<RequestDto> getRequestUser(long userId);

	List<RequestDto> getRequestsForEventTheUser(long userId, long eventId);

	EventRequestStatusUpdateResult updateStatusRequests(long userId, long eventId, EventRequestStatusUpdateRequestDto eventRequestStatusUpdateRequestDto);
}