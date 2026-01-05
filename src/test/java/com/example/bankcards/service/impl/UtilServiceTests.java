package com.example.bankcards.service.impl;

import com.example.bankcards.entity.User;
import com.example.bankcards.exception.authorizationException.SecurityContextHolderException;
import com.example.bankcards.properties.CardProperties;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.security.data.AppUserDetails;
import com.example.bankcards.utils.TestUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author 4ndr33w
 * @version 1.0
 */
@ExtendWith(MockitoExtension.class)
public class UtilServiceTests {
	
	@Mock
	private CardProperties cardProperties;
	
	@Mock
	private CardRepository cardRepository;
	
	@Mock
	private Authentication authentication;
	
	@Mock
	private SecurityContext securityContext;
	
	@InjectMocks
	private UtilService utilService;
	
	@Test
	@DisplayName("Успешное получение ID пользователя из контекста безопасности")
	void getUserIdFromSecurityContext_ShouldReturnUserId_WhenUserIsAuthenticated() {
		User user = TestUtils.testUser();
		UUID expectedUserId = user.getId();
		
		AppUserDetails userDetails = new AppUserDetails(user);
		
		when(securityContext.getAuthentication()).thenReturn(authentication);
		when(authentication.getPrincipal()).thenReturn(userDetails);
		SecurityContextHolder.setContext(securityContext);

		UUID result = utilService.getUserIdFromSecurityContext();

		assertEquals(expectedUserId, result);
	}
	
	@Test
	@DisplayName("Ошибка получения ID пользователя из контекста безопасности")
	void getUserIdFromSecurityContext_ShouldThrowException_WhenSecurityContextError() {
		when(securityContext.getAuthentication()).thenReturn(null);
		SecurityContextHolder.setContext(securityContext);

		assertThrows(SecurityContextHolderException.class, () -> utilService.getUserIdFromSecurityContext());
	}
	
	@Test
	@DisplayName("Получить  минимальное значение лимита записей на странице при null")
	void setPageLimit_ShouldReturnDefault_WhenLimitIsNull() {
		int result = utilService.setPageLimit(null);

		assertEquals(5, result);
	}
	
	@Test
	@DisplayName("Получить дефолтное минимальное значение лимита записей на странице при значении меньше 1")
	void setPageLimit_ShouldReturnDefault_WhenLimitIsLessThanOne() {
		int result = utilService.setPageLimit(0);

		assertEquals(5, result);
	}
	
	@Test
	@DisplayName("Получить дефолтное максимальное значение лимита записей на странице при значении больше 20")
	void setPageLimit_ShouldReturnMax_WhenLimitIsGreaterThanTwenty() {
		int result = utilService.setPageLimit(25);

		assertEquals(20, result);
	}
	
	@Test
	@DisplayName("Получить значение лимита записей на странице при корректном значении")
	void setPageLimit_ShouldReturnSameValue_WhenLimitIsValid() {
		int result = utilService.setPageLimit(15);

		assertEquals(15, result);
	}
	
	@Test
	@DisplayName("Успешная генерация CVV")
	void generateCvv_ShouldReturnThreeDigitString() {
		String result = utilService.generateCvv();
		
		assertNotNull(result);
		assertEquals(3, result.length());
		assertTrue(result.matches("\\d{3}"));
	}
	
	@Test
	@DisplayName("Успешная генерация номера карты")
	void generateCardNumber_ShouldReturnUniqueCardNumber() {
		when(cardProperties.getBinPrefix()).thenReturn(123456);
		when(cardRepository.existsByCardNumber(anyString()))
				.thenReturn(false);

		String result = utilService.generateCardNumber();

		assertNotNull(result);
		assertTrue(result.matches("\\d{4} \\d{4} \\d{4} \\d{4}"));
		verify(cardRepository, atLeastOnce()).existsByCardNumber(anyString());
	}
}