package ru.practicum.event.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventCreateDto {
	@NotNull
	@NotBlank
	private String title;
	private String description;
	@NotNull
	@NotBlank
	private String annotation;
	@NotNull
	private Long category;
	@NotNull
	private LocationDto location;
	@NotNull
	@NotBlank
	private String eventDate;
	@NotNull
	private Boolean paid;
	private Integer participantLimit;
	private Boolean requestModeration;
}