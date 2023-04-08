package ru.practicum.comment.service;

import ru.practicum.comment.dto.CommentCreateDto;
import ru.practicum.comment.dto.CommentDto;
import ru.practicum.comment.dto.CommentsUpdateState;

import java.util.List;

public interface CommentService {
	CommentDto addComment(long userId, long eventId, CommentCreateDto commentCreateDto);

	CommentDto updateComment(long userId, long commentId, CommentCreateDto commentCreateDto);

	void deleteComment(long userId, long commentId);

	List<CommentDto> getComments(long userId, Integer from, Integer size);

	List<CommentDto> updateStateComments(CommentsUpdateState commentsUpdateState);

}