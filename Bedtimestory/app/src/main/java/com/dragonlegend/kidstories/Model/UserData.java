package com.dragonlegend.kidstories.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserData {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("first_name")
    @Expose
    private String first_name;
    @SerializedName("last_name")
    @Expose
    private String last_name;
    @SerializedName("is_admin")
    @Expose
    private Integer is_admin;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("location")
    @Expose
    private String location;
    @SerializedName("postal_code")
    @Expose
    private String postal_code;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("token")
    @Expose
    private String token;

    @SerializedName("image_url")
    @Expose
    private String image;

    public UserData(String id, String first_name, String last_name, Integer is_admin, String email, String location, String postal_code, String phone, String token) {
        this.id = id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.is_admin = is_admin;
        this.email = email;
        this.location = location;
        this.postal_code = postal_code;
        this.phone = phone;
        this.token = token;
    }

    public String getId() {
        return id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public Integer getIs_admin() {
        return is_admin;
    }

    public String getEmail() {
        return email;
    }

    public String getLocation() {
        return location;
    }

    public String getPostal_code() {
        return postal_code;
    }

    public String getPhone() {
        return phone;
    }

    public String getToken() {
        return token;
    }

    public String getImage() {
        return image;
    }
}
