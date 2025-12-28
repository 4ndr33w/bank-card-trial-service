package com.example.bankcards.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author 4ndr33w
 * @version 1.0
 */
@Getter
@RequiredArgsConstructor
public enum HttpHeaderKey {
	
	AUTHORIZATION("Authorization");
	
	private final String value;
}
