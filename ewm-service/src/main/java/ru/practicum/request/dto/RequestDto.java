package ru.practicum.request.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Builder
@Data
public class RequestDto {
	private Long id;
	private Long event;
	private Long requester;
	private LocalDateTime created;
	private String status;
}
