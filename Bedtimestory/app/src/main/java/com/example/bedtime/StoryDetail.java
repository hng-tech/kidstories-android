package com.example.bedtime;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.bedtime.Api.ApiInterface;
import com.example.bedtime.Api.Client;
import com.example.bedtime.Api.Responses.StoryResponse;
import com.example.bedtime.Model.Story;
import com.example.bedtime.Model.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StoryDetail extends AppCompatActivity {
    public static final String STORY_ID = "story_id";
    ImageView mStoryImage;
    TextView mTitle, mDetail;
    ImageButton mBookmark;
    Button mAddComment;
    ProgressBar mProgressBar;
    LinearLayout mLinearLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_detail);

        Toolbar toolbar = findViewById(R.id.toolbar);
        //customize custom toolbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_primary);
        getSupportActionBar().setElevation(0);

        initViews();
        Client.getInstance().create(ApiInterface.class).getStory(getIntent().getStringExtra(STORY_ID))
                .enqueue(new Callback<StoryResponse>() {
                    @Override
                    public void onResponse(Call<StoryResponse> call, Response<StoryResponse> response) {
                        if(response.isSuccessful()){
                            Story story = response.body().getData().getStory();
                            Glide.with(StoryDetail.this)
                                    .load(story.getImage())
                                    .into(mStoryImage);
                            mTitle.setText(story.getTitle());
                            mDetail.setText(story.getStory());
                        }
                        mProgressBar.setVisibility(View.GONE);
                        mLinearLayout.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onFailure(Call<StoryResponse> call, Throwable t) {

                    }
                });
    }

    public void initViews(){
        mStoryImage = findViewById(R.id.detail_image);
        mTitle = findViewById(R.id.detail_title);
        mDetail = findViewById(R.id.story);
        mBookmark = findViewById(R.id.bookmark_button);
        mAddComment = findViewById(R.id.add_comment);
        mProgressBar = findViewById(R.id.progress);
        mLinearLayout = findViewById(R.id.story_ll);
    }
}
