package com.dragonlegend.kidstories;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.dragonlegend.kidstories.Adapters.StoryListingAdapter;
import com.dragonlegend.kidstories.Api.ApiInterface;
import com.dragonlegend.kidstories.Api.Client;
import com.dragonlegend.kidstories.Api.Responses.StoryCategoryResponse;
import com.dragonlegend.kidstories.Model.Story;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StoryListingActivity extends AppCompatActivity {
    public static final String CATEGORY_ID = "category_id";
    public static final String CATEGORY_NAME = "category_name";
    RecyclerView mStoriesRv;
    StoryListingAdapter mAdapter;
    List<Story> mStories;
    String mCatId;
    String mCatName;

    TextView mNoStories;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_listing);
        Toolbar toolbar = findViewById(R.id.toolbar);
        //customize custom toolbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setElevation(1);
        TextView toolbar_title = toolbar.findViewById(R.id.toolbar_title);
        Intent intent = getIntent();
        if(intent.hasExtra(CATEGORY_ID) ){
            mCatId = intent.getStringExtra(CATEGORY_ID);
            mCatName = intent.getStringExtra(CATEGORY_NAME);
            toolbar_title.setText(mCatName);

        }

        mNoStories = findViewById(R.id.no_stories);
        mStories = new ArrayList<>(); //create empty lis of stories
        mAdapter = new StoryListingAdapter(this,mStories);
        mStoriesRv = findViewById(R.id.stories_rv);
        mStoriesRv.setLayoutManager(new LinearLayoutManager(this));
        mStoriesRv.setAdapter(mAdapter);

        if(mCatId !=null){
            loadStories();
        }
    }

    private void loadStories(){
        Client.getInstance().create(ApiInterface.class).getCategory(mCatId).enqueue(new Callback<StoryCategoryResponse>() {
            @Override
            public void onResponse(Call<StoryCategoryResponse> call, Response<StoryCategoryResponse> response) {
                if(response.isSuccessful()){
                    List<Story> stories= response.body().getData().getStories();
                    mAdapter.addStories(stories);
                    if(stories.size()==0){
                        mNoStories.setVisibility(View.VISIBLE);
                    }

                }
            }

            @Override
            public void onFailure(Call<StoryCategoryResponse> call, Throwable t) {
                t.toString();
            }
        });
    }
}
