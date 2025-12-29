package com.example.bankcards.controller;

import com.example.bankcards.config.openapi.constant.ApiResponseExamples;
import com.example.bankcards.dto.request.CardRequestDto;
import com.example.bankcards.dto.response.CardBalanceResponseDto;
import com.example.bankcards.dto.response.CardPageViewResponseDto;
import com.example.bankcards.dto.response.CardResponseDto;
import com.example.bankcards.exception.dto.ErrorResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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
@Tag(name = "Контроллер администрирования операций над картами клиентов")
@RequestMapping("/api/v1/cards")
public interface AdminCardController {
	
	@ApiResponses({
			@ApiResponse(
					responseCode = "202",
					description = "Успешно создана карта",
					content = @Content(schema = @Schema(implementation = CardResponseDto.class), mediaType = "application/json")
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
					responseCode = "500",
					description = "Внутренняя ошибка сервера",
					content = @Content(schema = @Schema(implementation = ErrorResponseDto.class), mediaType = "application/json")
			)
	})
	@Operation(summary = "Создание карты")
	@PostMapping
	ResponseEntity<CardResponseDto> create(@RequestBody CardRequestDto request);
	
	@ApiResponses({
			@ApiResponse(
					responseCode = "200",
					description = "Получен список всех карт клиента с пагинацией",
					content = @Content(schema = @Schema(implementation = CardPageViewResponseDto.class), mediaType = "application/json")
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
					responseCode = "500",
					description = "Внутренняя ошибка сервера",
					content = @Content(schema = @Schema(implementation = ErrorResponseDto.class), mediaType = "application/json")
			)
	})
	@Operation(summary = "Просмотр всех карт клиента")
	@PostMapping("/{clientId}")
	ResponseEntity<CardPageViewResponseDto> getAllCardsByClientId(@PathVariable UUID clientId, @RequestParam Integer page);
	
	@ApiResponses({
			@ApiResponse(
					responseCode = "200",
					description = "Получен список всех постранично",
					content = @Content(schema = @Schema(implementation = CardPageViewResponseDto.class), mediaType = "application/json")
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
					responseCode = "500",
					description = "Внутренняя ошибка сервера",
					content = @Content(schema = @Schema(implementation = ErrorResponseDto.class), mediaType = "application/json")
			)
	})
	@Operation(summary = "Просмотр всех карт постранично")
	@PostMapping("/page/{page}")
	ResponseEntity<CardPageViewResponseDto> getAllCardsByPage(@PathVariable Integer page, @RequestParam Integer limit);
	
	@ApiResponses({
			@ApiResponse(
					responseCode = "204",
					description = "Карта клиента целиком удалена"
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
					responseCode = "500",
					description = "Внутренняя ошибка сервера",
					content = @Content(schema = @Schema(implementation = ErrorResponseDto.class), mediaType = "application/json")
			)
	})
	@Operation(summary = "Удаление карты")
	@DeleteMapping("/{cardId}")
	ResponseEntity<?> deleteCard(@PathVariable UUID cardId);
	
	@ApiResponses({
			@ApiResponse(
					responseCode = "200",
					description = "Карта заблокирована"
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
					responseCode = "500",
					description = "Внутренняя ошибка сервера",
					content = @Content(schema = @Schema(implementation = ErrorResponseDto.class), mediaType = "application/json")
			)
	})
	@Operation(summary = "Блокировка карты")
	@GetMapping("/block/{cardId}")
	ResponseEntity<Boolean> blockCard(@PathVariable UUID cardId);
	
	@ApiResponses({
			@ApiResponse(
					responseCode = "200",
					description = "Карта активирована"
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
					responseCode = "500",
					description = "Внутренняя ошибка сервера",
					content = @Content(schema = @Schema(implementation = ErrorResponseDto.class), mediaType = "application/json")
			)
	})
	@Operation(summary = "Разблокировка карты")
	@GetMapping("/activate/{cardId}")
	ResponseEntity<Boolean> activateCard(@PathVariable UUID cardId);
}