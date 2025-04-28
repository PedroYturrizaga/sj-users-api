package com.practica.usersapi.security;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class EncryptionPassword {
	private static final String SECRET_KEY = "Pa5sT03ncrYpt10n"; //solo para la prueba
	private static final String ALGORITHM = "AES";

	public static String encrypt(String data) throws Exception {
		SecretKeySpec key = new SecretKeySpec(SECRET_KEY.getBytes("UTF-8"), ALGORITHM);
		Cipher cipher = Cipher.getInstance(ALGORITHM);
		cipher.init(Cipher.ENCRYPT_MODE, key);
		byte[] encryptedBytes = cipher.doFinal(data.getBytes("UTF-8"));
		return Base64.getEncoder().encodeToString(encryptedBytes);
	}

	public static String decrypt(String encryptedData) throws Exception {
		SecretKeySpec key = new SecretKeySpec(SECRET_KEY.getBytes("UTF-8"), ALGORITHM);
		Cipher cipher = Cipher.getInstance(ALGORITHM);
		cipher.init(Cipher.DECRYPT_MODE, key);
		byte[] decodedBytes = Base64.getDecoder().decode(encryptedData);
		byte[] decryptedBytes = cipher.doFinal(decodedBytes);
		return new String(decryptedBytes, "UTF-8");
	}
}