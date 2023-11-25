package com.made_easy;

public class UsersRatings {
    public String username;
    public String date;
    public String comment;
    public String imgUrl;

    public UsersRatings(String username, String date, String comment, String imgUrl) {
        this.username = username;
        this.date = date;
        this.comment = comment;
        this.imgUrl = imgUrl;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getUsername() {
        return username;
    }

    public String getDate() {
        return date;
    }

    public String getComment() {
        return comment;
    }

    public String getImgUrl() {
        return imgUrl;
    }
}
