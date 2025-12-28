package com.example.bankcards.exception.businessException;

public class UserNotFoundException extends RuntimeException {
	public UserNotFoundException(String message) {super(message);}
	public UserNotFoundException(String message, Throwable cause) {super(message, cause);}
}
