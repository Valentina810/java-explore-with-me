package ru.practicum.comment.dto;

import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
public class CommentsUpdateState {
	@NotEmpty
	private List<Long> commentIds;
	@NotNull
	@NotBlank
	private String state;
}