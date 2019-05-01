package com.dragonlegend.kidstories.Model;

public class UserRegResponse {
    private  String status;
    private String code;
    private String message;
    private UserData data;

    public UserRegResponse(String status, String code, String message, UserData data) {
        this.status = status;
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public UserData getData() {
        return data;
    }
}
