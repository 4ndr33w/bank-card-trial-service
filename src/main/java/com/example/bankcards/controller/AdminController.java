package com.example.bankcards.controller;

import com.example.bankcards.config.openapi.constant.ApiResponseExamples;
import com.example.bankcards.dto.request.UserUpdateDto;
import com.example.bankcards.dto.response.UserResponseDto;
import com.example.bankcards.enums.UserRole;
import com.example.bankcards.exception.dto.ErrorResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Контроллер администрирования пользователей")
@RequestMapping("/api/v1/admin")
public interface AdminController {
	
	@ApiResponses({
			@ApiResponse(
					responseCode = "202",
					description = "Пользователь успешно обновлен",
					content = @Content(schema = @Schema(implementation = UserResponseDto.class), mediaType = "application/json")
			),
			@ApiResponse(
					responseCode = "400",
					description = "Некорректный запрос",
					content = @Content(examples = {@ExampleObject(ApiResponseExamples.BAD_REQUEST_EXAMPLE)}, mediaType = "application/json")
			),
			@ApiResponse(
					responseCode = "401",
					description = "Требуется авторизация",
					content = @Content(examples = {@ExampleObject(ApiResponseExamples.UNAUTHORIZED_EXAMPLE)}, mediaType = "application/json")
			),
			@ApiResponse(
					responseCode = "403",
					description = "Нет прав на выполнение операции",
					content = @Content(examples = {@ExampleObject(ApiResponseExamples.FORBIDDEN_EXAMPLE)}, mediaType = "application/json")
			),
			@ApiResponse(
					responseCode = "404",
					description = "Пользователь не найден",
					content = @Content(examples = {@ExampleObject(ApiResponseExamples.USER_NOT_FOUND_BY_ID_EXAMPLE)}, mediaType = "application/json")
			),
			@ApiResponse(
					responseCode = "500",
					description = "Внутренняя ошибка сервера",
					content = @Content(schema = @Schema(implementation = ErrorResponseDto.class), mediaType = "application/json")
			)
	})
	@Operation(summary = "Обновление пользователя по его Id")
	@PatchMapping("/{clientId}")
	ResponseEntity<UserResponseDto> updateByClientId(@RequestBody UserUpdateDto request, @PathVariable UUID clientId);
	
	@ApiResponses({
			@ApiResponse(
					responseCode = "200",
					description = "Пользователь успешно удалён"
			),
			@ApiResponse(
					responseCode = "400",
					description = "Некорректный запрос",
					content = @Content(examples = {@ExampleObject(ApiResponseExamples.BAD_REQUEST_EXAMPLE)}, mediaType = "application/json")
			),
			@ApiResponse(
					responseCode = "401",
					description = "Требуется авторизация",
					content = @Content(examples = {@ExampleObject(ApiResponseExamples.UNAUTHORIZED_EXAMPLE)}, mediaType = "application/json")
			),
			@ApiResponse(
					responseCode = "403",
					description = "Нет прав на выполнение операции",
					content = @Content(examples = {@ExampleObject(ApiResponseExamples.FORBIDDEN_EXAMPLE)}, mediaType = "application/json")
			),
			@ApiResponse(
					responseCode = "404",
					description = "Пользователь не найден",
					content = @Content(examples = {@ExampleObject(ApiResponseExamples.USER_NOT_FOUND_BY_ID_EXAMPLE)}, mediaType = "application/json")
			),
			@ApiResponse(
					responseCode = "500",
					description = "Внутренняя ошибка сервера",
					content = @Content(schema = @Schema(implementation = ErrorResponseDto.class), mediaType = "application/json")
			)
	})
	@Operation(summary = "Удаление пользователя по его Id")
	@DeleteMapping("/{clientId}")
	ResponseEntity<?> deleteByClientId(@PathVariable UUID clientId);
	
	@ApiResponses({
			@ApiResponse(
					responseCode = "200",
					description = "Пользователь успешно заблокирован",
					content = @Content(schema = @Schema(implementation = Boolean.class), mediaType = "application/json")
			),
			@ApiResponse(
					responseCode = "400",
					description = "Некорректный запрос",
					content = @Content(examples = {@ExampleObject(ApiResponseExamples.BAD_REQUEST_EXAMPLE)}, mediaType = "application/json")
			),
			@ApiResponse(
					responseCode = "401",
					description = "Требуется авторизация",
					content = @Content(examples = {@ExampleObject(ApiResponseExamples.UNAUTHORIZED_EXAMPLE)}, mediaType = "application/json")
			),
			@ApiResponse(
					responseCode = "403",
					description = "Нет прав на выполнение операции",
					content = @Content(examples = {@ExampleObject(ApiResponseExamples.FORBIDDEN_EXAMPLE)}, mediaType = "application/json")
			),
			@ApiResponse(
					responseCode = "404",
					description = "Пользователь не найден",
					content = @Content(examples = {@ExampleObject(ApiResponseExamples.USER_NOT_FOUND_BY_ID_EXAMPLE)}, mediaType = "application/json")
			),
			@ApiResponse(
					responseCode = "500",
					description = "Внутренняя ошибка сервера",
					content = @Content(schema = @Schema(implementation = ErrorResponseDto.class), mediaType = "application/json")
			)
	})
	@Operation(summary = "Блокировка пользователя по его Id")
	@GetMapping("/block/{clientId}")
	ResponseEntity<Boolean> blockAccountByClientId(@PathVariable UUID clientId);
	
	@ApiResponses({
			@ApiResponse(
					responseCode = "200",
					description = "Пользователь успешно деактивирован",
					content = @Content(schema = @Schema(implementation = Boolean.class), mediaType = "application/json")
			),
			@ApiResponse(
					responseCode = "400",
					description = "Некорректный запрос",
					content = @Content(examples = {@ExampleObject(ApiResponseExamples.BAD_REQUEST_EXAMPLE)}, mediaType = "application/json")
			),
			@ApiResponse(
					responseCode = "401",
					description = "Требуется авторизация",
					content = @Content(examples = {@ExampleObject(ApiResponseExamples.UNAUTHORIZED_EXAMPLE)}, mediaType = "application/json")
			),
			@ApiResponse(
					responseCode = "403",
					description = "Нет прав на выполнение операции",
					content = @Content(examples = {@ExampleObject(ApiResponseExamples.FORBIDDEN_EXAMPLE)}, mediaType = "application/json")
			),
			@ApiResponse(
					responseCode = "404",
					description = "Пользователь не найден",
					content = @Content(examples = {@ExampleObject(ApiResponseExamples.USER_NOT_FOUND_BY_ID_EXAMPLE)}, mediaType = "application/json")
			),
			@ApiResponse(
					responseCode = "500",
					description = "Внутренняя ошибка сервера",
					content = @Content(schema = @Schema(implementation = ErrorResponseDto.class), mediaType = "application/json")
			)
	})
	@Operation(summary = "Деактивировать пользователя по его Id")
	@GetMapping("/deactivate/{clientId}")
	ResponseEntity<Boolean> deactivateAccountByClientId(@PathVariable UUID clientId);
	
	@ApiResponses({
			@ApiResponse(
					responseCode = "200",
					description = "Пользователь успешно разблокирован",
					content = @Content(schema = @Schema(implementation = Boolean.class), mediaType = "application/json")
			),
			@ApiResponse(
					responseCode = "400",
					description = "Некорректный запрос",
					content = @Content(examples = {@ExampleObject(ApiResponseExamples.BAD_REQUEST_EXAMPLE)}, mediaType = "application/json")
			),
			@ApiResponse(
					responseCode = "401",
					description = "Требуется авторизация",
					content = @Content(examples = {@ExampleObject(ApiResponseExamples.UNAUTHORIZED_EXAMPLE)}, mediaType = "application/json")
			),
			@ApiResponse(
					responseCode = "403",
					description = "Нет прав на выполнение операции",
					content = @Content(examples = {@ExampleObject(ApiResponseExamples.FORBIDDEN_EXAMPLE)}, mediaType = "application/json")
			),
			@ApiResponse(
					responseCode = "404",
					description = "Пользователь не найден",
					content = @Content(examples = {@ExampleObject(ApiResponseExamples.USER_NOT_FOUND_BY_ID_EXAMPLE)}, mediaType = "application/json")
			),
			@ApiResponse(
					responseCode = "500",
					description = "Внутренняя ошибка сервера",
					content = @Content(schema = @Schema(implementation = ErrorResponseDto.class), mediaType = "application/json")
			)
	})
	@Operation(summary = "Разблокировать пользователя по его Id")
	@GetMapping("/unblock/{clientId}")
	ResponseEntity<Boolean> unblockAccountByClientId(@PathVariable UUID clientId);
	
	@ApiResponses({
			@ApiResponse(
					responseCode = "200",
					description = "Пользователь успешно активирован",
					content = @Content(schema = @Schema(implementation = Boolean.class), mediaType = "application/json")
			),
			@ApiResponse(
					responseCode = "400",
					description = "Некорректный запрос",
					content = @Content(examples = {@ExampleObject(ApiResponseExamples.BAD_REQUEST_EXAMPLE)}, mediaType = "application/json")
			),
			@ApiResponse(
					responseCode = "401",
					description = "Требуется авторизация",
					content = @Content(examples = {@ExampleObject(ApiResponseExamples.UNAUTHORIZED_EXAMPLE)}, mediaType = "application/json")
			),
			@ApiResponse(
					responseCode = "403",
					description = "Нет прав на выполнение операции",
					content = @Content(examples = {@ExampleObject(ApiResponseExamples.FORBIDDEN_EXAMPLE)}, mediaType = "application/json")
			),
			@ApiResponse(
					responseCode = "404",
					description = "Пользователь не найден",
					content = @Content(examples = {@ExampleObject(ApiResponseExamples.USER_NOT_FOUND_BY_ID_EXAMPLE)}, mediaType = "application/json")
			),
			@ApiResponse(
					responseCode = "500",
					description = "Внутренняя ошибка сервера",
					content = @Content(schema = @Schema(implementation = ErrorResponseDto.class), mediaType = "application/json")
			)
	})
	@Operation(summary = "Активировать пользователя по его Id")
	@GetMapping("/activate/{clientId}")
	ResponseEntity<Boolean> activateAccountByClientId(@PathVariable UUID clientId);
	
	@ApiResponses({
			@ApiResponse(
					responseCode = "200",
					description = "Пользователю успешно добавлена роль",
					content = @Content(schema = @Schema(implementation = Boolean.class), mediaType = "application/json")
			),
			@ApiResponse(
					responseCode = "400",
					description = """
							1) Некорректный запрос;
							2) Роль уже назначена пользователю.
							""",
					content = @Content(examples = {
							@ExampleObject(value = ApiResponseExamples.BAD_REQUEST_EXAMPLE),
							@ExampleObject(name = "Роль уже назначена пользователю", value = ApiResponseExamples.USER_ALREADY_HAVE_ROLE_EXAMPLE)},
							mediaType = "application/json")
			),
			@ApiResponse(
					responseCode = "401",
					description = "Требуется авторизация",
					content = @Content(examples = {@ExampleObject(value = ApiResponseExamples.UNAUTHORIZED_EXAMPLE)}, mediaType = "application/json")
			),
			@ApiResponse(
					responseCode = "403",
					description = "Нет прав на выполнение операции",
					content = @Content(examples = {@ExampleObject(value = ApiResponseExamples.FORBIDDEN_EXAMPLE)}, mediaType = "application/json")
			),
			@ApiResponse(
					responseCode = "404",
					description = """
							1) Пользователь не найден;
							2) Роль не найдена.
							""",
					content = @Content(examples = {
							@ExampleObject(name = "Пользователь не найден", value = ApiResponseExamples.USER_NOT_FOUND_BY_ID_EXAMPLE),
							@ExampleObject(name = "Роль не найдена", value = ApiResponseExamples.ROLE_NOT_FOUND_BY_ID_EXAMPLE)
					}, mediaType = "application/json")
			),
			@ApiResponse(
					responseCode = "500",
					description = "Внутренняя ошибка сервера",
					content = @Content(schema = @Schema(implementation = ErrorResponseDto.class), mediaType = "application/json")
			)
	})
	@Operation(summary = "Добавление роль пользователю")
	@PostMapping("/role/add")
	ResponseEntity<Boolean> addRoleToUser(@RequestParam UserRole role, @RequestParam UUID userId);
	
	@ApiResponses({
			@ApiResponse(
					responseCode = "200",
					description = "У пользователя успешно удалена роль",
					content = @Content(schema = @Schema(implementation = Boolean.class), mediaType = "application/json")
			),
			@ApiResponse(
					responseCode = "400",
					description = """
							1) Некорректный запрос;
							2) У пользователя отсутствует указанная роль.
							""",
					content = @Content(examples = {
							@ExampleObject(value = ApiResponseExamples.BAD_REQUEST_EXAMPLE),
							@ExampleObject(name = "Роль уже назначена пользователю", value = ApiResponseExamples.USER_DOES_NOT_HAVE_ROLE_EXAMPLE)
					}, mediaType = "application/json")
			),
			@ApiResponse(
					responseCode = "401",
					description = "Требуется авторизация",
					content = @Content(examples = {@ExampleObject(value = ApiResponseExamples.UNAUTHORIZED_EXAMPLE)}, mediaType = "application/json")
			),
			@ApiResponse(
					responseCode = "403",
					description = "Нет прав на выполнение операции",
					content = @Content(examples = {@ExampleObject(value = ApiResponseExamples.FORBIDDEN_EXAMPLE)}, mediaType = "application/json")
			),
			@ApiResponse(
					responseCode = "404",
					description = """
							1) Пользователь не найден;
							2) Роль не найдена.
							""",
					content = @Content(examples = {
							@ExampleObject(name = "Пользователь не найден", value = ApiResponseExamples.USER_NOT_FOUND_BY_ID_EXAMPLE),
							@ExampleObject(name = "Роль не найдена", value = ApiResponseExamples.ROLE_NOT_FOUND_BY_ID_EXAMPLE)
					}, mediaType = "application/json")
			),
			@ApiResponse(
					responseCode = "500",
					description = "Внутренняя ошибка сервера",
					content = @Content(schema = @Schema(implementation = ErrorResponseDto.class), mediaType = "application/json")
			)
	})
	@Operation(summary = "Удаление роли у пользователя")
	@PostMapping("/role/remove")
	ResponseEntity<Boolean> removeRoleFromUser(@RequestParam UserRole role, @RequestParam UUID userId);
}