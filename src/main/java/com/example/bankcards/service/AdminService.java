package com.example.bankcards.service;

import com.example.bankcards.dto.request.UserUpdateDto;
import com.example.bankcards.dto.response.UserResponseDto;
import com.example.bankcards.enums.UserRole;
import com.example.bankcards.exception.businessException.UserNotFoundException;

import java.util.UUID;

/**
 * @author 4ndr33w
 * @version 1.0
 */
public interface AdminService {
	
	/**
	 * Удаляет профиль пользователя по его уникальному идентификатору.
	 *
	 * @param id уникальный идентификатор пользователя, профиль которого требуется удалить.
	 * @return {@code true}, если удаление произошло успешно.
	 * @throws UserNotFoundException если пользователь с таким идентификатором не найден.
	 * @throws NullPointerException  если переданный параметр {@code id} равен {@code null}.
	 */
	boolean deleteByClientId(UUID id);
	
	/**
	 * Обновляет профиль пользователя по его уникальному идентификатору.
	 *
	 * @param updateDto DTO с обновлёнными данными пользователя.
	 * @param clientId        уникальный идентификатор пользователя, профиль которого требуется обновить.
	 * @return DTO с обновлёнными данными профиля пользователя.
	 * @throws UserNotFoundException если пользователь с таким идентификатором не найден.
	 * @throws NullPointerException  если хотя бы один из параметров ({@code updateDto} или {@code clientId}) равен {@code null}.
	 */
	UserResponseDto updateByClientId(UserUpdateDto updateDto, UUID clientId);
	
	/**
	 * Блокирует профиль пользователя по его уникальному идентификатору.
	 *
	 * @param clientId уникальный идентификатор пользователя, профиль которого требуется заблокировать.
	 * @return {@code true}, если блокировка прошла успешно.
	 * @throws UserNotFoundException если пользователь с таким идентификатором не найден.
	 * @throws NullPointerException  если переданный параметр {@code clientId} равен {@code null}.
	 */
	boolean blockByClientId(UUID clientId);
	
	/**
	 * Разблокирует профиль пользователя по его уникальному идентификатору.
	 *
	 * @param clientId уникальный идентификатор пользователя, профиль которого требуется разблокировать.
	 * @return {@code true}, если разблокировка прошла успешно.
	 * @throws UserNotFoundException если пользователь с таким идентификатором не найден.
	 * @throws NullPointerException  если переданный параметр {@code clientId} равен {@code null}.
	 */
	boolean unblockByClientId(UUID clientId);
	
	/**
	 * Активирует профиль пользователя по его уникальному идентификатору.
	 *
	 * @param clientId уникальный идентификатор пользователя, профиль которого требуется активировать.
	 * @return {@code true}, если активация прошла успешно.
	 * @throws UserNotFoundException если пользователь с таким идентификатором не найден.
	 * @throws NullPointerException  если переданный параметр {@code clientId} равен {@code null}.
	 */
	boolean activateByClientId(UUID clientId);
	
	/**
	 * Деактивирует профиль пользователя по его уникальному идентификатору.
	 *
	 * @param clientId уникальный идентификатор пользователя, профиль которого требуется деактивировать.
	 * @return {@code true}, если деактивация прошла успешно.
	 * @throws UserNotFoundException если пользователь с таким идентификатором не найден.
	 * @throws NullPointerException  если переданный параметр {@code clientId} равен {@code null}.
	 */
	boolean deactivateByClientId(UUID clientId);
	
	boolean addRoleToUser(UserRole role, UUID id);
	
	boolean removeRoleFromUser(UserRole role, UUID id);
}