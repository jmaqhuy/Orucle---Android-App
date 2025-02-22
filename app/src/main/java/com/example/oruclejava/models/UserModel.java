package com.example.oruclejava.models;

public class UserModel {
    private String userId;
    private String encodedAvatar;
    private String username;
    private boolean isFollowing;

    public UserModel(String userId, String encodedAvatar, String username, boolean isFollowing) {
        this.userId = userId;
        this.encodedAvatar = encodedAvatar;
        this.username = username;
        this.isFollowing = isFollowing;
    }

    public boolean isFollowing() {
        return isFollowing;
    }

    public void setFollowing(boolean following) {
        isFollowing = following;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEncodedAvatar() {
        return encodedAvatar;
    }

    public void setEncodedAvatar(String encodedAvatar) {
        this.encodedAvatar = encodedAvatar;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
