package com.example.bankcards.service;

import com.example.bankcards.dto.mapper.CardMapper;
import com.example.bankcards.dto.request.CardRequestDto;
import com.example.bankcards.dto.response.CardPageViewResponseDto;
import com.example.bankcards.dto.response.CardResponseDto;
import com.example.bankcards.dto.response.UserResponseDto;
import com.example.bankcards.entity.Card;
import com.example.bankcards.enums.CardStatus;
import com.example.bankcards.exception.businessException.CardActivationException;
import com.example.bankcards.exception.businessException.CardNotFoundException;
import com.example.bankcards.exception.businessException.UserNotFoundException;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.service.impl.AdminCardServiceImpl;
import com.example.bankcards.service.impl.UtilService;
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
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
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
public class AdminCardServiceTests {
	
	@Mock
	private UserService userService;
	@Mock
	private CardRepository cardRepository;
	@Mock
	private UtilService utilService;
	@Mock
	private CardMapper cardMapper;
	@InjectMocks
	private AdminCardServiceImpl adminCardService;
	
	@Test
	@DisplayName("Создание карты - успешный сценарий")
	void createCard_ShouldCreateCard_WhenUserExists() {
		CardRequestDto cardRequestDto = TestUtils.testCardRequestDto();
		UserResponseDto userResponseDto = TestUtils.testUserResponseDto();
		Card newCard = TestUtils.testNewUserCard;
		Card savedCard = TestUtils.testNewSavedCard;
		CardResponseDto expectedResponse = TestUtils.testCardResponseDto;
		UUID userId = userResponseDto.id();
		UUID cardId = savedCard.getId();
		
		when(userService.findById(userId)).thenReturn(userResponseDto);
		when(cardMapper.mapRequestToEntity(cardRequestDto, userResponseDto)).thenReturn(newCard);
		when(cardRepository.save(newCard)).thenReturn(savedCard);
		when(cardMapper.mapEntityToResponse(savedCard)).thenReturn(expectedResponse);

		CardResponseDto result = adminCardService.createCard(cardRequestDto);

		assertNotNull(result);
		assertEquals(cardId, result.id());
		
		verify(userService).findById(userId);
		verify(cardMapper).mapRequestToEntity(cardRequestDto, userResponseDto);
		verify(cardRepository).save(newCard);
		verify(cardMapper).mapEntityToResponse(savedCard);
	}
	
	@Test
	@DisplayName("Создание карты - выбрасывает исключение когда пользователь не найден")
	void createCard_ShouldThrowException_WhenUserNotFound() {
		CardRequestDto cardRequestDto = TestUtils.testCardRequestDto();
		UserResponseDto userResponseDto = TestUtils.testUserResponseDto();
		UUID userId = userResponseDto.id();
		
		when(userService.findById(userId)).thenThrow(new UserNotFoundException("Пользователь не найден"));

		assertThrows(UserNotFoundException.class, () -> adminCardService.createCard(cardRequestDto));
		
		verify(userService).findById(userId);
		verify(cardMapper, never()).mapRequestToEntity(any(), any());
		verify(cardRepository, never()).save(any());
	}
	
	@Test
	@DisplayName("Блокировка карты - успешный сценарий")
	void blockCard_ShouldBlockCard_WhenCardExistsAndNotBlocked() {
		Card card = TestUtils.testNewSavedCard;
		UUID cardId = card.getId();
		
		when(cardRepository.findById(cardId)).thenReturn(Optional.of(card));

		boolean result = adminCardService.blockCard(cardId);

		assertTrue(result);
		assertEquals(CardStatus.BLOCKED, card.getStatus());
		
		verify(cardRepository).findById(cardId);
		verify(cardRepository, never()).save(card);
	}
	
	@Test
	@DisplayName("Блокировка карты - выбрасывает исключение когда карта не найдена")
	void blockCard_ShouldThrowCardNotFoundException_WhenCardNotFound() {
		UUID cardId = TestUtils.testNewSavedCard.getId();
		
		when(cardRepository.findById(cardId)).thenReturn(Optional.empty());
		assertThrows(CardNotFoundException.class, () -> adminCardService.blockCard(cardId));
		verify(cardRepository).findById(cardId);
	}
	
	@Test
	@DisplayName("Блокировка карты - выбрасывает исключение когда карта уже заблокирована")
	void blockCard_ShouldThrowCardActivationException_WhenCardAlreadyBlocked() {
		UUID cardId = TestUtils.testNewSavedCard.getId();
		Card card = TestUtils.testBlockedCard;
		
		when(cardRepository.findById(cardId)).thenReturn(Optional.of(card));
		assertThrows(CardActivationException.class, () -> adminCardService.blockCard(cardId));
		verify(cardRepository).findById(cardId);
	}
	
	@Test
	@DisplayName("Активация карты - успешный сценарий")
	void activateCard_ShouldActivateCard_WhenCardExistsAndNotActive() {
		Card card = TestUtils.testNewSavedCard;
		UUID cardId = TestUtils.testNewSavedCard.getId();
		
		when(cardRepository.findById(cardId)).thenReturn(Optional.of(card));

		boolean result = adminCardService.activateCard(cardId);

		assertTrue(result);
		assertEquals(CardStatus.ACTIVE, card.getStatus());
		
		verify(cardRepository).findById(cardId);
	}
	
	@Test
	@DisplayName("Активация карты - выбрасывает исключение когда карта уже активна")
	void activateCard_ShouldThrowCardActivationException_WhenCardAlreadyActive() {
		Card card = TestUtils.testNewSavedCard;
		UUID cardId = TestUtils.testNewSavedCard.getId();
		
		when(cardRepository.findById(cardId)).thenReturn(Optional.of(card));
		assertThrows(CardActivationException.class, () -> adminCardService.activateCard(cardId));
		verify(cardRepository).findById(cardId);
	}
	
	@Test
	@DisplayName("Удаление карты - успешный сценарий")
	void deleteCard_ShouldDeleteCard_WhenCardExists() {
		Card card = TestUtils.testNewSavedCard;
		UUID cardId = TestUtils.testNewSavedCard.getId();
		
		when(cardRepository.findById(cardId)).thenReturn(Optional.of(card));

		adminCardService.deleteCard(cardId);

		verify(cardRepository).findById(cardId);
		verify(cardRepository).delete(card);
	}
	
	@Test
	@DisplayName("Удаление карты - выбрасывает исключение когда карта не найдена")
	void deleteCard_ShouldThrowCardNotFoundException_WhenCardNotFound() {
		UUID cardId = UUID.randomUUID();
		
		when(cardRepository.findById(cardId)).thenReturn(Optional.empty());

		assertThrows(CardNotFoundException.class, () -> adminCardService.deleteCard(cardId));
		
		verify(cardRepository).findById(cardId);
		verify(cardRepository, never()).delete(any());
	}
	
	@Test
	@DisplayName("Получение всех карт с пагинацией - успешный сценарий")
	void getAllCardsByPage_ShouldReturnPaginatedCards() {
		Integer page = 1;
		Integer limit = 10;
		int pageLimit = 10;
		int paginationPage = 0;
		
		UUID userId = UUID.randomUUID();
		UUID cardId1 = UUID.randomUUID();
		UUID cardId2 = UUID.randomUUID();
		
		Card card1 = Card.builder()
				.id(cardId1)
				.clientId(userId)
				.status(CardStatus.ACTIVE)
				.build();
		
		Card card2 = Card.builder()
				.id(cardId2)
				.clientId(userId)
				.status(CardStatus.ACTIVE)
				.build();
		
		List<Card> cards = List.of(card1, card2);
		Page<Card> cardsPage = new PageImpl<>(cards, PageRequest.of(paginationPage, pageLimit), 2);
		
		CardResponseDto cardResponse1 = new CardResponseDto(
				cardId1, userId, "1234 5678 9012 3456", "Вася Пупкин",
				String.valueOf(LocalDate.of(2030, 1, 1)), CardStatus.ACTIVE, BigDecimal.ZERO
		);
		
		CardResponseDto cardResponse2 = new CardResponseDto(
				cardId2, userId, "1234 5678 9012 3457", "Вася Пупкин",
				String.valueOf(LocalDate.of(2030, 1, 1)), CardStatus.ACTIVE, BigDecimal.ZERO
		);
		
		List<CardResponseDto> cardResponses = List.of(cardResponse1, cardResponse2);
		
		CardPageViewResponseDto expectedResponse = new CardPageViewResponseDto(
				1, pageLimit, 1, 2, cardResponses
		);
		
		when(utilService.setPageLimit(limit)).thenReturn(pageLimit);
		when(cardRepository.findAll(any(Pageable.class))).thenReturn(cardsPage);
		when(cardRepository.count()).thenReturn(2L);
		when(cardMapper.mapEntityToResponse(card1)).thenReturn(cardResponse1);
		when(cardMapper.mapEntityToResponse(card2)).thenReturn(cardResponse2);

		CardPageViewResponseDto result = adminCardService.getAllCardsByPage(page, limit);

		assertNotNull(result);
		assertEquals(expectedResponse.currentPage(), result.currentPage());
		assertEquals(expectedResponse.limit(), result.limit());
		assertEquals(expectedResponse.totalPages(), result.totalPages());
		assertEquals(expectedResponse.totalCards(), result.totalCards());
		assertEquals(expectedResponse.cards().size(), result.cards().size());
		
		verify(utilService).setPageLimit(limit);
		verify(cardRepository).count();
		verify(cardMapper, times(2)).mapEntityToResponse(any(Card.class));
	}
	
	@Test
	@DisplayName("Получение всех карт с пагинацией - некорректные параметры")
	void getAllCardsByPage_ShouldUseDefaultValues_WhenParametersInvalid() {
		Integer page = null;
		Integer limit = null;
		int pageLimit = 5;
		int paginationPage = 0;
		
		Card card = TestUtils.testNewSavedCard;
		
		List<Card> cards = List.of(card);
		Page<Card> cardsPage = new PageImpl<>(cards, PageRequest.of(paginationPage, pageLimit), 1);

		CardResponseDto cardResponse = new CardResponseDto(
				UUID.randomUUID(), UUID.randomUUID(), "1234 5678 9012 3457", "Вася Пупкин",
				String.valueOf(LocalDate.of(2030, 1, 1)), CardStatus.ACTIVE, BigDecimal.ZERO
		);
		
		when(utilService.setPageLimit(limit)).thenReturn(pageLimit);
		when(cardRepository.findAll(any(Pageable.class))).thenReturn(cardsPage);
		when(cardRepository.count()).thenReturn(1L);
		when(cardMapper.mapEntityToResponse(card)).thenReturn(cardResponse);
	
		CardPageViewResponseDto result = adminCardService.getAllCardsByPage(page, limit);

		assertNotNull(result);
		assertEquals(1, result.currentPage());
		assertEquals(pageLimit, result.limit());
		
		verify(utilService).setPageLimit(limit);

	}
	
	@Test
	@DisplayName("Получение всех карт клиента - успешный сценарий")
	void getAllCardsByClientId_ShouldReturnClientCards() {
		// Arrange
		UUID clientId = UUID.randomUUID();
		Integer page = 1;
		int pageLimit = 20;
		int paginationPage = 0;
		
		UUID cardId1 = UUID.randomUUID();
		UUID cardId2 = UUID.randomUUID();
		
		Card card1 = Card.builder()
				.id(cardId1)
				.clientId(clientId)
				.status(CardStatus.ACTIVE)
				.build();
		
		Card card2 = Card.builder()
				.id(cardId2)
				.clientId(clientId)
				.status(CardStatus.BLOCKED)
				.build();
		
		List<Card> cards = List.of(card1, card2);
		Page<Card> cardsPage = new PageImpl<>(cards, PageRequest.of(paginationPage, pageLimit), 2);
		
		CardResponseDto cardResponse1 = new CardResponseDto(
				cardId1, clientId, "1234 5678 9012 3456", "Вася Пупкин",
				String.valueOf(LocalDate.of(2030, 1, 1)), CardStatus.ACTIVE, BigDecimal.ZERO
		);
		
		CardResponseDto cardResponse2 = new CardResponseDto(
				cardId2, clientId, "1234 5678 9012 3457", "Вася Пупкин",
				String.valueOf(LocalDate.of(2030, 1, 1)), CardStatus.ACTIVE, BigDecimal.ZERO
		);
		
		List<CardResponseDto> cardResponses = List.of(cardResponse1, cardResponse2);
		
		CardPageViewResponseDto expectedResponse = new CardPageViewResponseDto(
				1, pageLimit, 1, 2, cardResponses
		);
		
		when(cardRepository.findAllByClientId(eq(clientId), any(Pageable.class))).thenReturn(cardsPage);
		when(cardRepository.count()).thenReturn(2L);
		when(cardMapper.mapEntityToResponse(card1)).thenReturn(cardResponse1);
		when(cardMapper.mapEntityToResponse(card2)).thenReturn(cardResponse2);

		CardPageViewResponseDto result = adminCardService.getAllCardsByClientId(clientId, page);

		assertNotNull(result);
		assertEquals(expectedResponse.currentPage(), result.currentPage());
		assertEquals(expectedResponse.limit(), result.limit());
		assertEquals(expectedResponse.totalPages(), result.totalPages());
		assertEquals(expectedResponse.totalCards(), result.totalCards());
		assertEquals(expectedResponse.cards().size(), result.cards().size());
		assertEquals(clientId, result.cards().get(0).clientId());
		assertEquals(clientId, result.cards().get(1).clientId());
		
		verify(cardRepository).findAllByClientId(eq(clientId), argThat(pageable ->
				pageable.getPageNumber() == paginationPage && pageable.getPageSize() == pageLimit
		));
		verify(cardRepository).count();
		verify(cardMapper, times(2)).mapEntityToResponse(any(Card.class));
	}
	
	@Test
	@DisplayName("Получение всех карт клиента - пустой список при отсутствии карт")
	void getAllCardsByClientId_ShouldReturnEmpty_WhenClientHasNoCards() {
		UUID clientId = UUID.randomUUID();
		Integer page = 1;
		int pageLimit = 20;
		int paginationPage = 0;
		
		List<Card> cards = List.of();
		Page<Card> cardsPage = new PageImpl<>(cards, PageRequest.of(paginationPage, pageLimit), 0);
		
		when(cardRepository.findAllByClientId(eq(clientId), any(Pageable.class))).thenReturn(cardsPage);
		when(cardRepository.count()).thenReturn(0L);

		CardPageViewResponseDto result = adminCardService.getAllCardsByClientId(clientId, page);

		assertNotNull(result);
		assertEquals(1, result.currentPage());
		assertEquals(pageLimit, result.limit());
		assertEquals(1, result.totalPages());
		assertEquals(0, result.totalCards());
		assertTrue(result.cards().isEmpty());
		
		verify(cardRepository).findAllByClientId(eq(clientId), any(Pageable.class));
		verify(cardRepository).count();
		verify(cardMapper, never()).mapEntityToResponse(any());
	}
	
	@Test
	@DisplayName("Получение всех карт клиента - некорректная страница")
	void getAllCardsByClientId_ShouldHandleInvalidPage() {
		UUID clientId = UUID.randomUUID();
		Integer page = -1;
		int pageLimit = 20;
		int paginationPage = 0;
		
		Card card = TestUtils.testNewSavedCard;
		
		List<Card> cards = List.of(card);
		Page<Card> cardsPage = new PageImpl<>(cards, PageRequest.of(paginationPage, pageLimit), 1);

		CardResponseDto cardResponse = TestUtils.testCardResponseDto;
		
		when(cardRepository.findAllByClientId(eq(clientId), any(Pageable.class))).thenReturn(cardsPage);
		when(cardRepository.count()).thenReturn(1L);
		when(cardMapper.mapEntityToResponse(card)).thenReturn(cardResponse);

		CardPageViewResponseDto result = adminCardService.getAllCardsByClientId(clientId, page);

		assertNotNull(result);
		assertEquals(1, result.currentPage());
		assertEquals(pageLimit, result.limit());
		
		verify(cardRepository).findAllByClientId(eq(clientId), argThat(pageable ->
				pageable.getPageNumber() == paginationPage
		));
	}
	
	@Test
	@DisplayName("Получение всех карт с пагинацией - расчет количества страниц")
	void getAllCardsByPage_ShouldCalculateTotalPagesCorrectly() {
		Integer page = 3;
		Integer limit = 5;
		int pageLimit = 5;
		int paginationPage = 2;
		
		List<Card> cards = List.of(
				Card.builder().id(UUID.randomUUID()).clientId(UUID.randomUUID()).build(),
				Card.builder().id(UUID.randomUUID()).clientId(UUID.randomUUID()).build(),
				Card.builder().id(UUID.randomUUID()).clientId(UUID.randomUUID()).build()
		);
		
		Page<Card> cardsPage = new PageImpl<>(cards, PageRequest.of(paginationPage, pageLimit), 23);
		
		CardResponseDto cardResponse = new CardResponseDto(
				UUID.randomUUID(), UUID.randomUUID(), "1234 5678 9012 3457", "Вася Пупкин",
				String.valueOf(LocalDate.of(2030, 1, 1)), CardStatus.ACTIVE, BigDecimal.ZERO
		);
		
		when(utilService.setPageLimit(limit)).thenReturn(pageLimit);
		when(cardRepository.findAll(any(Pageable.class))).thenReturn(cardsPage);
		when(cardRepository.count()).thenReturn(23L);
		when(cardMapper.mapEntityToResponse(any(Card.class))).thenReturn(cardResponse);

		CardPageViewResponseDto result = adminCardService.getAllCardsByPage(page, limit);

		assertNotNull(result);
		assertEquals(3, result.currentPage());
		assertEquals(pageLimit, result.limit());

		assertEquals(5, result.totalPages());
		assertEquals(23, result.totalCards());
	}
}