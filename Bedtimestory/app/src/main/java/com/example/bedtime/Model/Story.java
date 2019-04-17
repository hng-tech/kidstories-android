package com.example.bedtime.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Story {
    @SerializedName("cat_id")
    @Expose
    private List<Category> catId = null;
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("release_date")
    @Expose
    private String releaseDate;
    @SerializedName("likes")
    @Expose
    private List<Object> likes = null;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("story")
    @Expose
    private String story;
    @SerializedName("designation")
    @Expose
    private String designation;
    @SerializedName("author")
    @Expose
    private String author;
    @SerializedName("imageId")
    @Expose
    private String imageId;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("__v")
    @Expose
    private Integer v;

    public List<Category> getCatId() {
        return catId;
    }

    public void setCatId(List<Category> catId) {
        this.catId = catId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public List<Object> getLikes() {
        return likes;
    }

    public void setLikes(List<Object> likes) {
        this.likes = likes;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStory() {
        return story;
    }

    public void setStory(String story) {
        this.story = story;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Integer getV() {
        return v;
    }

    public void setV(Integer v) {
        this.v = v;
    }
}
