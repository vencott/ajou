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

    public void setInitiator(String initiator) {
        this.initiator = initiator;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getEncryptedSessionKey() {
        return encryptedSessionKey;
    }

    public void setEncryptedSessionKey(String encryptedSessionKey) {
        this.encryptedSessionKey = encryptedSessionKey;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
