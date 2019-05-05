package com.dragonlegend.kidstories.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Comment {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("story_id")
    @Expose
    private Integer storyId;
    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("body")
    @Expose
    private String body;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("user")
    @Expose
    private User user;

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Integer getId() {
        return id;
    }

    public Integer getStoryId() {
        return storyId;
    }

    public Integer getUserId() {
        return userId;
    }

    public String getBody() {
        return body;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public User getUser() {
        return user;
    }
}
