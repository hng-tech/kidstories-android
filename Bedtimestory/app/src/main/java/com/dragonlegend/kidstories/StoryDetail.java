package com.dragonlegend.kidstories;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.dragonlegend.kidstories.Api.ApiInterface;
import com.dragonlegend.kidstories.Api.Client;
import com.dragonlegend.kidstories.Api.Responses.CommentResponse;
import com.dragonlegend.kidstories.Api.Responses.StoryResponse;
import com.dragonlegend.kidstories.Model.Story;
import com.pixplicity.easyprefs.library.Prefs;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.support.design.widget.Snackbar.LENGTH_LONG;
import static android.support.design.widget.Snackbar.make;

public class StoryDetail extends AppCompatActivity implements View.OnClickListener {
public class StoryDetail extends AppCompatActivity {

    public static final String STORY_ID = "story_id";
    ImageView mStoryImage;
    TextView mTitle, mDetail,mPost,mStoryAge;
    ImageButton mBookmark;
    EditText mAddComment;
    ProgressBar mProgressBar;
    LinearLayout mLinearLayout,commentLayout;
    String comments,title, content, image;

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

//        if (getIntent().getExtras().getString("type")!= null && getIntent().getExtras().getString("type").equals("fav")){
//            mTitle.setText(getIntent().getExtras().getString("title"));
//            Glide.with(this).load(getIntent().getExtras().getString("image")).into(mStoryImage);
//            mDetail.setText(getIntent().getExtras().getString("content"));
//            mBookmark.setVisibility(View.INVISIBLE);
//
//        }else {
            Client.getInstance().create(ApiInterface.class).getStory(Prefs.getInt("story_id", 0))
                    .enqueue(new Callback<StoryResponse>() {
                        @Override
                        public void onResponse(Call<StoryResponse> call, Response<StoryResponse> response) {
                            Log.d("TAG", "detailsResponse: -> " +response.message());
                            if(response.isSuccessful()){
                                Story story = response.body().getData();
                                title = story.getTitle();
                                content = story.getBody();
                                image = story.getImageUrl();
                                if(image != null) {
                                    Glide.with(StoryDetail.this)
                                            .load(image)
                                            .into(mStoryImage);
                                }
                                mTitle.setText(title);
                                mDetail.setText(content);
                                mStoryAge.setText("For Kids " +story.getAge() +" years");
                            }
                            else {
                                validate("We are having trouble fetching the story, please try again");
                            }

                            mProgressBar.setVisibility(View.GONE);
                            mLinearLayout.setVisibility(View.VISIBLE);

                        }

                        @Override
                        public void onFailure(Call<StoryResponse> call, Throwable t) {
                                t.toString();
                            Log.d("TAG", "onFailure: -> "+t.getMessage());
                            if (t.getMessage() == "timeout") {
                                validate("We are having trouble fetching the story, please try again");
                            }
                        }
                    });

        Client.getInstance().create(ApiInterface.class).getStory(getIntent().getStringExtra(STORY_ID))
                .enqueue(new Callback<StoryResponse>() {
                    @Override
                    public void onResponse(Call<StoryResponse> call, Response<StoryResponse> response) {
                        if(response.isSuccessful()){
                            Story story = response.body().getData();
                            title=story.getTitle();
                            content=story.getBody();
                            image=story.getImageUrl();
                            if(image!=null) {
                                Glide.with(StoryDetail.this)
                                        .load(image)
                                        .into(mStoryImage);
                            }
                            mTitle.setText(title);
                            mDetail.setText(content);
                            mStoryAge.setText("for kids"+story+"years");
                       }
                        mProgressBar.setVisibility(View.GONE);
                        mLinearLayout.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onFailure(Call<StoryResponse> call, Throwable t) {

                    }
                });


//    }

        mAddComment.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus){
                    commentLayout.setBackgroundResource(R.drawable.comment_bg);
                    mPost.setVisibility(View.VISIBLE);
                }

            }
        });
        mPost.setOnClickListener(view -> {
            //post the comment
            comments  = mAddComment.getText().toString().trim();
            addComment(comments);
        });

    }

    public void initViews(){
        mStoryImage = findViewById(R.id.detail_image);
        commentLayout=findViewById(R.id.commentLayout);
        mTitle = findViewById(R.id.detail_title);
        mPost = findViewById(R.id.post);
        mDetail = findViewById(R.id.story);
        mBookmark = findViewById(R.id.bookmark_button);
        mAddComment = findViewById(R.id.add_comment);
       // mProgressBar = findViewById(R.id.progress);
        mLinearLayout = findViewById(R.id.story_ll);
    }

    //addcomment
    private void addComment(String comment){
        Client.getInstance().create(ApiInterface.class).addComment(getIntent().getStringExtra(STORY_ID), comment)
                .enqueue(new Callback<CommentResponse>() {
                    @Override
                    public void onResponse(Call<CommentResponse> call, Response<CommentResponse> response) {
                        if (response.isSuccessful()){
                            Toast.makeText(StoryDetail.this, "Successful upload", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(StoryDetail.this, "Failed", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<CommentResponse> call, Throwable t) {
                        Toast.makeText(StoryDetail.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }


    public void validate(String message) {
        Snackbar snackbar = make(findViewById(android.R.id.content), message, LENGTH_LONG);
// get snackbar view
        View mView = snackbar.getView();
        TextView mTextView = (TextView) mView.findViewById(android.support.design.R.id.snackbar_text);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1)
            mTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        else
            mTextView.setGravity(Gravity.CENTER_HORIZONTAL);
        snackbar.setAction("OK", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recreate();
            }
        });
// show the snackbar
        snackbar.show();
    }

}
