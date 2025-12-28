package com.example.bankcards.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import java.util.UUID;

/**
 * Класс-сущность для ролей пользователей
 *
 * @author 4ndr33w
 * @version 1.0
 */
@Getter
@Setter
@Entity
@Table(name = "roles")
@NoArgsConstructor
public class Role implements GrantedAuthority {
	
	/**
	 * Уникальный идентификатор роль пользователя
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;
	
	/**
	 * Наименование роли пользователя
	 */
	@Column(nullable = false, unique = true)
	private String role;
	
	@Override
	public String getAuthority() {
		return role;
	}
}