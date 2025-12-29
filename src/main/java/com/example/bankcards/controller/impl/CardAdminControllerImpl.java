package com.example.bankcards.controller.impl;

import com.example.bankcards.controller.CardAdminController;
import com.example.bankcards.dto.request.CardRequestDto;
import com.example.bankcards.dto.response.CardPageViewResponseDto;
import com.example.bankcards.dto.response.CardResponseDto;
import com.example.bankcards.service.AdminCardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * @author 4ndr33w
 * @version 1.0
 */
@RestController
@RequiredArgsConstructor
public class CardAdminControllerImpl implements CardAdminController {
	
	private final AdminCardService cardService;
	
	@Override
	public ResponseEntity<CardResponseDto> create(CardRequestDto request) {
		return ResponseEntity.status(HttpStatus.CREATED).body(cardService.createCard(request));
	}
	
	@Override
	public ResponseEntity<CardPageViewResponseDto> getAllCardsByClientId(UUID clientId, Integer page) {
		return ResponseEntity.status(HttpStatus.OK).body(cardService.getAllCardsByClientId(clientId, page));
	}
	
	@Override
	public ResponseEntity<CardPageViewResponseDto> getAllCardsByPage(Integer page, Integer limit) {
		return ResponseEntity.status(HttpStatus.OK).body(cardService.getAllCardsByPage(page, limit));
	}
	
	@Override
	public ResponseEntity<?> deleteCard(UUID cardId) {
		cardService.deleteCard(cardId);
		return ResponseEntity.ok().build();
	}
	
	@Override
	public ResponseEntity<Boolean> blockCard(UUID cardId) {
		return ResponseEntity.status(HttpStatus.OK).body(cardService.blockCard(cardId));
	}
	
	@Override
	public ResponseEntity<Boolean> activateCard(UUID cardId) {
		return ResponseEntity.status(HttpStatus.OK).body(cardService.activateCard(cardId));
	}
}