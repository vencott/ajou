package com.f041.team4.global;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class CryptoManager {
    private static Cipher getCipher(int mode, String algorithm, Key key) {
        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance(algorithm);
            cipher.init(mode, key);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException e) {
            e.printStackTrace();
        }
        return cipher;
    }

    // Encrypt, decrypt texts
    public static String encrypt(Algorithm algorithm, String plainText, Key key) {
        String ret = null;
        try {
            Cipher cipher = getCipher(Cipher.ENCRYPT_MODE, algorithm.getName(), key);
            byte[] bytes = cipher.doFinal(plainText.getBytes());
            ret = Base64.getEncoder().encodeToString(bytes);
        } catch (IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
        }
        return ret;
    }

    public static String decrypt(Algorithm algorithm, String cipherText, Key key) {
        String ret = null;
        try {
            Cipher cipher = getCipher(Cipher.DECRYPT_MODE, algorithm.getName(), key);
            byte[] bytes = Base64.getDecoder().decode(cipherText);
            ret = new String(cipher.doFinal(bytes));
        } catch (IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
        }
        return ret;
    }

    // Encrypt, decrypt keys
    public static String encryptSessionKey(Algorithm algorithm, Key sessionKey, PublicKey publicKey) {
        String ret = null;
        try {
            Cipher cipher = getCipher(Cipher.ENCRYPT_MODE, algorithm.getName(), publicKey);
            byte[] bytes = cipher.doFinal(KeyManager.encodeKeyBytes(sessionKey));
            ret = Base64.getEncoder().encodeToString(bytes);
        } catch (IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
        }
        return ret;
    }

    public static Key decryptSessionKey(Algorithm algorithm, String sessionKey, PrivateKey privateKey) {
        Key ret = null;
        try {
            Cipher cipher = getCipher(Cipher.DECRYPT_MODE, algorithm.getName(), privateKey);
            byte[] bytes = Base64.getDecoder().decode(sessionKey);
            ret = KeyManager.decodeKeyBytes(cipher.doFinal(bytes));
        } catch (IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
        }
        return ret;
    }
}
