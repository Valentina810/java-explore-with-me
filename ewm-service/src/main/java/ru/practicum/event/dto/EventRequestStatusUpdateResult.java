package ru.practicum.event.dto;

import lombok.*;
import ru.practicum.request.dto.RequestDto;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventRequestStatusUpdateResult {
	private List<RequestDto> confirmedRequests;
	private List<RequestDto> rejectedRequests;
}