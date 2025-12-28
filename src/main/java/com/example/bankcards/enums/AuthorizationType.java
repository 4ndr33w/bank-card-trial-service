package com.example.bankcards.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author 4ndr33w
 * @version 1.0
 */
@Getter
@RequiredArgsConstructor
public enum AuthorizationType {
	
	BEARER("Bearer "),
	BASIC("Basic ");
	
	public final String value;
}
