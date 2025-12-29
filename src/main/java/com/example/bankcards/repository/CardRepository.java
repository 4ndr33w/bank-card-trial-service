package com.example.bankcards.repository;

import com.example.bankcards.dto.projection.CardBalanceProjection;
import com.example.bankcards.entity.Card;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @author 4ndr33w
 * @version 1.0
 */
@Repository
public interface CardRepository extends JpaRepository<Card, UUID> {
	
	@Query("SELECT c FROM Card c WHERE c.id IN :ids")
	List<Card> findAllByIds(@Param("ids") List<UUID> ids);

	Page<Card> findAllByClientId(UUID clientId, Pageable pageable);
	
	@Query("SELECT c FROM Card c WHERE c.id = :id AND c.clientId = :clientId")
	Optional<Card> findCardByIdAndClientId(@Param("id")UUID id, @Param("clientId") UUID clientId);
	
	List<Card> findAllByClientId(@Param("clientId") UUID clientId);
	
	@Query("SELECT c FROM Card c WHERE c.id IN :ids AND c.clientId = :clientId")
	List<Card> findAllByIdsAndClientId(@Param("ids") List<UUID> ids, @Param("clientId") UUID clientId);
	
	@Query("SELECT c.balance FROM Card c WHERE c.id = :cardId AND c.clientId = :clientId")
	Optional<CardBalanceProjection> findBalanceByIdAndClientId(@Param("cardId") UUID cardId, @Param("clientId") UUID clientId);
	
	boolean existsByCardNumber(String cardNumber);
}