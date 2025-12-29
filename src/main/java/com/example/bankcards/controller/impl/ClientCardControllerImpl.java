package com.example.bankcards.controller.impl;

import com.example.bankcards.controller.ClientCardController;
import com.example.bankcards.dto.response.CardBalanceResponseDto;
import com.example.bankcards.dto.response.CardPageViewResponseDto;
import com.example.bankcards.dto.response.CardResponseDto;
import com.example.bankcards.service.ClientCardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * @author 4ndr33w
 * @version 1.0
 */
@RestController
@RequiredArgsConstructor
public class ClientCardControllerImpl implements ClientCardController {
	
	private final ClientCardService cardService;
	
	@Override
	public ResponseEntity<CardBalanceResponseDto> getBalance(UUID cardId) {
		return ResponseEntity.status(HttpStatus.OK).body(cardService.getCardBalance(cardId));
	}
	
	@Override
	public ResponseEntity<CardPageViewResponseDto> getAllCardsByPage(Integer page, Integer limit) {
		return ResponseEntity.status(HttpStatus.OK).body(cardService.getAllCardsByPage(page, limit));
	}
	
	@Override
	public ResponseEntity<CardResponseDto> getCardById(UUID cardId) {
		return ResponseEntity.status(HttpStatus.OK).body(cardService.findCardById(cardId));
	}
	
	@Override
	public ResponseEntity<Boolean> blockCardRequest(UUID cardId) {
		return ResponseEntity.status(HttpStatus.OK).body(cardService.blockCardRequest(cardId));
	}
	
	@Override
	public ResponseEntity<Boolean> transferMoney(BigDecimal amount, UUID cardIdFrom, UUID cardIdTo) {
		return ResponseEntity.status(HttpStatus.OK).body(cardService.transferMoney(amount, cardIdFrom, cardIdTo));
	}
}