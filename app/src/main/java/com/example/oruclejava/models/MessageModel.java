package com.example.oruclejava.models;

public class MessageModel {
    private int avatarId;
    private String username;
    private String lastMessage;
    private boolean unread;

    public MessageModel(int avatarId, String username, String lastMessage, boolean unread) {
        this.avatarId = avatarId;
        this.username = username;
        this.lastMessage = lastMessage;
        this.unread = unread;
    }

    public int getAvatarId() {
        return avatarId;
    }

    public void setAvatarId(int avatarId) {
        this.avatarId = avatarId;
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
}
