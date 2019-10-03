package com.uuhnaut69.todo.controller.advice;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.uuhnaut69.todo.exception.BadRequestException;
import com.uuhnaut69.todo.exception.ResourceNotFoundException;

@ControllerAdvice
public class CustomGlobalExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler({ ResourceNotFoundException.class })
	public ResponseEntity<ErrorResponse> resourceNotFound(Exception ex, WebRequest request) {
		logger.info(ex.getClass().getName());
		ErrorResponse errorResponse = new ErrorResponse();
		errorResponse.setStatus(HttpStatus.NOT_FOUND.value());
		errorResponse.setTimestamp(LocalDateTime.now());
		errorResponse.setError(ex.getMessage());
		return new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler({ BadRequestException.class })
	public ResponseEntity<ErrorResponse> badRequest(Exception ex, WebRequest request) {
		logger.info(ex.getClass().getName());
		ErrorResponse errorResponse = new ErrorResponse();
		errorResponse.setStatus(HttpStatus.BAD_REQUEST.value());
		errorResponse.setTimestamp(LocalDateTime.now());
		errorResponse.setError(ex.getMessage());
		return new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler({ Exception.class })
	public ResponseEntity<ErrorResponse> handleAll(Exception ex, WebRequest request) {
		logger.info(ex.getClass().getName());
		logger.error("error", ex);
		ErrorResponse errorResponse = new ErrorResponse();
		errorResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
		errorResponse.setTimestamp(LocalDateTime.now());
		errorResponse.setError(ex.getMessage());
		return new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
