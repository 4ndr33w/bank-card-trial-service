package com.example.bankcards.logging;

import com.example.bankcards.logging.dto.LoggingDataDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LoggingException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collection;
import java.util.Set;

/**
 * @author 4ndr33w
 * @version 1.0
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class LoggingAspect {
	
	private static final Set<String> SENSITIVE_FIELDS = Set.of(
			"password", "token", "accessToken", "refreshToken", "bearer", "Bearer", "Basic", "cardNumber", "cvv"
	);
	
	@Pointcut("within(@org.springframework.stereotype.Service *)")
	public void serviceMethods() {}
	
	@Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
	public void controllerMethods() {}
	
	@Pointcut("within(@org.springframework.web.bind.annotation.RestControllerAdvice *)")
	public void exceptionHandlerMethods() {}
	
	@Pointcut("execution(* com.example.bankcards.security.component.*.*(..))")
	public void securityComponentsMethods() {}
	
	@Pointcut("execution(* com.example.bankcards.security.filter.*.*(..))")
	public void securityFilterMethods() {}
	
	@Around("""
            serviceMethods() ||
            controllerMethods() ||
            securityComponentsMethods() ||
            securityMethods()
            """)
	public Object logMethods(ProceedingJoinPoint joinPoint) throws Throwable {
		LoggingDataDto logData = getLogData(joinPoint);
		logInfoLevel(logData);
		logDebugLevel(logData);
		
		return tryProceedResult(joinPoint, logData);
	}
	
	@Around("exceptionHandlerMethods()")
	public Object logExceptions(ProceedingJoinPoint joinPoint) throws Throwable {
		LoggingDataDto logData = getLogData(joinPoint);
		log.error("ERROR: Исключение: {}.{}", logData.className(), logData.methodName());
		
		return tryProceedExceptionResult(joinPoint, logData);
	}
	
	private void logInfoLevel(LoggingDataDto logData) {
		if (log.isInfoEnabled()) {
			log.info("INFO: Начал выполнение метод: {}.{}", logData.className(), logData.methodName());
		}
	}
	
	private void logDebugLevel(LoggingDataDto logData) {
		if(log.isDebugEnabled()) {
			log.debug("DEBUG: Вызов метода: {}.{} с параметрами: {}",
					logData.className(),
					logData.methodName(),
					formatArguments(logData.args(), logData.methodName()));
		}
	}
	
	private void logDebugLevel(LoggingDataDto logData, Object result, long executionTime) {
		if(log.isDebugEnabled()) {
			log.debug("DEBUG: Метод: {}.{} вернул результат: '{}'; время выполнения: {} мс",
					logData.className(),
					logData.methodName(),
					safeToString(result, logData.methodName()),
					executionTime);
		}
	}
	
	private void logErrorLevel(LoggingDataDto logData, Throwable ex) {
		long executionTime = System.currentTimeMillis() - logData.startTime();
		log.error("ERROR: Ошибка в методе: {}.{} - {}; время выполнения: {} мс",
				logData.className(),
				logData.methodName(),
				ex.getMessage(),
				executionTime);
	}
	
	private LoggingDataDto getLogData(ProceedingJoinPoint joinPoint) {
		if(joinPoint != null) {
			return new LoggingDataDto(
					joinPoint.getTarget().getClass().getSimpleName(),
					joinPoint.getSignature().getName(),
					joinPoint.getArgs(),
					System.currentTimeMillis()
			);
		}
		else {
			log.error("Ошибка при определении ProceedingJoinPoint");
			throw new LoggingException("Ошибка при определении ProceedingJoinPoint");
		}
	}
	
	private Object tryProceedResult(ProceedingJoinPoint joinPoint, LoggingDataDto logData) throws Throwable {
		Object result;
		try {
			long executionTime = System.currentTimeMillis() - logData.startTime();
			result = joinPoint.proceed();
			logDebugLevel(logData, result, executionTime);
			
			return result;
		}
		catch (Throwable ex) {
			logErrorLevel(logData, ex);
			throw ex;
		}
	}
	
	private Object tryProceedExceptionResult(ProceedingJoinPoint joinPoint, LoggingDataDto logData) throws Throwable {
		Object result;
		try {
			result = joinPoint.proceed();
			log.error("ERROR: Сработало исключение: {}", formatArguments(logData.args(), logData.methodName()));
			
			return result;
		}
		catch (Throwable ex) {
			logErrorLevel(logData, ex);
			throw ex;
		}
	}
	
	private Object[] formatArguments(Object[] args, String methodName) {
		if (args == null) return new Object[0];
		return Arrays.stream(args)
				.map(x -> {return safeToString(x, methodName);})
				.toArray();
	}
	
	private String safeToString(Object obj, String methodName) {
		
		String exceptionPackageName = "com.example.bankcards.exception.";
		int exceptionPackageNameLength = exceptionPackageName.length();
		
		if(methodName.toLowerCase().contains("password")) {
			return "***MASKED***";
		}
		if (obj == null) return "null";
		
		String stringValue;
		
		if (obj.toString().contains(exceptionPackageName)) {
			stringValue = obj.toString().substring(exceptionPackageNameLength);
		} // При выбрасывании исключения отбрасываем имя пакета, оставляя имя класса исключения
		
		else stringValue = obj.toString();
		
		if (containsSensitiveData(stringValue)) {
			return "***MASKED***";
		}
		
		if (stringValue.length() > 200) {
			return stringValue.substring(0, 200) + "...[truncated]";
		}
		
		if (obj instanceof Collection<?> collection) {
			return String.format("Collection[size=%d]", collection.size());
		}
		
		return stringValue;
	}
	
	private boolean containsSensitiveData(String value) {
		if (value == null) return false;
		
		String lowerValue = value.toLowerCase();
		return SENSITIVE_FIELDS.stream()
				.anyMatch(lowerValue::contains);
	}
}