package com.example.oruclejava.models;

public class PostModel {
    private int avatar;
    private String username;
    private String content;
    private int imageId;
    private int likeCount;
    private int commentCount;

    public PostModel(int avatar, String username, String content, int imageId, int likeCount, int commentCount) {
        this.avatar = avatar;
        this.username = username;
        this.content = content;
        this.imageId = imageId;
        this.likeCount = likeCount;
        this.commentCount = commentCount;
    }
    public PostModel(){}

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public int getAvatar() {
        return avatar;
    }

    public void setAvatar(int avatar) {
        this.avatar = avatar;
    }
}
