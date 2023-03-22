package ru.practicum.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class ViewStatDto {
	private String app;
	private String uri;
	private Long hits;
}