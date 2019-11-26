package com.example.group22_hw07;

import java.util.Date;

public class Message {
    String message_user, message_text, message_userid;
    Long message_time;

    public Message(String message_user, String message_text, String message_userid) {
        this.message_user = message_user;
        this.message_text = message_text;
        this.message_userid = message_userid;
        message_time = new Date().getTime();
    }

    public String getMessage_user() {
        return message_user;
    }

    public void setMessage_user(String message_user) {
        this.message_user = message_user;
    }

    public String getMessage_text() {
        return message_text;
    }

    public void setMessage_text(String message_text) {
        this.message_text = message_text;
    }

    public String getMessage_userid() {
        return message_userid;
    }

    public void setMessage_userid(String message_userid) {
        this.message_userid = message_userid;
    }

    public Long getMessage_time() {
        return message_time;
    }

    public void setMessage_time(Long message_time) {
        this.message_time = message_time;
    }

    @Override
    public String toString() {
        return "Message{" +
                "message_user='" + message_user + '\'' +
                ", message_text='" + message_text + '\'' +
                ", message_userid='" + message_userid + '\'' +
                ", message_time=" + message_time +
                '}';
    }
}
