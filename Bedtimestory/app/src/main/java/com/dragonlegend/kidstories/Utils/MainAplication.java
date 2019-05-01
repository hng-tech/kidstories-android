package com.dragonlegend.kidstories.Utils;

import android.app.Application;
import android.content.ContextWrapper;
import android.util.Log;
import android.view.View;

import com.dragonlegend.kidstories.Api.ApiInterface;
import com.dragonlegend.kidstories.Api.Client;
import com.dragonlegend.kidstories.Api.Responses.CategoryAllResponse;
import com.dragonlegend.kidstories.Model.Category;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pixplicity.easyprefs.library.Prefs;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainAplication extends Application {

    public static final String BASE_URL = "https://api-kidstories.herokuapp.com/api/v1/";

    private static ApiInterface apiInterface;
    Gson gson = new GsonBuilder()
            .setLenient()
            .create();

    static Retrofit mRetrofit;
    OkHttpClient okHttpClient;


    public static ApiInterface getApiInterface() {
        if (apiInterface == null)
            apiInterface = mRetrofit.create(ApiInterface.class);
        return apiInterface;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        new Prefs.Builder()
                .setContext(this)
                .setMode(ContextWrapper.MODE_PRIVATE)
                .setPrefsName(getPackageName())
                .setUseDefaultSharedPreference(true)
                .build();


        loadData();

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();

        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .addInterceptor(chain -> {
                    Request request = chain.request().newBuilder()
                            .addHeader("Accept", "application/json")
                            .addHeader("token", "Bearer" + Prefs.getString("token", ""))
                            .build();
                    return chain.proceed(request);
                })
                .build();


        mRetrofit =new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();


    }


    private void loadData() {
        Client.getInstance().create(ApiInterface.class).getAllCategories().enqueue(new Callback<CategoryAllResponse>() {
            @Override
            public void onResponse(Call<CategoryAllResponse> call, Response<CategoryAllResponse> response) {
                if(response.isSuccessful()){
                    CategoryAllResponse  categoryList = response.body();
                    List<Category> cl= categoryList.getData();
                    if(cl !=null){

                        for (Category category : cl ) {
                            if (category.getName().equals("Poem") ){
                                Log.d("TAG", "poemResponse:-> " +category.getName());
                                Prefs.putString("poem", category.getName());
                            }else if (category.getName() . equals("Fantasy")){
                                Log.d("TAG", "poemResponse:-> " +category.getName());
                                Prefs.putString("fantasy", category.getName());
                            }else if (category.getName().equals("Moral")){
                                Log.d("TAG", "poemResponse:-> " +category.getName());
                                Prefs.putString("moral", category.getName());
                            }else { }
                        }
                    }
                }
                else { }
            }

            @Override
            public void onFailure(Call<CategoryAllResponse> call, Throwable t) {

            }
        });
    }
}
