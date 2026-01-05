package com.example.bankcards.exception.businessException;

public class NegativeTransferAmountException extends RuntimeException {
	public NegativeTransferAmountException(String message) {
		super(message);
	}
  public NegativeTransferAmountException(String message, Throwable cause) {
    super(message, cause);
  }
}
