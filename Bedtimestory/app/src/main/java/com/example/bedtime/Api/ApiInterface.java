package com.example.bedtime.Api;

import com.example.bedtime.Api.Responses.CategoryAllResponse;
import com.example.bedtime.Api.Responses.LoginResponse;
import com.example.bedtime.Api.Responses.StoryAllResponse;
import com.example.bedtime.Api.Responses.StoryCategoryResponse;
import com.example.bedtime.Api.Responses.StoryResponse;
import com.example.bedtime.Model.User;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
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

    @POST("user/login")
    @FormUrlEncoded
    Call<LoginResponse> loginUser(@Field("email") String email, @Field("password") String password);

    @GET("user/profile/{id}")
    Call<User> getProfile(@Path("id") String id);

    @Multipart
    @POST("story/create")
    Call<ResponseBody> addStory(
            @Part("title") RequestBody title,
            @Part("story") RequestBody story,
            @Part MultipartBody.Part image
    );
}
