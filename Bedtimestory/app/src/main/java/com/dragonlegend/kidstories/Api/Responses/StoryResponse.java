
package com.dragonlegend.kidstories.Api.Responses;

import com.dragonlegend.kidstories.Model.Story;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StoryResponse {

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

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }


    public class Data {

        @SerializedName("story")
        @Expose
        private Story story;

        public Story getStory() {
            return story;
        }

        public void setStory(Story story) {
            this.story = story;
        }

    }


}
