package com.f041.team4.global;

import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;

public class KeyManager {
    public static Key createNewSessionKey(Algorithm algorithm) {
        Key ret = null;
        try {
            KeyGenerator keyGenerator;
            keyGenerator = KeyGenerator.getInstance(algorithm.getName());
            keyGenerator.init(algorithm.getKeySize());
            ret = keyGenerator.generateKey();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return ret;
    }

    public static KeyPair createNewKeyPair(Algorithm algorithm) {
        KeyPair ret = null;
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(algorithm.getName());
            keyPairGenerator.initialize(algorithm.getKeySize());
            ret = keyPairGenerator.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return ret;
    }

    public static String encodePublicKey(PublicKey key) {
        return Base64.getEncoder().encodeToString(key.getEncoded());
    }

    public static PublicKey decodePublicKey(String encoded) {
        byte[] publicKeyData = Base64.getDecoder().decode(encoded);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(publicKeyData);
        KeyFactory kf;
        PublicKey publicKey = null;
        try {
            kf = KeyFactory.getInstance(Algorithm.RSA.getName());
            publicKey = kf.generatePublic(spec);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return publicKey;
    }

    public static byte[] encodeKeyBytes(Key key) {
        return key.getEncoded();
    }

    public static Key decodeKeyBytes(byte[] encoded) {
        return new SecretKeySpec(encoded, 0, encoded.length, Algorithm.AES.getName());
    }
}
