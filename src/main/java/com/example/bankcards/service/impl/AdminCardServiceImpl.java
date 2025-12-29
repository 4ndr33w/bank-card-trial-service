package com.example.bankcards.service.impl;

import com.example.bankcards.dto.mapper.CardMapper;
import com.example.bankcards.dto.request.CardRequestDto;
import com.example.bankcards.dto.response.CardPageViewResponseDto;
import com.example.bankcards.dto.response.CardResponseDto;
import com.example.bankcards.dto.response.UserResponseDto;
import com.example.bankcards.entity.Card;
import com.example.bankcards.enums.CardStatus;
import com.example.bankcards.exception.businessException.CardActivationException;
import com.example.bankcards.exception.businessException.CardNotFoundException;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.service.AdminCardService;
import com.example.bankcards.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * @author 4ndr33w
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
public class AdminCardServiceImpl implements AdminCardService {
	
	private final UserService userService;
	private final CardRepository cardRepository;
	private final UtilService utilService;
	private final CardMapper cardMapper;
	
		@Override
		@Transactional
		public CardResponseDto createCard(CardRequestDto cardRequestDto) {
			// Если пользователь не сеществует, то выскочит UserNotFoundException
			UserResponseDto existingUser = userService.findById(cardRequestDto.clientId());
			Card newCard = cardMapper.mapRequestToEntity(cardRequestDto, existingUser);
			Card savedCard = cardRepository.save(newCard);
			
			return cardMapper.mapEntityToResponse(savedCard);
		}

		@Override
		@Transactional
		public boolean blockCard(UUID cardId) {
			Card card = cardRepository.findById(cardId)
					.orElseThrow(() -> new CardNotFoundException("Не найдена карта с id: %s".formatted(cardId)));
			
			if(card.getStatus().equals(CardStatus.BLOCKED)) {
				throw new CardActivationException("Карта c с id: %s уже заблокирована".formatted(cardId));
			}
			card.setStatus(CardStatus.BLOCKED);
			
			return true;
		}

		@Override
		@Transactional
		public boolean activateCard(UUID cardId) {
			Card card = cardRepository.findById(cardId)
					.orElseThrow(() -> new CardNotFoundException("Не найдена карта с id: %s".formatted(cardId)));
			
			if(card.getStatus().equals(CardStatus.ACTIVE)) {
				throw new CardActivationException("Картаc с id: %s уже активирована".formatted(cardId));
			}
			card.setStatus(CardStatus.ACTIVE);
			
			return true;
		}

		@Override
		@Transactional
		public void deleteCard(UUID cardId) {
			Card card = cardRepository.findById(cardId)
					.orElseThrow(() -> new CardNotFoundException("Не найдена карта с id: %s".formatted(cardId)));
			
			cardRepository.delete(card);
		}

		@Override
		@Transactional(readOnly = true)
		public CardPageViewResponseDto getAllCardsByPage(Integer page, Integer limit) {
			int pageLimit = utilService.setPageLimit(limit);
			int paginationPage = (page == null || page < 1) ? 0 : (page - 1);

			Pageable pageable = PageRequest.of(paginationPage, pageLimit);
			Page<Card> cardsPage = cardRepository.findAll(pageable);
			
			long totalCards = cardRepository.count();
			long totalPages = (totalCards / pageLimit) + 1;
			List<CardResponseDto> cardResponseDtoList = cardsPage.stream().map(cardMapper::mapEntityToResponse).toList();
			
			return new CardPageViewResponseDto(paginationPage + 1, pageLimit, totalPages, totalCards, cardResponseDtoList);
		}

		@Override
		public CardPageViewResponseDto getAllCardsByClientId(UUID clientId, Integer page) {
			int pageLimit = 20;
			int paginationPage = (page == null || page < 1) ? 0 : (page - 1);
			
			Pageable pageable = PageRequest.of(paginationPage, pageLimit);
			Page<Card> cardsPage = cardRepository.findAllByClientId(clientId, pageable);
			
			long totalCards = cardRepository.count();
			long totalPages = (totalCards / pageLimit) + 1;
			List<CardResponseDto> cardResponseDtoList = cardsPage.stream().map(cardMapper::mapEntityToResponse).toList();
			
			return new CardPageViewResponseDto(paginationPage + 1, pageLimit, totalPages, totalCards, cardResponseDtoList);
		}
}