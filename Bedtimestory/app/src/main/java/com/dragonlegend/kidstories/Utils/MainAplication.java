package com.dragonlegend.kidstories.Utils;

import android.app.Application;
import android.content.ContextWrapper;
import android.util.Log;
import android.view.View;

import com.dragonlegend.kidstories.Api.ApiInterface;
import com.dragonlegend.kidstories.Api.Client;
import com.dragonlegend.kidstories.Api.Responses.CategoryAllResponse;
import com.dragonlegend.kidstories.Model.Category;
import com.pixplicity.easyprefs.library.Prefs;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainAplication extends Application {
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
