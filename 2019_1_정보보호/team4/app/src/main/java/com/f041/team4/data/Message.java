package com.f041.team4.data;

public class Message {
    String from;
    String to;
    String encryptedSessionKey;
    String message;
    Long time;

    public Message() {
    }

    public Message(String from, String to, String encryptedSessionKey, String message, Long time) {
        this.from = from;
        this.to = to;
        this.encryptedSessionKey = encryptedSessionKey;
        this.message = message;
        this.time = time;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public String getMessage() {
        return message;
    }

    public String getEncryptedSessionKey() {
        return encryptedSessionKey;
    }
}
