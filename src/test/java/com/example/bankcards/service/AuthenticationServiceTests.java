package com.example.bankcards.service;

import com.example.bankcards.dto.response.AuthenticationResponseDto;
import com.example.bankcards.entity.User;
import com.example.bankcards.exception.authorizationException.TokenValidationException;
import com.example.bankcards.exception.businessException.UserNotFoundException;
import com.example.bankcards.properties.JwtProperties;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.security.component.JwtTokenProvider;
import com.example.bankcards.security.data.AppUserDetails;
import com.example.bankcards.service.impl.AuthenticationServiceImpl;
import com.example.bankcards.utils.TestUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
*
* @version 1.0
* @author 4ndr33w
*/
@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceTests {
	
	@Mock
	private JwtTokenProvider tokenProvider;
	@Mock
	private UserRepository userRepository;
	@Mock
	private JwtProperties jwtProperties;
	
	@InjectMocks
	private AuthenticationServiceImpl authenticationService;

	@Test
	@DisplayName("Успешное обновление пары токенов")
	void refreshAccessToken_ShouldReturnNewTokens_WhenRefreshTokenIsValid() {
		String refreshToken = TestUtils.testRefreshToken();
		UUID userId = TestUtils.testUser().getId();
		User existingUser = TestUtils.testUser();
		
		String newAccessToken = TestUtils.testNewAccessToken();
		String newRefreshToken = TestUtils.testNewRefreshToken();
		long accessTokenLifetime = TestUtils.testAccessTokenLifetime();
		long refreshTokenLifetime = TestUtils.testRefreshTokenLifetime();
		
		AuthenticationResponseDto expectedResponse = new AuthenticationResponseDto(
				newAccessToken,
				newRefreshToken,
				accessTokenLifetime,
				refreshTokenLifetime
		);
		
		when(tokenProvider.validateRefreshToken(refreshToken)).thenReturn(true);
		when(tokenProvider.getUserIdFromRefreshToken(refreshToken)).thenReturn(userId);
		when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));

		when(tokenProvider.refreshAccessToken(eq(refreshToken), any(AppUserDetails.class)))
				.thenReturn(newAccessToken);
		when(tokenProvider.createRefreshToken(any(AppUserDetails.class)))
				.thenReturn(newRefreshToken);
		when(jwtProperties.getTokenLifeTime(anyLong())).thenReturn(accessTokenLifetime).thenReturn(refreshTokenLifetime);

		AuthenticationResponseDto result = authenticationService.refreshAccessToken(refreshToken);

		assertNotNull(result);
		assertEquals(expectedResponse.accessToken(), result.accessToken());
		assertEquals(expectedResponse.refreshToken(), result.refreshToken());
		assertEquals(expectedResponse.accessTokenLifetimeMinutes(), result.accessTokenLifetimeMinutes());
		assertEquals(expectedResponse.refreshTokenLifetimeMinutes(), result.refreshTokenLifetimeMinutes());
		
		verify(tokenProvider).validateRefreshToken(refreshToken);
		verify(tokenProvider).getUserIdFromRefreshToken(refreshToken);
		verify(userRepository).findById(userId);
		verify(tokenProvider).refreshAccessToken(eq(refreshToken), any(AppUserDetails.class));
		verify(tokenProvider).createRefreshToken(any(AppUserDetails.class));
		verify(jwtProperties, times(2)).getTokenLifeTime(anyLong());
	}
	
	@Test
	@DisplayName("Обновление access токена - выбрасывает TokenValidationException при невалидном токене")
	void refreshAccessToken_ShouldThrowTokenValidationException_WhenTokenIsInvalid() {
		String invalidRefreshToken = TestUtils.testInvalidRefreshToken();
		
		when(tokenProvider.validateRefreshToken(invalidRefreshToken)).thenReturn(false);

		TokenValidationException exception = assertThrows(TokenValidationException.class,
				() -> authenticationService.refreshAccessToken(invalidRefreshToken));
		
		assertEquals("Токен не прошел валидацию", exception.getMessage());
		
		verify(tokenProvider).validateRefreshToken(invalidRefreshToken);
		verify(tokenProvider, never()).getUserIdFromRefreshToken(any());
		verify(userRepository, never()).findById(any());
		verify(tokenProvider, never()).refreshAccessToken(any(), any());
	}
	
	@Test
	@DisplayName("Обновление access токена - выбрасывает UserNotFoundException при отсутствии пользователя")
	void refreshAccessToken_ShouldThrowUserNotFoundException_WhenUserNotFound() {
		String refreshToken = TestUtils.testRefreshToken();
		UUID userId = TestUtils.testUser().getId();
		
		when(tokenProvider.validateRefreshToken(refreshToken)).thenReturn(true);
		when(tokenProvider.getUserIdFromRefreshToken(refreshToken)).thenReturn(userId);
		when(userRepository.findById(userId)).thenReturn(Optional.empty());

		UserNotFoundException exception = assertThrows(UserNotFoundException.class,
				() -> authenticationService.refreshAccessToken(refreshToken));
		
		assertEquals("Не найден пользователь с id: " + userId, exception.getMessage());
		
		verify(tokenProvider).validateRefreshToken(refreshToken);
		verify(tokenProvider).getUserIdFromRefreshToken(refreshToken);
		verify(userRepository).findById(userId);
		verify(tokenProvider, never()).refreshAccessToken(any(), any());
	}
	
	@Test
	@DisplayName("Обновление access токена - проверка создания AppUserDetails")
	void refreshAccessToken_ShouldCreateAppUserDetailsCorrectly() {
		String refreshToken = TestUtils.testRefreshToken();
		UUID userId = TestUtils.testUser().getId();
		String userName = TestUtils.testUser().getUserName();
		User testUser = TestUtils.testUser();
		
		when(tokenProvider.validateRefreshToken(refreshToken)).thenReturn(true);
		when(tokenProvider.getUserIdFromRefreshToken(refreshToken)).thenReturn(userId);
		when(userRepository.findById(userId)).thenReturn(Optional.of(testUser));
		when(tokenProvider.refreshAccessToken(eq(refreshToken), any(AppUserDetails.class))).thenReturn("newAccessToken");
		when(tokenProvider.createRefreshToken(any(AppUserDetails.class))).thenReturn("newRefreshToken");
		when(jwtProperties.getTokenLifeTime(anyLong())).thenReturn(900000L).thenReturn(2592000000L);

		AuthenticationResponseDto result = authenticationService.refreshAccessToken(refreshToken);

		assertNotNull(result);

		verify(tokenProvider).refreshAccessToken(eq(refreshToken), argThat(userDetails ->
				userDetails.getUser().getId().equals(userId) && userDetails.getUsername().equals(userName)));
		
		verify(tokenProvider).createRefreshToken(argThat(userDetails -> userDetails.getUser().getId().equals(userId)));
	}
	
	@Test
	@DisplayName("Обновление access токена - пустой refresh токен")
	void refreshAccessToken_ShouldHandleEmptyRefreshToken() {
		String emptyRefreshToken = "";
		
		when(tokenProvider.validateRefreshToken(emptyRefreshToken)).thenReturn(false);

		TokenValidationException exception = assertThrows(TokenValidationException.class,
				() -> authenticationService.refreshAccessToken(emptyRefreshToken));
		
		assertEquals("Токен не прошел валидацию", exception.getMessage());
		
		verify(tokenProvider).validateRefreshToken(emptyRefreshToken);
	}
	
	@Test
	@DisplayName("Обновление access токена - null refresh токен")
	void refreshAccessToken_ShouldThrowNullPointerException_WhenTokenIsNull() {
		assertThrows(NullPointerException.class,
				() -> authenticationService.refreshAccessToken(null));
		
		verify(tokenProvider, never()).validateRefreshToken(any());
	}
	
	@Test
	@DisplayName("Обновление access токена - пользователь заблокирован")
	void refreshAccessToken_ShouldWork_WhenUserIsBlocked() {
		String refreshToken = TestUtils.testRefreshToken();
		User blockedUser = TestUtils.testBlockedUserWithUserAndAdminRoles();
		UUID userId = blockedUser.getId();
		
		when(tokenProvider.validateRefreshToken(refreshToken)).thenReturn(true);
		when(tokenProvider.getUserIdFromRefreshToken(refreshToken)).thenReturn(userId);
		when(userRepository.findById(userId)).thenReturn(Optional.of(blockedUser));

		when(tokenProvider.refreshAccessToken(eq(refreshToken), any(AppUserDetails.class)))
				.thenReturn("newAccessToken");
		when(tokenProvider.createRefreshToken(any(AppUserDetails.class)))
				.thenReturn("newRefreshToken");
		when(jwtProperties.getTokenLifeTime(anyLong())).thenReturn(900000L).thenReturn(2592000000L);

		AuthenticationResponseDto result = authenticationService.refreshAccessToken(refreshToken);

		assertNotNull(result);
		
		verify(tokenProvider).validateRefreshToken(refreshToken);
		verify(userRepository).findById(userId);
		verify(tokenProvider).refreshAccessToken(eq(refreshToken), any(AppUserDetails.class));
		verify(tokenProvider).createRefreshToken(any(AppUserDetails.class));
	}
}