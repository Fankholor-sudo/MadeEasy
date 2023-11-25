package com.made_easy;

public class UserCustomer implements Comparable<UserCustomer>
{
    public String username;
    public String email;
    public String phoneNumber;
    public String city;
    public String imgUrl;
    public float rating;
    public int low_rating;
    public int high_rating;

    public UserCustomer(String username, String email, String phoneNumber, String city, String imgUr, float rating, int low_rating , int high_rating) {
        this.username = username;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.city = city;
        this.imgUrl = imgUr;
        this.rating = rating;
        this.low_rating = low_rating;
        this.high_rating = high_rating;
    }

    @Override
    public int compareTo(UserCustomer other) {
        return this.getUsername().compareTo(other.getUsername());
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
    }

    public String getImgUrl() {
        return imgUrl;
    }
    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public float getRating() {
        return rating;
    }
    public void setRating(float rating) {
        this.rating = rating;
    }

    public int getLow_rating() {
        return low_rating;
    }
    public void setLow_rating(int low_rating) {
        this.low_rating = low_rating;
    }

    public int getHigh_rating() {
        return high_rating;
    }
    public void setHigh_rating(int high_rating) {
        this.high_rating = high_rating;
    }

}
