package com.example.bankcards.service;

import com.example.bankcards.dto.request.UserRequestDto;
import com.example.bankcards.dto.request.UserUpdateDto;
import com.example.bankcards.dto.response.UserPageViewResponseDto;
import com.example.bankcards.dto.response.UserResponseDto;
import com.example.bankcards.exception.authorizationException.SecurityContextHolderException;
import com.example.bankcards.exception.businessException.UserNotFoundException;

import java.util.UUID;

/**
 * @author 4ndr33w
 * @version 1.0
 */
public interface UserService {
	
	/**
	 * Создать профиль нового пользователя.
	 *
	 * @param request DTO с данными для создания профиля пользователя.
	 * @return Профиль созданного пользователя.
	 * @throws NullPointerException Если переданный параметр {@code UserRequestDto} не инициализирован.
	 * @throws UserNotFoundException Если пользователь с таким email уже существует.
	 */
	UserResponseDto create(UserRequestDto request);
	
	/**
	 * Обновляет данные собственного профиля пользователя.
	 * <p>
	 * Извлекает ID пользователя из контекста безопасности (SecurityContextHolder),
	 * находит соответствующего пользователя и обновляет его профиль.
	 *
	 * @param update DTO с обновленными данными пользователя.
	 * @return Обновленный профиль пользователя.
	 * @throws UserNotFoundException              Если пользователь не найден.
	 * @throws NullPointerException               Если переданное DTO не инициализировано.
	 * @throws SecurityContextHolderException    Если SecurityContextHolder не содержит данных или не инициализирован.
	 */
	UserResponseDto update (UserUpdateDto update);
	
	/**
	 * Находит профиль пользователя по его уникальному идентификатору.
	 *
	 * @param id уникальный идентификатор пользователя, профиль которого требуется найти.
	 * @return DTO с данными найденного профиля пользователя.
	 * @throws UserNotFoundException если пользователь с таким идентификатором не найден.
	 * @throws NullPointerException  если переданный параметр {@code id} равен {@code null}.
	 */
	UserResponseDto findById(UUID id);
	
	/**
	 * Найти данные собственного профиля пользователя.
	 * <p>
	 * Извлекает ID пользователя из SecurityContextHolder и ищет соответствующий профиль.
	 *
	 * @return Найденный профиль пользователя.
	 * @throws UserNotFoundException              Если пользователь не найден.
	 * @throws SecurityContextHolderException    Если SecurityContextHolder не содержит данных или не инициализирован.
	 */
	UserResponseDto find();
	
	/**
	 * Удалить собственный профиль пользователя.
	 * <p>
	 * Извлекает ID пользователя из SecurityContextHolder и удаляет соответствующий профиль.
	 *
	 * @throws UserNotFoundException              Если пользователь не найден.
	 * @throws SecurityContextHolderException    Если SecurityContextHolder не содержит данных или не инициализирован.
	 */
	void delete();
	
	/**
	 * Получить профиль пользователя по его email.
	 *
	 * @param email Email пользователя, чей профиль требуется получить.
	 * @return DTO с данными профиля запрашиваемого пользователя.
	 * @throws UserNotFoundException Если пользователь не найден.
	 * @throws NullPointerException  Если переданный email не инициализирован.
	 */
	UserResponseDto findByEmail(String email);
	
	/**
	 * Получить представление страницы списка пользователей.
	 *
	 * @param page Номер страницы.
	 * @param limit Лимит отображаемых на одной странице пользователей.
	 * @return DTO, включающее:
	 * <ul>
	 *     <li>номер запрашиваемой страницы;</li>
	 *     <li>лимит пользователей на странице;</li>
	 *     <li>общее количество страниц (согласно указанному лимиту);</li>
	 *     <li>общее количество пользователей;</li>
	 *     <li>список пользователей на указанной странице.</li>
	 * </ul>
	 */
	UserPageViewResponseDto findAllByPage(Integer page, Integer limit);
	
	/**
	 * Получить профиль пользователя по его username.
	 *
	 * @param userName Username пользователя, чей профиль требуется получить.
	 * @return DTO с данными профиля запрашиваемого пользователя.
	 * @throws UserNotFoundException Если пользователь не найден.
	 * @throws NullPointerException  Если переданный username не инициализирован.
	 */
	UserResponseDto findByUserName(String userName);
	
	/**
	 * Заблокировать собственный профиль пользователя.
	 *
	 * @return {@code true}, если блокировка выполнена успешно.
	 * @throws UserNotFoundException              Если пользователь не найден.
	 * @throws SecurityContextHolderException    Если SecurityContextHolder не содержит данных или не инициализирован.
	 */
	boolean block();
	
	/**
	 * Деактивировать собственный профиль пользователя.
	 *
	 * @return {@code true}, если деактивация выполнена успешно.
	 * @throws UserNotFoundException              Если пользователь не найден.
	 * @throws SecurityContextHolderException    Если SecurityContextHolder не содержит данных или не инициализирован.
	 */
	boolean deactivate();
}