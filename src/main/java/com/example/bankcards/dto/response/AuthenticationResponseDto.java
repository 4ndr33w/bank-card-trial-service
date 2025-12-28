package com.example.bankcards.dto.response;

/**
 * @author 4ndr33w
 * @version 1.0
 */
public record AuthenticationResponseDto(
		String accessToken,
		String refreshToken,
		long accessTokenLifetimeMinutes,
		long refreshTokenLifetimeMinutes
) {
}
