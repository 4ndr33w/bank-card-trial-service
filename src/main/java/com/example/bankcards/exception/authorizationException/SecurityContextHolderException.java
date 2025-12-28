package com.example.bankcards.exception.authorizationException;

public class SecurityContextHolderException extends RuntimeException {
	public SecurityContextHolderException(String message) {super(message);}
	public SecurityContextHolderException(String message, Throwable cause) {super(message, cause);}
}
