package ru.practicum.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StatCreateDto {
	private String app;
	private String uri;
	private String ip;
	private String timestamp;
}
