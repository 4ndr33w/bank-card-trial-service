package com.example.bankcards.exception.authorizationException;

public class TokenExpirationException extends RuntimeException {
	public TokenExpirationException(String message) {
		super(message);
	}
	public TokenExpirationException(String message, Throwable cause) {
		super(message, cause);
	}
}
