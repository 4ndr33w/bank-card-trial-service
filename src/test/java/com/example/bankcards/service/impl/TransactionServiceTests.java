package com.example.bankcards.service.impl;

import com.example.bankcards.entity.Card;
import com.example.bankcards.exception.businessException.CardBalanceException;
import com.example.bankcards.exception.businessException.NegativeTransferAmountException;
import com.example.bankcards.repository.CardRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verifyNoInteractions;

/**
 * @author 4ndr33w
 * @version 1.0
 */
@ExtendWith(MockitoExtension.class)
public class TransactionServiceTests {
	
	@Mock
	CardRepository cardRepository;
	@InjectMocks
	TransferService transferService;
	
	@Test
	@DisplayName("Успешный перевод денег при достаточном балансе")
	void transferMoney_ShouldTransferMoney_WhenSufficientBalance() {
		UUID cardFromId = UUID.randomUUID();
		UUID cardToId = UUID.randomUUID();
		
		BigDecimal initialBalanceFrom = new BigDecimal("1000.00");
		BigDecimal initialBalanceTo = new BigDecimal("500.00");
		BigDecimal transferAmount = new BigDecimal("300.00");
		
		Card cardFrom = Card.builder()
				.id(cardFromId)
				.balance(initialBalanceFrom)
				.build();
		
		Card cardTo = Card.builder()
				.id(cardToId)
				.balance(initialBalanceTo)
				.build();

		boolean result = transferService.transferMoney(cardFrom, cardTo, transferAmount);

		assertTrue(result);

		BigDecimal expectedBalanceFrom = new BigDecimal("700.00");
		BigDecimal expectedBalanceTo = new BigDecimal("800.00");
		
		assertEquals(0, expectedBalanceFrom.compareTo(cardFrom.getBalance()));
		assertEquals(0, expectedBalanceTo.compareTo(cardTo.getBalance()));
	}
	
	@Test
	@DisplayName("Перевод денег - CardBalanceException при недостаточном балансе")
	void transferMoney_ShouldThrowException_WhenInsufficientBalance() {
		BigDecimal initialBalanceFrom = new BigDecimal("200.00");
		BigDecimal initialBalanceTo = new BigDecimal("100.00");
		BigDecimal transferAmount = new BigDecimal("300.00");
		
		Card cardFrom = Card.builder()
				.balance(initialBalanceFrom)
				.build();
		
		Card cardTo = Card.builder()
				.balance(initialBalanceTo)
				.build();

		CardBalanceException exception = assertThrows(CardBalanceException.class,
				() -> transferService.transferMoney(cardFrom, cardTo, transferAmount));
		
		assertEquals("Недостаточно средств для выполнения операции перевода", exception.getMessage());

		assertEquals(0, initialBalanceFrom.compareTo(cardFrom.getBalance()));
		assertEquals(0, initialBalanceTo.compareTo(cardTo.getBalance()));
		
		verifyNoInteractions(cardRepository);
	}
	
	@Test
	@DisplayName("Перевод денег - NegativeTransferAmountException при отрицательной сумме перевода")
	void transferMoney_ShouldThrowException_WhenNegativeAmount() {
		BigDecimal initialBalanceFrom = new BigDecimal("1000.00");
		BigDecimal initialBalanceTo = new BigDecimal("500.00");
		BigDecimal transferAmount = new BigDecimal("-100.00"); // Отрицательная сумма
		
		Card cardFrom = Card.builder()
				.balance(initialBalanceFrom)
				.build();
		
		Card cardTo = Card.builder()
				.balance(initialBalanceTo)
				.build();
		
		NegativeTransferAmountException exception = assertThrows(NegativeTransferAmountException.class,
				() -> transferService.transferMoney(cardFrom, cardTo, transferAmount));
		
		assertEquals("Сумма перевода не может быть отрицательной", exception.getMessage());
		
		verifyNoInteractions(cardRepository);
	}
	
	@Test
	@DisplayName("Перевод денег - нулевая сумма перевода")
	void transferMoney_ShouldHandleZeroAmount() {
		BigDecimal initialBalanceFrom = new BigDecimal("1000.00");
		BigDecimal initialBalanceTo = new BigDecimal("500.00");
		BigDecimal transferAmount = BigDecimal.ZERO;
		
		Card cardFrom = Card.builder()
				.balance(initialBalanceFrom)
				.build();
		
		Card cardTo = Card.builder()
				.balance(initialBalanceTo)
				.build();

		boolean result = transferService.transferMoney(cardFrom, cardTo, transferAmount);

		assertTrue(result);

		assertEquals(0, initialBalanceFrom.compareTo(cardFrom.getBalance()));
		assertEquals(0, initialBalanceTo.compareTo(cardTo.getBalance()));
		
		verifyNoInteractions(cardRepository);
	}
	
	@Test
	@DisplayName("Перевод денег - одна и та же карта")
	void transferMoney_ShouldHandleSameCard() {
		BigDecimal initialBalance = new BigDecimal("1000.00");
		BigDecimal transferAmount = new BigDecimal("500.00");
		
		Card card = Card.builder()
				.id(UUID.randomUUID())
				.balance(initialBalance)
				.build();

		boolean result = transferService.transferMoney(card, card, transferAmount);

		assertTrue(result);

		assertEquals(0, initialBalance.compareTo(card.getBalance()));
		
		verifyNoInteractions(cardRepository);
	}
	
	@Test
	@DisplayName("Перевод денег - null карты")
	void transferMoney_ShouldThrowNullPointerException_WhenCardIsNull() {
		Card validCard = Card.builder()
				.balance(new BigDecimal("1000.00"))
				.build();
		
		BigDecimal transferAmount = new BigDecimal("100.00");
		
		// карта-отправитель null
		assertThrows(NullPointerException.class,
				() -> transferService.transferMoney(null, validCard, transferAmount));
		
		// карта-получатель null
		assertThrows(NullPointerException.class,
				() -> transferService.transferMoney(validCard, null, transferAmount));
		
		// обе карты null
		assertThrows(NullPointerException.class,
				() -> transferService.transferMoney(null, null, transferAmount));
		
		verifyNoInteractions(cardRepository);
	}
	
	@Test
	@DisplayName("Перевод денег - null сумма")
	void transferMoney_ShouldThrowNullPointerException_WhenAmountIsNull() {
		Card cardFrom = Card.builder()
				.balance(new BigDecimal("1000.00"))
				.build();
		
		Card cardTo = Card.builder()
				.balance(new BigDecimal("500.00"))
				.build();

		assertThrows(NullPointerException.class,
				() -> transferService.transferMoney(cardFrom, cardTo, null));
		
		verifyNoInteractions(cardRepository);
	}
}