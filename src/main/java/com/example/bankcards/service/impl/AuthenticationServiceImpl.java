package com.example.bankcards.service.impl;

import com.example.bankcards.dto.response.AuthenticationResponseDto;
import com.example.bankcards.entity.User;
import com.example.bankcards.exception.authorizationException.TokenValidationException;
import com.example.bankcards.exception.businessException.UserNotFoundException;
import com.example.bankcards.properties.JwtProperties;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.security.data.AppUserDetails;
import com.example.bankcards.security.component.JwtTokenProvider;
import com.example.bankcards.service.AuthenticationService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * @author 4ndr33w
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
	
	private final JwtTokenProvider tokenProvider;
	private final UserRepository userRepository;
	private final JwtProperties jwtProperties;
	
	@Override
	public AuthenticationResponseDto refreshAccessToken(@NonNull String refreshToken) {
		boolean isTokenValid = tokenProvider.validateRefreshToken(refreshToken);
		
		if(!isTokenValid) {
			throw new TokenValidationException("Токен не прошел валидацию");
		}
		UUID userId = tokenProvider.getUserIdFromRefreshToken(refreshToken);
		User existingUser = userRepository.findById(userId).orElseThrow(
				() -> new UserNotFoundException("Не найден пользователь с id: %s".formatted(userId.toString())));
		AppUserDetails userDetails = new AppUserDetails(existingUser);
		
		return new AuthenticationResponseDto(
				tokenProvider.refreshAccessToken(refreshToken, userDetails),
				tokenProvider.createRefreshToken(userDetails),
				jwtProperties.getTokenLifeTime(jwtProperties.getAccessTokenLifetime()),
				jwtProperties.getTokenLifeTime(jwtProperties.getRefreshTokenLifetime())
		);
	}
}