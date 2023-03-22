package ru.practicum.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.PrintWriter;
import java.io.StringWriter;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {

	@ExceptionHandler
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public ErrorResponse handleInternalException(final InternalError e) {
		log.info(e.getMessage(), e);
		return new ErrorResponse("Неизвестная ошибка сервера!", e.getMessage(), getStackTrace(e));
	}

//	@ExceptionHandler
//	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
//	public ErrorResponse handleDeserializationException(final DeserializationException e) {
//		log.info(e.getMessage(), e);
//		return new ErrorResponse("Ошибка десериализации объекта!", e.getMessage(), getStackTrace(e));
//	}

	private static String getStackTrace(Throwable e) {
		StringWriter sw = new StringWriter();
		e.printStackTrace(new PrintWriter(sw));
		return sw.toString();
	}
}