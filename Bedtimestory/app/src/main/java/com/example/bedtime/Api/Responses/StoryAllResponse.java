package com.example.bedtime.Api.Responses;

import com.example.bedtime.Model.Story;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class StoryAllResponse {
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("method")
    @Expose
    private String method;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private Data data;

    public Integer getStatus() {
        return status;
    }

    public String getMethod() {
        return method;
    }

    public String getMessage() {
        return message;
    }

    public Data getData() {
        return data;
    }

    public class Data {

        @SerializedName("stories")
        @Expose
        private List<Story> stories = null;

        public List<Story> getStories() {
            return stories;
        }

        public void setStories(List<Story> stories) {
            this.stories = stories;
        }
    }
}
