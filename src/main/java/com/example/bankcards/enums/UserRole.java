package com.example.bankcards.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

/**
 * @author 4ndr33w
 * @version 1.0
 */
@Getter
@RequiredArgsConstructor
public enum UserRole implements GrantedAuthority {
	
	USER("USER"),
	ADMIN("ADMIN");
	
	public final String value;
	
	@Override
	public String getAuthority() {
		return value;
	}
}
