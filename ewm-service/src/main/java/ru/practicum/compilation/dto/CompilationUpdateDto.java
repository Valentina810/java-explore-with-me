package ru.practicum.compilation.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Collections;
import java.util.List;

@Getter
@Setter
public class CompilationUpdateDto {
	private Boolean pinned;
	private String title;
	private List<Long> events;

	public List<Long> getEvents() {
		if (events == null)
			return Collections.emptyList();
		else return events;
	}
}