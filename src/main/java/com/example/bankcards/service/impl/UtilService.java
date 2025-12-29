package com.example.bankcards.service.impl;

import com.example.bankcards.entity.User;
import com.example.bankcards.exception.authorizationException.SecurityContextHolderException;
import com.example.bankcards.properties.CardProperties;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.security.data.AppUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.UUID;

/**
 * @author 4ndr33w
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
public class UtilService {
	
	private final CardProperties cardProperties;
	private final CardRepository cardRepository;
	
	/**
	 * Получить уникальный идентификатор пользователя из контекста безопасности
	 * @return UUID - уникальный идентификатор пользователя
	 */
	public UUID getUserIdFromSecurityContext() {
		try {
			return ((AppUserDetails) SecurityContextHolder
					.getContext()
					.getAuthentication()
					.getPrincipal())
					.getUser()
					.getId();
		}
		catch (Exception e) {
			throw new SecurityContextHolderException("Ошибка при извлечении данных пользователя", e);
		}
	}
	
	/**
	 * Получить объект пользователя из контекста безопасности
	 * @return User - объект пользователя
	 */
	public User getUserFromSecurityContext() {
		try {
			return ((AppUserDetails)SecurityContextHolder
					.getContext()
					.getAuthentication()
					.getPrincipal())
					.getUser();
		}
		catch (Exception e) {
			throw new SecurityContextHolderException("Ошибка при извлечении данных пользователя", e);
		}
	}
	
	/**
	 * Установить лимит отображения записей на странице
	 * В пределах от 5 до 20 записей
	 *
	 * @param limit - лимит записей
	 * @return Integer - лимит записей
	 */
	public int setPageLimit(Integer limit) {
		if(limit == null || limit < 1) {
			return 5;
		}
		if(limit > 20) {
			return 20;
		}
		return limit;
	}
	
	public String generateCvv() {
		Random random = new Random();
		int cvv = random.nextInt(1000);
		
		return String.format("%03d", cvv);
	}
	
	/**
	 * Метод генерирует номер карты
	 *
	 * Подход не очень оптимальный. Возможные варианты:
	 * 1) Сгенерировать сразу все возможные вариации и занести в базу с пометкой "незадействован"
	 *    и при создании новой карты искать номер в БД и менять статус на "задействован" и добавлять clientId
	 * 2) Отправлять заявку на создание карты асинхронно
	 *    и не ждать ответа - пускай номер генерируется где-то там в другом сервисе
	 *
	 * @return String - сгенерированный номер карты
	 */
	public String generateCardNumber() {
		int binPrefix = cardProperties.getBinPrefix();
		String cardNumber = generateBankCardNumber(binPrefix);
		
		boolean isExists = cardRepository.existsByCardNumber(cardNumber);
		while(isExists) {
			cardNumber = generateBankCardNumber(binPrefix);
			isExists = cardRepository.existsByCardNumber(cardNumber);
		}
		
		return cardNumber;
	}
	
	/**
	 * Метод генерирует номер карты
	 * в основу идет префикс с кодом банка
	 * @param binPrefix - префикс с кодом банка
	 * @return String - сгенерированный номер карты
	 */
	private String generateBankCardNumber(int binPrefix) {
		Random random = new Random();
		StringBuilder cardNumber = new StringBuilder(Integer.toString(binPrefix));

		for (int i = 0; i < 9; i++) {
			cardNumber.append(random.nextInt(10));
		}
		int checkDigit = calculateCheckDigit(cardNumber.toString());
		cardNumber.append(checkDigit);
		
		return formatCardNumber(cardNumber.toString());
	}
	
	/**
	 * Метод форматирует номер карты с пробелами каждые 4 цифры
	 * @param number - номер карты
	 * @return String - отформатированный номер карты
	 */
	private String formatCardNumber(String number) {
		return number.replaceAll("(\\d{4})(?=\\d)", "$1 ");
	}
	
	/**
	 * Алгоритм расчета контрольной суммы по Luhn algorithm
	 * @param cardNumberWithoutChecksum - номер карты без контрольной суммы
	 * @return int - контрольная сумма
	 */
	private int calculateCheckDigit(String cardNumberWithoutChecksum) {
		int sum = 0;
		boolean isSecond = false;
		for (int i = cardNumberWithoutChecksum.length() - 1; i >= 0; i--) {
			int digit = Character.getNumericValue(cardNumberWithoutChecksum.charAt(i));
			if (isSecond) {
				digit *= 2;
				if (digit > 9) digit -= 9;
			}
			sum += digit;
			isSecond = !isSecond;
		}
		return ((sum * 9) % 10);
	}
}