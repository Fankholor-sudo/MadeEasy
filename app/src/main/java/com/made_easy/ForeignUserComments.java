package com.made_easy;

public class ForeignUserComments {

    public String getName() {
        return name;
    }
    public String getImgUrl() {
        return imgUrl;
    }
    public String getDate() {
        return date;
    }
    public String getComment() {
        return comment;
    }
    public float getRating() {
        return rating;
    }

    public void setName(String name) {
        this.name = name;
    }
    public void setComment(String comment) {
        this.comment = comment;
    }
    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public void setRating(float rating) {
        this.rating = rating;
    }

    public ForeignUserComments(String name, String comment, String imgUrl, String date, float rating) {
        this.name = name;
        this.comment = comment;
        this.imgUrl = imgUrl;
        this.date = date;
        this.rating = rating;
    }

    public String name;
    public String comment;
    public String imgUrl;
    public String date;
    public float rating;
}
