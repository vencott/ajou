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

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }
}
