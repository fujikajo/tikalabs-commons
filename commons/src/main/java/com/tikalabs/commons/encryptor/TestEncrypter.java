package com.tikalabs.commons.encryptor;


public class TestEncrypter {

    private static final String SECRET_KEY = "zyBqUIrQqt48btfYYDdZmxDNqhTFpK+/3aKWP4l6Bko="; // Geheimschlüssel zum
    // Verschlüsseln und
    // Entschlüsseln

    public static void main(String[] args) throws Exception {
        String originalKey = SECRET_KEY;
        String extendedKey = PasswordEncryptor.extendKey(originalKey);
        System.out.println("Extended Key: " + extendedKey);

        String decryptedPassword = "12345";
        String encryptedPassword = PasswordEncryptor.encrypt(decryptedPassword);
        System.out.println("Encrypted Password: " + encryptedPassword);
        System.out.println("Decrypted Password: " + PasswordEncryptor.decrypt(encryptedPassword));
    }

}
