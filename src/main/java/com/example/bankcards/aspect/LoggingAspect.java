package com.example.bankcards.aspect;

import org.aspectj.lang.annotation.Pointcut;

import java.util.Set;

/**
 * @author 4ndr33w
 * @version 1.0
 */
public class LoggingAspect {
	
	private static final Set<String> SENSITIVE_FIELDS = Set.of(
			"password", "token", "accessToken", "refreshToken", "bearer", "Bearer", "Basic", "cardNumber"
	);
	
	@Pointcut("within(@org.springframework.stereotype.Service *)")
	public void serviceMethods() {}
	
	@Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
	public void controllerMethods() {}
}
