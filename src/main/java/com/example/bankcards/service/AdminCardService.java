package com.example.bankcards.service;

import com.example.bankcards.dto.request.CardRequestDto;
import com.example.bankcards.dto.response.CardPageViewResponseDto;
import com.example.bankcards.dto.response.CardResponseDto;
import com.example.bankcards.exception.businessException.CardNotFoundException;
import com.example.bankcards.exception.businessException.CardActivationException;

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
	
	/**
	 * Заблокировать карту книента по уникальному идентификатору карты.
	 *
	 * @param cardId {@code UUID} - уникальный идентификатор карты.
	 * @return true - если карта успешно заблокирована
	 *
	 * @throws CardNotFoundException в случае если карта не найдена
	 * @throws CardActivationException в случае если карта уже заблокирована на момент попытки блокировки
	 */
	boolean blockCard(UUID cardId);
	
	/**
	 * Активировать карту клиента по уникальному идентификатору карты.
	 * @param cardId {@code UUID} - уникальный идентификатор карты.
	 * @return true - если карта успешно активирована
	 *
	 * @throws CardNotFoundException в случае если карта не найдена
	 * @throws CardActivationException в случае если карта уже активирована на момент попытки активации
	 */
	boolean activateCard(UUID cardId);
	
	/**
	 * Удалить карту клиента по уникальному идентификатору карты.
	 * @param cardId {@code UUID} - уникальный идентификатор карты.
	 * @throws CardNotFoundException в случае если карта не найдена
	 */
	void deleteCard(UUID cardId);
	
	/**
	 * Получить список всех карт постранично
	 * @param page номер страницы
	 * @param limit количество отображаемых карт на странице
	 * @return DTO с данными
	 */
	CardPageViewResponseDto getAllCardsByPage(Integer page, Integer limit);
	
	/**
	 * Получить список всех карт клиента постранично
	 * @param clientId {@code UUID} - уникальный идентификатор клиента
	 * @param page номер страницы
	 * @return DTO с данными
	 */
	CardPageViewResponseDto getAllCardsByClientId(UUID clientId, Integer page);
}