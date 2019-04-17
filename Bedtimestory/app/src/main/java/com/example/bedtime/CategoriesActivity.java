package com.example.bedtime;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.example.bedtime.Adapters.CategoriesAdapter;
import com.example.bedtime.Api.ApiInterface;
import com.example.bedtime.Api.Client;
import com.example.bedtime.Api.Responses.CategoryAllResponse;
import com.example.bedtime.Model.Category;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoriesActivity extends AppCompatActivity {
    RecyclerView mCategory_rv;
    CategoriesAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);

        Toolbar toolbar = findViewById(R.id.toolbar);
        //customize custom toolbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setElevation(0);

        TextView mTitle =  toolbar.findViewById(R.id.toolbar_title);
        mTitle.setText("Categories");

        mCategory_rv = findViewById(R.id.category_rv);
        List<Category> categories = new ArrayList<>();
        mAdapter = new CategoriesAdapter(this, categories,"");
        mCategory_rv.setLayoutManager(new LinearLayoutManager(this));
        mCategory_rv.setAdapter(mAdapter);
        Client.getInstance().create(ApiInterface.class).getAllCategories().enqueue(new Callback<CategoryAllResponse>() {
            @Override
            public void onResponse(Call<CategoryAllResponse> call, Response<CategoryAllResponse> response) {
                if(response.isSuccessful()){
                    CategoryAllResponse  categoryList = response.body();
                    List<Category> cl= categoryList.getData();
                    if(cl !=null){
                        mAdapter.addCategories(cl);
                    }
                }
            }

            @Override
            public void onFailure(Call<CategoryAllResponse> call, Throwable t) {

            }
        });
    }
}
