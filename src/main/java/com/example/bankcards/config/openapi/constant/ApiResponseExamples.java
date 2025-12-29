package com.example.bankcards.config.openapi.constant;

/**
 * @author 4ndr33w
 * @version 1.0
 */
public class ApiResponseExamples {
	
	private ApiResponseExamples() {}
	
	public static final String UNAUTHORIZED_EXAMPLE = """
            {
                "status": 401,
                "message":"Требуется авторизация",
                "timestamp": "29.12.2025 23:45:59"
            }
            """;
	
	public static final String FORBIDDEN_EXAMPLE = """
            {
                "status": 403,
                "message":"У вас нет прав на выполнение этой операции",
                "timestamp": "29.12.2025 23:45:59"
            }
            """;
	
	public static final String BAD_REQUEST_EXAMPLE = """
            {
                "status": 400,
                "message":"Некорректный запрос",
                "timestamp": "29.12.2025 23:45:59"
            }
            """;
	
	public static final String USER_NOT_FOUND_BY_ID_EXAMPLE = """
            {
                "status": 404,
                "message":"Не найден пользователь с id: 019aa9af-71c4-75f1-a4a5-76ba592988ba",
                "timestamp": "29.12.2025 23:45:59"
            }
            """;
	
	public static final String CARD_NOT_FOUND_BY_ID_EXAMPLE = """
            {
                "status": 404,
                "message":"Не найдена карта с id: 019aa9af-71c4-75f1-a4a5-76ba592988ba у пользователя с id: 019aa9af-71c4-75f1-a4a5-76ba592988ba",
                "timestamp": "29.12.2025 23:45:59"
            }
            """;
	
	public static final String ROLE_NOT_FOUND_BY_ID_EXAMPLE = """
            {
                "status": 404,
                "message":"Роль SUPER_PUPER_MEGA_ADMINISTRATORR не найдена",
                "timestamp": "29.12..2025 23:45:59"
            }
            """;
	
	public static final String USER_DOES_NOT_HAVE_ROLE_EXAMPLE = """
            {
                "status": 404,
                "message":"У пользователя с id: 019aa9af-71c4-75f1-a4a5-76ba592988ba отсутствует роль SUPER_PUPER_DUPER_MEGA_ADMIN",
                "timestamp": "29.12..2025 23:45:59"
            }
            """;
	
	public static final String USER_ALREADY_HAVE_ROLE_EXAMPLE = """
            {
                "status": 404,
                "message":"Пользователь с id: 019aa9af-71c4-75f1-a4a5-76ba592988ba уже имеет роль USER",
                "timestamp": "29.12..2025 23:45:59"
            }
            """;
	
	public static final String USER_NOT_FOUND_BY_EMAIL_EXAMPLE = """
            {
                "status": 404,
                "message":"Не найден пользователь с email: 123@123.ru",
                "timestamp": "29.12.2025 23:45:59"
            }
            """;
	
	public static final String USER_NOT_FOUND_BY_USERNAME_EXAMPLE = """
            {
                "status": 404,
                "message":"Не найден пользователь с userName: vasyan",
                "timestamp": "29.12.2025 23:45:59"
            }
            """;
	
	public static final String USER_ALREADY_BLOCKED_EXAMPLE = """
            {
                "status": 200,
                "message":"",
                "timestamp": "29.12.2025 23:45:59"
            }
            """;
	
	public static final String PASSWORD_VALIDATION_EXCEPTION_EXAMPLE = """
            {
                "password": "Пароль должен быть не короче 8 символов и содержать хотя бы 1 заглавную букву, минимум 1 цифру и минимум 1 символ"
            }
            """;
	
	public static final String NAME_VALIDATION_EXCEPTION_EXAMPLE = """
            {
                "name": "размер должен находиться в диапазоне от 1 до 30"
            }
            """;
	
	public static final String USERNAME_VALIDATION_EXCEPTION_EXAMPLE = """
            {
                "userName": "размер должен находиться в диапазоне от 1 до 30"
            }
            """;
	
	public static final String BIRTH_DATE_EXCEPTION_EXAMPLE = """
            {
                "birthDate": "Дата должна быть в прошлом"
            }
            """;
	
	public static final String EMAIL_VALIDATION_EXCEPTION_EXAMPLE = """
            {
                "email": "Неверный формат e-mail"
            }
            """;
}