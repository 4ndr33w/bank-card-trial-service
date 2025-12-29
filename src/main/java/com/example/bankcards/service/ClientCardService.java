package com.example.bankcards.service;

import com.example.bankcards.dto.response.CardBalanceResponseDto;
import com.example.bankcards.dto.response.CardPageViewResponseDto;
import com.example.bankcards.dto.response.CardResponseDto;
import com.example.bankcards.exception.businessException.CardBalanceException;
import com.example.bankcards.exception.businessException.CardNotFoundException;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * @author 4ndr33w
 * @version 1.0
 */
public interface ClientCardService {
	
	/**
	 * Отправить запрос на блокировку своей карты по её уникальному идентификатору
	 *
	 * @param cardId - уникальный идентификатор карты
	 * @return true - если запрос на блокировку карты отправлен
	 * @throws CardNotFoundException - если у аутенфицированного пользователя карта не найдена
	 */
	boolean blockCardRequest(UUID cardId);
	
	/**
	 * Получить список собственных карт аутенфицированного пользователя с пагинацией
	 *
	 * @param page номер страницы
	 * @param limit количество карт на странице
	 * @return DTO со списком карт
	 */
	CardPageViewResponseDto getAllCardsByPage(Integer page, Integer limit);
	
	/**
	 * Получить информацию о собственной карте аутенфицированного пользователя по её уникальному идентификатору
	 *
	 * @param cardId - уникальный идентификатор карты
	 * @return DTO с информацией о карте
	 */
	CardResponseDto findCardById(UUID cardId);
	
	/**
	 * Перевод средств с между собственными картами аутенфицированного пользователя
	 *
	 * @param amount - сумма перевода
	 * @param cardIdFrom - уникальный идентификатор карты с которой переводим средства
	 * @param cardIdTo - уникальный идентификатор карты на которую переводим средства
	 * @return true - если перевод прошёл успешно
	 *
	 * @throws CardNotFoundException в случае если у аутенфицированного нет одной или нескольких карт среди указанных
	 * @throws CardBalanceException в случае если на карте с которой осуществляется перевод недостаточно средств
	 */
	boolean transferMoney(BigDecimal amount, UUID cardIdFrom, UUID cardIdTo);
	
	/**
	 * Получить баланс собственной карты аутенфицированного пользователя по её уникальному идентификатору
	 *
	 * @param cardId - уникальный идентификатор карты
	 * @return DTO с балансом карты
	 * @throws CardNotFoundException - если у аутенфицированного пользователя карта не найдена
	 */
	CardBalanceResponseDto getCardBalance(UUID cardId);
}