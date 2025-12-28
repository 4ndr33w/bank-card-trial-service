package com.example.bankcards.exception.handler;

import com.example.bankcards.exception.authorizationException.AuthenticationException;
import com.example.bankcards.exception.authorizationException.TokenExpirationException;
import com.example.bankcards.exception.authorizationException.TokenValidationException;
import com.example.bankcards.exception.dto.ErrorResponseDto;
import com.example.bankcards.properties.JwtProperties;
import com.example.bankcards.util.constant.Constants;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.ZonedDateTime;

/**
 * @author 4ndr33w
 * @version 1.0
 */
@Component
@RequiredArgsConstructor
public class SecurityExceptionHandler {
	
	private final ObjectMapper objectMapper;
	private final JwtProperties properties;
	
	public void accessDeniedHandler(HttpServletRequest request, HttpServletResponse response, Exception ex) throws IOException {
		printResponse(response, new ErrorResponseDto(
				HttpStatus.FORBIDDEN.value(),
				Constants.FORBIDDEN_MESSAGE,
				ZonedDateTime.now()
		));
	}
	
	public void unauthorizedHandler(HttpServletRequest request, HttpServletResponse response, Exception ex) throws IOException {
		String message;
		
		if (ex instanceof TokenExpirationException) {
			message = Constants.ACCESS_TOKEN_EXPIRED_MESSAGE;
		} else if (ex instanceof TokenValidationException) {
			message = Constants.INVALID_ACCESS_TOKEN_MESSAGE;
		} else if (ex instanceof AuthenticationException) {
			message = ex.getMessage();
		} else {
			message = Constants.UNAUTHORIZED_MESSAGE;
		}
		printResponse(response, new ErrorResponseDto(
				HttpStatus.UNAUTHORIZED.value(),
				message,
				ZonedDateTime.now()
		));
	}
	
	private void printResponse (HttpServletResponse response, ErrorResponseDto responseDto) throws IOException {
		response.setStatus(responseDto.httpStatus());
		response.setContentType("application/json; charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(objectMapper.writeValueAsString(responseDto));
	}
}
