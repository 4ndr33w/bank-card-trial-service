package com.example.bankcards.controller.impl;

import com.example.bankcards.controller.UserController;
import com.example.bankcards.dto.request.UserRequestDto;
import com.example.bankcards.dto.request.UserUpdateDto;
import com.example.bankcards.dto.response.UserPageViewResponseDto;
import com.example.bankcards.dto.response.UserResponseDto;
import com.example.bankcards.service.UserService;
import jakarta.validation.Valid;
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
public class UserControllerImpl implements UserController {
	
	private final UserService userService;
	
	@Override
	public ResponseEntity<UserResponseDto> create(@Valid UserRequestDto request) {
		return ResponseEntity.status(HttpStatus.CREATED).body(userService.create(request));
	}
	
	@Override
	public ResponseEntity<UserResponseDto> getById(UUID id) {
		return ResponseEntity.status(HttpStatus.OK).body(userService.findById(id));
	}
	
	@Override
	public ResponseEntity<UserResponseDto> update(@Valid UserUpdateDto update) {
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(userService.update(update));
	}
	
	@Override
	public ResponseEntity<UserResponseDto> get() {
		return ResponseEntity.status(HttpStatus.OK).body(userService.find());
	}
	
	@Override
	public ResponseEntity<UserResponseDto> getByEmail(String email) {
		return ResponseEntity.status(HttpStatus.OK).body(userService.findByEmail(email));
	}
	
	@Override
	public ResponseEntity<UserResponseDto> getByUsername(String username) {
		return ResponseEntity.status(HttpStatus.OK).body(userService.findByUserName(username));
	}
	
	@Override
	public ResponseEntity<UserResponseDto> delete() {
		userService.delete();
		return ResponseEntity.status(HttpStatus.OK).build();
	}
	
	@Override
	public ResponseEntity<UserPageViewResponseDto> getPage(Integer page, Integer limit) {
		return ResponseEntity.status(HttpStatus.OK).body(userService.findAllByPage(page, limit));
	}
	
	@Override
	public ResponseEntity<Boolean> block() {
		return ResponseEntity.status(HttpStatus.OK).body(userService.block());
	}
	
	@Override
	public ResponseEntity<Boolean> deactivate() {
		return ResponseEntity.status(HttpStatus.OK).body(userService.deactivate());
	}
}