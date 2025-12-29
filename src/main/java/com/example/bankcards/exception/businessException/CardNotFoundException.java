package com.example.bankcards.exception.businessException;

/**
 * @author 4ndr33w
 * @version 1.0
 */
public class CardNotFoundException extends RuntimeException {
		public CardNotFoundException(String message) {
				super(message);
		}
		public CardNotFoundException(String message, Throwable cause) {
				super(message, cause);
		}
}
