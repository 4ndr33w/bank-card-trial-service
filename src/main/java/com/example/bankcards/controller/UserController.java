package com.example.bankcards.controller;

import com.example.bankcards.dto.request.UserRequestDto;
import com.example.bankcards.dto.request.UserUpdateDto;
import com.example.bankcards.dto.response.UserPageViewResponseDto;
import com.example.bankcards.dto.response.UserResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
@RequestMapping("/api/v1/users")
public interface UserController {
	
	@PostMapping
	ResponseEntity<UserResponseDto> create(@RequestBody UserRequestDto request);
	
	@GetMapping("/{id}")
	ResponseEntity<UserResponseDto> getById(@PathVariable UUID id);
	
	@PatchMapping
	ResponseEntity<UserResponseDto> update(@RequestBody UserUpdateDto update);
	
	@GetMapping
	ResponseEntity<UserResponseDto> get();
	
	@GetMapping("/email/{email}")
	@PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
	ResponseEntity<UserResponseDto> getByEmail(@PathVariable String email);
	
	@GetMapping("/username/{username}")
	@PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
	ResponseEntity<UserResponseDto> getByUsername(@PathVariable String username);
	
	@DeleteMapping
	ResponseEntity<UserResponseDto> delete();
	
	@PostMapping("/page/{page}")
	@PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
	ResponseEntity<UserPageViewResponseDto> getPage(@PathVariable Integer page, @RequestParam Integer limit);
	
	@GetMapping("/block")
	ResponseEntity<Boolean> block();
	
	@GetMapping("/deactivate")
	ResponseEntity<Boolean> deactivate();
}