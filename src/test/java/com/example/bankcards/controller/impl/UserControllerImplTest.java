package com.example.bankcards.controller;

import com.example.bankcards.BankcardsApplication;
import com.example.bankcards.configuration.TestConfig;
import com.example.bankcards.configuration.TestSecurityConfig;
import com.example.bankcards.controller.impl.UserControllerImpl;
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
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockBeans;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
*
* @version 1.0
* @author 4ndr33w
*/
//@SpringBootTest(classes = {UserControllerImpl.class, BankcardsApplication.class, TestConfig.class, UserService.class, TestSecurityConfig.class})
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
@ContextConfiguration(classes = {TestConfig.class})
@Import(TestSecurityConfig.class)
@WebMvcTest(UserControllerImpl.class)
public class UserControllerTest {
	
	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ObjectMapper objectMapper;
	@MockitoBean
	private UserService userService;
	
	@Test
	@DisplayName("Создание пользователя - успешный сценарий")
	void create_ShouldReturnCreatedUser_WhenRequestIsValid() throws Exception {
		// Arrange
//		UserRequestDto request = TestUtils.testUserRequestDto();
//
//		UserResponseDto response = TestUtils.testUpdatedUserResponseDto();
		UserRequestDto request = new UserRequestDto(
				"Иван",
				"Иванов",
				"ivan@example.com",
				"ivanov",
				"Password123!",
				LocalDate.of(1990, 1, 1)
		);
		
		UserResponseDto response = new UserResponseDto(
				UUID.randomUUID(),
				"Иван",
				"Иванов",
				"ivan@example.com",
				"ivanov",
				ZonedDateTime.now(),
				true,
				false,
				Set.of()
		);
		
		when(userService.create(any(UserRequestDto.class))).thenReturn(response);
		
		String content = objectMapper.writeValueAsString(request);
		// Act & Assert
		MvcResult mvcResult = mockMvc.perform(post("/api/v1/users")
						.contentType(MediaType.APPLICATION_JSON)
						.content(content))
//				.andExpect(status().isCreated())
//				.andExpect(jsonPath("$.id").exists())
//				.andExpect(jsonPath("$.name").value(request.name()))
//				.andExpect(jsonPath("$.lastName").value(request.lastName()))
//				.andExpect(jsonPath("$.email").value(request.email()))
//				.andExpect(jsonPath("$.userName").value(request.userName()))
				.andDo(print())
				.andReturn();
		var test = mvcResult.getResponse().getContentAsString();
		
		var status = mvcResult.getResponse().getStatus();
		
		assertTrue(status == 201);
		
		verify(mvcResult.getResponse().getStatus());
		verify(userService).create(request);
	}
	
	@Test
	void test_WithResponseBody() throws Exception {
		String json = "{\"name\":\"Test\"}"; // Неполный JSON для валидационной ошибки
		
		mockMvc.perform(post("/api/v1/users")
						.content(json)
						.contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isBadRequest()); // Должно быть 400
		// Если 200 - значит вызывается НЕ метод create
	}
	
	@Test
	void test_MethodAnnotations() throws Exception {
		// Проверяем аннотации через рефлексию
		Method method = UserControllerImpl.class
				.getMethod("create", UserRequestDto.class);
		
		Annotation[][] paramAnnotations = method.getParameterAnnotations();
		
		System.out.println("Параметры метода:");
		for (Annotation[] annotations : paramAnnotations) {
			for (Annotation annotation : annotations) {
				System.out.println("  - " + annotation.annotationType().getSimpleName());
			}
		}
		
		// Или проверяем напрямую
		boolean hasRequestBody = Arrays.stream(paramAnnotations[0])
				.anyMatch(a -> a.annotationType().equals(RequestBody.class));
		
		assertTrue(hasRequestBody, "Метод должен иметь @RequestBody на параметре");
	}
	
	@Test
	void debug_CheckMethodCalls() throws Exception {
		// Создаем шпиона, чтобы отследить вызовы
		UserRequestDto request = new UserRequestDto(
				"Тест", "Тестов", "test@example.com",
				"testuser", "Password123!", LocalDate.of(1990, 1, 1)
		);
		
		// НЕ создаем spy, используем существующий мок
		// Просто сбрасываем все взаимодействия
		Mockito.reset(userService);
		
		// Настраиваем мок
		when(userService.create(any())).thenReturn(null);
		
		mockMvc.perform(post("/api/v1/users")
						.content("{}") // Пустое тело
						.contentType(MediaType.APPLICATION_JSON))
				.andDo(print());
		
		// Assert - проверяем, вызывался ли метод
		verify(userService, never()).create(any());
		// Если не был вызван - проблема в контроллере
	}
	
	@Test
	@DisplayName("Создание пользователя - валидация полей")
	void create_ShouldReturnBadRequest_WhenRequestIsInvalid() throws Exception {
		// Arrange - некорректный email и короткий пароль
		UserRequestDto invalidRequest = new UserRequestDto(
				"", // пустое имя
				"", // пустая фамилия
				"invalid-email", // некорректный email
				"", // пустой username
				"123", // слишком короткий пароль
				LocalDate.of(2050, 1, 1) // дата в будущем
		);
		
		// Act & Assert
		mockMvc.perform(post("/api/v1/users")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(invalidRequest)))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.status").value("400"))
				.andExpect(jsonPath("$.message").value("Некорректный запрос"))
				.andExpect(jsonPath("$.name").exists())
				.andExpect(jsonPath("$.email").exists())
				.andExpect(jsonPath("$.userName").exists())
				.andExpect(jsonPath("$.password").exists())
				.andExpect(jsonPath("$.birthDate").exists());
		
		verify(userService, never()).create(any());
	}
	
	@Test
	@DisplayName("Получение пользователя по ID - успешный сценарий")
	void getById_ShouldReturnUser_WhenUserExists() throws Exception {
		// Arrange
		UUID userId = UUID.randomUUID();
		UserResponseDto response = new UserResponseDto(
				userId,
				"Петр",
				"Петров",
				"petr@example.com",
				"petrov",
				ZonedDateTime.now(),
				true,
				false,
				Set.of()
		);
		
		when(userService.findById(userId)).thenReturn(response);
		
		// Act & Assert
		mockMvc.perform(get("/api/v1/users/{id}", userId))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(userId.toString()))
				.andExpect(jsonPath("$.name").value("Петр"))
				.andExpect(jsonPath("$.email").value("petr@example.com"));
		
		verify(userService).findById(userId);
	}
	
	@Test
	@DisplayName("Получение пользователя по ID - пользователь не найден")
	void getById_ShouldReturnNotFound_WhenUserNotExists() throws Exception {
		// Arrange
		UUID userId = UUID.randomUUID();
		
		when(userService.findById(userId))
				.thenThrow(new UserNotFoundException("Пользователь не найден"));
		
		// Act & Assert
		mockMvc.perform(get("/api/v1/users/{id}", userId))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.status").value(404))
				.andExpect(jsonPath("$.message").value("Пользователь не найден"));
		
		verify(userService).findById(userId);
	}
	
	@Test
	@DisplayName("Обновление пользователя - успешный сценарий")
	void update_ShouldReturnUpdatedUser_WhenRequestIsValid() throws Exception {
		// Arrange
		UserUpdateDto updateRequest = TestUtils.testUserUpdateDto();
		
		UserResponseDto response = TestUtils.testUpdatedUserResponseDto();
		
		when(userService.update(any(UserUpdateDto.class))).thenReturn(response);
		
		// Act & Assert
		mockMvc.perform(patch("/api/v1/users")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(updateRequest)))
				.andExpect(status().isAccepted())
				.andExpect(jsonPath("$.name").value("ОбновленноеИмя"))
				.andExpect(jsonPath("$.lastName").value("ОбновленнаяФамилия"));
		
		verify(userService).update(any(UserUpdateDto.class));
	}
	
	@Test
	@DisplayName("Получение текущего пользователя - успешный сценарий")
	void get_ShouldReturnCurrentUser() throws Exception {
		// Arrange
		UserResponseDto response = new UserResponseDto(
				UUID.randomUUID(),
				"Текущий",
				"Пользователь",
				"current@example.com",
				"currentuser",
				ZonedDateTime.now(),
				true,
				false,
				Set.of()
		);
		
		when(userService.find()).thenReturn(response);
		
		// Act & Assert
		mockMvc.perform(get("/api/v1/users/me"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.name").value("Текущий"))
				.andExpect(jsonPath("$.userName").value("currentuser"));
		
		verify(userService).find();
	}
	
	@Test
	@DisplayName("Получение пользователя по email - успешный сценарий")
	void getByEmail_ShouldReturnUser_WhenUserExists() throws Exception {
		// Arrange
		String email = "user@example.com";
		UserResponseDto response = new UserResponseDto(
				UUID.randomUUID(),
				"User",
				"Userov",
				email,
				"user123",
				ZonedDateTime.now(),
				true,
				false,
				Set.of()
		);
		
		when(userService.findByEmail(email)).thenReturn(response);
		
		// Act & Assert
		mockMvc.perform(get("/api/v1/users/by-email")
						.param("email", email))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.email").value(email))
				.andExpect(jsonPath("$.userName").value("user123"));
		
		verify(userService).findByEmail(email);
	}
	
	@Test
	@DisplayName("Получение пользователя по username - успешный сценарий")
	void getByUsername_ShouldReturnUser_WhenUserExists() throws Exception {
		// Arrange
		String username = "john_doe";
		UserResponseDto response = new UserResponseDto(
				UUID.randomUUID(),
				"John",
				"Doe",
				"john@example.com",
				username,
				ZonedDateTime.now(),
				true,
				false,
				Set.of()
		);
		
		when(userService.findByUserName(username)).thenReturn(response);
		
		// Act & Assert
		mockMvc.perform(get("/api/v1/users/by-username")
						.param("username", username))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.userName").value(username))
				.andExpect(jsonPath("$.name").value("John"));
		
		verify(userService).findByUserName(username);
	}
	
	@Test
	@DisplayName("Удаление текущего пользователя - успешный сценарий")
	void delete_ShouldReturnOk() throws Exception {
		// Arrange
		doNothing().when(userService).delete();
		
		// Act & Assert
		mockMvc.perform(delete("/api/v1/users"))
				.andExpect(status().isOk());
		
		verify(userService).delete();
	}
	
	@Test
	@DisplayName("Получение пользователей с пагинацией - успешный сценарий")
	void getPage_ShouldReturnPaginatedUsers() throws Exception {
		// Arrange
		Integer page = 1;
		Integer limit = 10;
		
		UserResponseDto user1 = new UserResponseDto(
				UUID.randomUUID(),
				"User1",
				"Last1",
				"user1@example.com",
				"user1",
				ZonedDateTime.now(),
				true,
				false,
				Set.of()
		);
		
		UserResponseDto user2 = new UserResponseDto(
				UUID.randomUUID(),
				"User2",
				"Last2",
				"user2@example.com",
				"user2",
				ZonedDateTime.now(),
				true,
				false,
				Set.of()
		);
		
		UserPageViewResponseDto response = new UserPageViewResponseDto(
				1,
				10,
				5,
				50,
				List.of(user1, user2)
		);
		
		when(userService.findAllByPage(page, limit)).thenReturn(response);
		
		// Act & Assert
		mockMvc.perform(get("/api/v1/users")
						.param("page", String.valueOf(page))
						.param("limit", String.valueOf(limit)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.currentPage").value(1))
				.andExpect(jsonPath("$.limit").value(10))
				.andExpect(jsonPath("$.totalPages").value(5))
				.andExpect(jsonPath("$.totalUsers").value(50))
				.andExpect(jsonPath("$.users").isArray())
				.andExpect(jsonPath("$.users", hasSize(2)))
				.andExpect(jsonPath("$.users[0].name").value("User1"))
				.andExpect(jsonPath("$.users[1].name").value("User2"));
		
		verify(userService).findAllByPage(page, limit);
	}
	
	@Test
	@DisplayName("Получение пользователей с пагинацией - параметры по умолчанию")
	void getPage_ShouldUseDefaultParameters_WhenNoParametersProvided() throws Exception {
		// Arrange
		UserPageViewResponseDto response = new UserPageViewResponseDto(
				1,
				5,
				1,
				3,
				List.of()
		);
		
		when(userService.findAllByPage(null, null)).thenReturn(response);
		
		// Act & Assert
		mockMvc.perform(get("/api/v1/users"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.currentPage").value(1))
				.andExpect(jsonPath("$.limit").value(5));
		
		verify(userService).findAllByPage(null, null);
	}
	
	@Test
	@DisplayName("Блокировка пользователя - успешный сценарий")
	void block_ShouldReturnTrue_WhenUserBlocked() throws Exception {
		// Arrange
		when(userService.block()).thenReturn(true);
		
		// Act & Assert
		mockMvc.perform(patch("/api/v1/users/block"))
				.andExpect(status().isOk())
				.andExpect(content().string("true"));
		
		verify(userService).block();
	}
	
	@Test
	@DisplayName("Деактивация пользователя - успешный сценарий")
	void deactivate_ShouldReturnTrue_WhenUserDeactivated() throws Exception {
		// Arrange
		when(userService.deactivate()).thenReturn(true);
		
		// Act & Assert
		mockMvc.perform(patch("/api/v1/users/deactivate"))
				.andExpect(status().isOk())
				.andExpect(content().string("true"));
		
		verify(userService).deactivate();
	}
	
	@Test
	@DisplayName("Блокировка пользователя - возвращает false")
	void block_ShouldReturnFalse_WhenBlockFailed() throws Exception {
		// Arrange
		when(userService.block()).thenReturn(false);
		
		// Act & Assert
		mockMvc.perform(patch("/api/v1/users/block"))
				.andExpect(status().isOk())
				.andExpect(content().string("false"));
		
		verify(userService).block();
	}
	
	@Test
	@DisplayName("Валидация JSON формата даты")
	void create_ShouldHandleJsonDateFormat() throws Exception {
		// Arrange
		String jsonRequest = """
            {
                "name": "Иван",
                "lastName": "Иванов",
                "email": "ivan@example.com",
                "userName": "ivanov",
                "password": "Password123!",
                "birthDate": "01.01.1990"
            }
            """;
		
		UserResponseDto response = new UserResponseDto(
				UUID.randomUUID(),
				"Иван",
				"Иванов",
				"ivan@example.com",
				"ivanov",
				ZonedDateTime.now(),
				true,
				false,
				Set.of()
		);
		
		when(userService.create(any(UserRequestDto.class))).thenReturn(response);
		
		// Act & Assert
		mockMvc.perform(post("/api/v1/users")
						.contentType(MediaType.APPLICATION_JSON)
						.content(jsonRequest))
				.andExpect(status().isCreated());
		
		verify(userService).create(any(UserRequestDto.class));
	}
	
	@Test
	@DisplayName("Создание пользователя - проверка паттерна имени")
	void create_ShouldValidateNamePattern() throws Exception {
		// Arrange - имя с цифрами (нарушает паттерн)
		UserRequestDto invalidRequest = new UserRequestDto(
				"Иван123", // цифры в имени
				"Иванов",
				"ivan@example.com",
				"ivanov",
				"Password123!",
				LocalDate.of(1990, 1, 1)
		);
		
		// Act & Assert
		mockMvc.perform(post("/api/v1/users")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(invalidRequest)))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.name").exists());
		
		verify(userService, never()).create(any());
	}
	
	@Test
	@DisplayName("Создание пользователя - проверка паттерна пароля")
	void create_ShouldValidatePasswordPattern() throws Exception {
		// Arrange - пароль без спецсимвола
		UserRequestDto invalidRequest = new UserRequestDto(
				"Иван",
				"Иванов",
				"ivan@example.com",
				"ivanov",
				"Password123", // нет спецсимвола
				LocalDate.of(1990, 1, 1)
		);
		
		// Act & Assert
		mockMvc.perform(post("/api/v1/users")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(invalidRequest)))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.password").exists());
		
		verify(userService, never()).create(any());
	}

//	@Test
//	@DisplayName("Обновление пользователя - частичное обновление")
//	void update_ShouldHandlePartialUpdate() throws Exception {
//		// Arrange - только имя, остальные поля null
//		UserUpdateDto partialUpdate = new UserUpdateDto(
//				"НовоеИмя",
//				null,
//				null
//		);
//
//		UserResponseDto response = new UserResponseDto(
//				UUID.randomUUID(),
//				"НовоеИмя",
//				"СтараяФамилия", // остается старая
//				"email@example.com",
//				"username",
//				ZonedDateTime.now(),
//				true,
//				false,
//				Set.of()
//		);
//
//		when(userService.update(any(UserUpdateDto.class))).thenReturn(response);
//
//		// Act & Assert
//		mockMvc.perform(put("/api/v1/users")
//						.contentType(MediaType.APPLICATION_JSON)
//						.content(objectMapper.writeValueAsString(partialUpdate)))
//				.andExpect(status().isAccepted());
//
//		verify(userService).update(any(UserUpdateDto.class));
//	}
	
	@Test
	@DisplayName("Получение пользователя по email - параметр обязателен")
	void getByEmail_ShouldReturnBadRequest_WhenEmailMissing() throws Exception {
		// Act & Assert
		mockMvc.perform(get("/api/v1/users/by-email"))
				.andExpect(status().isBadRequest());
	}
	
	@Test
	@DisplayName("Получение пользователя по username - параметр обязателен")
	void getByUsername_ShouldReturnBadRequest_WhenUsernameMissing() throws Exception {
		// Act & Assert
		mockMvc.perform(get("/api/v1/users/by-username"))
				.andExpect(status().isBadRequest());
	}
	
	@Test
	@DisplayName("Обработка внутренней ошибки сервера")
	void getById_ShouldReturnInternalServerError_WhenRuntimeException() throws Exception {
		// Arrange
		UUID userId = UUID.randomUUID();
		
		when(userService.findById(userId))
				.thenThrow(new RuntimeException("Внутренняя ошибка сервера"));
		
		// Act & Assert
		mockMvc.perform(get("/api/v1/users/{id}", userId))
				.andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.status").value(500))
				.andExpect(jsonPath("$.message").value("Внутренняя ошибка сервера"));
		
		verify(userService).findById(userId);
	}

//	@Test
//	@DisplayName("Проверка корректности HTTP статусов")
//	void endpoints_ShouldReturnCorrectHttpStatuses() throws Exception {
//		// Arrange
//		UserResponseDto userResponse = new UserResponseDto(
//				UUID.randomUUID(),
//				"Test",
//				"User",
//				"test@example.com",
//				"testuser",
//				ZonedDateTime.now(),
//				true,
//				false,
//				Set.of()
//		);
//
//		when(userService.create(any())).thenReturn(userResponse);
//		when(userService.findById(any())).thenReturn(userResponse);
//		when(userService.update(any())).thenReturn(userResponse);
//		when(userService.find()).thenReturn(userResponse);
//		when(userService.findByEmail(any())).thenReturn(userResponse);
//		when(userService.findByUserName(any())).thenReturn(userResponse);
//		doNothing().when(userService).delete();
//
//		// Act & Assert для каждого endpoint
//		mockMvc.perform(post("/api/v1/users")
//						.contentType(MediaType.APPLICATION_JSON)
//						.content(objectMapper.writeValueAsString(
//								new UserRequestDto("Test", "User", "test@example.com",
//										"testuser", "Password123!", LocalDate.of(1990, 1, 1)))))
//				.andExpect(status().isCreated()); // 201
//
//		mockMvc.perform(get("/api/v1/users/{id}", UUID.randomUUID()))
//				.andExpect(status().isOk()); // 200
//
//		mockMvc.perform(put("/api/v1/users")
//						.contentType(MediaType.APPLICATION_JSON)
//						.content(objectMapper.writeValueAsString(
//								new UserUpdateDto("New", "Name", null))))
//				.andExpect(status().isAccepted()); // 202
//
//		mockMvc.perform(get("/api/v1/users/me"))
//				.andExpect(status().isOk()); // 200
//
//		mockMvc.perform(delete("/api/v1/users"))
//				.andExpect(status().isOk()); // 200
//
//		mockMvc.perform(patch("/api/v1/users/block"))
//				.andExpect(status().isOk()); // 200
//
//		mockMvc.perform(patch("/api/v1/users/deactivate"))
//				.andExpect(status().isOk()); // 200
//	}
}
