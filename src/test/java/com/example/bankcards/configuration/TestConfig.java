package com.example.bankcards.configuration;

import com.example.bankcards.controller.AdminCardController;
import com.example.bankcards.controller.AdminController;
import com.example.bankcards.controller.AuthenticationController;
import com.example.bankcards.controller.ClientCardController;
import com.example.bankcards.controller.UserController;
import com.example.bankcards.controller.impl.AdminCardControllerImpl;
import com.example.bankcards.controller.impl.AdminControllerImpl;
import com.example.bankcards.controller.impl.AuthenticationControllerImpl;
import com.example.bankcards.controller.impl.ClientCardControllerImpl;
import com.example.bankcards.controller.impl.UserControllerImpl;
import com.example.bankcards.properties.JwtProperties;
import com.example.bankcards.security.component.JwtTokenProvider;
import com.example.bankcards.security.component.KeyProvider;
import com.example.bankcards.security.filter.JwtFilter;
import com.example.bankcards.security.filter.LoginAuthenticationFilter;
import com.example.bankcards.service.AdminCardService;
import com.example.bankcards.service.AdminService;
import com.example.bankcards.service.AuthenticationService;
import com.example.bankcards.service.ClientCardService;
import com.example.bankcards.service.UserService;
import com.example.bankcards.service.impl.AdminServiceImpl;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

import java.text.SimpleDateFormat;

/**
*
* @version 1.0
* @author 4ndr33w
*/
@Profile("test")
@Configuration
public class TestConfig {
	
	@Bean
	@Primary
	public ObjectMapper objectMapper() {
		ObjectMapper objectMapper = new ObjectMapper();
			// Регистрируем необходимые модули
			objectMapper.registerModule(new JavaTimeModule()); // Для LocalDate, ZonedDateTime
//			objectMapper.registerModule(new ParameterNamesModule()); // Для record конструкторов
//			objectMapper.registerModule(new Jdk8Module()); // Для Optional и др.

			// Важные настройки
			objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			//objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
			//objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
		
		objectMapper.findAndRegisterModules();
			// Для корректной работы с LocalDate через @JsonFormat
			//objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
		objectMapper.setDateFormat(new SimpleDateFormat("dd.MM.yyyy"));
		
		return objectMapper;
	}
	
	@Bean
	@Primary
	public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter(ObjectMapper objectMapper) {
		return new MappingJackson2HttpMessageConverter(objectMapper);
	}
	
	@Bean
	public UserController userController(UserService userService) {
		return new UserControllerImpl(userService);
	}
	
//	@Bean
//	@Primary
//	public JwtTokenProvider jwtTokenProvider(JwtProperties jwtProperties, KeyProvider keyProvider) {
//		//return new JwtTokenProvider(jwtProperties, keyProvider);
//		return Mockito.mock(JwtTokenProvider.class);
//	}
//
//	@Bean
//	public KeyProvider keyProvider() {
//		return Mockito.mock(KeyProvider.class);
//	}
//
//	@Bean
//	@Primary
//	public JwtProperties jwtProperties() {
//		//return new JwtProperties();
//		return Mockito.mock(JwtProperties.class);
//	}
//
//	@Bean
//	@Primary
//	public JwtFilter jwtFilter(/*JwtTokenProvider jwtTokenProvider, UserDetailsService userDetailsService, SecurityExceptionHandler exceptionHandler*/) {
//		//return new JwtFilter(jwtTokenProvider, userDetailsService, exceptionHandler);
//		return Mockito.mock(JwtFilter.class);
//	}
//
//	@Bean
//	@Primary
//	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//		http
//				.httpBasic(AbstractHttpConfigurer ::disable)
//				.csrf(AbstractHttpConfigurer::disable)
//				.authorizeHttpRequests(authz -> authz.anyRequest().permitAll());
//		return http.build();
//	}
//
//	@Bean
//	public HttpSecurity httpSecurity() {
//		return Mockito.mock(HttpSecurity.class);
//	}
//
//	@Bean
//	@Primary
//	public AuthenticationManager authenticationManager() {
//		return Mockito.mock(AuthenticationManager.class);
//	}
//
//	@Bean
//	@Primary
//	public LoginAuthenticationFilter loginAuthenticationProvider() {
//		return Mockito.mock(LoginAuthenticationFilter.class);
//	}
	
//	@Bean
//	public AdminCardService adminCardService() {
//		return Mockito.mock(AdminCardService.class);
//	}
//
//	@Bean
//	public AdminCardController adminCardController(AdminCardService adminCardService) {
//		return new AdminCardControllerImpl(adminCardService);
//	}
//
//	@Bean
//	public AdminService adminService() {
//		return Mockito.mock(AdminServiceImpl.class);
//	}
//
//	@Bean
//	public AdminController adminController(AdminService adminService) {
//		return new AdminControllerImpl(adminService);
//	}
//
//	@Bean
//	public AuthenticationController authenticationController(AuthenticationService authenticationService) {
//		return new AuthenticationControllerImpl(authenticationService);
//	}
//
//	@Bean
//	public AuthenticationService authenticationService() {
//		return Mockito.mock(AuthenticationService.class);
//	}
//
//
//	@Bean
//	ClientCardController clientCardController(ClientCardService cardService) {
//		return new ClientCardControllerImpl(cardService);
//	}
//
//	@Bean
//	public ClientCardService cardService() {
//		return Mockito.mock(ClientCardService.class);
//	}
}