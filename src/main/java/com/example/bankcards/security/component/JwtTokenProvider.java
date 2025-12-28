package com.example.bankcards.security.component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.bankcards.entity.Role;
import com.example.bankcards.exception.authorizationException.TokenExpirationException;
import com.example.bankcards.exception.authorizationException.TokenValidationException;
import com.example.bankcards.properties.JwtProperties;
import com.example.bankcards.security.AppUserDetails;
import com.example.bankcards.util.constant.Constants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

/**
 * @author 4ndr33w
 * @version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenProvider {
	
	private final JwtProperties jwtProperties;
	private final KeyProvider keyProvider;
	
	private Algorithm getAccessTokenAlgorithm() {
		return Algorithm.RSA256(
				keyProvider.getPublicKey(jwtProperties.getAccessPublic()),
				keyProvider.getPrivateKey(jwtProperties.getAccessPrivate()));
	}
	
	private Algorithm getRefreshTokenAlgorithm() {
		return Algorithm.RSA256(
				keyProvider.getPublicKey(jwtProperties.getRefreshPublic()),
				keyProvider.getPrivateKey(jwtProperties.getRefreshPrivate()));
	}
	
	private JWTVerifier getAccessTokenVerifier() {
		return JWT.require(getAccessTokenAlgorithm())
				.withIssuer(jwtProperties.getIssuer())
				.build();
	}
	
	private JWTVerifier getRefreshTokenVerifier() {
		return JWT.require(getRefreshTokenAlgorithm())
				.withIssuer(jwtProperties.getIssuer())
				.build();
	}
	
	/**
	 * Создание Access Token для пользователя
	 */
	public String createAccessToken(AppUserDetails userDetails) {
		Instant now = Instant.now();
		Instant expiry = now.plusMillis(jwtProperties.getAccessTokenLifetime());
		
		return JWT.create()
				.withIssuer(jwtProperties.getIssuer())
				.withAudience(jwtProperties.getAudience())
				.withSubject(userDetails.getUsername())
				.withClaim("userId", userDetails.getUser().getId().toString())
				.withClaim("email", userDetails.getUser().getEmail())
				.withClaim("tokenType", "access")
				.withClaim("roles", userDetails.getUser().getRoles().stream().map(Role::getAuthority)
						.toList())
				.withIssuedAt(Date.from(now))
				.withExpiresAt(Date.from(expiry))
				.withJWTId(UUID.randomUUID().toString())
				.sign(getAccessTokenAlgorithm());
	}
	
	/**
	 * Создание Refresh Token для пользователя
	 */
	public String createRefreshToken(AppUserDetails userDetails) {
		Instant now = Instant.now();
		Instant expiry = now.plusMillis(jwtProperties.getRefreshTokenLifetime());
		
		return JWT.create()
				.withIssuer(jwtProperties.getIssuer())
				.withAudience(jwtProperties.getAudience())
				.withSubject(userDetails.getUsername())
				.withClaim("userId", userDetails.getUser().getId().toString())
				.withClaim("tokenType", "refresh")
				.withIssuedAt(Date.from(now))
				.withExpiresAt(Date.from(expiry))
				.withJWTId(UUID.randomUUID().toString())
				.sign(getRefreshTokenAlgorithm());
	}
	
	/**
	 * Декодирование Access Token
	 */
	public DecodedJWT getDecodedAccessToken(String token) {
		try {
			return getAccessTokenVerifier().verify(token);
		} catch (TokenExpiredException e) {
			log.warn("{}: {}", Constants.ACCESS_TOKEN_EXPIRED_MESSAGE, e.getMessage());
			throw new TokenExpirationException(Constants.ACCESS_TOKEN_EXPIRED_MESSAGE, e);
		} catch (JWTVerificationException e) {
			log.warn("{}: {}", Constants.INVALID_ACCESS_TOKEN_MESSAGE, e.getMessage());
			throw new TokenValidationException(Constants.INVALID_ACCESS_TOKEN_MESSAGE, e);
		}
	}
	
	/**
	 * Декодирование Refresh Token
	 */
	public DecodedJWT getDecodedRefreshToken(String token) {
		try {
			return getRefreshTokenVerifier().verify(token);
		} catch (JWTVerificationException e) {
			log.error("{}: {}", Constants.INVALID_REFRESH_TOKEN_MESSAGE, e.getMessage());
			throw new TokenValidationException(Constants.INVALID_REFRESH_TOKEN_MESSAGE, e);
		}
	}
	
	/**
	 * Извлечение username из токена
	 */
	public String getUserNameFromToken(String token) {
		try {
			DecodedJWT decodedJWT = getDecodedAccessToken(token);
			return decodedJWT.getSubject();
		} catch (JWTVerificationException e) {
			log.error("{}: {}", Constants.FAILED_TO_GET_USERNAME_FROM_TOKEN_MESSAGE, e.getMessage());
			throw new TokenValidationException(Constants.FAILED_TO_GET_USERNAME_FROM_TOKEN_MESSAGE, e);
		}
	}
	
	/**
	 * Извлечение userId из Refresh Token
	 */
	public UUID getUserIdFromRefreshToken(String token) {
		try {
			DecodedJWT decodedJWT = getDecodedRefreshToken(token);
			return UUID.fromString(decodedJWT.getClaim("userId").asString());
		} catch (JWTVerificationException e) {
			log.error("{}: {}", Constants.FAILED_TO_GET_USER_ID_FROM_TOKEN_MESSAGE, e.getMessage());
			throw new TokenValidationException(Constants.FAILED_TO_GET_USER_ID_FROM_TOKEN_MESSAGE, e);
		}
	}
	
	/**
	 * Валидация Access Token
	 */
	public boolean validateAccessToken(String token) {
		try {
			getDecodedAccessToken(token);
			return true;
		} catch (JWTVerificationException e) {
			log.warn("{}: {}", Constants.INVALID_ACCESS_TOKEN_MESSAGE, e.getMessage());
			return false;
		}
	}
	
	/**
	 * Валидация Refresh Token
	 */
	public boolean validateRefreshToken(String token) {
		try {
			getDecodedRefreshToken(token);
			return true;
		} catch (JWTVerificationException e) {
			log.warn("{}: {}", Constants.INVALID_REFRESH_TOKEN_MESSAGE, e.getMessage());
			return false;
		}
	}
	
	/**
	 * Обновление Access Token с использованием Refresh Token
	 */
	public String refreshAccessToken(String refreshToken, AppUserDetails userDetails) {
		if (validateRefreshToken(refreshToken)) {
			// Дополнительная проверка: Id пользователя в refresh token должен совпадать
			UUID tokenUserId = getUserIdFromRefreshToken(refreshToken);
			if (!tokenUserId.equals(userDetails.getUser().getId())) {
				throw new RuntimeException("Несовпадение пользовательских данных в токене");
			}
			return createAccessToken(userDetails);
		}
		throw new TokenValidationException("Токен обновления не прошел валидацию");
	}
}