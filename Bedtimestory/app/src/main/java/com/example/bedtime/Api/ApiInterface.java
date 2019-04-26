package com.example.bedtime.Api;

import com.example.bedtime.Api.Responses.BaseResponse;
import com.example.bedtime.Api.Responses.CategoryAllResponse;
import com.example.bedtime.Api.Responses.LoginResponse;
import com.example.bedtime.Api.Responses.StoryAllResponse;
import com.example.bedtime.Api.Responses.StoryCategoryResponse;
import com.example.bedtime.Api.Responses.StoryReactionResponse;
import com.example.bedtime.Api.Responses.StoryResponse;
import com.example.bedtime.Model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiInterface {
    @GET("category/all")
    Call<CategoryAllResponse> getAllCategories();

    @GET("story/category/{id}")
    Call<StoryCategoryResponse> getCategory(@Path("id") String id);

    @GET("story")
    Call<StoryAllResponse> getAllStories();

    @GET("story/{id}")
    Call<StoryResponse> getStory(@Path("id") String id);


    @POST("user/register")
    Call<String> registerUser(@Body User user);

    @GET("story/{action}/{storyId}")
    Call<BaseResponse<StoryReactionResponse>> reactToStory(@Path("action") String action, @Path("storyId") String storyId);

    @POST("user/login")
    @FormUrlEncoded
    Call<LoginResponse> loginUser(@Field("email") String email, @Field("password") String password);

    @GET("user/profile/{id}")
    Call<User> getProfile(@Path("id") String id);
}
