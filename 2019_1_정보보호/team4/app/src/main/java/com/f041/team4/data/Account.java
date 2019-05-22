package com.f041.team4.data;

import com.f041.team4.global.Algorithm;
import com.f041.team4.global.KeyManager;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

public class Account {
    private static Account instance = new Account();
    String name;
    PublicKey publicKey;
    PrivateKey privateKey;

    private Account() {
        initKeys();
    }

    public static Account getInstance() {
        return instance;
    }

    private void initKeys() {
        KeyPair keyPair = KeyManager.createNewKeyPair(Algorithm.RSA);
        this.publicKey = keyPair.getPublic();
        this.privateKey = keyPair.getPrivate();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }
}
