package ru.practicum.compilation.dto;

import lombok.*;
import ru.practicum.event.model.Event;

import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CompilationDto {
	private Long id;
	private Boolean pinned;
	private String title;
	private List<Event> events;
}