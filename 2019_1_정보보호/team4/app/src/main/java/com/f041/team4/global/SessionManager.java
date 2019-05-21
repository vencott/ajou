package com.f041.team4.global;

import com.f041.team4.data.SessionPair;

import java.security.Key;
import java.util.ArrayList;
import java.util.List;

public class SessionManager {
    private static SessionManager instance = new SessionManager();
    List<SessionPair> sessionPairs;

    private SessionManager() {
        sessionPairs = new ArrayList<>();
    }

    public static SessionManager getInstance() {
        return instance;
    }

    public void add(SessionPair sessionPair) {
        sessionPairs.add(sessionPair);
    }

    public Key searchSessionKey(String encryptedSessionKey) {
        for (SessionPair sessionPair : sessionPairs) {
            if (sessionPair.getEncryptedSessionKey().equals(encryptedSessionKey))
                return sessionPair.getSessionKey();
        }
        return null;
    }
}
