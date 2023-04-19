package ru.practicum.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentDto {
	private Long id;
	private Long user;
	private Long event;
	private String text;
	private String state;
	private LocalDateTime created;
}