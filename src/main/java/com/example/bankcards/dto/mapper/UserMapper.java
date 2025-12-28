package com.example.bankcards.dto.mapper;

import com.example.bankcards.dto.mapper.decorator.UserMapperDecorator;
import com.example.bankcards.dto.request.UserRequestDto;
import com.example.bankcards.dto.request.UserUpdateDto;
import com.example.bankcards.dto.response.UserResponseDto;
import com.example.bankcards.entity.Role;
import com.example.bankcards.entity.User;
import org.mapstruct.BeanMapping;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

/**
 * @author 4ndr33w
 * @version 1.0
 */
@Mapper(
		componentModel = "spring",
		uses = UserMapperDecorator.class,
		unmappedTargetPolicy = ReportingPolicy.IGNORE,
		unmappedSourcePolicy = ReportingPolicy.IGNORE
)
@DecoratedWith(UserMapperDecorator.class)
public interface UserMapper {
	
	
	/**
	 * Преобразует данные формы регистрации пользователя в объект сущности User.
	 * <p>
	 * Дополнительно поддерживает обработку параметра, определяющего наличие изображения пользователя.
	 *
	 * @param request  DTO с данными формы регистрации пользователя
	 * @return Новая сущность пользователя
	 */
	@Mapping(target = "roles", ignore = true)
	@Mapping(target = "id", ignore = true)
	User mapRequestToEntity(UserRequestDto request, Role userRole);
	
	/**
	 * Частично обновляет данные существующего пользователя на основании полученных значений из UpdateDTO.
	 * <p>
	 * Поле считается обновляемым, только если оно не null. Это полезно для частичного обновления профилей пользователей.
	 *
	 * @param updateDto Данные обновления пользователя
	 * @param user      Текущая сущность пользователя
	 * @return Обновленная сущность пользователя
	 */
	@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
	User mapUpdateToEntity(UserUpdateDto updateDto, @MappingTarget User user);
	
	/**
	 * Преобразует сущность пользователя в представление DTO для вывода пользователю.
	 *
	 * @param entity Пользовательская сущность
	 * @return DTO с данными пользователя
	 */
	UserResponseDto mapToDto(User entity);
}