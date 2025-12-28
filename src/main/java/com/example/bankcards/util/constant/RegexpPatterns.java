package com.example.bankcards.util.constant;

/**
 * @author 4ndr33w
 * @version 1.0
 */
public final class RegexpPatterns {
	
	private RegexpPatterns() {}
	
	public static final String REGEXP_NAME_PATTERN = "^[A-Za-zА-Яа-яЁё\\-\\s]+$";
	public static final String REGEXP_PASSWORD_PATTERN = "^(?=.*[A-Z])(?=.*\\d)(?=.*[#@#$%^&*()_+\\-=\\[\\]{}|;:,.<>?>]).{8,}$";
	
	public static final String REGEXP_NAME_MESSAGE = "Неверный формат имени. Имя должно содержать только буквы";
	public static final String REGEXP_EMAIL_MESSAGE = "Неверный формат e-mail";
	public static final String REGEXP_PASSWORD_MESSAGE = "Пароль должен быть не короче 8 символов и содержать хотя бы 1 заглавную букву, минимум 1 цифру и минимум 1 символ";
}