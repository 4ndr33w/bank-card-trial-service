package com.example.bankcards.security.filter;

import com.example.bankcards.enums.AuthorizationType;
import com.example.bankcards.enums.HttpHeaderKey;
import com.example.bankcards.exception.authorizationException.TokenValidationException;
import com.example.bankcards.exception.handler.SecurityExceptionHandler;
import com.example.bankcards.security.component.JwtTokenProvider;
import com.example.bankcards.util.constant.Constants;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * @author 4ndr33w
 * @version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
	
	private final JwtTokenProvider jwtTokenProvider;
	private final UserDetailsService userDetailsService;
	private final SecurityExceptionHandler exceptionHandler;
	
	@Override
	protected void doFilterInternal(
			@NonNull HttpServletRequest request,
			@NonNull HttpServletResponse response,
			@NonNull FilterChain filterChain) throws IOException {
		try {
			String token = getTokenFromRequest(request);
			if (token == null) {
				filterChain.doFilter(request, response);
				return;
			}
			verifyToken(token);
			
			String username = jwtTokenProvider.getUserNameFromToken(token);
			UserDetails userDetails = userDetailsService.loadUserByUsername(username);
			
			UsernamePasswordAuthenticationToken authentication =
					new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
			authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
			
			SecurityContextHolder.getContext().setAuthentication(authentication);
			
			filterChain.doFilter(request, response);
			
		} catch (Exception e) {
			log.error("Вызов JwtFilter.getTokenFromRequest: Ошибка при попытке аутенфицировать пользователя: {}", e.getMessage());
			exceptionHandler.unauthorizedHandler(request, response, e);
		}
	}
	
	private void verifyToken(String token) {
		boolean isTokenValid = jwtTokenProvider.validateAccessToken(token);
		
		if(!isTokenValid) {
			log.warn("Вызов JwtFilter.doFilterInternal: Токен не прошел валидацию");
			throw new TokenValidationException(Constants.INVALID_ACCESS_TOKEN_MESSAGE);
		}
	}
	
	private String getTokenFromRequest(HttpServletRequest request) {
		String bearer = request.getHeader(HttpHeaderKey.AUTHORIZATION.getValue());
		if (StringUtils.hasText(bearer) && bearer.startsWith(AuthorizationType.BEARER.getValue())) {
			return bearer.substring(AuthorizationType.BEARER.getValue().length());
		}
		return null;
	}
}