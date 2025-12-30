package com.example.bankcards.service.impl;

import com.example.bankcards.dto.mapper.UserMapper;
import com.example.bankcards.dto.request.UserUpdateDto;
import com.example.bankcards.dto.response.UserResponseDto;
import com.example.bankcards.entity.Role;
import com.example.bankcards.entity.User;
import com.example.bankcards.enums.UserRole;
import com.example.bankcards.exception.businessException.RoleNotFoundException;
import com.example.bankcards.exception.businessException.UserRoleException;
import com.example.bankcards.exception.businessException.UserNotFoundException;
import com.example.bankcards.repository.RoleRepository;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.service.AdminService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * @author 4ndr33w
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
	
	private final UserRepository userRepository;
	private final RoleRepository roleRepository;
	private final UserMapper userMapper;
	
	@Override
	@Transactional
	public boolean deleteByClientId(@NonNull UUID id) {
		User existingUser = userRepository.findById(id).orElseThrow(
				() -> new UserNotFoundException("Не найден пользователь с id: %s".formatted(id.toString())));
		userRepository.delete(existingUser);
		return true;
	}
	
	@Override
	@Transactional
	public UserResponseDto updateByClientId(@NonNull UserUpdateDto updateDto,@NonNull  UUID clientId) {
		User existingUser = userRepository.findById(clientId).orElseThrow(
				() -> new UserNotFoundException("Не найден пользователь с id: %s".formatted(clientId.toString())));
		User updatedUser = userMapper.mapUpdateToEntity(updateDto, existingUser);
		
		return userMapper.mapToDto(updatedUser);
	}
	
	@Override
	public boolean blockByClientId(@NonNull UUID clientId) {
		int updatedRows = userRepository.blockUserById(clientId);
		return updatedRows > 0;
	}
	
	@Override
	@Transactional
	public boolean unblockByClientId(@NonNull UUID clientId) {
		int updatedRows = userRepository.unblockUserById(clientId);
		return updatedRows > 0;
	}
	
	@Override
	public boolean activateByClientId(@NonNull UUID clientId) {
		int updatedRows = userRepository.activateUserById(clientId);
		return updatedRows > 0;
	}
	
	@Override
	@Transactional
	public boolean deactivateByClientId(@NonNull UUID clientId) {
		int updatedRows = userRepository.deactivateUserById(clientId);
		return updatedRows > 0;
	}
	
	@Override
	@Transactional
	public boolean addRoleToUser(UserRole role, UUID clientId) {
		Role existingRole = roleRepository.findByRole(role.getValue())
				.orElseThrow(
				() -> new RoleNotFoundException("Роль %s не найдена".formatted(role.getValue())));
		User existingUser = userRepository.findById(clientId).orElseThrow(
				() -> new UserNotFoundException("Не найден пользователь с clientId: %s".formatted(clientId.toString())));
		if(!existingUser.getRoles().stream().anyMatch(x -> x.getRole().equals(role.getValue()))) {
			existingUser.getRoles().add(existingRole);
			return true;
		}
		throw new UserRoleException("Пользователь с id: %s уже имеет роль %s".formatted(clientId, role.getValue()));
	}
	
	@Override
	@Transactional
	public boolean removeRoleFromUser(UserRole role, UUID id) {
		Role existingRole = roleRepository.findByRole(role.getValue())
				.orElseThrow(
				() -> new RoleNotFoundException("Роль %s не найдена".formatted(role.getValue())));
		User existingUser = userRepository.findById(id)
				.orElseThrow(
				() -> new UserNotFoundException("Не найден пользователь с id: %s".formatted(id.toString())));
		if(existingUser.getRoles().stream().anyMatch(x -> x.getRole().equals(role.getValue()))) {
			existingUser.getRoles().remove(existingRole);
			return true;
		}
		throw new UserRoleException("У пользователя с id: %s отсутствует роль %s".formatted(id, role.getValue()));
	}
}