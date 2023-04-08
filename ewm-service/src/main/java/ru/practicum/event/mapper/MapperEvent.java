package ru.practicum.event.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.event.dto.EventCreateDto;
import ru.practicum.event.dto.EventDto;
import ru.practicum.event.model.Event;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MapperEvent {
	private static final String formatDate = "yyyy-MM-dd HH:mm:ss";

	public static Event toEvent(EventCreateDto eventCreateDto) {
		if (eventCreateDto.getParticipantLimit() == null) {
			eventCreateDto.setParticipantLimit(0);
		}
		return Event.builder()
				.title(eventCreateDto.getTitle())
				.description(eventCreateDto.getDescription())
				.annotation(eventCreateDto.getAnnotation())
				.createdOn(LocalDateTime.now())
				.publishedOn(LocalDateTime.now())
				.eventDate(stringToLocalDateTime(eventCreateDto.getEventDate()))
				.paid(eventCreateDto.getPaid())
				.confirmedRequests(0L)
				.views(0L)
				.participantLimit(eventCreateDto.getParticipantLimit())
				.requestModeration(eventCreateDto.getRequestModeration()).build();
	}

	public static LocalDateTime stringToLocalDateTime(String date) {
		if (date == null) {
			return null;
		} else return LocalDateTime.parse(date, DateTimeFormatter.ofPattern(formatDate));
	}

	public static String localDateTimeToString(LocalDateTime date) {
		return date.format(DateTimeFormatter.ofPattern(formatDate));
	}

	public static EventDto toEventDto(Event event) {
		return EventDto.builder()
				.id(event.getId())
				.title(event.getTitle())
				.description(event.getDescription())
				.annotation(event.getAnnotation())
				.category(event.getCategory())
				.initiator(event.getInitiator())
				.location(event.getLocation())
				.state(event.getState().getName())
				.createdOn(localDateTimeToString(event.getCreatedOn()))
				.eventDate(localDateTimeToString(event.getEventDate()))
				.publishedOn(localDateTimeToString(event.getPublishedOn()))
				.paid(event.getPaid())
				.confirmedRequests(event.getConfirmedRequests())
				.participantLimit(event.getParticipantLimit())
				.views(event.getViews())
				.requestModeration(event.getRequestModeration())
				.build();
	}
}