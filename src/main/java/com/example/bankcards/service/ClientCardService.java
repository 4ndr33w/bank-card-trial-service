package com.example.bankcards.service;

import com.example.bankcards.dto.request.CardRequestDto;
import com.example.bankcards.dto.response.CardBalanceResponseDto;
import com.example.bankcards.dto.response.CardPageViewResponseDto;
import com.example.bankcards.dto.response.CardResponseDto;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

/**
 * @author 4ndr33w
 * @version 1.0
 */
public interface ClientCardService {
	
	boolean blockCardRequest(UUID cardId);
	
	CardPageViewResponseDto getAllCardsByPage(Integer page, Integer limit);
	
	CardResponseDto findCardById(UUID cardId);
	
	boolean transferMoney(BigDecimal amount, UUID cardIdFrom, UUID cardIdTo);
	
	CardBalanceResponseDto getCardBalance(UUID cardId);
}