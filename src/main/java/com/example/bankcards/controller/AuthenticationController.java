package com.example.bankcards.controller;

import com.example.bankcards.config.openapi.constant.ApiResponseExamples;
import com.example.bankcards.dto.response.AuthenticationResponseDto;
import com.example.bankcards.dto.response.UserResponseDto;
import com.example.bankcards.exception.dto.ErrorResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author 4ndr33w
 * @version 1.0
 */
@Tag(name = "Контроллер управления токенами")
@RequestMapping("/api/v1/auth")
public interface AuthenticationController {
	
	@ApiResponses({
			@ApiResponse(
					responseCode = "200",
					description = "Успешно получена новая пара токенов",
					content = @Content(schema = @Schema(implementation = AuthenticationResponseDto.class), mediaType = "application/json")
			),
			@ApiResponse(
					responseCode = "400",
					description = "Некорректный запрос",
					content = @Content(examples = {@ExampleObject(ApiResponseExamples.BAD_REQUEST_EXAMPLE)}, mediaType = "application/json")
			),
			@ApiResponse(
					responseCode = "401",
					description = "Токен не прошел валидацию",
					content = @Content(examples = {@ExampleObject(ApiResponseExamples.UNAUTHORIZED_EXAMPLE)}, mediaType = "application/json")
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
	@Operation(summary = "Получение новой пары токенов")
	@PostMapping("/refresh")
	ResponseEntity<AuthenticationResponseDto> refresh(@RequestParam String refreshToken);
}