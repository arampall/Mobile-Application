package edu.uncc.mad.group14_hw08;

import java.io.Serializable;

/**
 * Created by achyu on 13-Apr-16.
 */
public class Message implements Serializable {
    String sender,receiver,message_text,timestamp, uid;
    boolean message_read;

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getMessage_text() {
        return message_text;
    }

    public void setMessage_text(String message_text) {
        this.message_text = message_text;
    }

    public boolean isMessage_read() {
        return message_read;
    }

    public void setMessage_read(boolean message_read) {
        this.message_read = message_read;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    @Override
    public String toString() {
        return "Message{" +
                "sender='" + sender + '\'' +
                ", receiver='" + receiver + '\'' +
                ", message_text='" + message_text + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", uid='" + uid + '\'' +
                ", message_read=" + message_read +
                '}';
    }
}
