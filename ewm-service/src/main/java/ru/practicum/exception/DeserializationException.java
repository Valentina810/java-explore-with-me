package ru.practicum.exception;

public class DeserializationException extends RuntimeException {
	public DeserializationException(String message) {
		super(message);
	}
}