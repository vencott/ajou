package com.f041.team4.data;

import java.security.Key;

public class SessionPair {

    Key SessionKey;
    String encryptedSessionKey;

    public SessionPair(Key sessionKey, String encryptedSessionKey) {
        SessionKey = sessionKey;
        this.encryptedSessionKey = encryptedSessionKey;
    }

    public Key getSessionKey() {
        return SessionKey;
    }

    public void setSessionKey(Key sessionKey) {
        SessionKey = sessionKey;
    }

    public String getEncryptedSessionKey() {
        return encryptedSessionKey;
    }

    public void setEncryptedSessionKey(String encryptedSessionKey) {
        this.encryptedSessionKey = encryptedSessionKey;
    }
}
