package com.example.bankcards.controller.impl;

import com.example.bankcards.configuration.TestSecurityConfig;
import com.example.bankcards.dto.request.UserRequestDto;
import com.example.bankcards.dto.request.UserUpdateDto;
import com.example.bankcards.dto.response.UserPageViewResponseDto;
import com.example.bankcards.dto.response.UserResponseDto;
import com.example.bankcards.exception.businessException.UserNotFoundException;
import com.example.bankcards.service.UserService;
import com.example.bankcards.utils.TestUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author 4ndr33w
 * @version 1.0
 */
@SpringBootTest
@AutoConfigureMockMvc
@Import({TestSecurityConfig.class})
@ActiveProfiles("test")
public class UserControllerImplTests {

	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private MockMvc mockMvc;
	@MockitoBean
	private UserService userService;
	@MockitoBean
	private AuthenticationManager authenticationManager;
	
	// ============================================================
	// ==========================CREATE============================
	// ============================================================
	@Test
	@DisplayName("Успешное создание пользователя")
	public void createUser_Successful() throws Exception {
		UserRequestDto request = TestUtils.testUserRequestDto();
		UserResponseDto response = TestUtils.testUserResponseDto();
		
		when(userService.create(request)).thenReturn(response);
		
		String jsonRequest = objectMapper.writeValueAsString(request);

		mockMvc.perform(post("/api/v1/users")
						.contentType(MediaType.APPLICATION_JSON)
						.content(jsonRequest))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.name").value("Вася"))
				.andExpect(jsonPath("$.lastName").value("Пупкин"))
				.andExpect(jsonPath("$.email").value("pup0k@example.ru"))
				.andExpect(jsonPath("$.active").value(true))
				.andExpect(jsonPath("$.blocked").value(false));
	}
	
	@Test
	@DisplayName("Создание пользователя без имени - Bad Request")
	void create_withEmptyName_shouldReturnBadRequest() throws Exception {
		UserRequestDto invalidRequest = TestUtils.invalidUserRequestWithoutName();

		mockMvc.perform(post("/api/v1/users")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(invalidRequest)))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.name").value("must not be blank"));
	}
	
	@Test
	@DisplayName("Создание пользователя с некорректным email - Bad Request")
	void create_withInvalidEmail_shouldReturnBadRequest() throws Exception {
		UserRequestDto invalidRequest = TestUtils.invalidUserRequestWithoutEmail();

		mockMvc.perform(post("/api/v1/users")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(invalidRequest)))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.email").value("Неверный формат e-mail"));
	}
	
	@Test
	@DisplayName("Создание пользователя с коротким паролем - Bad Request")
	void create_withShortPassword_shouldReturnBadRequest() throws Exception {
		UserRequestDto invalidRequest = TestUtils.invalidUserRequestWithInvalidPassword();

		mockMvc.perform(post("/api/v1/users")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(invalidRequest)))
				.andExpect(status().isBadRequest());
	}
	
	@Test
	@DisplayName("Создание пользователя с невалидной датой рождения = Bad Request")
	void create_withFutureBirthDate_shouldReturnBadRequest() throws Exception {
		UserRequestDto invalidRequest = TestUtils.invalidUserRequestWithInvalidBirthDate();

		mockMvc.perform(post("/api/v1/users")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(invalidRequest)))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.birthDate").value("Дата должна быть в прошлом"));
	}
	
	@Test
	@DisplayName("Создание пользователя с пустым запросом - Bad Request")
	void create_withNullRequest_shouldReturnBadRequest() throws Exception {
		mockMvc.perform(post("/api/v1/users")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{}"))
				.andExpect(status().isBadRequest());
	}
	// ============================================================
	// ==========================CREATE============================
	// ============================================================
	
	
	// ============================================================
	// ==========================GET===============================
	// ============================================================
	@Test
	@DisplayName("Успешное получение пользователя")
	public void getUser_successfull() throws Exception {
		UserResponseDto response = TestUtils.testUserResponseDto();
		
		when(userService.find()).thenReturn(response);
		
		mockMvc.perform(get("/api/v1/users"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.name").value("Вася"))
				.andExpect(jsonPath("$.lastName").value("Пупкин"))
				.andExpect(jsonPath("$.email").value("pup0k@example.ru"));
		
		verify(userService).find();
	}
	// ============================================================
	// ==========================GET===============================
	// ============================================================
	
	
	// ============================================================
	// ==========================GET BY EMAIL======================
	// ============================================================
	@Test
	@DisplayName("Успешное получение пользователя по email")
	@WithMockUser(username = "user", authorities = {"USER"})
	void getByEmail_shouldReturnUserResponseDto() throws Exception {
		UserResponseDto response = TestUtils.testUserResponseDto();
		String email = response.email();
		
		when(userService.findByEmail(email)).thenReturn(response);

		mockMvc.perform(get("/api/v1/users/email/{email}", email))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.email").value(email));
	}
	
	@Test
	@DisplayName("Получение пользователя по пустому email - Not Found")
	void getByEmail_withEmptyEmail_shouldReturnBadRequest() throws Exception {
		mockMvc.perform(get("/api/v1/users/email/{email}", ""))
				.andExpect(status().isNotFound());
	}
	
	@Test
	@DisplayName("Получить пользоввателя по несуществующему email - UserNotFoundException")
	@WithMockUser(username = "user", authorities = {"USER"})
	void getByEmail_whenServiceThrowsException_shouldReturnNotFound() throws Exception {
		String email = "nonexistent@example.com";
		when(userService.findByEmail(email)).thenThrow(new UserNotFoundException("User not found"));
		
		mockMvc.perform(get("/api/v1/users/email/{email}", email))
				.andExpect(status().isNotFound());
	}
	// ============================================================
	// ==========================GET BY EMAIL======================
	// ============================================================
	
	
	// ============================================================
	// ==========================GET BY ID========================
	// ============================================================
	@Test
	@DisplayName("Успешное получение пользователя по ID")
	void getById_shouldReturnUserResponseDto() throws Exception {
		UserResponseDto userResponseDto = TestUtils.testUserResponseDto();
		UUID userId = userResponseDto.id();

		when(userService.findById(userId)).thenReturn(userResponseDto);

		mockMvc.perform(get("/api/v1/users/{id}", userId))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(userId.toString()))
				.andExpect(jsonPath("$.name").value(userResponseDto.name()))
				.andExpect(jsonPath("$.email").value(userResponseDto.email()));
	}
	
	@Test
	@DisplayName("Получить пользователя по несуществующему ID - UserNotFoundException")
	void getById_whenServiceThrowsException_shouldReturnAppropriateStatus() throws Exception {
		UUID userId = UUID.randomUUID();
		
		when(userService.findById(userId)).thenThrow(new UserNotFoundException("User not found"));
		
		mockMvc.perform(get("/api/v1/users/{id}", userId))
				.andExpect(status().isNotFound());
	}
	// ============================================================
	// ==========================GET BY ID=========================
	// ============================================================

	
	// ============================================================
	// ==========================UPDATE============================
	// ============================================================
	@Test
	@DisplayName("Успешное обновление пользователя")
	void update_shouldReturnAcceptedStatusAndUserResponseDto() throws Exception {
		UserUpdateDto updateDto = TestUtils.testUserUpdateDto();
		UserResponseDto updatedResponse = TestUtils.testUpdatedUserResponseDto();
		
		when(userService.update(any(UserUpdateDto.class))).thenReturn(updatedResponse);

		mockMvc.perform(patch("/api/v1/users")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(updateDto)))
				.andExpect(status().isAccepted())
				.andExpect(jsonPath("$.lastName").value(updatedResponse.lastName()))
				.andExpect(jsonPath("$.email").value(updatedResponse.email()));
	}
	
	@Test
	@DisplayName("Обновление пользователя с невалидными данными - Bad Request")
	void update_withInvalidData_shouldReturnBadRequest() throws Exception {
		UserUpdateDto invalidDto = new UserUpdateDto(
				null,
				"Петров",
				LocalDate.now().plusDays(1)
		);

		mockMvc.perform(patch("/api/v1/users")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(invalidDto)))
				.andExpect(status().isBadRequest());
	}
	// ============================================================
	// ==========================UPDATE============================
	// ============================================================
	
	
	// ============================================================
	// ==========================GET BY USERNAME===================
	// ============================================================
	@Test
	@DisplayName("Успешное получение пользователя по username")
	@WithMockUser(username = "user", authorities = {"USER"})
	void getByUsername_shouldReturnUserResponseDto() throws Exception {
		UserResponseDto userResponseDto = TestUtils.testUserResponseDto();
		String username = userResponseDto.userName();
		when(userService.findByUserName(username)).thenReturn(userResponseDto);

		mockMvc.perform(get("/api/v1/users/username/{username}", username))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.userName").value(username));
		
	}
	
	@Test
	@DisplayName("Получение пользователя по пустому username - Bad Request")
	void getByUsername_withEmptyUsername_shouldReturnBadRequest() throws Exception {
		mockMvc.perform(get("/api/v1/users/username/{username}", ""))
				.andExpect(status().isNotFound());
	}
	
	@Test
	@DisplayName("Получить пользователя по несуществующему username - UserNotFoundException")
	@WithMockUser(username = "user", authorities = {"USER"})
	void getByUsername_whenUserNotFound_shouldReturnNotFound() throws Exception {
		String username = "nonexistent";

		when(userService.findByUserName(username)).thenThrow(new UserNotFoundException("User not found"));

		mockMvc.perform(get("/api/v1/users/username/{username}", username))
				.andExpect(status().isNotFound());
	}
	// ============================================================
	// ==========================GET BY USERNAME===================
	// ============================================================
	
	
	// ============================================================
	// =========================DELETE=============================
	// ============================================================
	@Test
	@DisplayName("Успешное удаление пользователя")
	void delete_shouldReturnOkStatus() throws Exception {
		doNothing().when(userService).delete();

		mockMvc.perform(delete("/api/v1/users"))
				.andExpect(status().isOk())
				.andExpect(content().string(""));
	}
	// ============================================================
	// =========================DELETE=============================
	// ============================================================
	
	
	// ============================================================
	// =========================GET PAGE===========================
	// ============================================================
	@Test
	@DisplayName("Успешное получение страницы пользователей")
	@WithMockUser(username = "user", authorities = {"USER"})
	void getPage_shouldReturnUserPageViewResponseDto() throws Exception {
		UserResponseDto user1 = TestUtils.testUserResponseDto();
		UserResponseDto user2 = TestUtils.testUserResponseDto2();
		UserPageViewResponseDto pageResponseDto = TestUtils.testUserPageViewResponseDto();
		int page = 1;
		int limit = 10;
		
		when(userService.findAllByPage(page, limit)).thenReturn(pageResponseDto);

		mockMvc.perform(post("/api/v1/users/page/{page}", page)
						.param("limit", String.valueOf(limit)))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.currentPage").value(1))
				.andExpect(jsonPath("$.limit").value(10))
				.andExpect(jsonPath("$.totalPages").value(1))
				.andExpect(jsonPath("$.totalUsers").value(2))
				.andExpect(jsonPath("$.users[1].email").value(user2.email()))
				.andExpect(jsonPath("$.users[0].email").value(user1.email()));
	}
	// ============================================================
	// =========================GET PAGE===========================
	// ============================================================
	
	
	// ============================================================
	// =========================BLOCK==============================
	// ============================================================
	@Test
	@DisplayName("Успешная блокировка профиля")
	void block_shouldReturnTrue() throws Exception {
		when(userService.block()).thenReturn(true);

		mockMvc.perform(get("/api/v1/users/block"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(content().string("true"));
	}
	// ============================================================
	// =========================BLOCK==============================
	// ============================================================
	
	
	// ============================================================
	// =========================DEACTIVATE=========================
	// ============================================================
	@Test
	@DisplayName("Успешная деактивация профиля")
	void deactivate_shouldReturnTrue() throws Exception {
		when(userService.deactivate()).thenReturn(true);

		mockMvc.perform(get("/api/v1/users/deactivate"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(content().string("true"));
	}
	// ============================================================
	// =========================DEACTIVATE=========================
	// ============================================================
}