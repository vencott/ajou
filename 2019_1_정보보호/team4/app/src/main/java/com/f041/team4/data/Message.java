package com.f041.team4.data;

public class Message {
    String from;
    String to;
    String encryptedSessionKey;
    String message;

    public Message() {

    }

    public Message(String from, String to, String encryptedSessionKey, String message) {
        this.from = from;
        this.to = to;
        this.encryptedSessionKey = encryptedSessionKey;
        this.message = message;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getEncryptedSessionKey() {
        return encryptedSessionKey;
    }

    public void setEncryptedSessionKey(String encryptedSessionKey) {
        this.encryptedSessionKey = encryptedSessionKey;
    }
}
