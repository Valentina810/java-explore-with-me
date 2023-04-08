package ru.practicum.comment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.comment.dto.CommentDto;
import ru.practicum.comment.dto.CommentsUpdateState;
import ru.practicum.comment.service.CommentService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping(path = "/admin")
@ResponseStatus(HttpStatus.OK)
public class AdminCommentController {
	private final CommentService commentService;

	@PatchMapping("/comment")
	public List<CommentDto> updateStateComments(@Valid @RequestBody CommentsUpdateState commentsUpdateState) {
		return commentService.updateStateComments(commentsUpdateState);
	}
}