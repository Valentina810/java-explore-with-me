package ru.practicum.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StatCreateDto {
	private String app;
	private String uri;
	private String ip;
	private String timestamp;
}
