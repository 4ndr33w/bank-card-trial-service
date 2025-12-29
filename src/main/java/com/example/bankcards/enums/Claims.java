package com.example.bankcards.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author 4ndr33w
 * @version 1.0
 */
@Getter
@RequiredArgsConstructor
public enum Claims {
	
	USER_ID("userId"),
	EMAIL("email"),
	TOKEN_TYPE("tokenType"),
	ROLES("roles");
	
	private final String name;
}