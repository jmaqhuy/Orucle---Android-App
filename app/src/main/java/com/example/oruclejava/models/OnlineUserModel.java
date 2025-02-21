package com.example.oruclejava.models;

public class OnlineUserModel {
    private int avatarId;
    private boolean onlineStatus;

    public OnlineUserModel(int avatarId, boolean onlineStatus) {
        this.avatarId = avatarId;
        this.onlineStatus = onlineStatus;
    }

    public int getAvatarId() {
        return avatarId;
    }

    public void setAvatarId(int avatarId) {
        this.avatarId = avatarId;
    }

    public boolean isOnlineStatus() {
        return onlineStatus;
    }

    public void setOnlineStatus(boolean onlineStatus) {
        this.onlineStatus = onlineStatus;
    }
}
