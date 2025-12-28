package com.example.bankcards.dto.request;

import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

/**
 * @author 4ndr33w
 * @version 1.0
 */
public record CardRequestDto(
		@NotBlank
		UUID clientId
) {
}