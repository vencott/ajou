package com.f041.team4.global;

public enum Algorithm {
    RSA("RSA", 2048),
    AES("AES", 128);

    private String name;
    private int keySize;

    Algorithm(String name, int keySize) {
        this.name = name;
        this.keySize = keySize;
    }

    public String getName() {
        return name;
    }

    public int getKeySize() {
        return keySize;
    }
}
