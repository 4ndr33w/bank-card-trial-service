package com.example.bankcards.dto.mapper.decorator;

import com.example.bankcards.dto.mapper.CardMapper;
import com.example.bankcards.dto.request.CardRequestDto;
import com.example.bankcards.entity.Card;
import com.example.bankcards.enums.CardStatus;
import com.example.bankcards.service.impl.UtilService;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * @author 4ndr33w
 * @version 1.0
 */
@Setter
@Component
public abstract class CardMapperDecorator implements CardMapper {
	
	/**
	 * Основной делегируемый маппер, осуществляющий базовые преобразования.
	 */
	@Autowired
	@Qualifier("delegate")
	private CardMapper delegate;
	@Autowired
	private UtilService utilService;
	
	@Override
	public Card mapRequestToEntity(CardRequestDto request) {
		Card card = delegate.mapRequestToEntity(request);
		card.setClientId(request.clientId());
		card.setBalance(BigDecimal.ZERO);
		card.setStatus(CardStatus.ACTIVE);
		card.setExpirationDate(LocalDate.now().plusYears(3));
		card.setCardNumber(utilService.generateCardNumber());
		card.setCvv(utilService.generateCvv());
		
		String cardHolder = utilService.getUserFromSecurityContext().getName() + " " + utilService.getUserFromSecurityContext().getLastName();
		card.setCardHolder(cardHolder);
		
		return card;
	}
}