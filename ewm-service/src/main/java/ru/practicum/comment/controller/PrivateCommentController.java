package ru.practicum.comment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.comment.dto.CommentCreateDto;
import ru.practicum.comment.dto.CommentDto;
import ru.practicum.comment.service.CommentService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping(path = "/users/{userId}/comment")
public class PrivateCommentController {
	private final CommentService commentService;

	@PostMapping("/events/{eventId}")
	@ResponseStatus(HttpStatus.CREATED)
	public CommentDto addComment(@PathVariable long userId,
	                             @PathVariable long eventId,
	                             @Valid @RequestBody CommentCreateDto commentCreateDto) {
		return commentService.addComment(userId, eventId, commentCreateDto);
	}

	@PatchMapping("/{commentId}")
	@ResponseStatus(HttpStatus.OK)
	public CommentDto updateComment(@PathVariable long userId,
	                                @PathVariable long commentId,
	                                @Valid @RequestBody CommentCreateDto commentCreateDto) {
		return commentService.updateComment(userId, commentId, commentCreateDto);
	}

	@DeleteMapping("/{commentId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteComment(@PathVariable long userId,
	                          @PathVariable long commentId) {
		commentService.deleteComment(userId, commentId);
	}

	@GetMapping
	public List<CommentDto> getComments(@PathVariable long userId,
	                                    @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
	                                    @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
		return commentService.getComments(userId, from, size);
	}
}