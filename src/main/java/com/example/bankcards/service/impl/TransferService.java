package com.example.bankcards.service.impl;

import com.example.bankcards.entity.Card;
import com.example.bankcards.exception.businessException.CardBalanceException;
import com.example.bankcards.exception.businessException.NegativeTransferAmountException;
import com.example.bankcards.repository.CardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

/**
 * @author 4ndr33w
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
public class TransferService {
	
	public final CardRepository cardRepository;
	
	@Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.SERIALIZABLE)
	public boolean transferMoney(Card cardFrom, Card cardTo, BigDecimal amount) {
		BigDecimal cardFromBalance = cardFrom.getBalance();
		BigDecimal cardToBalance = cardTo.getBalance();
		if(cardFromBalance.compareTo(amount) < 0) {
			throw new CardBalanceException("Недостаточно средств для выполнения операции перевода");
		}
		if(amount.compareTo(BigDecimal.ZERO) < 0) {
			throw new NegativeTransferAmountException("Сумма перевода не может быть отрицательной");
		}
		if(cardFrom.equals(cardTo)) {
			return  true;
		}
		
		cardFrom.setBalance(cardFromBalance.subtract(amount));
		cardTo.setBalance(cardToBalance.add(amount));
		
		return true;
	}
}