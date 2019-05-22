package com.f041.team4.data;

public class Session {
    String initiator;
    String receiver;
    String encryptedSessionKey;
    boolean enabled;

    public Session() {
    }

    public Session(String initiator, String receiver, String encryptedSessionKey, boolean enabled) {
        this.initiator = initiator;
        this.receiver = receiver;
        this.encryptedSessionKey = encryptedSessionKey;
        this.enabled = enabled;
    }

    public String getInitiator() {
        return initiator;
    }

    public String getReceiver() {
        return receiver;
    }

    public String getEncryptedSessionKey() {
        return encryptedSessionKey;
    }

    public boolean isEnabled() {
        return enabled;
    }
}
