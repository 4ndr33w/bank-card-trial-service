package com.example.bankcards.exception.businessException;

public class CardActivationException extends RuntimeException {
	public CardActivationException(String message) {
		super(message);
	}
	public CardActivationException(String message, Throwable cause) {super(message, cause); }
}