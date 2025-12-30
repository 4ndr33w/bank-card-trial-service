package com.example.bankcards.service;

import com.example.bankcards.dto.mapper.UserMapper;
import com.example.bankcards.dto.request.UserUpdateDto;
import com.example.bankcards.dto.response.UserResponseDto;
import com.example.bankcards.entity.Role;
import com.example.bankcards.entity.User;
import com.example.bankcards.enums.UserRole;
import com.example.bankcards.exception.businessException.RoleNotFoundException;
import com.example.bankcards.exception.businessException.UserNotFoundException;
import com.example.bankcards.exception.businessException.UserRoleException;
import com.example.bankcards.repository.RoleRepository;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.service.impl.AdminServiceImpl;
import com.example.bankcards.utils.TestUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author 4ndr33w
 * @version 1.0
 */
@ExtendWith(MockitoExtension.class)
public class AdminServiceTests {
	
	
	@Mock
	private UserRepository userRepository;
	
	@Mock
	private RoleRepository roleRepository;
	
	@Mock
	private UserMapper userMapper;
	
	@InjectMocks
	private AdminServiceImpl adminService;
	
	@Test
	@DisplayName("Удаление пользователя по ID - успешный сценарий")
	void deleteByClientId_ShouldDeleteUser_WhenUserExists() {
		UUID userId = TestUtils.testUser().getId();
		User existingUser = TestUtils.testUser();
		
		when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));

		boolean result = adminService.deleteByClientId(userId);

		assertTrue(result);
		verify(userRepository).findById(userId);
		verify(userRepository).delete(existingUser);
	}
	
	@Test
	@DisplayName("Удаление пользователя по ID - выбрасывает UserNotFoundException")
	void deleteByClientId_ShouldThrowUserNotFoundException_WhenUserNotFound() {
		UUID userId = UUID.randomUUID();
		
		when(userRepository.findById(userId)).thenReturn(Optional.empty());

		UserNotFoundException exception = assertThrows(UserNotFoundException.class,
				() -> adminService.deleteByClientId(userId));
		
		assertEquals("Не найден пользователь с id: " + userId, exception.getMessage());
		verify(userRepository).findById(userId);
		verify(userRepository, never()).delete(any());
	}
	
	@Test
	@DisplayName("Обновление пользователя по ID - успешный сценарий")
	void updateByClientId_ShouldUpdateUser_WhenUserExists() {
		UUID clientId = TestUtils.testUser().getId();
		UserUpdateDto updateDto = TestUtils.testUserUpdateDto();
		User existingUser = TestUtils.testUser();
		User updatedUser = TestUtils.testUpdatedUser();
		UserResponseDto expectedResponse = TestUtils.testUpdatedUserResponseDto();
		
		when(userRepository.findById(clientId)).thenReturn(Optional.of(existingUser));
		when(userMapper.mapUpdateToEntity(updateDto, existingUser)).thenReturn(updatedUser);
		when(userMapper.mapToDto(updatedUser)).thenReturn(expectedResponse);

		UserResponseDto result = adminService.updateByClientId(updateDto, clientId);
		
		assertNotNull(result);
		assertEquals(expectedResponse.id(), result.id());
		assertEquals("Василий", result.name());
		assertEquals("Тёркин", result.lastName());
		
		verify(userRepository).findById(clientId);
		verify(userMapper).mapUpdateToEntity(updateDto, existingUser);
		verify(userMapper).mapToDto(updatedUser);
	}
	
	@Test
	@DisplayName("Обновление пользователя по ID - выбрасывает UserNotFoundException")
	void updateByClientId_ShouldThrowUserNotFoundException_WhenUserNotFound() {
		UUID clientId = UUID.randomUUID();
		UserUpdateDto updateDto = TestUtils.testUserUpdateDto();
		
		when(userRepository.findById(clientId)).thenReturn(Optional.empty());

		UserNotFoundException exception = assertThrows(UserNotFoundException.class,
				() -> adminService.updateByClientId(updateDto, clientId));
		
		assertEquals("Не найден пользователь с id: " + clientId, exception.getMessage());
		verify(userRepository).findById(clientId);
		verify(userMapper, never()).mapUpdateToEntity(any(), any());
		verify(userMapper, never()).mapToDto(any());
	}
	
	@Test
	@DisplayName("Блокировка пользователя по ID - успешный сценарий")
	void blockByClientId_ShouldReturnTrue_WhenUserBlocked() {
		UUID clientId = TestUtils.testUser().getId();
		
		when(userRepository.blockUserById(clientId)).thenReturn(1);

		boolean result = adminService.blockByClientId(clientId);

		assertTrue(result);
		verify(userRepository).blockUserById(clientId);
	}
	
	@Test
	@DisplayName("Блокировка пользователя по ID - возвращает false при 0 обновленных строк")
	void blockByClientId_ShouldReturnFalse_WhenNoRowsUpdated() {
		UUID clientId = TestUtils.testUser().getId();
		
		when(userRepository.blockUserById(clientId)).thenReturn(0);

		boolean result = adminService.blockByClientId(clientId);
		
		assertFalse(result);
		verify(userRepository).blockUserById(clientId);
	}
	
	@Test
	@DisplayName("Разблокировка пользователя по ID - успешный сценарий")
	void unblockByClientId_ShouldReturnTrue_WhenUserUnblocked() {
		UUID clientId = TestUtils.testUser().getId();
		
		when(userRepository.unblockUserById(clientId)).thenReturn(1);

		boolean result = adminService.unblockByClientId(clientId);

		assertTrue(result);
		verify(userRepository).unblockUserById(clientId);
	}
	
	@Test
	@DisplayName("Активация пользователя по ID - успешный сценарий")
	void activateByClientId_ShouldReturnTrue_WhenUserActivated() {
		UUID clientId = TestUtils.testUser().getId();
		
		when(userRepository.activateUserById(clientId)).thenReturn(1);

		boolean result = adminService.activateByClientId(clientId);

		assertTrue(result);
		verify(userRepository).activateUserById(clientId);
	}
	
	@Test
	@DisplayName("Деактивация пользователя по ID - успешный сценарий")
	void deactivateByClientId_ShouldReturnTrue_WhenUserDeactivated() {
		UUID clientId = TestUtils.testUser().getId();
		
		when(userRepository.deactivateUserById(clientId)).thenReturn(1);

		boolean result = adminService.deactivateByClientId(clientId);

		assertTrue(result);
		verify(userRepository).deactivateUserById(clientId);
	}
	
	@Test
	@DisplayName("Добавление роли пользователю - успешный сценарий")
	void addRoleToUser_ShouldAddRole_WhenUserExistsAndRoleNotPresent() {
		UUID clientId = TestUtils.testUser().getId();
		UserRole role = UserRole.ADMIN;
		Role adminRole = TestUtils.testAdminRole();
		User existingUser = TestUtils.testUser();
		Set<Role> existingRoles = new HashSet<>(List.of(TestUtils.testUserRole()));

		User userWithNoAdminRole = User.builder()
				.id(existingUser.getId())
				.userName(existingUser.getUserName())
				.password(existingUser.getPassword())
				.name(existingUser.getName())
				.lastName(existingUser.getLastName())
				.email(existingUser.getEmail())
				.active(existingUser.isActive())
				.blocked(existingUser.isBlocked())
				.createdAt(existingUser.getCreatedAt())
				.updatedAt(existingUser.getUpdatedAt())
				.roles(existingRoles)
				.build();

		when(roleRepository.findByRole(role.getValue())).thenReturn(Optional.of(adminRole));
		when(userRepository.findById(clientId)).thenReturn(Optional.of(userWithNoAdminRole));

		boolean result = adminService.addRoleToUser(role, clientId);

		assertTrue(result);
		assertTrue(userWithNoAdminRole.getRoles().contains(adminRole));

		verify(roleRepository).findByRole(role.getValue());
		verify(userRepository).findById(clientId);
	}
	
	@Test
	@DisplayName("Добавление роли пользователю - выбрасывает RoleNotFoundException")
	void addRoleToUser_ShouldThrowRoleNotFoundException_WhenRoleNotFound() {
		UUID clientId = TestUtils.testUser().getId();
		UserRole role = UserRole.ADMIN;
		
		when(roleRepository.findByRole(role.getValue())).thenReturn(Optional.empty());
		RoleNotFoundException exception = assertThrows(RoleNotFoundException.class,
				() -> adminService.addRoleToUser(role, clientId));
		
		assertEquals("Роль ADMIN не найдена", exception.getMessage());
		verify(roleRepository).findByRole(role.getValue());
		verify(userRepository, never()).findById(any());
	}
	
	@Test
	@DisplayName("Добавление роли пользователю - выбрасывает UserNotFoundException")
	void addRoleToUser_ShouldThrowUserNotFoundException_WhenUserNotFound() {
		UUID clientId = TestUtils.testUser().getId();
		UserRole role = UserRole.ADMIN;
		Role adminRole = TestUtils.testAdminRole();
		
		when(roleRepository.findByRole(role.getValue())).thenReturn(Optional.of(adminRole));
		when(userRepository.findById(clientId)).thenReturn(Optional.empty());

		UserNotFoundException exception = assertThrows(UserNotFoundException.class,
				() -> adminService.addRoleToUser(role, clientId));
		
		assertEquals("Не найден пользователь с clientId: " + clientId, exception.getMessage());
		verify(roleRepository).findByRole(role.getValue());
		verify(userRepository).findById(clientId);
	}
	
	@Test
	@DisplayName("Добавление роли пользователю - выбрасывает UserRoleException при уже существующей роли")
	void addRoleToUser_ShouldThrowUserRoleException_WhenUserAlreadyHasRole() {
		UserRole role = UserRole.USER;
		Role userRole = TestUtils.testUserRole();
		User existingUser = TestUtils.testUserWithOnlyUserRole();
		Set<Role> existingRoles = new HashSet<>(List.of(TestUtils.testUserRole()));
		UUID clientId = existingUser.getId();
		
		User userWithUserRole = User.builder()
				.id(existingUser.getId())
				.userName(existingUser.getUserName())
				.password(existingUser.getPassword())
				.name(existingUser.getName())
				.lastName(existingUser.getLastName())
				.email(existingUser.getEmail())
				.active(existingUser.isActive())
				.blocked(existingUser.isBlocked())
				.createdAt(existingUser.getCreatedAt())
				.updatedAt(existingUser.getUpdatedAt())
				.roles(existingRoles)
				.build();

		when(roleRepository.findByRole(role.getValue())).thenReturn(Optional.of(userRole));
		when(userRepository.findById(clientId)).thenReturn(Optional.of(userWithUserRole));

		UserRoleException exception = assertThrows(UserRoleException.class,
				() -> adminService.addRoleToUser(role, clientId));

		assertEquals("Пользователь с id: " + clientId + " уже имеет роль USER", exception.getMessage());
		verify(roleRepository).findByRole(role.getValue());
		verify(userRepository).findById(clientId);
	}
	
	@Test
	@DisplayName("Удаление роли у пользователя - успешный сценарий")
	void removeRoleFromUser_ShouldRemoveRole_WhenUserExistsAndRolePresent() {
		UUID userId = TestUtils. testUserWithUserAndAdminRoles().getId();
		UserRole roleToRemove = UserRole.USER;
		Role userRole = TestUtils.testUserRole();
		Role adminRole = TestUtils.testAdminRole();
		User existingUser = TestUtils.testUserWithUserAndAdminRoles();
		
		Set<Role> existingRoles = new HashSet<>(List.of(userRole, adminRole));
		
		User userWithAllRoles = User.builder()
				.id(existingUser.getId())
				.userName(existingUser.getUserName())
				.password(existingUser.getPassword())
				.name(existingUser.getName())
				.lastName(existingUser.getLastName())
				.email(existingUser.getEmail())
				.active(existingUser.isActive())
				.blocked(existingUser.isBlocked())
				.createdAt(existingUser.getCreatedAt())
				.updatedAt(existingUser.getUpdatedAt())
				.roles(existingRoles)
				.build();

		when(roleRepository.findByRole(roleToRemove.getValue())).thenReturn(Optional.of(userRole));
		when(userRepository.findById(userId)).thenReturn(Optional.of(userWithAllRoles));

		boolean result = adminService.removeRoleFromUser(roleToRemove, userId);

		assertTrue(result);
		assertFalse(userWithAllRoles.getRoles().contains(userRole));

		verify(roleRepository).findByRole(roleToRemove.getValue());
		verify(userRepository).findById(userId);
	}
	
	@Test
	@DisplayName("Удаление роли у пользователя - выбрасывает RoleNotFoundException")
	void removeRoleFromUser_ShouldThrowRoleNotFoundException_WhenRoleNotFound() {
		UUID userId = TestUtils.testUser().getId();
		UserRole role = UserRole.ADMIN;
		
		when(roleRepository.findByRole(role.getValue())).thenReturn(Optional.empty());

		RoleNotFoundException exception = assertThrows(RoleNotFoundException.class,
				() -> adminService.removeRoleFromUser(role, userId));
		
		assertEquals("Роль ADMIN не найдена", exception.getMessage());
		verify(roleRepository).findByRole(role.getValue());
		verify(userRepository, never()).findById(any());
	}
	
	@Test
	@DisplayName("Удаление роли у пользователя - выбрасывает UserNotFoundException")
	void removeRoleFromUser_ShouldThrowUserNotFoundException_WhenUserNotFound() {
		UUID userId = TestUtils.testUser().getId();
		UserRole role = UserRole.USER;
		Role userRole = TestUtils.testUserRole();
		
		when(roleRepository.findByRole(role.getValue())).thenReturn(Optional.of(userRole));
		when(userRepository.findById(userId)).thenReturn(Optional.empty());

		UserNotFoundException exception = assertThrows(UserNotFoundException.class,
				() -> adminService.removeRoleFromUser(role, userId));
		
		assertEquals("Не найден пользователь с id: " + userId, exception.getMessage());
		verify(roleRepository).findByRole(role.getValue());
		verify(userRepository).findById(userId);
	}
	
	@Test
	@DisplayName("Удаление роли у пользователя - выбрасывает UserRoleException при отсутствующей роли")
	void removeRoleFromUser_ShouldThrowUserRoleException_WhenUserDoesNotHaveRole() {
		UUID userId = TestUtils.testUser().getId();
		UserRole role = UserRole.ADMIN;
		Role adminRole = TestUtils.testAdminRole();

		User userWithoutAdminRole = TestUtils.testUserWithOnlyUserRole();
		
		when(roleRepository.findByRole(role.getValue())).thenReturn(Optional.of(adminRole));
		when(userRepository.findById(userId)).thenReturn(Optional.of(userWithoutAdminRole));

		UserRoleException exception = assertThrows(UserRoleException.class,
				() -> adminService.removeRoleFromUser(role, userId));
		
		assertEquals("У пользователя с id: " + userId + " отсутствует роль ADMIN", exception.getMessage());
		verify(roleRepository).findByRole(role.getValue());
		verify(userRepository).findById(userId);
	}
	
	@Test
	@DisplayName("Блокировка пользователя по ID - несколько сценариев с разным количеством обновленных строк")
	void blockByClientId_ShouldHandleDifferentUpdateCounts() {
		UUID clientId = TestUtils.testUser().getId();

		when(userRepository.blockUserById(clientId)).thenReturn(1);
		boolean result1 = adminService.blockByClientId(clientId);
		assertTrue(result1);

		when(userRepository.blockUserById(clientId)).thenReturn(0);
		boolean result2 = adminService.blockByClientId(clientId);
		assertFalse(result2);

		when(userRepository.blockUserById(clientId)).thenReturn(2);
		boolean result3 = adminService.blockByClientId(clientId);
		assertTrue(result3);
		
		verify(userRepository, times(3)).blockUserById(clientId);
	}
	
	@Test
	@DisplayName("Удаление роли - пользователь с несколькими ролями")
	void removeRoleFromUser_ShouldWorkWithMultipleRoles() {
		UUID userId = TestUtils. testUserWithUserAndAdminRoles().getId();
		UserRole roleToRemove = UserRole.USER;
		Role userRole = TestUtils.testUserRole();
		Role adminRole = TestUtils.testAdminRole();
		User existingUser = TestUtils.testUserWithUserAndAdminRoles();
		
		Set<Role> existingRoles = new HashSet<>(List.of(userRole, adminRole));
		
		User userWithAllRoles = User.builder()
				.id(existingUser.getId())
				.userName(existingUser.getUserName())
				.password(existingUser.getPassword())
				.name(existingUser.getName())
				.lastName(existingUser.getLastName())
				.email(existingUser.getEmail())
				.active(existingUser.isActive())
				.blocked(existingUser.isBlocked())
				.createdAt(existingUser.getCreatedAt())
				.updatedAt(existingUser.getUpdatedAt())
				.roles(existingRoles)
				.build();

		when(roleRepository.findByRole(roleToRemove.getValue())).thenReturn(Optional.of(userRole));
		when(userRepository.findById(userId)).thenReturn(Optional.of(userWithAllRoles));

		boolean result = adminService.removeRoleFromUser(roleToRemove, userId);

		assertTrue(result);
		assertFalse(userWithAllRoles.getRoles().contains(userRole));
		assertTrue(userWithAllRoles.getRoles().contains(adminRole)); // ADMIN роль осталась

		verify(roleRepository).findByRole(roleToRemove.getValue());
		verify(userRepository).findById(userId);
	}
}