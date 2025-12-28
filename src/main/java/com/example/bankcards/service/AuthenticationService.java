package com.example.bankcards.service;

import com.example.bankcards.dto.response.AuthenticationResponseDto;

/**
 * @author 4ndr33w
 * @version 1.0
 */
public interface AuthenticationService {
	
	/**
	 * Получить новую пару accessToken - refreshToken
	 * по имеющемуся валидному refreshToken
	 * @param refreshToken валидный токен обновления
	 * @return DTO с обновлёнными токенами
	 */
	AuthenticationResponseDto refreshAccessToken(String refreshToken);
}
