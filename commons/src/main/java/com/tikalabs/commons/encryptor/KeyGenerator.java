package com.tikalabs.commons.encryptor;

import java.security.SecureRandom;
import java.util.Base64;

public class KeyGenerator {

    private static final int KEY_LENGTH = 32; // Länge des Schlüssels in Bytes

    public static String generateRandomKey() {
        byte[] keyBytes = new byte[KEY_LENGTH];
        new SecureRandom().nextBytes(keyBytes);
        return Base64.getEncoder().encodeToString(keyBytes);
    }

    public static void main(String[] args) throws Exception {
        String originalSecretKey = generateRandomKey();
        String extendedKey = PasswordEncryptor.extendKey(originalSecretKey);
        System.out.println("Generated Secret Key: " + extendedKey);
    }
}
