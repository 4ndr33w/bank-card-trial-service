package com.example.bankcards.service;

import com.example.bankcards.dto.mapper.UserMapper;
import com.example.bankcards.dto.request.UserRequestDto;
import com.example.bankcards.dto.request.UserUpdateDto;
import com.example.bankcards.dto.response.UserPageViewResponseDto;
import com.example.bankcards.dto.response.UserResponseDto;
import com.example.bankcards.entity.Role;
import com.example.bankcards.entity.User;
import com.example.bankcards.enums.UserRole;
import com.example.bankcards.exception.businessException.RoleNotFoundException;
import com.example.bankcards.exception.businessException.UserCreationException;
import com.example.bankcards.exception.businessException.UserNotFoundException;
import com.example.bankcards.repository.RoleRepository;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.service.impl.UserServiceImpl;
import com.example.bankcards.service.impl.UtilService;
import com.example.bankcards.utils.TestUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author 4ndr33w
 * @version 1.0
 */
@ExtendWith(MockitoExtension.class)
public class UserServiceTests {
	
	@Mock
	private UserRepository userRepository;
	@Mock
	private RoleRepository roleRepository;
	@Mock
	private UtilService utilService;
	@Mock
	private UserMapper userMapper;
	@InjectMocks
	private UserServiceImpl userService;
	
	// ============================================================
	// ==========================CREATE============================
	// ============================================================
	@Test
	@DisplayName("Успешное создание пользователя")
	void create_ShouldCreateUser_WhenEmailDoesNotExist() {
		UserRequestDto request = TestUtils.testUserRequestDto();
		
		Role userRole = TestUtils.testUserRole();
		
		User mappedUser = TestUtils.testUser();
		User savedUser = TestUtils.testUser();
		savedUser.setId(UUID.randomUUID());
		
		UserResponseDto expectedResponse = TestUtils.testUserResponseDto();
		
		when(userRepository.existsByEmail(request.email())).thenReturn(false);
		when(roleRepository.findByRole(UserRole.USER.getValue())).thenReturn(Optional.of(userRole));
		when(userMapper.mapRequestToEntity(request, userRole)).thenReturn(mappedUser);
		when(userRepository.save(mappedUser)).thenReturn(savedUser);
		when(userMapper.mapToDto(savedUser)).thenReturn(expectedResponse);

		UserResponseDto result = userService.create(request);

		assertNotNull(result);
		assertEquals(expectedResponse.id(), result.id());
		assertEquals(expectedResponse.email(), result.email());
		verify(userRepository).existsByEmail(request.email());
		verify(roleRepository).findByRole(UserRole.USER.getValue());
		verify(userRepository).save(mappedUser);
		verify(userMapper).mapToDto(savedUser);
	}
	
	@Test
	@DisplayName("Ошибка создания пользователя с уже существующим email")
	void create_ShouldThrowUserCreationException_WhenEmailAlreadyExists() {
		UserRequestDto request = TestUtils.testUserRequestDto();
		
		when(userRepository.existsByEmail(request.email())).thenReturn(true);

		assertThrows(UserCreationException.class, () -> userService.create(request));
		verify(userRepository).existsByEmail(request.email());
		verify(roleRepository, never()).findByRole(anyString());
		verify(userRepository, never()).save(any());
	}
	
	@Test
	@DisplayName("Ошибка создания пользователя с несуществующей ролью")
	void create_ShouldThrowRoleNotFoundException_WhenRoleNotFound() {
		UserRequestDto request = TestUtils.testUserRequestDto();
		
		when(userRepository.existsByEmail(request.email())).thenReturn(false);
		when(roleRepository.findByRole(UserRole.USER.getValue())).thenReturn(Optional.empty());

		assertThrows(RoleNotFoundException.class, () -> userService.create(request));
		verify(userRepository).existsByEmail(request.email());
		verify(roleRepository).findByRole(UserRole.USER.getValue());
		verify(userRepository, never()).save(any());
	}
	// ============================================================
	// ==========================CREATE============================
	// ============================================================
	
	
	// ============================================================
	// ==========================UPDATE============================
	// ============================================================
	
	@Test
	@DisplayName("Успешное обновление пользователя")
	void update_ShouldUpdateUser_WhenUserExists() {
		UserUpdateDto update = TestUtils.testUserUpdateDto();
		User existingUser = TestUtils.testUser();
		User updatedUser = TestUtils.testUpdatedUser();
		UUID userId = existingUser.getId();
		
		UserResponseDto expectedResponse = TestUtils.testUpdatedUserResponseDto();
		
		when(utilService.getUserIdFromSecurityContext()).thenReturn(userId);
		when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
		when(userMapper.mapUpdateToEntity(update, existingUser)).thenReturn(updatedUser);
		when(userMapper.mapToDto(updatedUser)).thenReturn(expectedResponse);

		UserResponseDto result = userService.update(update);

		assertNotNull(result);
		assertEquals(expectedResponse.id(), result.id());
		assertEquals(expectedResponse.name(), result.name());
		verify(utilService).getUserIdFromSecurityContext();
		verify(userRepository).findById(userId);
		verify(userMapper).mapUpdateToEntity(update, existingUser);
		verify(userMapper).mapToDto(updatedUser);
	}
	
	@Test
	@DisplayName("Ошибка обновления пользователя с несуществующим id")
	void update_ShouldThrowUserNotFoundException_WhenUserNotFound() {
		UUID userId = TestUtils.testUser().getId();
		UserUpdateDto update = TestUtils.testUserUpdateDto();
		
		when(utilService.getUserIdFromSecurityContext()).thenReturn(userId);
		when(userRepository.findById(userId)).thenReturn(Optional.empty());

		assertThrows(UserNotFoundException.class, () -> userService.update(update));
		verify(utilService).getUserIdFromSecurityContext();
		verify(userRepository).findById(userId);
		verify(userMapper, never()).mapUpdateToEntity(any(), any());
		verify(userMapper, never()).mapToDto(any());
	}
	// ============================================================
	// ==========================UPDATE============================
	// ============================================================
	
	
	// ============================================================
	// ========================FIND_BY_ID==========================
	// ============================================================
	@Test
	@DisplayName("Успешное получение пользователя по id")
	void findById_ShouldReturnUser_WhenUserExists() {
		User existingUser = TestUtils.testUser();
		UUID userId = existingUser.getId();
		UserResponseDto expectedResponse = TestUtils.testUserResponseDto();
		
		when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
		when(userMapper.mapToDto(existingUser)).thenReturn(expectedResponse);

		UserResponseDto result = userService.findById(userId);

		assertNotNull(result);
		assertEquals(expectedResponse.id(), result.id());
		verify(userRepository).findById(userId);
		verify(userMapper).mapToDto(existingUser);
	}
	
	@Test
	@DisplayName("Ошибка получения пользователя с несуществующим id")
	void findById_ShouldThrowUserNotFoundException_WhenUserNotFound() {
		UUID userId = UUID.randomUUID();
		
		when(userRepository.findById(userId)).thenReturn(Optional.empty());

		assertThrows(UserNotFoundException.class, () -> userService.findById(userId));
		verify(userRepository, times(1)).findById(userId);
		verify(userMapper, never()).mapToDto(any());
	}
	// ============================================================
	// ========================FIND_BY_ID==========================
	// ============================================================
	
	
	// ============================================================
	// ===========================FIND=============================
	// ============================================================
	@Test
	@DisplayName("Успешное получение текущего пользователя")
	void find_ShouldReturnCurrentUser() {
		User existingUser = TestUtils.testUser();
		UUID userId = existingUser.getId();
		UserResponseDto expectedResponse = TestUtils.testUserResponseDto();
		
		when(utilService.getUserIdFromSecurityContext()).thenReturn(userId);
		when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
		when(userMapper.mapToDto(existingUser)).thenReturn(expectedResponse);

		UserResponseDto result = userService.find();

		assertNotNull(result);
		assertEquals(expectedResponse.id(), result.id());
		verify(utilService).getUserIdFromSecurityContext();
		verify(userRepository).findById(userId);
		verify(userMapper).mapToDto(existingUser);
	}
	// ============================================================
	// ===========================FIND=============================
	// ============================================================
	
	
	// ============================================================
	// ==========================DELETE============================
	// ============================================================
	@Test
	@DisplayName("Успешное удаление текущего пользователя")
	void delete_ShouldDeleteCurrentUser() {
		User authenticatedUser = TestUtils.testUser();
		
		when(utilService.getUserFromSecurityContext()).thenReturn(authenticatedUser);

		userService.delete();

		verify(utilService).getUserFromSecurityContext();
		verify(userRepository).deleteById(authenticatedUser.getId());
	}
	// ============================================================
	// ==========================DELETE============================
	// ============================================================
	
	
	
	
	
	
	
	@Test
	@DisplayName("Успешное получение пользователя по email")
	void findByEmail_ShouldReturnUser_WhenEmailExists() {
		User existingUser = TestUtils.testUser();
		UserResponseDto expectedResponse = TestUtils.testUserResponseDto();
		String email = existingUser.getEmail();
		
		when(userRepository.findByEmail(email)).thenReturn(Optional.of(existingUser));
		when(userMapper.mapToDto(existingUser)).thenReturn(expectedResponse);

		UserResponseDto result = userService.findByEmail(email);

		assertNotNull(result);
		assertEquals(expectedResponse.email(), result.email());
		verify(userRepository).findByEmail(email);
		verify(userMapper).mapToDto(existingUser);
	}
	
	@Test
	@DisplayName("Получение списка пользователей с пагинацией")
	void findAllByPage_ShouldReturnPaginatedUsers() {
		int page = 1;
		int limit = 10;
		int expectedPage = 0;
		
		User testUser1 = TestUtils.testUser();
		User testUser2 = TestUtils.testUser2();
		List<User> users = List.of(testUser1, testUser2);
		
		Page<User> usersPage = new PageImpl<>(users, PageRequest.of(expectedPage, limit), 2);
		
		UserResponseDto userDto1 = TestUtils.testUserResponseDto();
		UserResponseDto userDto2 = TestUtils.testUserResponseDto2();
		
		UserPageViewResponseDto expectedResponse = TestUtils.testUserPageViewResponseDto();
		
		when(utilService.setPageLimit(limit)).thenReturn(limit);
		when(userRepository.findAll(any(Pageable.class))).thenReturn(usersPage);
		when(userRepository.count()).thenReturn(2L);

		when(userMapper.mapToDto(testUser1)).thenReturn(userDto1);
		when(userMapper.mapToDto(testUser2)).thenReturn(userDto2);

		UserPageViewResponseDto result = userService.findAllByPage(page, limit);

		assertNotNull(result);
		assertEquals(expectedResponse.currentPage(), result.currentPage());
		assertEquals(expectedResponse.limit(), result.limit());
		assertEquals(expectedResponse.totalPages(), result.totalPages());
		assertEquals(expectedResponse.totalUsers(), result.totalUsers());
		assertEquals(expectedResponse.users().size(), result.users().size());
		assertEquals(expectedResponse.users().get(0).email(), result.users().get(0).email());
		assertEquals(expectedResponse.users().get(1).email(), result.users().get(1).email());
		
		verify(userRepository).findAll(any(Pageable.class));
		verify(userRepository).count();
		verify(userMapper, times(2)).mapToDto(any(User.class));
	}
	
	@Test
	@DisplayName("Получение списка пользователей с page меньше 1")
	void findAllByPage_ShouldHandleNegativePage() {
		Integer page = -1;
		Integer limit = 10;
		int expectedPage = 0;
		
		User testUser = TestUtils.testUser();
		List<User> users = List.of(testUser);
		Page<User> usersPage = new PageImpl<>(users, PageRequest.of(expectedPage, limit), 1);
		
		UserResponseDto userDto = TestUtils.testUserResponseDto();
		
		when(utilService.setPageLimit(limit)).thenReturn(limit);
		when(userRepository.findAll(any(Pageable.class))).thenReturn(usersPage);
		when(userRepository.count()).thenReturn(1L);
		when(userMapper.mapToDto(testUser)).thenReturn(userDto);

		UserPageViewResponseDto result = userService.findAllByPage(page, limit);

		assertNotNull(result);
		assertEquals(1, result.currentPage());
		verify(utilService).setPageLimit(limit);
	}
	
	@Test
	@DisplayName("Попытка блокировки уже заблокированного пользователя")
	void block_ShouldReturnTrue_WhenUserIsBlocked() {
		User authenticatedUser = TestUtils.testUser();
		
		when(utilService.getUserFromSecurityContext()).thenReturn(authenticatedUser);
		when(userRepository.blockUserById(authenticatedUser.getId())).thenReturn(1);

		boolean result = userService.block();

		assertTrue(result);
		verify(utilService).getUserFromSecurityContext();
		verify(userRepository).blockUserById(authenticatedUser.getId());
	}
	
	@Test
	@DisplayName("Успешная блокировка пользователя")
	void block_ShouldReturnFalse_WhenUserNotBlocked() {
		User authenticatedUser = TestUtils.testUser();
		
		when(utilService.getUserFromSecurityContext()).thenReturn(authenticatedUser);
		when(userRepository.blockUserById(authenticatedUser.getId())).thenReturn(0);

		boolean result = userService.block();

		assertFalse(result);
		verify(utilService).getUserFromSecurityContext();
		verify(userRepository).blockUserById(authenticatedUser.getId());
	}
	
	@Test
	@DisplayName("Успешная деактивация пользователя")
	void deactivate_ShouldReturnTrue_WhenUserIsDeactivated() {
		User authenticatedUser = TestUtils.testUser();
		
		when(utilService.getUserFromSecurityContext()).thenReturn(authenticatedUser);
		when(userRepository.deactivateUserById(authenticatedUser.getId())).thenReturn(1);

		boolean result = userService.deactivate();

		assertTrue(result);
		verify(utilService).getUserFromSecurityContext();
		verify(userRepository).deactivateUserById(authenticatedUser.getId());
	}
	
	@Test
	@DisplayName("Успешное получение пользователя по userName")
	void findByUserName_ShouldReturnUser_WhenUserNameExists() {
		User existingUser = TestUtils.testUser2();
		String userName = existingUser.getUserName();
		
		User authenticatedUser = TestUtils.testUser();
		
		UserResponseDto expectedResponse = TestUtils.testUserResponseDto2();
		
		when(userRepository.findByUserName(userName)).thenReturn(Optional.of(existingUser));
		when(utilService.getUserFromSecurityContext()).thenReturn(authenticatedUser);
		when(userMapper.mapToDto(authenticatedUser)).thenReturn(expectedResponse);

		UserResponseDto result = userService.findByUserName(userName);

		assertNotNull(result);
		assertEquals(expectedResponse.id(), result.id());
		verify(userRepository).findByUserName(userName);
		verify(utilService).getUserFromSecurityContext();
		verify(userMapper).mapToDto(authenticatedUser);
	}
}