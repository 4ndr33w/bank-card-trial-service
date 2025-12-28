package com.example.bankcards.dto.projection;

import java.math.BigDecimal;

/**
 * Проекция для отображения баланса карты пользователя
 *
 * @author 4ndr33w
 * @version 1.0
 */
public interface CardBalanceProjection {
	
	BigDecimal getBalance();
}
