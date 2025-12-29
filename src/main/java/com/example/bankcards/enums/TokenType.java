package com.example.bankcards.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author 4ndr33w
 * @version 1.0
 */
@Getter
@RequiredArgsConstructor
public enum TokenType {
	
	ACCESS_TOKEN("access"),
	REFRESH_TOKEN("refresh");
	
	private final String value;
}
