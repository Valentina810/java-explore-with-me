package ru.practicum.compilation.dto;

import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.List;

@Getter
public class CompilationCreateDto {
	@NotNull
	private Boolean pinned;
	@NotNull
	@NotBlank
	private String title;

	private List<Long> events;

	public List<Long> getEvents() {
		if (events == null)
			return Collections.emptyList();
		else return events;
	}
}