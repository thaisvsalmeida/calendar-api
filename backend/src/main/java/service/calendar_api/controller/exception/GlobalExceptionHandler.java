package service.calendar_api.controller.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import service.calendar_api.exception.DuplicatedBlockException;
import service.calendar_api.exception.InvalidEnumArgumentException;
import service.calendar_api.exception.ResourceNotFoundException;

import java.time.LocalDateTime;
import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<StandardError> resourceNotFound(ResourceNotFoundException e, HttpServletRequest request) {
		var error = new StandardError(
				LocalDateTime.now().toString(),
				HttpStatus.NOT_FOUND.value(),
				"Resource Not Found",
				e.getMessage(),
				request.getRequestURI());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	private ResponseEntity<StandardError> methodArgumentNotValidException(MethodArgumentNotValidException e, HttpServletRequest request) {
		List<String> errors = e.getBindingResult().getAllErrors().stream()
				.map(DefaultMessageSourceResolvable::getDefaultMessage)
				.toList();

		var error = new StandardError(
				LocalDateTime.now().toString(),
				HttpStatus.BAD_REQUEST.value(),
				"Method Argument Not Valid",
				errors,
				request.getRequestURI());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
	}

	@ExceptionHandler(MissingServletRequestParameterException.class)
	private ResponseEntity<StandardError> missingServletRequestParameterException(MissingServletRequestParameterException e, HttpServletRequest request) {
		var error = new StandardError(
				LocalDateTime.now().toString(),
				HttpStatus.BAD_REQUEST.value(),
				"Missing Servlet Request Parameter",
				e.getMessage(),
				request.getRequestURI());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
	}

	@ExceptionHandler(InvalidEnumArgumentException.class)
	public ResponseEntity<StandardError> invalidEnumArgumentException(InvalidEnumArgumentException e, HttpServletRequest request) {
		var error = new StandardError(
				LocalDateTime.now().toString(),
				HttpStatus.BAD_REQUEST.value(),
				"Invalid Enum Argument",
				e.getMessage(),
				request.getRequestURI());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<StandardError> httpMessageNotReadableException(HttpMessageNotReadableException e, HttpServletRequest request) {
		var error = new StandardError(
				LocalDateTime.now().toString(),
				HttpStatus.BAD_REQUEST.value(),
				"Http Message Not Readable",
				e.getMessage(),
				request.getRequestURI());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
	}

	@ExceptionHandler(DuplicatedBlockException.class)
	public ResponseEntity<StandardError> duplicatedBlockException(DuplicatedBlockException e, HttpServletRequest request) {
		var error = new StandardError(
				LocalDateTime.now().toString(),
				HttpStatus.CONFLICT.value(),
				"Duplicated Block Recurring Event",
				e.getMessage(),
				request.getRequestURI());
		return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
	}
}
