package com.dragonlegend.kidstories.Api.Responses;

import com.dragonlegend.kidstories.Model.UserData;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginResponse {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private UserData data;

    public String getStatus() {
        return status;
    }

    public String getMethod() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public UserData getData() {
        return data;
    }
}
