package com.example.bankcards.config.security;

import com.example.bankcards.enums.UserRole;
import com.example.bankcards.exception.handler.SecurityExceptionHandler;
import com.example.bankcards.security.filter.JwtFilter;
import com.example.bankcards.security.filter.LoginAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @author 4ndr33w
 * @version 1.0
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {
	
	private final CorsConfig corsConfig;
	
	@Bean
	public SecurityFilterChain filterChain(
			HttpSecurity http,
			SecurityExceptionHandler exceptionHandler,
			JwtFilter jwtFilter,
			LoginAuthenticationFilter loginAuthenticationFilter) throws Exception {
		
		return http
				.cors(cors -> cors.configurationSource(corsConfig.corsConfigurationSource()))
				.csrf(AbstractHttpConfigurer::disable)
				.httpBasic(AbstractHttpConfigurer::disable)
				.formLogin(AbstractHttpConfigurer::disable)
				.sessionManagement(session ->
						session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authorizeHttpRequests(auth -> auth
						.requestMatchers(HttpMethod.GET, "/actuator/**").permitAll()
						.requestMatchers(HttpMethod.GET,"/v3/api-docs/**").permitAll()
						.requestMatchers(HttpMethod.POST, "/api/v1/users").permitAll()
						.requestMatchers(HttpMethod.POST, "/api/v1/auth/refresh").permitAll()
						.requestMatchers(HttpMethod.POST, "/api/v1/login").permitAll()
						.requestMatchers(HttpMethod.GET, "/docs/**").permitAll()
						.requestMatchers("/swagger-ui/**").permitAll()
						.requestMatchers("/api/v1/admin/**").hasAuthority(UserRole.ADMIN.getAuthority())
						.requestMatchers("/api/v1/cards/**").hasAuthority(UserRole.ADMIN.getAuthority())
						.anyRequest().authenticated())
				.addFilterBefore(loginAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
				.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
				.exceptionHandling(ex ->
						ex.authenticationEntryPoint(exceptionHandler::unauthorizedHandler)
								.accessDeniedHandler(exceptionHandler::accessDeniedHandler))
				.build();
	}
	
	@Bean
	public AuthenticationManager authenticationManager(
			AuthenticationConfiguration authenticationConfiguration) throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}
}