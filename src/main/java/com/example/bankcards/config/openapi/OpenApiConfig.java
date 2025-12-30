package com.example.bankcards.config.openapi;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author 4ndr33w
 * @version 1.0
 */
@Configuration
public class OpenApiConfig {
	
	@Bean
	public OpenAPI openApi() {
		SecurityScheme jwtSchema = new SecurityScheme()
				.type(SecurityScheme.Type.HTTP)
				.scheme("bearer")
				.bearerFormat("JWT")
				.name("Authorization")
				.in(SecurityScheme.In.HEADER)
				.description("Bearer Token");
		
		SecurityScheme basicSchema = new SecurityScheme()
				.type(SecurityScheme.Type.HTTP)
				.scheme("basic")
				.name("Authorization")
				.in(SecurityScheme.In.HEADER)
				.description("Basic Authentication. Формат: Basic {base64(логин:пароль)}");
		
		SecurityRequirement securityRequirement = new SecurityRequirement().addList("bearer");
		SecurityRequirement basicRequirement = new SecurityRequirement().addList("basic");
		
		return new OpenAPI()
				.info(new Info()
						.title("Сервис упраленния пользователями и пользовательскими продуктами (картами)")
						.contact(new Contact()
								.name("Andr33w")
								.email("andr33w@example.com"))
						.version("1.0")
						.description("Сервис упраленния пользователями и пользовательскими продуктами (картами)"))
				.components(new Components()
						.addSecuritySchemes("bearer", jwtSchema)
						.addSecuritySchemes("basic", basicSchema))
				.addSecurityItem(securityRequirement)
				.addSecurityItem(basicRequirement);
	}
}