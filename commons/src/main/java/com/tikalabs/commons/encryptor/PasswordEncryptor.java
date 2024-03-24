package com.tikalabs.commons.encryptor;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Base64;
import java.util.Properties;

public class PasswordEncryptor {

    private static final String CONFIG_FILE = "encryption.properties";
    private static final String SECRET_KEY_PROPERTY = "encryption.key";

    private static final String ALGORITHM = "AES";
    private static final String SECRET_KEY = "YourSecretKey"; // Geheimnis Schlüssel zum
    // Verschlüsseln und
    // Entschlüsseln

    public static String encrypt(String password) throws Exception {
        SecretKey key = new SecretKeySpec(getSecretKey().getBytes(), ALGORITHM);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encryptedBytes = cipher.doFinal(password.getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    public static String decrypt(String encryptedPassword) throws Exception {
        SecretKey key = new SecretKeySpec(getSecretKey().getBytes(), ALGORITHM);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedPassword));
        return new String(decryptedBytes);
    }

    // Methode zur Erweiterung des Schlüssels auf 16 Bytes (128 Bits) mit SHA-256
    public static String extendKey(String key) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(key.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(Arrays.copyOf(hash, 16)); // 16 Bytes für AES-128
    }

    private static String getSecretKey() throws IOException {
        Properties properties = new Properties();
        try (InputStream inputStream = PasswordEncryptor.class.getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            properties.load(inputStream);
        }
        return properties.getProperty(SECRET_KEY_PROPERTY);
    }

}
