package com.example.bankcards.service.impl;

import com.example.bankcards.dto.mapper.CardMapper;
import com.example.bankcards.dto.projection.CardBalanceProjection;
import com.example.bankcards.dto.response.CardBalanceResponseDto;
import com.example.bankcards.dto.response.CardPageViewResponseDto;
import com.example.bankcards.dto.response.CardResponseDto;
import com.example.bankcards.entity.Card;
import com.example.bankcards.exception.businessException.CardNotFoundException;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.service.ClientCardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

/**
 * @author 4ndr33w
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
public class ClientCardServiceImpl implements ClientCardService {
	
	private final TransferService transferService;
	private final CardRepository cardRepository;
	private final UtilService utilService;
	private final CardMapper cardMapper;
	
		@Override
		public boolean blockCardRequest(UUID cardId) {
				return false;
		}

		@Override
		@Transactional(readOnly = true)
		public CardPageViewResponseDto getAllCardsByPage(Integer page, Integer limit) {
			int pageLimit = utilService.setPageLimit(limit);
			int paginationPage = (page == null || page < 1) ? 1 : (page - 1);
			
			UUID userId = utilService.getUserIdFromSecurityContext();
			Pageable pageable = PageRequest.of(paginationPage, pageLimit);
			Page<Card> cardsPage = cardRepository.findAllByClientId(userId, pageable);
			
			long totalCards = cardRepository.count();
			long totalPages = (totalCards / pageLimit) + 1;
			List<CardResponseDto> cardResponseDtoList = cardsPage.stream().map(cardMapper::mapEntityToResponse).toList();
			
			return new CardPageViewResponseDto(paginationPage + 1, pageLimit, totalPages, totalCards, cardResponseDtoList);
		}

		@Override
		@Transactional(readOnly = true)
		public CardResponseDto findCardById(UUID cardId) {
				UUID userId = utilService.getUserIdFromSecurityContext();
				Card existingClientCard = cardRepository.findCardByIdAndClientId(cardId, userId)
								.orElseThrow(
										() -> new CardNotFoundException("Не найдена карта с id: %s у пользователя с id: %s".formatted(cardId, userId)));
				
				return cardMapper.mapEntityToResponse(existingClientCard);
		}

		@Override
		@Transactional
		public boolean transferMoney(BigDecimal amount, UUID cardIdFrom, UUID cardIdTo) {
			UUID userId = utilService.getUserIdFromSecurityContext();
			List<Card> cards = cardRepository.findAllByIdsAndClientId(List.of(cardIdFrom, cardIdTo), userId);
			
			if(cards.size() < 2) {
				throw new CardNotFoundException("Не найдна одна или несколько указанных карт у пользователя с id: %s".formatted(userId));
			}
			if(cards.size() > 2) {
				throw new CardNotFoundException("При поиске двух карт найдено более двух карт у пользователя с id: %s".formatted(userId));
			}
			Card cardFrom = cards.stream().filter(x -> x.getId().equals(cardIdFrom)).findFirst()
					.orElseThrow(
							() -> new CardNotFoundException("Не найдена карта с id: %s у пользователя с id: %s".formatted(cardIdFrom, userId)));
			Card cardTo = cards.stream().filter(x -> x.getId().equals(cardIdTo)).findFirst()
					.orElseThrow(
							() -> new CardNotFoundException("Не найдена карта с id: %s у пользователя с id: %s".formatted(cardIdTo, userId)));
			
				return transferService.transferMoney(cardFrom, cardTo, amount);
		}

		@Override
		@Transactional(readOnly = true)
		public CardBalanceResponseDto getCardBalance(UUID cardId) {
			UUID userId = utilService.getUserIdFromSecurityContext();
			CardBalanceProjection balance = cardRepository.findBalanceByIdAndClientId(cardId, userId)
					.orElseThrow(
							() -> new CardNotFoundException("Не найдена карта с id: %s у пользователя с id: %s".formatted(cardId, userId)));
			
				return cardMapper.mapBalanceResponse(balance);
		}
}