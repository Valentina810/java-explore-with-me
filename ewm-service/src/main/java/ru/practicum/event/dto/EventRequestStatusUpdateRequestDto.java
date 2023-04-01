package ru.practicum.event.dto;

import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
public class EventRequestStatusUpdateRequestDto {
	@NotEmpty
	private List<Long> requestIds;
	@NotNull
	@NotBlank
	private String status;
}