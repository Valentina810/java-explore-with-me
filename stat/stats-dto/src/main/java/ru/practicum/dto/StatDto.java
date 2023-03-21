package ru.practicum.dto;

import lombok.*;

import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class StatDto {
	private Long id;
	private String app;
	private String uri;
	private String ip;
	private LocalDateTime timestamp;
}
