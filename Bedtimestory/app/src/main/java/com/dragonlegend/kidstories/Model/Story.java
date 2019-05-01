
package com.dragonlegend.kidstories.Model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Story {


    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("body")
    @Expose
    private String body;
    @SerializedName("category_id")
    @Expose
    private Integer categoryId;
    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("image_url")
    @Expose
    private String imageUrl;
    @SerializedName("image_name")
    @Expose
    private String imageName;
    @SerializedName("author")
    @Expose
    private String author;
    @SerializedName("age")
    @Expose
    private String age;
    @SerializedName("story_duration")
    @Expose
    private String storyDuration;
    @SerializedName("is_premium")
    @Expose
    private Integer isPremium;
    @SerializedName("likes_count")
    @Expose
    private Integer likesCount;
    @SerializedName("dislikes_count")
    @Expose
    private Integer dislikesCount;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getStoryDuration() {
        return storyDuration;
    }

    public void setStoryDuration(String storyDuration) {
        this.storyDuration = storyDuration;
    }

    public Integer getIsPremium() {
        return isPremium;
    }

    public void setIsPremium(Integer isPremium) {
        this.isPremium = isPremium;
    }

    public Integer getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(Integer likesCount) {
        this.likesCount = likesCount;
    }

    public Integer getDislikesCount() {
        return dislikesCount;
    }

    public void setDislikesCount(Integer dislikesCount) {
        this.dislikesCount = dislikesCount;
    }
}
