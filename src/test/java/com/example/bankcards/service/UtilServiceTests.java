package com.example.bankcards.service;

import com.example.bankcards.entity.User;
import com.example.bankcards.exception.authorizationException.SecurityContextHolderException;
import com.example.bankcards.properties.CardProperties;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.security.data.AppUserDetails;
import com.example.bankcards.service.impl.UtilService;
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
	void getUserIdFromSecurityContext_ShouldReturnUserId_WhenUserIsAuthenticated() {
		// Arrange
		UUID expectedUserId = UUID.randomUUID();
		User user = new User();
		user.setId(expectedUserId);
		
		AppUserDetails userDetails = new AppUserDetails(user);
		
		when(securityContext.getAuthentication()).thenReturn(authentication);
		when(authentication.getPrincipal()).thenReturn(userDetails);
		SecurityContextHolder.setContext(securityContext);
		
		// Act
		UUID result = utilService.getUserIdFromSecurityContext();
		
		// Assert
		assertEquals(expectedUserId, result);
	}
	
	@Test
	void getUserIdFromSecurityContext_ShouldThrowException_WhenSecurityContextError() {
		// Arrange
		when(securityContext.getAuthentication()).thenReturn(null);
		SecurityContextHolder.setContext(securityContext);
		
		// Act & Assert
		assertThrows(SecurityContextHolderException.class, () -> utilService.getUserIdFromSecurityContext());
	}
	
	@Test
	void setPageLimit_ShouldReturnDefault_WhenLimitIsNull() {
		// Act
		int result = utilService.setPageLimit(null);
		
		// Assert
		assertEquals(5, result);
	}
	
	@Test
	void setPageLimit_ShouldReturnDefault_WhenLimitIsLessThanOne() {
		// Act
		int result = utilService.setPageLimit(0);
		
		// Assert
		assertEquals(5, result);
	}
	
	@Test
	void setPageLimit_ShouldReturnMax_WhenLimitIsGreaterThanTwenty() {
		// Act
		int result = utilService.setPageLimit(25);
		
		// Assert
		assertEquals(20, result);
	}
	
	@Test
	void setPageLimit_ShouldReturnSameValue_WhenLimitIsValid() {
		// Act
		int result = utilService.setPageLimit(15);
		
		// Assert
		assertEquals(15, result);
	}
	
	@Test
	void generateCvv_ShouldReturnThreeDigitString() {
		// Act
		String result = utilService.generateCvv();
		
		// Assert
		assertNotNull(result);
		assertEquals(3, result.length());
		assertTrue(result.matches("\\d{3}"));
	}
	
	@Test
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