package com.example.bankcards.repository;

import com.example.bankcards.entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * @author 4ndr33w
 * @version 1.0
 */
@Repository
public interface CardRepository extends JpaRepository<Card, UUID> {
	
	//List<Card> findAllByIds(List<UUID> ids);
}
