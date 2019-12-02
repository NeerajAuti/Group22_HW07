package com.example.group22_hw07;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Message {
    String message_user, message_text, message_userid,TripID;
    Long message_time;

    public Message(String message_user, String message_text, String message_userid,String TripID) {
        this.message_user = message_user;
        this.message_text = message_text;
        this.message_userid = message_userid;
        this.TripID = TripID;
        this.message_time = new Date().getTime();
    }

    public Message() {
    }

    public Message(Map<String, Object> MessagesMap) {
        this.message_text = (String) MessagesMap.get("message_text");
        this.message_user = (String) MessagesMap.get("message_user");
        this.message_userid = (String) MessagesMap.get("message_userid");
        this.TripID = (String) MessagesMap.get("TripID");
        this.message_time = (Long) MessagesMap.get("message_time");
    }

    public Map Message() {
        Map<String, Object> MessagesMap = new HashMap<>();

        MessagesMap.put("message_text", this.message_text);
        MessagesMap.put("message_user", this.message_user);
        MessagesMap.put("message_userid", this.message_userid);
        MessagesMap.put("message_time", this.message_time);
        MessagesMap.put("TripID", this.TripID);

        return MessagesMap;
    }

    public String getTripID() {
        return TripID;
    }

    public void setTripID(String tripID) {
        TripID = tripID;
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
