package com.example.bankcards.dto.response;

import com.example.bankcards.entity.Role;

import java.time.ZonedDateTime;
import java.util.Set;
import java.util.UUID;

/**
 * @author 4ndr33w
 * @version 1.0
 */
public record UserResponseDto(
		UUID id,
		String name,
		String lastName,
		String email,
		String userName,
		ZonedDateTime createdAt,
		boolean active,
		boolean blocked,
		Set<Role> roles
) {
}