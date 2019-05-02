package com.dragonlegend.kidstories.Utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.dragonlegend.kidstories.Api.ApiInterface;
import com.dragonlegend.kidstories.Api.Client;
import com.dragonlegend.kidstories.Home;
import com.pixplicity.easyprefs.library.Prefs;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.support.constraint.Constraints.TAG;

public class UploadImage {

    public static final String BASE_URL = "https://dragon-legend-5.herokuapp.com/api/v1/";

    public static void uploadFile(String fileUri, String name, String storyTitle, String storyBody,
                                  final Context context, int category_id,
                                  String  user_age, String duration, String story_author, String premium) {

        // use the FileUtils to get the actual imageFile by uri
        Uri uri = Uri.parse(fileUri);
        File imageFile = new File(Uri.decode(fileUri));

        // create RequestBody instance from imageFile


//        Uri ImageUri = Uri.parse(fileUri);
        RequestBody requestFile =
                RequestBody.create(MediaType.parse("image/*"),imageFile);

        // MultipartBody.Part is used to send also the actual imageFile name
        MultipartBody.Part photo =
                MultipartBody.Part.createFormData(name, imageFile.getName(), requestFile);

        // add another part within the multipart request
//        String storyTitle = tilte;
//        String storyBody = story;
        RequestBody title =
                RequestBody.create(
                        okhttp3.MultipartBody.FORM, storyTitle);

        RequestBody story =
                RequestBody.create(
                        okhttp3.MultipartBody.FORM, storyBody);

//        RequestBody category_id =
//                RequestBody.create(
//                        okhttp3.MultipartBody.FORM, storyCategory);
//
        RequestBody age =
                RequestBody.create(
                        okhttp3.MultipartBody.FORM, user_age );

        RequestBody author =
                RequestBody.create(
                        okhttp3.MultipartBody.FORM, story_author);


        RequestBody stroy_duration =
                RequestBody.create(
                        okhttp3.MultipartBody.FORM, duration);

//        RequestBody category_id =
//                RequestBody.create(
//                        okhttp3.MultipartBody.FORM, category);


        RequestBody is_premium =
                RequestBody.create(
                        okhttp3.MultipartBody.FORM, premium);



        String token = Prefs.getString("token", "");




        // finally, execute the request
        Client.getInstance().create(ApiInterface.class).addStory(
                token,title,story,category_id,photo,age,author,is_premium).
                enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.d(TAG, "onResponse: ->" + "Bearer " +token);
                if (response.isSuccessful()){

                    Log.d(TAG, "onResponse: " + response.code());
                    showMessage(context, "Volla!!!!! story created");
                    Intent intent = new Intent(context, Home.class);
                    context.startActivity(intent);

                }else {
                    Log.d(TAG, "onResponse: " + response.message());
                    showMessage(context, "Error Occur Please check your internet");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    public static void showMessage(final Context context, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setMessage(message);
                 builder.create().show();

    }

}
