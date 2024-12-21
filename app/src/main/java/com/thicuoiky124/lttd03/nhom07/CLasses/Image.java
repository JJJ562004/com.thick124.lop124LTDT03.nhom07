package com.thicuoiky124.lttd03.nhom07.CLasses;

import java.util.List;

public class Image {
    private String imageUrl; // Change to URL for dynamic images
    private String title;
    private int likes;
    private List<Comment> comments;
    private Boolean isFavourite;

    public Image() {
        // Default constructor for Firebase deserialization
    }

    public Image(String imageUrl, String title, int likes, List<Comment> comments, boolean isFavourite) {
        this.imageUrl = imageUrl;
        this.title = title;
        this.likes = likes;
        this.comments = comments;
        this.isFavourite = isFavourite;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public Boolean getIsFavourite() {
        return isFavourite;
    }

    public void setIsFavourite(Boolean isFavourite) {
        this.isFavourite = isFavourite;
    }
}
