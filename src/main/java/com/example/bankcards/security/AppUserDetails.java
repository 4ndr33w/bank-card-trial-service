package com.example.bankcards.security;

import com.example.bankcards.entity.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * @author 4ndr33w
 * @version 1.0
 */
@Getter
@RequiredArgsConstructor
public class AppUserDetails implements UserDetails {
	
	private final User user;
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return user.getRoles();
	}
	
	@Override
	public String getPassword() {
		return user.getPassword();
	}
	
	@Override
	public String getUsername() {
		return user.getUserName();
	}
	
	@Override
	public boolean isAccountNonLocked() {
		return !user.isBlocked();
	}
	
	@Override
	public boolean isEnabled() {
		return user.isActive();
	}
}