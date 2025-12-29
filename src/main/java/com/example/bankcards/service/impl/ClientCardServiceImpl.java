package com.example.bankcards.service.impl;

import com.example.bankcards.dto.response.CardBalanceResponseDto;
import com.example.bankcards.dto.response.CardPageViewResponseDto;
import com.example.bankcards.dto.response.CardResponseDto;
import com.example.bankcards.service.ClientCardService;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * @author 4ndr33w
 * @version 1.0
 */
public class ClientCardServiceImpl implements ClientCardService {
		@Override
		public boolean blockCardRequest(UUID cardId) {
				return false;
		}

		@Override
		public CardPageViewResponseDto getAllCardsByPage(Integer page, Integer limit) {
				return null;
		}

		@Override
		public CardResponseDto findCardById(UUID cardId) {
				return null;
		}

		@Override
		public boolean transferMoney(BigDecimal amount, UUID cardIdFrom, UUID cardIdTo) {
				return false;
		}

		@Override
		public CardBalanceResponseDto getCardBalance(UUID cardId) {
				return null;
		}
}
