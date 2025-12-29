package com.example.bankcards.controller.impl;

import com.example.bankcards.controller.AuthenticationController;
import com.example.bankcards.dto.response.AuthenticationResponseDto;
import com.example.bankcards.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 4ndr33w
 * @version 1.0
 */
@RestController
@RequiredArgsConstructor
public class AuthenticationControllerImpl implements AuthenticationController {
	
	private final AuthenticationService authenticationService;
	
	@Override
	public ResponseEntity<AuthenticationResponseDto> refresh(String refreshToken) {
		return ResponseEntity.status(HttpStatus.OK).body(authenticationService.refreshAccessToken(refreshToken));
	}
}