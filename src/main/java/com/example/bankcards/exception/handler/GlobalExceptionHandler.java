package com.example.bankcards.exception.handler;

import com.example.bankcards.exception.authorizationException.AuthenticationException;
import com.example.bankcards.exception.authorizationException.SecurityContextHolderException;
import com.example.bankcards.exception.authorizationException.TokenExpirationException;
import com.example.bankcards.exception.authorizationException.TokenValidationException;
import com.example.bankcards.exception.businessException.UserNotFoundException;
import com.example.bankcards.exception.businessException.UserRoleException;
import com.example.bankcards.exception.dto.ErrorResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author 4ndr33w
 * @version 1.0
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, String>> handleValidationException(MethodArgumentNotValidException ex) {
		Map<String, String> errors = new HashMap<>();
		errors.put("status", "400");
		errors.put("message", "Некорректный запрос");
		ex.getBindingResult().getFieldErrors().forEach(error -> {
			String field = error.getField();
			String message = error.getDefaultMessage();
			errors.put(field, message);
		});
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
		errors.put("timestamp", ZonedDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")));
		log.error("ERROR: Сработало исключение: {}; {}", ex.getClass(), ex.getMessage());
		return ResponseEntity.badRequest().body(errors);
	}
	
	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<ErrorResponseDto> handleConstraintViolationException(ConstraintViolationException ex) {
		log.error("ERROR: Сработало исключение: {}; {}", ex.getClass(), ex.getMessage());
		return buildResponse(ex, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	public ResponseEntity<ErrorResponseDto> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
		String message = Objects.requireNonNull(ex.getRootCause()).getMessage();
		log.error("ERROR: Сработало исключение: {}; {}", ex.getClass(), ex.getMessage());
		return ResponseEntity
				.status(HttpStatus.BAD_REQUEST)
				.body(new ErrorResponseDto(HttpStatus.BAD_REQUEST.value(), message, ZonedDateTime.now()));
	}
	
	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<ErrorResponseDto> handleUserNotFoundException(UserNotFoundException ex) {
		log.error("ERROR: Сработало исключение: {}; {}", ex.getClass(), ex.getMessage());
		return buildResponse(ex, HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(UserRoleException.class)
	public ResponseEntity<ErrorResponseDto> handleUserRoleException(UserRoleException ex) {
		log.error("ERROR: Сработало исключение: {}; {}", ex.getClass(), ex.getMessage());
		return buildResponse(ex, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(SecurityContextHolderException.class)
	public ResponseEntity<ErrorResponseDto> handleSecurityContextException(SecurityContextHolderException ex) {
		log.error("ERROR: Сработало исключение: {}; {}", ex.getClass(), ex.getMessage());
		return buildResponse(ex, HttpStatus.UNAUTHORIZED);
	}
	
	@ExceptionHandler(AuthenticationException.class)
	public ResponseEntity<ErrorResponseDto> handleAuthorizationException(AuthenticationException ex) {
		log.error("ERROR: Сработало исключение: {}; {}", ex.getClass(), ex.getMessage());
		return buildResponse(ex, HttpStatus.UNAUTHORIZED);
	}
	
	@ExceptionHandler(TokenValidationException.class)
	public ResponseEntity<ErrorResponseDto> handleTokenValidationException(TokenValidationException ex) {
		log.error("ERROR: Сработало исключение: {}; {}", ex.getClass(), ex.getMessage());
		return buildResponse(ex, HttpStatus.UNAUTHORIZED);
	}
	
	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<ErrorResponseDto> handleRuntimeException(RuntimeException ex) {
		log.error("ERROR: Сработало исключение: {}; {}", ex.getClass(), ex.getMessage());
		return buildResponse(ex, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@ExceptionHandler(TokenExpirationException.class)
	public ResponseEntity<ErrorResponseDto> handleTokenExpirationException(TokenExpirationException ex) {
		log.error("ERROR: Сработало исключение: {}; {}", ex.getClass(), ex.getMessage());
		return buildResponse(ex, HttpStatus.UNAUTHORIZED);
	}
	
	private ResponseEntity<ErrorResponseDto> buildResponse(Exception ex, HttpStatus status) {
		ErrorResponseDto response = new ErrorResponseDto(
				status.value(),
				ex.getMessage(),
				ZonedDateTime.now());
		return ResponseEntity.status(status).body(response);
	}
}