package com.example.bankcards.service.impl;

import com.example.bankcards.dto.mapper.UserMapper;
import com.example.bankcards.dto.request.UserRequestDto;
import com.example.bankcards.dto.request.UserUpdateDto;
import com.example.bankcards.dto.response.UserPageViewResponseDto;
import com.example.bankcards.dto.response.UserResponseDto;
import com.example.bankcards.entity.Role;
import com.example.bankcards.entity.User;
import com.example.bankcards.enums.UserRole;
import com.example.bankcards.exception.businessException.RoleNotFoundException;
import com.example.bankcards.exception.businessException.UserCreationException;
import com.example.bankcards.exception.businessException.UserNotFoundException;
import com.example.bankcards.repository.RoleRepository;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.service.UserService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * @author 4ndr33w
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;
	private final RoleRepository roleRepository;
	private final UtilService utilService;
	private final UserMapper userMapper;
	
	@Override
	@Transactional
	public UserResponseDto create(@NonNull UserRequestDto request) {
		if(userRepository.existsByEmail(request.email())) {
			throw new UserCreationException("Пользователь уже существует");
		}
		Role userRole = roleRepository.findByRole(UserRole.USER.getValue())
				.orElseThrow(
				() -> new RoleNotFoundException("Роль пользователя не найдена"));
		try {
			User mappedUser = userMapper.mapRequestToEntity(request, userRole);
			User user = userRepository.save(mappedUser);
			return userMapper.mapToDto(user);
		}
		catch(Exception e) {
			throw new UserCreationException("Ошибка при создании пользователя", e);
		}
	}
	
	@Override
	@Transactional
	public UserResponseDto update(@NonNull UserUpdateDto update) {
		UUID authenticatedUserId = utilService.getUserIdFromSecurityContext();
		User existingUser = userRepository.findById(authenticatedUserId)
				.orElseThrow(
				() -> new UserNotFoundException("Не найден пользователь с id: %s".formatted(authenticatedUserId)));
		User updatedUser = userMapper.mapUpdateToEntity(update, existingUser);
		
		return userMapper.mapToDto(updatedUser);
	}
	
	@Override
	@Transactional(readOnly = true)
	public UserResponseDto findById(@NonNull UUID id) {
		User existingUser = userRepository.findById(id)
				.orElseThrow(
				() -> new UserNotFoundException("Не найден пользователь с id: %s".formatted(id.toString())));
		
		return userMapper.mapToDto(existingUser);
	}
	
	@Override
	@Transactional(readOnly = true)
	public UserResponseDto find() {
		UUID id = utilService.getUserIdFromSecurityContext();
		User existingUser = userRepository.findById(id)
				.orElseThrow(
				() -> new UserNotFoundException("Не найден пользователь с id: %s".formatted(id.toString())));
		
		return userMapper.mapToDto(existingUser);
	}
	
	@Override
	@Transactional
	public void delete() {
		User authenticatedUser = utilService.getUserFromSecurityContext();
		userRepository.deleteById(authenticatedUser.getId());
	}
	
	@Override
	@Transactional(readOnly = true)
	public UserResponseDto findByEmail(String email) {
		User existingUser = userRepository.findByEmail(email).orElseThrow(
				() -> new UserNotFoundException("Не найден пользователь с email: %s".formatted(email)));
		return userMapper.mapToDto(existingUser);
	}
	
	@Override
	@Transactional(readOnly = true)
	public UserPageViewResponseDto findAllByPage(Integer page, Integer limit) {
		int pageLimit = utilService.setPageLimit(limit);
		int paginationPage = (page == null || page < 1) ? 1 : (page - 1);
		
		Pageable pageable = PageRequest.of(paginationPage, pageLimit);
		Page<User> usersPage = userRepository.findAll(pageable);
		
		long totalUsers = userRepository.count();
		long totalPages = (totalUsers / pageLimit) + 1;
		
		List<UserResponseDto> userResponseDtoList = usersPage.stream().map(userMapper::mapToDto).toList();
		
		return new UserPageViewResponseDto(
				paginationPage + 1,
				pageLimit,
				totalPages,
				totalUsers,
				userResponseDtoList
		);
	}
	
	@Override
	@Transactional(readOnly = true)
	public UserResponseDto findByUserName(String userName) {
		User existingUser = userRepository.findByUserName(userName)
				.orElseThrow(
				() -> new UserNotFoundException("Не найден пользователь с email: %s".formatted(userName)));
		
		return userMapper.mapToDto(utilService.getUserFromSecurityContext());
	}
	
	@Override
	@Transactional
	public boolean block() {
		User authenticatedUser = utilService.getUserFromSecurityContext();
		int updatedRows = userRepository.blockUserById(authenticatedUser.getId());
		return updatedRows > 0;
	}
	
	@Override
	@Transactional
	public boolean deactivate() {
		User authenticatedUser = utilService.getUserFromSecurityContext();
		int updatedRows = userRepository.deactivateUserById(authenticatedUser.getId());
		return updatedRows > 0;
	}
}