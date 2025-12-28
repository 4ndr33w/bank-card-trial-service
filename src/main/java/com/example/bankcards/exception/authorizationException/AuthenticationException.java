package com.example.bankcards.exception.authorizationException;

public class AuthenticationException extends RuntimeException {
	public AuthenticationException(String message) {
		super(message);
	}
	public AuthenticationException(String message, Throwable cause) {
		super(message, cause);
	}
}
