package ru.practicum.event.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.category.model.Category;
import ru.practicum.event.model.Location;
import ru.practicum.user.model.User;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventDto {
	private Long id;
	private String title;
	private String description;
	private String annotation;
	private Category category;
	private User initiator;
	private Location location;
	private String state;
	private String createdOn;
	private String eventDate;
	private String publishedOn;
	private Boolean paid;
	private Long confirmedRequests;
	private Integer participantLimit;
	private Long views;
	private Boolean requestModeration;
}