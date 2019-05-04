package com.dragonlegend.kidstories.Api.Responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BookmarkResponse {
    @SerializedName("code")
    @Expose
    private int code;

    @SerializedName("message")
    @Expose
    private String message;

    public BookmarkResponse(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
