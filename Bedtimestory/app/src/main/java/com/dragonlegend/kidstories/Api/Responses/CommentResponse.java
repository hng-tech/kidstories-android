package com.dragonlegend.kidstories.Api.Responses;

import com.google.gson.annotations.SerializedName;

public class CommentResponse {
    @SerializedName("comment")
    private String comment;
    @SerializedName("comment")
    private String publisher;
    @SerializedName("comment")
    private String userImage;


    public CommentResponse(String comment, String publisher, String userImage) {
        this.comment = comment;
        this.publisher = publisher;
        this.userImage = userImage;
    }
    public CommentResponse() {

    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }
}

