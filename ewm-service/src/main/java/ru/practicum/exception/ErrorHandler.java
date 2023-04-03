package ru.practicum.exception;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {

	@ExceptionHandler
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public ErrorResponse handleInternalException(final InternalError e) {
		log.info(e.getMessage(), e);
		return ErrorResponse.builder()
				.status("INTERNAL_SERVER_ERROR")
				.reason("Unknown error")
				.message(getStackTrace(e))
				.timestamp(localDateTimeToString(LocalDateTime.now()))
				.build();
	}

	@ExceptionHandler
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ErrorResponse handleMethodArgumentNotValidException(final MethodArgumentNotValidException e) {
		return getBadRequest(e);
	}

	@ExceptionHandler
	@ResponseStatus(HttpStatus.FORBIDDEN)
	public ErrorResponse handleBadRequestException(final BadDataException e) {
		log.info(e.getMessage(), e);
		return ErrorResponse.builder()
				.status(HttpStatus.FORBIDDEN.name())
				.reason("For the requested operation the conditions are not met.")
				.message(getStackTrace(e))
				.timestamp(localDateTimeToString(LocalDateTime.now()))
				.build();
	}

	@ExceptionHandler
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ErrorResponse handleNumberFormatException(final NumberFormatException e) {
		return getBadRequest(e);
	}

	@ExceptionHandler
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ErrorResponse handleMissingServletRequestParameterException(final MissingServletRequestParameterException e) {
		log.info(e.getMessage(), e);
		return ErrorResponse.builder()
				.status(HttpStatus.BAD_REQUEST.name())
				.reason("Incorrectly made request.")
				.message(getStackTrace(e))
				.timestamp(localDateTimeToString(LocalDateTime.now()))
				.build();
	}

	@ExceptionHandler
	@ResponseStatus(HttpStatus.CONFLICT)
	public ErrorResponse handleBadRequestException(final ConstraintViolationException e) {
		log.info(e.getMessage(), e);
		return ErrorResponse.builder()
				.status(HttpStatus.CONFLICT.name())
				.reason("Integrity constraint has been violated.")
				.message(getStackTrace(e))
				.timestamp(localDateTimeToString(LocalDateTime.now()))
				.build();
	}

	@ExceptionHandler
	@ResponseStatus(HttpStatus.CONFLICT)
	public ErrorResponse handleBadRequestException(final ConditionsAreNotMetException e) {
		log.info(e.getMessage(), e);
		return ErrorResponse.builder()
				.status(HttpStatus.CONFLICT.name())
				.reason("For the requested operation the conditions are not met.")
				.message(getStackTrace(e))
				.timestamp(localDateTimeToString(LocalDateTime.now()))
				.build();
	}

	@ExceptionHandler
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ErrorResponse handleNotFoundException(final NotFoundException e) {
		log.info(e.getMessage(), e);
		return ErrorResponse.builder()
				.status(HttpStatus.NOT_FOUND.name())
				.reason("The required object was not found.")
				.message(getStackTrace(e))
				.timestamp(localDateTimeToString(LocalDateTime.now()))
				.build();
	}

	@ExceptionHandler
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public ErrorResponse handleDeserializationException(final DeserializationException e) {
		log.info(e.getMessage(), e);
		return ErrorResponse.builder()
				.status(HttpStatus.NOT_FOUND.name())
				.reason("Ошибка десериализации объекта!")
				.message(getStackTrace(e))
				.timestamp(localDateTimeToString(LocalDateTime.now()))
				.build();
	}

	private static ErrorResponse getBadRequest(Exception e) {
		log.info(e.getMessage(), e);
		return ErrorResponse.builder()
				.status(HttpStatus.BAD_REQUEST.name())
				.reason("Incorrectly made request.")
				.message(getStackTrace(e))
				.timestamp(localDateTimeToString(LocalDateTime.now()))
				.build();
	}

	private static String getStackTrace(Throwable e) {
		StringWriter sw = new StringWriter();
		e.printStackTrace(new PrintWriter(sw));
		return sw.toString();
	}

	public static String localDateTimeToString(LocalDateTime date) {
		return date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
	}
}