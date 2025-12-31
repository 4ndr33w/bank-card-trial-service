package com.example.bankcards.controller;

import com.example.bankcards.config.openapi.constant.ApiResponseExamples;
import com.example.bankcards.dto.request.UserRequestDto;
import com.example.bankcards.dto.request.UserUpdateDto;
import com.example.bankcards.dto.response.UserPageViewResponseDto;
import com.example.bankcards.dto.response.UserResponseDto;
import com.example.bankcards.exception.dto.ErrorResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

/**
 * @author 4ndr33w
 * @version 1.0
 */
@Tag(name = "Контероллер менеджмента профилей клиентов")
@RequestMapping("/api/v1/users")
public interface UserController {
	
	@ApiResponses({
			@ApiResponse(
					responseCode = "201",
					description = "Пользователь успешно создан",
					content = @Content(schema = @Schema(implementation = UserResponseDto.class), mediaType = MediaType.APPLICATION_JSON_VALUE)
			),
			@ApiResponse(
					responseCode = "400",
					description = "Некорректный запрос",
					content = @Content(examples = {@ExampleObject(ApiResponseExamples.BAD_REQUEST_EXAMPLE)}, mediaType = MediaType.APPLICATION_JSON_VALUE)
			),
			@ApiResponse(
					responseCode = "500",
					description = "Внутренняя ошибка сервера",
					content = @Content(schema = @Schema(implementation = ErrorResponseDto.class), mediaType = MediaType.APPLICATION_JSON_VALUE)
			)
	})
	@Operation(summary = "Создание нового профиля клиента")
	@PostMapping
	ResponseEntity<UserResponseDto> create(@RequestBody UserRequestDto request);
	
	@ApiResponses({
			@ApiResponse(
					responseCode = "200",
					description = "Успешно получены пользовательские данные",
					content = @Content(schema = @Schema(implementation = UserResponseDto.class), mediaType = MediaType.APPLICATION_JSON_VALUE)
			),
			@ApiResponse(
					responseCode = "400",
					description = "Некорректный запрос",
					content = @Content(examples = {@ExampleObject(ApiResponseExamples.BAD_REQUEST_EXAMPLE)}, mediaType = MediaType.APPLICATION_JSON_VALUE)
			),
			@ApiResponse(
					responseCode = "401",
					description = "Требуется авторизация",
					content = @Content(examples = {@ExampleObject(ApiResponseExamples.UNAUTHORIZED_EXAMPLE)}, mediaType = MediaType.APPLICATION_JSON_VALUE)
			),
			@ApiResponse(
					responseCode = "404",
					description = "Пользователь не найден",
					content = @Content(examples = {@ExampleObject(ApiResponseExamples.USER_NOT_FOUND_BY_ID_EXAMPLE)}, mediaType = MediaType.APPLICATION_JSON_VALUE)
			),
			@ApiResponse(
					responseCode = "500",
					description = "Внутренняя ошибка сервера",
					content = @Content(schema = @Schema(implementation = ErrorResponseDto.class), mediaType = MediaType.APPLICATION_JSON_VALUE)
			)
	})
	@Operation(summary = "Получение пользовательских данных по id")
	@GetMapping("/{id}")
	ResponseEntity<UserResponseDto> getById(@PathVariable UUID id);
	
	@ApiResponses({
			@ApiResponse(
					responseCode = "202",
					description = "Пользователь успешно обновлен",
					content = @Content(schema = @Schema(implementation = UserResponseDto.class), mediaType = MediaType.APPLICATION_JSON_VALUE)
			),
			@ApiResponse(
					responseCode = "400",
					description = "Некорректный запрос",
					content = @Content(examples = {@ExampleObject(ApiResponseExamples.BAD_REQUEST_EXAMPLE)}, mediaType = MediaType.APPLICATION_JSON_VALUE)
			),
			@ApiResponse(
					responseCode = "401",
					description = "Требуется авторизация",
					content = @Content(examples = {@ExampleObject(ApiResponseExamples.UNAUTHORIZED_EXAMPLE)}, mediaType = MediaType.APPLICATION_JSON_VALUE)
			),
			@ApiResponse(
					responseCode = "404",
					description = "Пользователь не найден",
					content = @Content(examples = {@ExampleObject(ApiResponseExamples.USER_NOT_FOUND_BY_ID_EXAMPLE)}, mediaType = MediaType.APPLICATION_JSON_VALUE)
			),
			@ApiResponse(
					responseCode = "500",
					description = "Внутренняя ошибка сервера",
					content = @Content(schema = @Schema(implementation = ErrorResponseDto.class), mediaType = MediaType.APPLICATION_JSON_VALUE)
			)
	})
	@Operation(summary = "Обновление пользовательских данных")
	@PatchMapping
	ResponseEntity<UserResponseDto> update(@RequestBody UserUpdateDto update);
	
	@ApiResponses({
			@ApiResponse(
					responseCode = "200",
					description = "Успешно получены данные собственого профиля аутенфицированного пользователя",
					content = @Content(schema = @Schema(implementation = UserResponseDto.class), mediaType = MediaType.APPLICATION_JSON_VALUE)
			),
			@ApiResponse(
					responseCode = "401",
					description = "Требуется авторизация",
					content = @Content(examples = {@ExampleObject(ApiResponseExamples.UNAUTHORIZED_EXAMPLE)}, mediaType = MediaType.APPLICATION_JSON_VALUE)
			),
			@ApiResponse(
					responseCode = "404",
					description = "Пользователь не найден",
					content = @Content(examples = {@ExampleObject(ApiResponseExamples.USER_NOT_FOUND_BY_ID_EXAMPLE)}, mediaType = MediaType.APPLICATION_JSON_VALUE)
			),
			@ApiResponse(
					responseCode = "500",
					description = "Внутренняя ошибка сервера",
					content = @Content(schema = @Schema(implementation = ErrorResponseDto.class), mediaType = MediaType.APPLICATION_JSON_VALUE)
			)
	})
	@Operation(summary = "Получение пользовательских данных аутенфицированного пользователя")
	@GetMapping
	ResponseEntity<UserResponseDto> get();
	
	@ApiResponses({
			@ApiResponse(
					responseCode = "200",
					description = "Успешно получены пользовательские данные по email",
					content = @Content(schema = @Schema(implementation = UserResponseDto.class), mediaType = MediaType.APPLICATION_JSON_VALUE)
			),
			@ApiResponse(
					responseCode = "400",
					description = "Некорректный запрос",
					content = @Content(examples = {@ExampleObject(ApiResponseExamples.BAD_REQUEST_EXAMPLE)}, mediaType = MediaType.APPLICATION_JSON_VALUE)
			),
			@ApiResponse(
					responseCode = "401",
					description = "Требуется авторизация",
					content = @Content(examples = {@ExampleObject(ApiResponseExamples.UNAUTHORIZED_EXAMPLE)}, mediaType = MediaType.APPLICATION_JSON_VALUE)
			),
			@ApiResponse(
					responseCode = "403",
					description = "Нет прав на выполнение операции",
					content = @Content(examples = {@ExampleObject(ApiResponseExamples.FORBIDDEN_EXAMPLE)}, mediaType = MediaType.APPLICATION_JSON_VALUE)
			),
			@ApiResponse(
					responseCode = "404",
					description = "Пользователь не найден",
					content = @Content(examples = {@ExampleObject(ApiResponseExamples.USER_NOT_FOUND_BY_ID_EXAMPLE)}, mediaType = MediaType.APPLICATION_JSON_VALUE)
			),
			@ApiResponse(
					responseCode = "500",
					description = "Внутренняя ошибка сервера",
					content = @Content(schema = @Schema(implementation = ErrorResponseDto.class), mediaType = MediaType.APPLICATION_JSON_VALUE)
			)
	})
	@Operation(summary = "Получение пользовательских данных по email")
	@GetMapping("/email/{email}")
	@PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
	ResponseEntity<UserResponseDto> getByEmail(@PathVariable String email);
	
	@ApiResponses({
			@ApiResponse(
					responseCode = "200",
					description = "Успешно получены пользовательские данные по username",
					content = @Content(schema = @Schema(implementation = UserResponseDto.class), mediaType = MediaType.APPLICATION_JSON_VALUE)
			),
			@ApiResponse(
					responseCode = "400",
					description = "Некорректный запрос",
					content = @Content(examples = {@ExampleObject(ApiResponseExamples.BAD_REQUEST_EXAMPLE)}, mediaType = MediaType.APPLICATION_JSON_VALUE)
			),
			@ApiResponse(
					responseCode = "401",
					description = "Требуется авторизация",
					content = @Content(examples = {@ExampleObject(ApiResponseExamples.UNAUTHORIZED_EXAMPLE)}, mediaType = MediaType.APPLICATION_JSON_VALUE)
			),
			@ApiResponse(
					responseCode = "403",
					description = "Нет прав на выполнение операции",
					content = @Content(examples = {@ExampleObject(ApiResponseExamples.FORBIDDEN_EXAMPLE)}, mediaType = MediaType.APPLICATION_JSON_VALUE)
			),
			@ApiResponse(
					responseCode = "404",
					description = "Пользователь не найден",
					content = @Content(examples = {@ExampleObject(ApiResponseExamples.USER_NOT_FOUND_BY_ID_EXAMPLE)}, mediaType = MediaType.APPLICATION_JSON_VALUE)
			),
			@ApiResponse(
					responseCode = "500",
					description = "Внутренняя ошибка сервера",
					content = @Content(schema = @Schema(implementation = ErrorResponseDto.class), mediaType = MediaType.APPLICATION_JSON_VALUE)
			)
	})
	@Operation(summary = "Получение пользовательских данных по username")
	@GetMapping("/username/{username}")
	@PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
	ResponseEntity<UserResponseDto> getByUsername(@PathVariable String username);
	
	@ApiResponses({
			@ApiResponse(
					responseCode = "204",
					description = "Пользователь успешно удалён"
			),
			@ApiResponse(
					responseCode = "401",
					description = "Требуется авторизация",
					content = @Content(examples = {@ExampleObject(ApiResponseExamples.UNAUTHORIZED_EXAMPLE)}, mediaType = MediaType.APPLICATION_JSON_VALUE)
			),
			@ApiResponse(
					responseCode = "404",
					description = "Пользователь не найден",
					content = @Content(examples = {@ExampleObject(ApiResponseExamples.USER_NOT_FOUND_BY_ID_EXAMPLE)}, mediaType = MediaType.APPLICATION_JSON_VALUE)
			),
			@ApiResponse(
					responseCode = "500",
					description = "Внутренняя ошибка сервера",
					content = @Content(schema = @Schema(implementation = ErrorResponseDto.class), mediaType = MediaType.APPLICATION_JSON_VALUE)
			)
	})
	@Operation(summary = "Удаление собственного профиля клиента")
	@DeleteMapping
	ResponseEntity<?> delete();
	
	@ApiResponses({
			@ApiResponse(
					responseCode = "200",
					description = "Успешно получены пользовательские данные",
					content = @Content(schema = @Schema(implementation = UserPageViewResponseDto.class), mediaType = MediaType.APPLICATION_JSON_VALUE)
			),
			@ApiResponse(
					responseCode = "400",
					description = "Некорректный запрос",
					content = @Content(examples = {@ExampleObject(ApiResponseExamples.BAD_REQUEST_EXAMPLE)}, mediaType = MediaType.APPLICATION_JSON_VALUE)
			),
			@ApiResponse(
					responseCode = "401",
					description = "Требуется авторизация",
					content = @Content(examples = {@ExampleObject(ApiResponseExamples.UNAUTHORIZED_EXAMPLE)}, mediaType = MediaType.APPLICATION_JSON_VALUE)
			),
			@ApiResponse(
					responseCode = "403",
					description = "Нет прав на выполнение операции",
					content = @Content(examples = {@ExampleObject(ApiResponseExamples.FORBIDDEN_EXAMPLE)}, mediaType = MediaType.APPLICATION_JSON_VALUE)
			),
			@ApiResponse(
					responseCode = "500",
					description = "Внутренняя ошибка сервера",
					content = @Content(schema = @Schema(implementation = ErrorResponseDto.class), mediaType = MediaType.APPLICATION_JSON_VALUE)
			)
	})
	@Operation(summary = "Получение постранично информацию о пользователях")
	@PostMapping("/page/{page}")
	@PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
	ResponseEntity<UserPageViewResponseDto> getPage(@PathVariable Integer page, @RequestParam Integer limit);
	
	@ApiResponses({
			@ApiResponse(
					responseCode = "200",
					description = "Пользователь заблокировал собственный профиль",
					content = @Content(schema = @Schema(implementation = Boolean.class), mediaType = MediaType.APPLICATION_JSON_VALUE)
			),
			@ApiResponse(
					responseCode = "401",
					description = "Требуется авторизация",
					content = @Content(examples = {@ExampleObject(ApiResponseExamples.UNAUTHORIZED_EXAMPLE)}, mediaType = MediaType.APPLICATION_JSON_VALUE)
			),
			@ApiResponse(
					responseCode = "500",
					description = "Внутренняя ошибка сервера",
					content = @Content(schema = @Schema(implementation = ErrorResponseDto.class), mediaType = MediaType.APPLICATION_JSON_VALUE)
			)
	})
	@Operation(summary = "Блокировка собственного профиля клиента")
	@GetMapping("/block")
	ResponseEntity<Boolean> block();
	
	@ApiResponses({
			@ApiResponse(
					responseCode = "200",
					description = "Пользователь деактивировал собственный профиль",
					content = @Content(schema = @Schema(implementation = Boolean.class), mediaType = MediaType.APPLICATION_JSON_VALUE)
			),
			@ApiResponse(
					responseCode = "401",
					description = "Требуется авторизация",
					content = @Content(examples = {@ExampleObject(ApiResponseExamples.UNAUTHORIZED_EXAMPLE)}, mediaType = MediaType.APPLICATION_JSON_VALUE)
			),
			@ApiResponse(
					responseCode = "500",
					description = "Внутренняя ошибка сервера",
					content = @Content(schema = @Schema(implementation = ErrorResponseDto.class), mediaType = MediaType.APPLICATION_JSON_VALUE)
			)
	})
	@Operation(summary = "Деактивация собственного профиля клиента")
	@GetMapping("/deactivate")
	ResponseEntity<Boolean> deactivate();
}