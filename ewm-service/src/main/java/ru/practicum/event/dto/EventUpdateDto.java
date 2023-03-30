package ru.practicum.event.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventUpdateDto {
	private String title;
	private String description;
	private String annotation;
	private Long category;
	private LocationDto location;
	private String eventDate;
	private Boolean paid;
	private String stateAction;
	private Integer participantLimit;
	private Boolean requestModeration;
}