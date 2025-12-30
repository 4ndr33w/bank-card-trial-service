package com.example.bankcards.security.service;

import com.example.bankcards.entity.User;
import com.example.bankcards.exception.businessException.UserNotFoundException;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.security.data.AppUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author 4ndr33w
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
public class AppUserDetailsService implements UserDetailsService {
	
	private final UserRepository userRepository;
	
	/**
	 * Поиск пользователя по логину. В качестве логина может вы ступать:
	 * <ul>
	 *     <li>{@code email}</li>
	 *     <li>{@code userName}</li>
	 * </ul>
	 * @param login поле, идентифицирующее пользователя, чьи данные запрашиваются.
	 * @return объект, реализующий {@code UserDetails}, содержащий данные профиля пользователя
	 * @throws UserNotFoundException при неудачной попытке найти пользователя по {@code email} или {@code userName}
	 */
	@Override
	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
		try {
			User user = userRepository.findByUserName(login)
					.orElseGet(() ->userRepository.findByEmail(login)
							.orElseThrow(() ->
									new UsernameNotFoundException("Не найден пользователь с логином: %s".formatted(login)))
					);
			return new AppUserDetails(user);
		}
		catch (UsernameNotFoundException e) {
			throw new UserNotFoundException("Не найден пользователь с userName: %s".formatted(login), e);
		}
	}
}