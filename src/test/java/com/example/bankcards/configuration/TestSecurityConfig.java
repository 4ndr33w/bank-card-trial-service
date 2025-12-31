package com.example.bankcards.configuration;

import com.example.bankcards.enums.UserRole;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

/**
*
* @version 1.0
* @author 4ndr33w
*/
@Profile("test")
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(proxyTargetClass = true)
public class TestSecurityConfig {

	@Bean
	@Primary
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
				.csrf(AbstractHttpConfigurer::disable)
				.authorizeHttpRequests(authz -> authz
						.requestMatchers("/api/v1/admin/**").hasAuthority(UserRole.ADMIN.getAuthority())
						.requestMatchers("/api/v1/cards/**").hasAuthority(UserRole.ADMIN.getAuthority())
						.anyRequest().permitAll()

				);
		return http.build();
	}
	
	@Bean
	public HandlerMappingIntrospector handlerMappingIntrospector() {
		return Mockito.mock(HandlerMappingIntrospector.class);
	}
}