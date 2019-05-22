package com.f041.team4.data;

import java.security.Key;

public class SessionPair {
    Key sessionKey;
    String encryptedSessionKey;

    public SessionPair(Key sessionKey, String encryptedSessionKey) {
        this.sessionKey = sessionKey;
        this.encryptedSessionKey = encryptedSessionKey;
    }

    public Key getSessionKey() {
        return sessionKey;
    }

    public String getEncryptedSessionKey() {
        return encryptedSessionKey;
    }
}
