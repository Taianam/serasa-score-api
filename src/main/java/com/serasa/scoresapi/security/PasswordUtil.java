package com.serasa.scoresapi.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Component
public class PasswordUtil {

	@Value("${security.password.salt}")
	private String salt;

	public String encryptPassword(String password) {
		try {
			String saltedPassword = password + salt;
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] hash = digest.digest(saltedPassword.getBytes());
			return Base64.getEncoder().encodeToString(hash);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("Erro ao criptografar senha", e);
		}
	}

	public boolean verifyPassword(String password, String storedHash) {
		String encryptedPassword = encryptPassword(password);
		return encryptedPassword.equals(storedHash);
	}

}
