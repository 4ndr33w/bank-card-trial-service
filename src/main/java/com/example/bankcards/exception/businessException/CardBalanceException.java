package com.example.bankcards.exception.businessException;

public class CardBalanceException extends RuntimeException {
	public CardBalanceException(String message) {
		super(message);
	}
	public CardBalanceException(String message, Throwable cause) {
		super(message, cause);
	}
}
