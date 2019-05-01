
package com.dragonlegend.kidstories.Api.Responses;

import com.dragonlegend.kidstories.Model.Story;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StoryResponse {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("method")
    @Expose
    private String method;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private Story data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
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

    public Story getData() {
        return data;
    }

    public void setData(Story data) {
        this.data = data;
    }



}
