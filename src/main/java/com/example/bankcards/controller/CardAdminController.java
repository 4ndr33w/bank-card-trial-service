package com.example.bankcards.controller;

import com.example.bankcards.dto.request.CardRequestDto;
import com.example.bankcards.dto.response.CardPageViewResponseDto;
import com.example.bankcards.dto.response.CardResponseDto;
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
@RequestMapping("/api/v1/cards")
public interface CardAdminController {
	
	@PostMapping
	ResponseEntity<CardResponseDto> create(@RequestBody CardRequestDto request);
	
	@PostMapping("/{clientId}")
	ResponseEntity<CardPageViewResponseDto> getAllCardsByClientId(@PathVariable UUID clientId, @RequestParam Integer page);
	
	@PostMapping("/page/{page}")
	ResponseEntity<CardPageViewResponseDto> getAllCardsByPage(@PathVariable Integer page, @RequestParam Integer limit);
	
	@DeleteMapping("/{cardId}")
	ResponseEntity<?> deleteCard(@PathVariable UUID cardId);
	
	@GetMapping("/block/{cardId}")
	ResponseEntity<Boolean> blockCard(@PathVariable UUID cardId);
	
	@GetMapping("/activate/{cardId}")
	ResponseEntity<Boolean> activateCard(@PathVariable UUID cardId);
}