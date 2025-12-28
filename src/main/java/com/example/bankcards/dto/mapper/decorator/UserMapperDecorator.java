package com.example.bankcards.dto.mapper.decorator;

import com.example.bankcards.dto.mapper.UserMapper;
import com.example.bankcards.dto.request.UserRequestDto;
import com.example.bankcards.entity.Role;
import com.example.bankcards.entity.User;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Set;

/**
 * Декоратор для {@link UserMapper}, позволяющей расширить функциональность маппинга пользователей.
 * <p>
 * Сюда помещаются операции, которые нельзя выразить средствами простого MapStruct-mapping.
 * Например, здесь выполняется шифрование паролей и установка начальных ролей для новых пользователей.
 *
 * @author 4ndr33w
 * @version 1.0
 */
@Setter
@Component
public abstract class UserMapperDecorator implements UserMapper {
	
	/**
	 * Основной делегируемый маппер, осуществляющий базовые преобразования.
	 */
	@Autowired
	@Qualifier("delegate")
	private UserMapper delegate;
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	/**
	 * Реализует маппинг регистрационных данных пользователя в сущность.
	 * <p>
	 * По умолчанию устанавливает роль GUEST новому пользователю и
	 * хэширует пароль перед сохранением.
	 *
	 * @param request  Форма регистрации пользователя
	 * @return Подготовленная сущность пользователя
	 */
	@Override
	public User mapRequestToEntity(UserRequestDto request, Role userRole) {
		User user = delegate.mapRequestToEntity(request, userRole);
		
		String rawPassword = user.getPassword();
		user.setPassword(passwordEncoder.encode(rawPassword));
		user.setBirthDate(request.birthDate() == null ? LocalDate.now() : request.birthDate());
		user.setRoles(Set.of(userRole));
		user.setActive(true);
		user.setBlocked(false);
		
		return user;
	}
}