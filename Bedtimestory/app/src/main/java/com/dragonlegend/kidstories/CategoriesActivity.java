package com.dragonlegend.kidstories;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dragonlegend.kidstories.Adapters.CategoriesAdapter;
import com.dragonlegend.kidstories.Api.ApiInterface;
import com.dragonlegend.kidstories.Api.Client;
import com.dragonlegend.kidstories.Api.Responses.CategoryAllResponse;
import com.dragonlegend.kidstories.Model.Category;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoriesActivity extends AppCompatActivity {
    RecyclerView mCategory_rv;
    CategoriesAdapter mAdapter;
    ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);

        Toolbar toolbar = findViewById(R.id.toolbar);
        //customize custom toolbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_primary);
        getSupportActionBar().setElevation(0);

        TextView mTitle =  toolbar.findViewById(R.id.toolbar_title);
        mTitle.setText("Categories");
        mCategory_rv = findViewById(R.id.category_rv);
        mProgressBar = findViewById(R.id.progress_b);
        List<Category> categories = new ArrayList<>();
        mAdapter = new CategoriesAdapter(this, categories,"");
        mCategory_rv.setLayoutManager(new LinearLayoutManager(this));
        mCategory_rv.setAdapter(mAdapter);
        loadData();
    }

    private void loadData() {
        mProgressBar.setVisibility(View.VISIBLE);
        Client.getInstance().create(ApiInterface.class).getAllCategories().enqueue(new Callback<CategoryAllResponse>() {
            @Override
            public void onResponse(Call<CategoryAllResponse> call, Response<CategoryAllResponse> response) {
                if(response.isSuccessful()){
                    CategoryAllResponse  categoryList = response.body();
                    List<Category> cl= categoryList.getData();
//                    Log.d("TAG", "categoryResponse: -> " + categoryList.getData());
//                    for (Category category : cl ) {
//                        if (category.getName().equals("Poem") ){
//                            Log.d("TAG", "poemResponse:-> " +category.getName());
//                        }else if (category.getName() . equals("Fantasy")){
//                            Log.d("TAG", "poemResponse:-> " +category.getName());
//                        }else if (category.getName().equals("Moral")){
//                            Log.d("TAG", "poemResponse:-> " +category.getName());
//                        }else {
//
//                        }
//                    }
                    if(cl !=null){
                        mAdapter.addCategories(cl);
                    }
                    mProgressBar.setVisibility(View.GONE);
                }else {
                    showNetworkError();
                }
            }

            @Override
            public void onFailure(Call<CategoryAllResponse> call, Throwable t) {
                showNetworkError();
                mProgressBar.setVisibility(View.GONE);

            }
        });
    }

    public void showNetworkError(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setMessage("Unable to connect")
                .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        loadData();
                    }
                });
        builder.create().show();

    }
}
