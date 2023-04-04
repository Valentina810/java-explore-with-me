package ru.practicum.request.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.request.dto.RequestDto;
import ru.practicum.request.model.Request;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MapperRequest {
	public static RequestDto toRequestDto(Request request) {
		return RequestDto.builder()
				.id(request.getId())
				.event(request.getEvent().getId())
				.requester(request.getRequester().getId())
				.created(request.getCreated())
				.status(request.getStatus().getName())
				.build();
	}
}