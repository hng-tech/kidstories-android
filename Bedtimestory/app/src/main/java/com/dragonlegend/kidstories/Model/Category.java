package com.dragonlegend.kidstories.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Category {

    @SerializedName("id")
    @Expose
    protected int id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("image_name")
    @Expose
    private String imageName;
    @SerializedName("image_url")
    @Expose
    private String imageUrl;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getImageName() {
        return imageName;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
