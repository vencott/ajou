package com.f041.team4.data;

import com.f041.team4.global.KeyManager;

import java.security.PublicKey;

public class User {
    String name;
    String encodedPublicKey;

    public User() {
    }

    public User(String name, String encodedPublicKey) {
        this.name = name;
        this.encodedPublicKey = encodedPublicKey;
    }

    public User(String name, PublicKey publicKey) {
        this.name = name;
        this.encodedPublicKey = KeyManager.encodePublicKey(publicKey);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEncodedPublicKey() {
        return encodedPublicKey;
    }
}
