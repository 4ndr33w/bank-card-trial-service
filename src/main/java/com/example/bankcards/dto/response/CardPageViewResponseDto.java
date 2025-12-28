package com.example.bankcards.dto.response;

import java.util.List;

/**
 * @author 4ndr33w
 * @version 1.0
 */
public record CardPageViewResponseDto(
		int currentPage,
		int limit,
		long totalPages,
		long totalCards,
		List<CardResponseDto> cards
) {
}