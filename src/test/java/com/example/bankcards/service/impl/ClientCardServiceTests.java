package com.example.bankcards.service.impl;

import com.example.bankcards.dto.mapper.CardMapper;
import com.example.bankcards.dto.projection.CardBalanceProjection;
import com.example.bankcards.dto.response.CardBalanceResponseDto;
import com.example.bankcards.dto.response.CardPageViewResponseDto;
import com.example.bankcards.dto.response.CardResponseDto;
import com.example.bankcards.entity.Card;
import com.example.bankcards.enums.CardStatus;
import com.example.bankcards.exception.businessException.CardNotFoundException;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.utils.TestUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author 4ndr33w
 * @version 1.0
 */
@ExtendWith(MockitoExtension.class)
public class ClientCardServiceTests {
	
	
	@Mock
	private TransferService transferService;
	
	@Mock
	private CardRepository cardRepository;
	
	@Mock
	private UtilService utilService;
	
	@Mock
	private CardMapper cardMapper;
	
	@InjectMocks
	private ClientCardServiceImpl clientCardService;
	
	@Test
	@DisplayName("Блокировка карты - успешный сценарий")
	void blockCardRequest_ShouldBlockCard_WhenCardBelongsToUser() {
		UUID userId = TestUtils.testUser().getId();
		UUID cardId = UUID.fromString("f70907df-196d-483f-8faa-b04e9d988b0c");
		
		Card existingCard = Card.builder()
				.id(cardId)
				.clientId(userId)
				.status(CardStatus.ACTIVE)
				.cardHolder("Вася Пупкин")
				.cardNumber("1234 5678 9012 3456")
				.balance(new BigDecimal("1000.00"))
				.build();
		
		when(utilService.getUserIdFromSecurityContext()).thenReturn(userId);
		when(cardRepository.findCardByIdAndClientId(cardId, userId))
				.thenReturn(Optional.of(existingCard));

		boolean result = clientCardService.blockCardRequest(cardId);

		assertTrue(result);
		assertEquals(CardStatus.BLOCKED, existingCard.getStatus());
		
		verify(utilService).getUserIdFromSecurityContext();
		verify(cardRepository).findCardByIdAndClientId(cardId, userId);
	}
	
	@Test
	@DisplayName("Блокировка карты - выбрасывает исключение если карта не принадлежит пользователю")
	void blockCardRequest_ShouldThrowCardNotFoundException_WhenCardNotBelongsToUser() {
		UUID userId = TestUtils.testUser().getId();
		UUID cardId = UUID.fromString("f70907df-196d-483f-8faa-b04e9d988b0c");
		
		when(utilService.getUserIdFromSecurityContext()).thenReturn(userId);
		when(cardRepository.findCardByIdAndClientId(cardId, userId))
				.thenReturn(Optional.empty());

		CardNotFoundException exception = assertThrows(CardNotFoundException.class,
				() -> clientCardService.blockCardRequest(cardId));
		
		assertEquals("Не найдена карта с id: " + cardId + " у пользователя с id: " + userId,
				exception.getMessage());
		
		verify(utilService).getUserIdFromSecurityContext();
		verify(cardRepository).findCardByIdAndClientId(cardId, userId);
	}
	
	@Test
	@DisplayName("Получение всех карт с пагинацией - успешный сценарий")
	void getAllCardsByPage_ShouldReturnPaginatedCardsForUser() {
		UUID userId = TestUtils.testUser().getId();
		Integer page = 1;
		Integer limit = 10;
		int pageLimit = 10;
		int paginationPage = 0;
		
		Card card1 = Card.builder()
				.id(UUID.fromString("f70907df-196d-483f-8faa-b04e9d988b0c"))
				.clientId(userId)
				.cardNumber("1234 5678 9012 3456")
				.cardHolder("Вася Пупкин")
				.status(CardStatus.ACTIVE)
				.balance(new BigDecimal("1000.00"))
				.build();
		
		Card card2 = Card.builder()
				.id(UUID.fromString("f70907df-196d-483f-8faa-b04e9d988b0d"))
				.clientId(userId)
				.cardNumber("9876 5432 1098 7654")
				.cardHolder("Вася Пупкин")
				.status(CardStatus.ACTIVE)
				.balance(new BigDecimal("500.00"))
				.build();
		
		List<Card> cards = List.of(card1, card2);
		Page<Card> cardsPage = new PageImpl<>(cards, PageRequest.of(paginationPage, pageLimit), 2);
		
		CardResponseDto cardResponse1 = new CardResponseDto(
				"1234 5678 9012 3456",
				"Вася Пупкин",
				"01/30",
				CardStatus.ACTIVE,
				new BigDecimal("1000.00")
		);
		
		CardResponseDto cardResponse2 = new CardResponseDto(
				"9876 5432 1098 7654",
				"Вася Пупкин",
				"01/30",
				CardStatus.ACTIVE,
				new BigDecimal("500.00")
		);
		
		CardResponseDto maskedCardResponse1 = new CardResponseDto(
				"1234 **** **** 3456",
				"Вася Пупкин",
				"01/30",
				CardStatus.ACTIVE,
				new BigDecimal("1000.00")
		);
		
		CardResponseDto maskedCardResponse2 = new CardResponseDto(
				"9876 **** **** 7654",
				"Вася Пупкин",
				"01/30",
				CardStatus.ACTIVE,
				new BigDecimal("500.00")
		);
		
		List<CardResponseDto> cardResponses = List.of(maskedCardResponse1, maskedCardResponse2);
		
		CardPageViewResponseDto expectedResponse = new CardPageViewResponseDto(
				1, pageLimit, 1, 2, cardResponses
		);
		
		when(utilService.getUserIdFromSecurityContext()).thenReturn(userId);
		when(utilService.setPageLimit(limit)).thenReturn(pageLimit);
		when(cardRepository.findAllByClientId(userId, PageRequest.of(paginationPage, pageLimit)))
				.thenReturn(cardsPage);
		when(cardRepository.count()).thenReturn(2L);
		when(cardMapper.mapEntityToResponse(card1)).thenReturn(cardResponse1);
		when(cardMapper.mapEntityToResponse(card2)).thenReturn(cardResponse2);
		when(utilService.maskCardNumber(cardResponse1)).thenReturn(maskedCardResponse1);
		when(utilService.maskCardNumber(cardResponse2)).thenReturn(maskedCardResponse2);
		
		// Act
		CardPageViewResponseDto result = clientCardService.getAllCardsByPage(page, limit);
		
		// Assert
		assertNotNull(result);
		assertEquals(expectedResponse.currentPage(), result.currentPage());
		assertEquals(expectedResponse.limit(), result.limit());
		assertEquals(expectedResponse.totalPages(), result.totalPages());
		assertEquals(expectedResponse.totalCards(), result.totalCards());
		assertEquals(expectedResponse.cards().size(), result.cards().size());
		assertEquals("1234 **** **** 3456", result.cards().get(0).cardNumber());
		assertEquals("9876 **** **** 7654", result.cards().get(1).cardNumber());
		
		verify(utilService).getUserIdFromSecurityContext();
		verify(utilService).setPageLimit(limit);
		verify(cardRepository).findAllByClientId(eq(userId), any(Pageable.class));
		verify(cardRepository).count();
		verify(cardMapper, times(2)).mapEntityToResponse(any(Card.class));
		verify(utilService, times(2)).maskCardNumber(any(CardResponseDto.class));
	}
	
	@Test
	@DisplayName("Получение карты по ID - успешный сценарий")
	void findCardById_ShouldReturnCard_WhenCardBelongsToUser() {
		Card existingCard = TestUtils.testCard1();
		CardResponseDto cardResponse = TestUtils.testCardResponseDto1();
		CardResponseDto maskedCardResponse = TestUtils.testMaskedCardResponseDto1();
		
		UUID userId = TestUtils.testUser().getId();
		UUID cardId = existingCard.getId();
		
		when(utilService.getUserIdFromSecurityContext()).thenReturn(userId);
		when(cardRepository.findCardByIdAndClientId(cardId, userId))
				.thenReturn(Optional.of(existingCard));
		when(cardMapper.mapEntityToResponse(existingCard)).thenReturn(cardResponse);
		when(utilService.maskCardNumber(cardResponse)).thenReturn(maskedCardResponse);

		CardResponseDto result = clientCardService.findCardById(cardId);

		assertNotNull(result);
		assertEquals(maskedCardResponse.cardNumber(), result.cardNumber());
		assertEquals(maskedCardResponse.cardHolder(), result.cardHolder());
		assertEquals(0, maskedCardResponse.balance().compareTo(result.balance()));
		
		verify(utilService).getUserIdFromSecurityContext();
		verify(cardRepository).findCardByIdAndClientId(cardId, userId);
		verify(cardMapper).mapEntityToResponse(existingCard);
		verify(utilService).maskCardNumber(cardResponse);
	}
	
	@Test
	@DisplayName("Перевод денег между картами - успешный сценарий")
	void transferMoney_ShouldTransferMoney_WhenBothCardsBelongToUser() {
		UUID userId = TestUtils.testUser().getId();
		UUID cardIdFrom = UUID.fromString("f70907df-196d-483f-8faa-b04e9d988b0c");
		UUID cardIdTo = UUID.fromString("f70907df-196d-483f-8faa-b04e9d988b0d");
		BigDecimal amount = new BigDecimal("100.00");
		
		Card cardFrom = Card.builder()
				.id(cardIdFrom)
				.clientId(userId)
				.cardNumber("1234 5678 9012 3456")
				.balance(new BigDecimal("1000.00"))
				.build();
		
		Card cardTo = Card.builder()
				.id(cardIdTo)
				.clientId(userId)
				.cardNumber("9876 5432 1098 7654")
				.balance(new BigDecimal("500.00"))
				.build();
		
		List<Card> cards = List.of(cardFrom, cardTo);
		
		when(utilService.getUserIdFromSecurityContext()).thenReturn(userId);
		when(cardRepository.findAllByIdsAndClientId(List.of(cardIdFrom, cardIdTo), userId))
				.thenReturn(cards);
		when(transferService.transferMoney(cardFrom, cardTo, amount)).thenReturn(true);

		boolean result = clientCardService.transferMoney(amount, cardIdFrom, cardIdTo);

		assertTrue(result);
		
		verify(utilService).getUserIdFromSecurityContext();
		verify(cardRepository).findAllByIdsAndClientId(List.of(cardIdFrom, cardIdTo), userId);
		verify(transferService).transferMoney(cardFrom, cardTo, amount);
	}
	
	@Test
	@DisplayName("Перевод денег - выбрасывает исключение если не найдена одна или обе указанные карты")
	void transferMoney_ShouldThrowCardNotFoundException_WhenLessThanTwoCardsFound() {
		UUID userId = TestUtils.testUser().getId();
		UUID cardIdFrom = UUID.fromString("f70907df-196d-483f-8faa-b04e9d988b0c");
		UUID cardIdTo = UUID.fromString("f70907df-196d-483f-8faa-b04e9d988b0d");
		BigDecimal amount = new BigDecimal("100.00");
		
		Card cardFrom = Card.builder()
				.id(cardIdFrom)
				.clientId(userId)
				.balance(new BigDecimal("1000.00"))
				.build();

		List<Card> cards = List.of(cardFrom);
		
		when(utilService.getUserIdFromSecurityContext()).thenReturn(userId);
		when(cardRepository.findAllByIdsAndClientId(List.of(cardIdFrom, cardIdTo), userId))
				.thenReturn(cards);

		CardNotFoundException exception = assertThrows(CardNotFoundException.class,
				() -> clientCardService.transferMoney(amount, cardIdFrom, cardIdTo));
		
		assertEquals("Не найдна одна или несколько указанных карт у пользователя с id: " + userId,
				exception.getMessage());
		
		verify(utilService).getUserIdFromSecurityContext();
		verify(cardRepository).findAllByIdsAndClientId(List.of(cardIdFrom, cardIdTo), userId);
		verify(transferService, never()).transferMoney(any(), any(), any());
	}
	
	@Test
	@DisplayName("Перевод денег - выбрасывает исключение если найдено более 2 карт")
	void transferMoney_ShouldThrowCardNotFoundException_WhenMoreThanTwoCardsFound() {
		UUID userId = TestUtils.testUser().getId();
		UUID cardIdFrom = UUID.fromString("f70907df-196d-483f-8faa-b04e9d988b0c");
		UUID cardIdTo = UUID.fromString("f70907df-196d-483f-8faa-b04e9d988b0d");
		BigDecimal amount = new BigDecimal("100.00");
		
		Card card1 = Card.builder().id(cardIdFrom).clientId(userId).build();
		Card card2 = Card.builder().id(cardIdTo).clientId(userId).build();
		Card card3 = Card.builder().id(UUID.randomUUID()).clientId(userId).build();

		List<Card> cards = List.of(card1, card2, card3);
		
		when(utilService.getUserIdFromSecurityContext()).thenReturn(userId);
		when(cardRepository.findAllByIdsAndClientId(List.of(cardIdFrom, cardIdTo), userId))
				.thenReturn(cards);

		CardNotFoundException exception = assertThrows(CardNotFoundException.class,
				() -> clientCardService.transferMoney(amount, cardIdFrom, cardIdTo));
		
		assertEquals("При поиске двух карт найдено более двух карт у пользователя с id: " + userId,
				exception.getMessage());
		
		verify(utilService).getUserIdFromSecurityContext();
		verify(cardRepository).findAllByIdsAndClientId(List.of(cardIdFrom, cardIdTo), userId);
		verify(transferService, never()).transferMoney(any(), any(), any());
	}
	
	@Test
	@DisplayName("Перевод денег - выбрасывает исключение если карта-отправитель не найдена")
	void transferMoney_ShouldThrowCardNotFoundException_WhenCardFromNotFound() {
		UUID userId = TestUtils.testUser().getId();
		UUID cardIdFrom = UUID.fromString("f70907df-196d-483f-8faa-b04e9d988b0c");
		UUID cardIdTo = UUID.fromString("f70907df-196d-483f-8faa-b04e9d988b0d");
		BigDecimal amount = new BigDecimal("100.00");
		
		Card cardTo = Card.builder()
				.id(cardIdTo)
				.clientId(userId)
				.balance(new BigDecimal("500.00"))
				.build();

		Card wrongCard = Card.builder()
				.id(UUID.randomUUID())
				.clientId(userId)
				.balance(new BigDecimal("1000.00"))
				.build();
		
		List<Card> cards = List.of(wrongCard, cardTo);
		
		when(utilService.getUserIdFromSecurityContext()).thenReturn(userId);
		when(cardRepository.findAllByIdsAndClientId(List.of(cardIdFrom, cardIdTo), userId))
				.thenReturn(cards);

		CardNotFoundException exception = assertThrows(CardNotFoundException.class,
				() -> clientCardService.transferMoney(amount, cardIdFrom, cardIdTo));
		
		assertEquals("Не найдена карта с id: " + cardIdFrom + " у пользователя с id: " + userId,
				exception.getMessage());
		
		verify(utilService).getUserIdFromSecurityContext();
		verify(cardRepository).findAllByIdsAndClientId(List.of(cardIdFrom, cardIdTo), userId);
		verify(transferService, never()).transferMoney(any(), any(), any());
	}
	
	@Test
	@DisplayName("Получение баланса карты - успешный сценарий")
	void getCardBalance_ShouldReturnBalance_WhenCardBelongsToUser() {
		UUID userId = TestUtils.testUser().getId();
		UUID cardId = UUID.fromString("f70907df-196d-483f-8faa-b04e9d988b0c");
		
		CardBalanceProjection balanceProjection = new CardBalanceProjection() {
			@Override
			public BigDecimal getBalance() {
				return new BigDecimal("1234.56");
			}
		};
		
		CardBalanceResponseDto expectedResponse = new CardBalanceResponseDto(
				new BigDecimal("1234.56")
		);
		
		when(utilService.getUserIdFromSecurityContext()).thenReturn(userId);
		when(cardRepository.findBalanceByIdAndClientId(cardId, userId))
				.thenReturn(Optional.of(balanceProjection));
		when(cardMapper.mapBalanceResponse(balanceProjection)).thenReturn(expectedResponse);

		CardBalanceResponseDto result = clientCardService.getCardBalance(cardId);

		assertNotNull(result);
		assertEquals(0, expectedResponse.balance().compareTo(result.balance()));
		
		verify(utilService).getUserIdFromSecurityContext();
		verify(cardRepository).findBalanceByIdAndClientId(cardId, userId);
		verify(cardMapper).mapBalanceResponse(balanceProjection);
	}
	
	@Test
	@DisplayName("Получение баланса карты - выбрасывает исключение если карта не найдена")
	void getCardBalance_ShouldThrowCardNotFoundException_WhenCardNotFound() {
		UUID userId = TestUtils.testUser().getId();
		UUID cardId = UUID.fromString("f70907df-196d-483f-8faa-b04e9d988b0c");
		
		when(utilService.getUserIdFromSecurityContext()).thenReturn(userId);
		when(cardRepository.findBalanceByIdAndClientId(cardId, userId))
				.thenReturn(Optional.empty());

		CardNotFoundException exception = assertThrows(CardNotFoundException.class,
				() -> clientCardService.getCardBalance(cardId));
		
		assertEquals("Не найдена карта с id: " + cardId + " у пользователя с id: " + userId,
				exception.getMessage());
		
		verify(utilService).getUserIdFromSecurityContext();
		verify(cardRepository).findBalanceByIdAndClientId(cardId, userId);
		verify(cardMapper, never()).mapBalanceResponse(any());
	}
	
	@Test
	@DisplayName("Получение всех карт - некорректные параметры пагинации")
	void getAllCardsByPage_ShouldHandleInvalidPaginationParameters() {
		UUID userId = TestUtils.testUser().getId();
		Integer page = null;
		Integer limit = null;
		int defaultLimit = 5;
		int expectedPage = 0;

		Card card = Card.builder()
				.id(UUID.randomUUID())
				.clientId(userId)
				.cardNumber("1234 5678 9012 3456")
				.cardHolder("Вася Пупкин")
				.status(CardStatus.ACTIVE)
				.balance(new BigDecimal("1000.00"))
				.build();

		List<Card> cards = List.of(card);
		Page<Card> cardsPage = new PageImpl<>(cards, PageRequest.of(expectedPage, defaultLimit), 1);

		CardResponseDto cardResponse = new CardResponseDto(
				"1234 5678 9012 3456",
				"Вася Пупкин",
				"01/30",
				CardStatus.ACTIVE,
				new BigDecimal("1000.00")
		);

		CardResponseDto maskedCardResponse = new CardResponseDto(
				"1234 **** **** 3456",
				"Вася Пупкин",
				"01/30",
				CardStatus.ACTIVE,
				new BigDecimal("1000.00")
		);

		when(utilService.getUserIdFromSecurityContext()).thenReturn(userId);
		when(utilService.setPageLimit(limit)).thenReturn(defaultLimit);
		when(cardRepository.findAllByClientId(userId, PageRequest.of(expectedPage, defaultLimit)))
				.thenReturn(cardsPage);
		when(cardRepository.count()).thenReturn(1L);
		when(cardMapper.mapEntityToResponse(card)).thenReturn(cardResponse);
		when(utilService.maskCardNumber(cardResponse)).thenReturn(maskedCardResponse);

		CardPageViewResponseDto result = clientCardService.getAllCardsByPage(page, limit);

		assertNotNull(result);
		assertEquals(1, result.currentPage());
		assertEquals(defaultLimit, result.limit());

		verify(utilService).getUserIdFromSecurityContext();
		verify(utilService).setPageLimit(limit);
	}
	
	@Test
	@DisplayName("Перевод денег - перевод на ту же карту")
	void transferMoney_ShouldWork_WhenTransferToSameCard() {
		UUID userId = TestUtils.testUser().getId();
		UUID cardId = UUID.fromString("f70907df-196d-483f-8faa-b04e9d988b0c");
		BigDecimal amount = new BigDecimal("100.00");
		
		Card card = Card.builder()
				.id(cardId)
				.clientId(userId)
				.cardNumber("1234 5678 9012 3456")
				.balance(new BigDecimal("1000.00"))
				.build();

		List<Card> cards = List.of(card);
		
		when(utilService.getUserIdFromSecurityContext()).thenReturn(userId);
		when(cardRepository.findAllByIdsAndClientId(List.of(cardId, cardId), userId))
				.thenReturn(cards);

		CardNotFoundException exception = assertThrows(CardNotFoundException.class,
				() -> clientCardService.transferMoney(amount, cardId, cardId));
		
		assertEquals("Не найдна одна или несколько указанных карт у пользователя с id: " + userId,
				exception.getMessage());

		verify(utilService).getUserIdFromSecurityContext();
		verify(cardRepository).findAllByIdsAndClientId(List.of(cardId, cardId), userId);
	}
}
