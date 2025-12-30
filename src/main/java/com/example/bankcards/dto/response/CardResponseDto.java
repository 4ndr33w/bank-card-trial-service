package com.example.bankcards.dto.response;

import com.example.bankcards.enums.CardStatus;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * @author 4ndr33w
 * @version 1.0
 */
public record CardResponseDto(
		String cardNumber,
		String cardHolder,
		String expirationDate,
		CardStatus status,
		BigDecimal balance
) {
}