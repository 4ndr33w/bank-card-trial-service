package com.example.bankcards.repository;

import com.example.bankcards.entity.User;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * @author 4ndr33w
 * @version 1.0
 */
@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
	
	Optional<User> findByEmail(@NonNull String email);
	
	Optional<User> findByUserName(@NonNull String userName);
	
	boolean existsByEmail(@NonNull String email);
	
	@Modifying
	@Query("UPDATE User u SET u.blocked = true, u.updatedAt = CURRENT_TIMESTAMP WHERE u.id = :userId AND u.blocked = false")
	int blockUserById(UUID userId);
	
	@Modifying
	@Query("UPDATE User u SET u.active = false, u.updatedAt = CURRENT_TIMESTAMP WHERE u.id = :userId AND u.active = true")
	int deactivateUserById(UUID userId);
	
	@Modifying
	@Query("UPDATE User u SET u.blocked = false, u.updatedAt = CURRENT_TIMESTAMP WHERE u.id = :userId AND u.blocked = true")
	int unblockUserById(UUID userId);
	
	@Modifying
	@Query("UPDATE User u SET u.active = true, u.updatedAt = CURRENT_TIMESTAMP WHERE u.id = :userId AND u.active = false")
	int activateUserById(UUID userId);
}
