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
                                  final Context context, String category,
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

        RequestBody body =
                RequestBody.create(
                        okhttp3.MultipartBody.FORM, storyBody);

        RequestBody category_id =
                RequestBody.create(
                        okhttp3.MultipartBody.FORM, category);
//
        RequestBody age =
                RequestBody.create(
                        okhttp3.MultipartBody.FORM, user_age );

        RequestBody author =
                RequestBody.create(
                        okhttp3.MultipartBody.FORM, story_author);


//        RequestBody stroy_duration =
//                RequestBody.create(
//                        okhttp3.MultipartBody.FORM, duration);

//        RequestBody category_id =
//                RequestBody.create(
//                        okhttp3.MultipartBody.FORM, category);


        RequestBody is_premium =
                RequestBody.create(
                        okhttp3.MultipartBody.FORM, premium);



        String token = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImp0aSI6IjMyYWVlM2FhOGEzYThhOTYwZWM2MThkZDZiODU0M2M1ZTg2ZjczNTc2MGZlMDU0NTdkNzhiZDdiMWViZjAyODIwZjI5ZmUwZjE4ZGY5ZmVhIn0.eyJhdWQiOiIxIiwianRpIjoiMzJhZWUzYWE4YTNhOGE5NjBlYzYxOGRkNmI4NTQzYzVlODZmNzM1NzYwZmUwNTQ1N2Q3OGJkN2IxZWJmMDI4MjBmMjlmZTBmMThkZjlmZWEiLCJpYXQiOjE1NTY0ODU5NjMsIm5iZiI6MTU1NjQ4NTk2MywiZXhwIjoxNTg4MTA4MzYyLCJzdWIiOiIyIiwic2NvcGVzIjpbXX0.FOH-NJ_k4CHsdyWk4dxRw3iB0sRuBGFQnrBzqfuDcvPdBGZCojU8y0XyyLzSj8MFh7TWzPZBbiDjL2x6pLe27VuYtotdfGvn4HPLA9jLDFk3ozMh20Uea_Ttj6ich6-_h9o9KjU3zzb58uoQ6Ev9XqIHGRZ7z4eRX22HyVownacueT1zFTnzoUUIAlLryS_CUzjq4qdoSBhHYsjl3MorLNkQRVQuKaWG2gQ5mEOVS92a5wfLiqagWhTQGdw1X-7B4JJaDx0edV6r8OdEcEXBnfzcaGeYIPDeAQ6YESHpNy8SABWSXkFzCQIqIK7lRw1jIGGS6bmpBYaW0SI5VDknG35j0cJ-svVrk6-AAikQVk1AgrkILND3gg30-2jWrhqRHh3aXAHvXDP2smHSknfj0w_xcl1UcWZwCfn3csN77J2cv1p9rc3nAnrAxIazDMFIMd0Hal2P0LYUrxC0BmQ6xYQTYhKPe7FIm7DnVhnkoa1iMjJ3TsZxb_efbQJO6I4270So_k_r3-Q61l1ZPFb9AgOqtA_odC9uguEVoGdwgPzeNGTyRpXt7Ajj0IhsG8HCelxNMMz7oeKIpz6K8lMHTqIAgNI6MaG-Rs0v1wPn63mIaYpPdZEbFTwPfJMjbFLCxn6YsEB8gRS6hbAA4HK4trI7gAWCjE1iKAsPHNXHmAU";

                //Prefs.getString("token", "");




        // finally, execute the request
        Client.getInstance().create(ApiInterface.class).addStory(
                token,title,body,category_id,photo,age,author,is_premium).
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

    public static void uploadProfilePic(String userToken,String profilePath){
        Log.e("TAG",profilePath);
        File profPic = new File(Uri.decode(profilePath));

        RequestBody requestFile =
                RequestBody.create(MediaType.parse("image/*"),profPic);
        MultipartBody.Part photo =
                MultipartBody.Part.createFormData("photo", profPic.getName(), requestFile);

        //String userToken = Prefs.getString("token", "");
        //Log.e("TAG 2",userToken);

        Client.getInstance().create(ApiInterface.class).uploadProfilPic(userToken,photo).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful())
                    Log.e("TAG","Image upload successful");
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("TAG",t.getMessage());
            }
        });
    }

}
