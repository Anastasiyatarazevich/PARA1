package com.example.para1;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class Cryptography {
    public static String encryptMessage(String message, String secret_key) throws Exception {
        SecretKeySpec aesKey = new SecretKeySpec(secret_key.getBytes(), "AES");
        Cipher cipher;

        cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, aesKey);
        byte[] encrypted;
        encrypted = cipher.doFinal(message.getBytes());

        return android.util.Base64.encodeToString(cipher.doFinal(message.getBytes()), 0);
    }

    public static String decryptMessage(String encrypted_message, String secret_key) throws Exception {
        SecretKeySpec aesKey = new SecretKeySpec(secret_key.getBytes(), "AES");
        Cipher cipher;
        cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, aesKey);
        String decrypted;
        byte[] decoded;
        decoded = android.util.Base64.decode(encrypted_message.getBytes(), 0);
        decrypted = new String(cipher.doFinal(decoded));

        return decrypted;
    }
}
