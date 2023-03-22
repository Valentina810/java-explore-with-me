package ru.practicum.dto;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class StatDto {
	private Long id;
	private String app;
	private String uri;
	private String ip;
	private String timestamp;
}
