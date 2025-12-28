package com.example.bankcards.controller.impl;

import com.example.bankcards.controller.AdminController;
import com.example.bankcards.dto.request.UserUpdateDto;
import com.example.bankcards.dto.response.UserResponseDto;
import com.example.bankcards.enums.UserRole;
import com.example.bankcards.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * @author 4ndr33w
 * @version 1.0
 */
@RestController
@RequiredArgsConstructor
public class AdminControllerImpl implements AdminController {
	
	private final AdminService adminService;
	
	@Override
	public ResponseEntity<UserResponseDto> updateByClientId(UserUpdateDto request, UUID clientId) {
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(adminService.updateByClientId(request, clientId));
	}
	
	@Override
	public ResponseEntity<UserResponseDto> deleteByClientId(UUID clientId) {
		adminService.deleteByClientId(clientId);
		return ResponseEntity.status(HttpStatus.OK).build();
	}
	
	@Override
	public ResponseEntity<Boolean> blockAccountByClientId(UUID clientId) {
		return ResponseEntity.status(HttpStatus.OK).body(adminService.blockByClientId(clientId));
	}
	
	@Override
	public ResponseEntity<Boolean> deactivateAccountByClientId(UUID clientId) {
		return ResponseEntity.status(HttpStatus.OK).body(adminService.deactivateByClientId(clientId));
	}
	
	@Override
	public ResponseEntity<Boolean> unblockAccountByClientId(UUID clientId) {
		return ResponseEntity.status(HttpStatus.OK).body(adminService.unblockByClientId(clientId));
	}
	
	@Override
	public ResponseEntity<Boolean> activateAccountByClientId(UUID clientId) {
		return ResponseEntity.status(HttpStatus.OK).body(adminService.activateByClientId(clientId));
	}
	
	@Override
	public ResponseEntity<Boolean> addRoleToUser(UserRole role, UUID userId) {
		return ResponseEntity.status(HttpStatus.OK).body(adminService.addRoleToUser(role, userId));
	}
	
	@Override
	public ResponseEntity<Boolean> removeRoleFromUser(UserRole role, UUID userId) {
		return ResponseEntity.status(HttpStatus.OK).body(adminService.removeRoleFromUser(role, userId));
	}
}