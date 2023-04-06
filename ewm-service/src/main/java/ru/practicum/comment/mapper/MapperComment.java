package ru.practicum.comment.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.comment.dto.CommentDto;
import ru.practicum.comment.model.Comment;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MapperComment {
	public static CommentDto toCommentDto(Comment comment) {
		return CommentDto.builder()
				.id(comment.getId())
				.user(comment.getUser().getId())
				.event(comment.getEvent().getId())
				.text(comment.getText())
				.state(comment.getStateComment().getName())
				.created(comment.getCreated())
				.build();
	}
}