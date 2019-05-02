package com.dragonlegend.kidstories.Api;

import com.dragonlegend.kidstories.Api.Responses.BaseResponse;
import com.dragonlegend.kidstories.Api.Responses.CategoryAllResponse;
import com.dragonlegend.kidstories.Api.Responses.CategoryResponse;
import com.dragonlegend.kidstories.Api.Responses.CommentResponse
import com.dragonlegend.kidstories.Api.Responses.LoginResponse;
import com.dragonlegend.kidstories.Api.Responses.RegistrationResponse;
import com.dragonlegend.kidstories.Api.Responses.StoryAllResponse;
import com.dragonlegend.kidstories.Api.Responses.StoryCategoryResponse;
import com.dragonlegend.kidstories.Api.Responses.StoryReactionResponse;
import com.dragonlegend.kidstories.Api.Responses.StoryResponse;
import com.dragonlegend.kidstories.Model.User;
import com.dragonlegend.kidstories.Model.UserReg;
import com.dragonlegend.kidstories.Model.UserRegResponse;

import org.w3c.dom.Comment;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {
    @GET("categories")
    Call<CategoryAllResponse> getAllCategories();


    @GET("categories/{id}/stories")
    Call<BaseResponse<CategoryResponse>> getCategory(@Path("id") String id);

    @GET("stories/category/{id}")
    Call<StoryCategoryResponse> getCategory(@Path("id") String id);

    @GET("stories")
    Call<StoryAllResponse> getAllStories();

    @GET("stories/{id}")

    Call<StoryResponse> getStory(@Path("id") Integer id);



    Call<StoryResponse> getStory(@Path("id") String id);



    @FormUrlEncoded
    @POST("auth/register")
    Call<BaseResponse<RegistrationResponse>> registerUser(@Field("phone") String phone,
                                @Field("email") String email,
                                @Field("password") String passwprd,
                                @Field("first_name") String first_name,
                                @Field("last_name") String last_name);


    @POST("auth/login")
    @FormUrlEncoded
    Call<LoginResponse> loginUser(@Header("Authorization") String token,
            @Field("email") String email, @Field("password") String password);

    @GET("users/profile")
    Call<LoginResponse> getProfile(@Header("Authorization") String token);


    @POST("stories/{storyId}/reactions/{action}")
    Call<BaseResponse<StoryReactionResponse>> reactToStory(@Path("action") String action, @Path("storyId") String storyId);


    @Multipart
    @POST("stories")
    Call<ResponseBody> addStory(
            @Header("Authorization") String token,
            @Part("title") RequestBody title,
            @Part("body") RequestBody body,
            @Part("category_id") Integer category_id,
            @Part("age") String age,
            @Part("author") RequestBody author,
            @Part("story_duration") RequestBody story_duration,
            @Part MultipartBody.Part story_image
    );

//    @POST("comments")
//    Call<CommentResponse> addComment(@Path("id") String id,
//                                     @Field("body") String comment);
    @POST("comments")
            Call<CommentResponse>addComment(@Path("body") String body,
            @Path("story_id") String storyid);
}
