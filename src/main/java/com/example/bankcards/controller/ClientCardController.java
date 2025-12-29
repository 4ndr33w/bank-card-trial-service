package com.example.bankcards.controller;

import com.example.bankcards.dto.response.CardBalanceResponseDto;
import com.example.bankcards.dto.response.CardPageViewResponseDto;
import com.example.bankcards.dto.response.CardResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * @author 4ndr33w
 * @version 1.0
 */
@RequestMapping("/api/v1/clients/cards")
public interface ClientCardController {

	@GetMapping("/balance/{cardId}")
	ResponseEntity<CardBalanceResponseDto> getBalance(@PathVariable UUID cardId);
	
	@PostMapping("/page/{page}")
	ResponseEntity<CardPageViewResponseDto> getAllCardsByPage(@PathVariable Integer page, @RequestParam Integer limit);
	
	@GetMapping("/{cardId}")
	ResponseEntity<CardResponseDto> getCardById(@PathVariable UUID cardId);
	
	@GetMapping("/block/{cardId}")
	ResponseEntity<Boolean> blockCardRequest(@PathVariable UUID cardId);
	
	@PostMapping("/transfer")
	ResponseEntity<Boolean> transferMoney(@RequestParam BigDecimal amount, @RequestParam UUID cardIdFrom, @RequestParam UUID cardIdTo);
}