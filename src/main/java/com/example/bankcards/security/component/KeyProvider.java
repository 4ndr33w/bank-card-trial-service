package com.example.bankcards.security.component;

import com.example.bankcards.util.constant.Constants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Scanner;

/**
 * @author 4ndr33w
 * @version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class KeyProvider {
	
	private final ResourceLoader resourceLoader;
	
	public RSAPrivateKey getPrivateKey(String resourcePath) {
		try {
			String privateKeyContent = readKeyFile(resourcePath)
					.replaceAll("\\s", "");
			
			byte[] decoded = Base64.getDecoder().decode(privateKeyContent);
			PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decoded);
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
		}
		catch (Exception e) {
			log.error("{}, {}", Constants.FAILED_TO_READ_SECURITY_KEYS_MESSAGE, e.getMessage());
			throw new RuntimeException(Constants.FAILED_TO_READ_SECURITY_KEYS_MESSAGE, e);
		}
	}
	
	public RSAPublicKey getPublicKey(String resourcePath) {
		try {
			String publicKeyContent = readKeyFile(resourcePath)
					.replaceAll("\\s", "");
			
			byte[] decoded = Base64.getDecoder().decode(publicKeyContent);
			X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decoded);
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			return (RSAPublicKey) keyFactory.generatePublic(keySpec);
		}
		catch (Exception e) {
			log.error("{}, {}", Constants.FAILED_TO_READ_SECURITY_KEYS_MESSAGE, e.getMessage());
			throw new RuntimeException(Constants.FAILED_TO_READ_SECURITY_KEYS_MESSAGE, e);
		}
	}
	
	private Resource getResource(String resourcePath) {
		return resourceLoader.getResource(resourcePath);
	}
	
	private String readKeyFile(String resourcePath) throws Exception {
		
		Resource resource = getResource(resourcePath);
		try (InputStream inputStream = resource.getInputStream();
		     Scanner scanner = new Scanner(inputStream).useDelimiter("\\A")) {
			return scanner.hasNext() ? scanner.next() : "";
		}
	}
}
