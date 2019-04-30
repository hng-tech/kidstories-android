package com.dragonlegend.kidstories;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.dragonlegend.kidstories.Api.ApiInterface;
import com.dragonlegend.kidstories.Api.Client;
import com.dragonlegend.kidstories.Api.Responses.StoryResponse;
import com.dragonlegend.kidstories.Model.Story;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StoryDetail extends AppCompatActivity implements View.OnClickListener {
    public static final String STORY_ID = "story_id";
    ImageView mStoryImage;
    TextView mTitle, mDetail;
    ImageButton mBookmark,mCommentSend;
    EditText mCommentfield;
    Button mAddComment;
    ProgressBar mProgressBar;
    LinearLayout mLinearLayout,mCommentLayout;
    InputMethodManager imm;
    ScrollView mScrollView;
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


        imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

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
        mCommentfield = findViewById(R.id.comment_box);
        mCommentSend = findViewById(R.id.comment_send);
        mCommentLayout = findViewById(R.id.comment_layout);
        mScrollView = findViewById(R.id.detail_scroll);
        mScrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                imm.hideSoftInputFromWindow(mCommentfield.getWindowToken(), 0);
                if(mScrollView.getScrollY() == 0){
                    mCommentLayout.setVisibility(View.GONE);
                }
            }
        });
        //Set onClickListeners on Views with actions
        mAddComment.setOnClickListener(this);
        mBookmark.setOnClickListener(this);
        mCommentSend.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.add_comment:
                mCommentLayout.setVisibility(View.VISIBLE);
                mCommentfield.requestFocus();
                imm.showSoftInput(mCommentfield, InputMethodManager.SHOW_IMPLICIT);
                break;
            case R.id.comment_send:
                Toast.makeText(this,mCommentfield.getText().toString().trim(),Toast.LENGTH_LONG).show();
                break;
            case R.id.bookmark_button:
                Toast.makeText(this,"bookmark clicked",Toast.LENGTH_LONG).show();
            default:
               break;
        }
    }
}
