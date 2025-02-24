package com.example.oruclejava.models;

public class MessageModel {
    private String avatarEncodedImage;
    private String username;
    private String lastMessage;
    private boolean unread;

    public MessageModel(String avatarEncodedImage, String username, String lastMessage, boolean unread) {
        this.avatarEncodedImage = avatarEncodedImage;
        this.username = username;
        this.lastMessage = lastMessage;
        this.unread = unread;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public boolean isUnread() {
        return unread;
    }

    public void setUnread(boolean unread) {
        this.unread = unread;
    }

    public String getAvatarEncodedImage() {
        return avatarEncodedImage;
    }

    public void setAvatarEncodedImage(String avatarEncodedImage) {
        this.avatarEncodedImage = avatarEncodedImage;
    }
}
