package com.example.bankcards.controller;

import com.example.bankcards.dto.response.AuthenticationResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author 4ndr33w
 * @version 1.0
 */
@RequestMapping("/api/v1/auth")
public interface AuthenticationController {
	
	@PostMapping("/refresh")
	ResponseEntity<AuthenticationResponseDto> updateById(@RequestParam String refreshToken);
}