package com.example.bankcards.service;

import com.example.bankcards.dto.request.CardRequestDto;
import com.example.bankcards.dto.response.CardPageViewResponseDto;
import com.example.bankcards.dto.response.CardResponseDto;

import java.util.List;
import java.util.UUID;

/**
 * @author 4ndr33w
 * @version 1.0
 */
public interface AdminCardService {
	
	/**
	 * Создать новую карту для пользователя.
	 *
	 * @param cardRequestDto DTO - содержит {@code UUID} - уникальный идентификатор пользователя,
	 * для которого требуется создать карту.
	 * @return DTO с данными созданной карты.
	 * @throws NullPointerException Если переданный параметр {@code CardRequestDto} не инициализирован.
	 */
	CardResponseDto createCard(CardRequestDto cardRequestDto);
	
	boolean blockCard(UUID cardId);
	
	boolean activateCard(UUID cardId);
	
	void deleteCard(UUID cardId);
	
	CardPageViewResponseDto getAllCardsByPage(Integer page, Integer limit);
	
	List<CardResponseDto> getAllCardsByClientId(UUID clientId);
}
