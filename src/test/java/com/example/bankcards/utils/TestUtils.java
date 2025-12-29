package com.example.bankcards.utils;

import com.example.bankcards.dto.request.CardRequestDto;
import com.example.bankcards.dto.request.UserRequestDto;
import com.example.bankcards.dto.request.UserUpdateDto;
import com.example.bankcards.dto.response.UserPageViewResponseDto;
import com.example.bankcards.dto.response.UserResponseDto;
import com.example.bankcards.entity.Role;
import com.example.bankcards.entity.User;

import java.time.LocalDate;
import java.time.ZonedDateTime;
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
}