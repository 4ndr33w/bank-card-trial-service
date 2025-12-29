package com.example.bankcards.util.constant;

/**
 * @author 4ndr33w
 * @version 1.0
 */
public class Constants {
	
	public static final String UNAUTHORIZED_MESSAGE = "Требуется авторизация";
	
	public static final String FORBIDDEN_MESSAGE = "Недостаточно прав доступа";
	
	public static final String BAD_REQUEST_MESSAGE = "Некорректный запрос";
	
	public static final String USER_NOT_FOUND_BY_ID_MESSAGE = "Не найден пользователь с id: ";
	
	public static final String USER_NOT_FOUND_BY_EMAIL_MESSAGE = "Не найден пользователь с email: ";
	
	public static final String USER_NOT_FOUND_BY_USERNAME_MESSAGE = "Не найден пользователь с userName: ";
	
	public static final String ACCESS_TOKEN_EXPIRED_MESSAGE = "Истёк срок действия токена";
	
	public static final String REFRESH_TOKEN_EXPIRED_MESSAGE = "Истёк срок действия токена обновления";
	
	public static final String INVALID_ACCESS_TOKEN_MESSAGE = "Токен не прошел валидацию";
	public static final String INVALID_REFRESH_TOKEN_MESSAGE = "Токен обновления не прошел валидацию";
	
	public static final String FAILED_TO_GET_USERNAME_FROM_TOKEN_MESSAGE = "Не удалось извлечь логин из токена";
	
	public static final String FAILED_TO_GET_USER_ID_FROM_TOKEN_MESSAGE = "Не удалось извлечь Id пользователя из токена";
	
	public static final String FAILED_TO_READ_SECURITY_KEYS_MESSAGE = "Ошибка чтения ключей шифрования";
	
	public static final String TOKEN_DATA_MISMATCH_MESSAGE = "Несовпадение пользовательских данных в токене";
}
