package com.example.bankcards.controller;

import com.example.bankcards.dto.request.UserUpdateDto;
import com.example.bankcards.dto.response.UserResponseDto;
import com.example.bankcards.enums.UserRole;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

/**
 * @author 4ndr33w
 * @version 1.0
 */
@RequestMapping("/api/v1/admin")
public interface AdminController {
	
	@PatchMapping("/{clientId}")
	ResponseEntity<UserResponseDto> updateByClientId(@RequestBody UserUpdateDto request, @PathVariable UUID clientId);
	
	@DeleteMapping("/{clientId}")
	ResponseEntity<UserResponseDto> deleteByClientId(@PathVariable UUID clientId);
	
	@GetMapping("/block/{clientId}")
	ResponseEntity<Boolean> blockAccountByClientId(@PathVariable UUID clientId);
	
	@GetMapping("/deactivate/{clientId}")
	ResponseEntity<Boolean> deactivateAccountByClientId(@PathVariable UUID clientId);
	
	@GetMapping("/unblock/{clientId}")
	ResponseEntity<Boolean> unblockAccountByClientId(@PathVariable UUID clientId);
	
	@GetMapping("/activate/{clientId}")
	ResponseEntity<Boolean> activateAccountByClientId(@PathVariable UUID clientId);
	
	@PostMapping("/role/add")
	ResponseEntity<Boolean> addRoleToUser(@RequestParam UserRole role, @RequestParam UUID userId);
	
	@PostMapping("/role/remove")
	ResponseEntity<Boolean> removeRoleFromUser(@RequestParam UserRole role, @RequestParam UUID userId);
}