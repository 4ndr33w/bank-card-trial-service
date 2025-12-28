package com.example.bankcards.exception.businessException;

public class UserRoleException extends RuntimeException {
	public UserRoleException(String message) {
		super(message);
	}
  public UserRoleException(String message, Throwable cause) {
    super(message, cause);
  }
}
