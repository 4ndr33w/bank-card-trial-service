package com.example.bankcards.dto.response;

import java.math.BigDecimal;

/**
 * @author 4ndr33w
 * @version 1.0
 */
public record CardBalanceResponseDto(
		BigDecimal balance
) {
}