package ru.practicum.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public final class ErrorResponse {
	private final String error;
	private final String description;
	private final String stackTrace;
}