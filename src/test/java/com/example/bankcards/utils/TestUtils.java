package com.example.bankcards.utils;

import com.example.bankcards.dto.request.CardRequestDto;
import com.example.bankcards.dto.request.UserRequestDto;
import com.example.bankcards.dto.request.UserUpdateDto;
import com.example.bankcards.dto.response.AuthenticationResponseDto;
import com.example.bankcards.dto.response.CardBalanceResponseDto;
import com.example.bankcards.dto.response.CardResponseDto;
import com.example.bankcards.dto.response.UserPageViewResponseDto;
import com.example.bankcards.dto.response.UserResponseDto;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.Role;
import com.example.bankcards.entity.User;
import com.example.bankcards.enums.CardStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * @author 4ndr33w
 * @version 1.0
 */
public class TestUtils {
	
	public static UserRequestDto testUserRequestDto() {
		return new UserRequestDto(
				"Вася",
				"Пупкин",
				"pup0k@example.ru",
				"pup0ck",
				"qwErty!23@",
				LocalDate.of(1999, 1, 1));
	}
	
	public static User testUser() {
		return User.builder()
				.id(UUID.fromString("f70907de-196d-483f-8faa-b04e9d988b0c"))
				.userName("pup0ck")
				.password("$2a$10$FaenRvqyIkJe8DZ/1R2lne34Qxf5mghzCYnQDM1oMF3iSa4U5zhGe")
				.name("Вася")
				.lastName("Пупкин")
				.email("pup0k@example.ru")
				.active(true)
				.blocked(false)
				.createdAt(ZonedDateTime.now())
				.updatedAt(ZonedDateTime.now())
				.roles(Set.of(testUserRole()))
				.build();
	}

	
	public static UserResponseDto testUserResponseDto() {
		return new UserResponseDto(
				UUID.fromString("f70907de-196d-483f-8faa-b04e9d988b0c"),
				"Вася",
				"Пупкин",
				"pup0k@example.ru",
				"pup0ck",
				ZonedDateTime.now(),
				true,
				false,
				Set.of(testUserRole())
		);
	}
	
	public static UserResponseDto testUserResponseDto2() {
		return new UserResponseDto(
				UUID.fromString("f70907de-196d-483f-8faa-b04e9d988b1c"),
				"Андрей",
				"Батькович",
				"Andr33w@example.ru",
				"Andr33w",
				ZonedDateTime.now(),
				true,
				false,
				Set.of(testUserRole())
		);
	}
	
	public static User testUser2() {
		return User.builder()
				.id(UUID.fromString("f70907de-196d-483f-8faa-b04e9d988b1c"))
				.userName("Andr33w")
				.password("$2a$10$FaenRvqyIkJe8DZ/1R2lne34Qxf5mghzCYnQDM1oMF3iSa4U5zhGe")
				.name("Андрей")
				.lastName("Батькович")
				.email("Andr33w@example.ru")
				.active(true)
				.blocked(false)
				.createdAt(ZonedDateTime.now())
				.updatedAt(ZonedDateTime.now())
				.roles(Set.of(testUserRole()))
				.build();
	}
	
	public static Role testUserRole() {
		return Role.builder()
				.id(UUID.fromString("f70907de-196d-483f-8faa-b04e9d988b0a"))
				.role("USER")
				.build();
	}
	
	public static Role testAdminRole() {
		return Role.builder()
				.id(UUID.fromString("f70907de-196d-483f-8faa-b04e9d988b0b"))
				.role("ADMIN")
				.build();
	}
	
	public static UserPageViewResponseDto testUserPageViewResponseDto() {
		return new UserPageViewResponseDto(
				1,
				10,
				1L,
				2L,
				List.of(testUserResponseDto(), testUserResponseDto2())
		);
	}
	
	public static UserUpdateDto testUserUpdateDto() {
		return new UserUpdateDto(
				"Василий",
				"Тёркин",
				null
		);
	}
	
	public static User testUpdatedUser() {
		return User.builder()
				.id(UUID.fromString("f70907de-196d-483f-8faa-b04e9d988b0c"))
				.userName("pup0ck")
				.password("$2a$10$FaenRvqyIkJe8DZ/1R2lne34Qxf5mghzCYnQDM1oMF3iSa4U5zhGe")
				.name("Василий")
				.lastName("Тёркин")
				.email("pup0k@example.ru")
				.active(true)
				.blocked(false)
				.createdAt(ZonedDateTime.now())
				.updatedAt(ZonedDateTime.now())
				.roles(Set.of(testUserRole()))
				.build();
	}
	
	public static UserResponseDto testUpdatedUserResponseDto() {
		return new UserResponseDto(
				UUID.fromString("f70907de-196d-483f-8faa-b04e9d988b0c"),
				"Василий",
				"Тёркин",
				"pup0k@example.ru",
				"pup0ck",
				ZonedDateTime.now(),
				true,
				false,
				Set.of(testUserRole())
		);
	}
	
	public static CardRequestDto testCardRequestDto() {
		return new CardRequestDto(UUID.fromString("f70907de-196d-483f-8faa-b04e9d988b0c"));
	}
	
	public static CardRequestDto testCardRequestDto = new CardRequestDto(testUser().getId());
	
	
	public static Card testNewUserCard = Card.builder()
			.clientId(testUser().getId())
			.status(CardStatus.ACTIVE)
			.cardHolder("Вася Пупкин")
			.build();
	
	public static Card testNewSavedCard = Card.builder()
			.id(UUID.fromString("f70907df-196d-483f-8faa-b04e9d988b0c"))
			.clientId(testUser().getId())
			.status(CardStatus.ACTIVE)
			.cardHolder("Вася Пупкин")
			.build();
	
	public static Card testBlockedCard = Card.builder()
			.id(UUID.fromString("f70907df-196d-483f-8faa-b04e9d988b0c"))
			.clientId(testUser().getId())
			.status(CardStatus.BLOCKED)
			.cardHolder("Вася Пупкин")
			.build();
	
	public static CardResponseDto testCardResponseDto = new CardResponseDto(
			"1234 5678 9012 3456",
			"Вася Пупкин",
			String.valueOf(LocalDate.of(2030, 1, 1)),
			CardStatus.ACTIVE,
			BigDecimal.ZERO
	);

	public static User testUserWithOnlyUserRole() {
		return User.builder()
				.id(UUID.fromString("f70907de-196d-483f-8faa-b04e9d988b0c"))
				.userName("pup0ck")
				.password("$2a$10$FaenRvqyIkJe8DZ/1R2lne34Qxf5mghzCYnQDM1oMF3iSa4U5zhGe")
				.name("Вася")
				.lastName("Пупкин")
				.email("pup0k@example.ru")
				.active(true)
				.blocked(false)
				.createdAt(ZonedDateTime.now())
				.updatedAt(ZonedDateTime.now())
				.roles(Set.of(testUserRole()))
				.build();
	}
	
	public static User testUserWithOnlyAdminRole() {
		return User.builder()
				.id(UUID.fromString("f70907de-196d-483f-8faa-b04e9d988b0c"))
				.userName("pup0ck")
				.password("$2a$10$FaenRvqyIkJe8DZ/1R2lne34Qxf5mghzCYnQDM1oMF3iSa4U5zhGe")
				.name("Вася")
				.lastName("Пупкин")
				.email("pup0k@example.ru")
				.active(true)
				.blocked(false)
				.createdAt(ZonedDateTime.now())
				.updatedAt(ZonedDateTime.now())
				.roles(Set.of(testAdminRole()))
				.build();
	}
	
	public static User testUserWithUserAndAdminRoles() {
		return User.builder()
				.id(UUID.fromString("f70907ae-196d-483f-8faa-b04e9d988b0c"))
				.userName("pup0ck")
				.password("$2a$10$FaenRvqyIkJe8DZ/1R2lne34Qxf5mghzCYnQDM1oMF3iSa4U5zhGe")
				.name("Вася")
				.lastName("Пупкин")
				.email("pup0k@example.ru")
				.active(true)
				.blocked(false)
				.createdAt(ZonedDateTime.now())
				.updatedAt(ZonedDateTime.now())
				.roles(Set.of(testAdminRole(), testUserRole()))
				.build();
	}
	
	public static User testBlockedUserWithUserAndAdminRoles() {
		return User.builder()
				.id(UUID.fromString("f70907ae-196d-483f-8faa-b04e9d988b0c"))
				.userName("pup0ck")
				.password("$2a$10$FaenRvqyIkJe8DZ/1R2lne34Qxf5mghzCYnQDM1oMF3iSa4U5zhGe")
				.name("Вася")
				.lastName("Пупкин")
				.email("pup0k@example.ru")
				.active(true)
				.blocked(true)
				.createdAt(ZonedDateTime.now())
				.updatedAt(ZonedDateTime.now())
				.roles(Set.of(testAdminRole(), testUserRole()))
				.build();
	}
	
	public static String testRefreshToken() {
		return "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9." +
				"eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ." +
				"SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c";
	}
	
	public static String testInvalidRefreshToken() {
		return "invalid.refresh.token";
	}
	
	public static String testNewAccessToken() {
		return "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9." +
				"eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6Ik5ldyBBY2Nlc3MiLCJpYXQiOjE1MTYyMzkwMjJ9." +
				"new_access_token_signature";
	}
	
	public static String testNewRefreshToken() {
		return "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9." +
				"eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6Ik5ldyBSZWZyZXNoIiwiaWF0IjoxNTE2MjM5MDIyfQ." +
				"new_refresh_token_signature";
	}
	
	public static long testAccessTokenLifetime() {
		return 900000L; // 15 минут в миллисекундах
	}
	
	public static long testRefreshTokenLifetime() {
		return 2592000000L; // 30 дней в миллисекундах
	}
	
	public static Set<Role> testSetOfUserRole() {
		Set<Role> roles = new HashSet<>();
		roles.add(testUserRole());
		return roles;
	}
	
	public static Set<Role> testSetOfUserAndAdminRoles() {
		Set<Role> roles = new HashSet<>();
		roles.add(testUserRole());
		roles.add(testAdminRole());
		return roles;
	}
	
	public static AuthenticationResponseDto testAuthenticationResponseDto() {
		return new AuthenticationResponseDto(
				testNewAccessToken(),
				testNewRefreshToken(),
				testAccessTokenLifetime(),
				testRefreshTokenLifetime()
		);
	}
	
	public static Card testCard1() {
		return Card.builder()
				.id(UUID.fromString("f70907df-196d-483f-8faa-b04e9d988b0c"))
				.clientId(UUID.fromString("f70907de-196d-483f-8faa-b04e9d988b0c"))
				.cardNumber("1234 5678 9012 3456")
				.cardHolder("Вася Пупкин")
				.status(CardStatus.ACTIVE)
				.balance(new BigDecimal("1000.00"))
				.expirationDate(LocalDate.of(2030, 1, 1))
				.build();
	}
	
	public static Card testCard2() {
		return Card.builder()
				.id(UUID.fromString("f70907df-196d-483f-8faa-b04e9d988b0d"))
				.clientId(UUID.fromString("f70907de-196d-483f-8faa-b04e9d988b0c"))
				.cardNumber("9876 5432 1098 7654")
				.cardHolder("Вася Пупкин")
				.status(CardStatus.ACTIVE)
				.balance(new BigDecimal("500.00"))
				.expirationDate(LocalDate.of(2030, 1, 1))
				.build();
	}
	
	public static Card testBlockedUserCard() {
		return Card.builder()
				.id(UUID.fromString("f70907df-196d-483f-8faa-b04e9d988b0e"))
				.clientId(UUID.fromString("f70907de-196d-483f-8faa-b04e9d988b0c"))
				.cardNumber("1111 2222 3333 4444")
				.cardHolder("Вася Пупкин")
				.status(CardStatus.BLOCKED)
				.balance(new BigDecimal("0.00"))
				.expirationDate(LocalDate.of(2030, 1, 1))
				.build();
	}
	
	public static CardResponseDto testCardResponseDto1() {
		return new CardResponseDto(
				"1234 5678 9012 3456",
				"Вася Пупкин",
				"01/30",
				CardStatus.ACTIVE,
				new BigDecimal("1000.00")
		);
	}
	
	public static CardResponseDto testCardResponseDto2() {
		return new CardResponseDto(
				"9876 5432 1098 7654",
				"Вася Пупкин",
				"01/30",
				CardStatus.ACTIVE,
				new BigDecimal("500.00")
		);
	}
	
	public static CardResponseDto testMaskedCardResponseDto1() {
		return new CardResponseDto(
				"1234 **** **** 3456",
				"Вася Пупкин",
				"01/30",
				CardStatus.ACTIVE,
				new BigDecimal("1000.00")
		);
	}
	
	public static CardResponseDto testMaskedCardResponseDto2() {
		return new CardResponseDto(
				"9876 **** **** 7654",
				"Вася Пупкин",
				"01/30",
				CardStatus.ACTIVE,
				new BigDecimal("500.00")
		);
	}
	
	public static CardBalanceResponseDto testCardBalanceResponseDto() {
		return new CardBalanceResponseDto(
				new BigDecimal("1234.56")
		);
	}
}