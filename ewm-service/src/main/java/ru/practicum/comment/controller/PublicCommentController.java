package ru.practicum.comment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.comment.dto.CommentPublicDto;
import ru.practicum.comment.dto.CommentPublicWithEventDto;
import ru.practicum.comment.service.CommentService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping(path = "/comment")
@ResponseStatus(HttpStatus.OK)
public class PublicCommentController {
	private final CommentService commentService;

	@GetMapping("/events/{eventId}")
	public List<CommentPublicDto> getCommentsForEvents(@PathVariable long eventId,
	                                                   @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
	                                                   @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
		return commentService.getCommentsForEvents(eventId, from, size);
	}

	@GetMapping("/{commentId}")
	public CommentPublicWithEventDto getComment(@PathVariable long commentId) {
		return commentService.getComment(commentId);
	}
}