package com.example.oruclejava.models;

public class ChatMessage {
    private String senderId;
    private String receiverId;
    private String receiverAvatar;
    private String text;
    private String timestamp;

    public ChatMessage(String senderId, String receiverId, String receiverAvatar, String text, String timestamp) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.receiverAvatar = receiverAvatar;
        this.text = text;
        this.timestamp = timestamp;
    }

    public ChatMessage() {
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getReceiverAvatar() {
        return receiverAvatar;
    }

    public void setReceiverAvatar(String receiverAvatar) {
        this.receiverAvatar = receiverAvatar;
    }
}
