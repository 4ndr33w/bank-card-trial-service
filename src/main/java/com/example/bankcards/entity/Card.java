package com.example.bankcards.entity;

import com.example.bankcards.enums.CardStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Version;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * @author 4ndr33w
 * @version 1.0
 */
@Getter
@Setter
@Entity
@Table(name = "cards")
@NoArgsConstructor
public class Card {
	
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;
	@Version
	private Integer version = 0;
	private UUID clientId;
	private String cardNumber;
	private String cardHolder;
	private String expirationDate;
	private String cvv;
	@Enumerated(EnumType.STRING)
	private CardStatus status;
	private BigDecimal balance;
}
