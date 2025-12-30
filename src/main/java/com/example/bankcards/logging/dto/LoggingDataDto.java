package com.example.bankcards.logging.dto;

/**
 * Объект для хранения данных об объекте логирования
 *
 * @author 4ndr33w
 * @version 1.0
 */
public record LoggingDataDto(
		String className,
		String methodName,
		Object[] args,
		long startTime) {
}
