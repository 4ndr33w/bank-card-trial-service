package com.example.bankcards.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author 4ndr33w
 * @version 1.0
 */
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {
	
	private Long accessTokenLifetime;
	private Long refreshTokenLifetime;
	private String issuer;
	private String audience;
	private String accessPublic;
	private String refreshPublic;
	private String accessPrivate;
	private String refreshPrivate;
	
	public Long getTokenLifeTime(Long millis) {
		return millis / 60_000 ;
	}
}
