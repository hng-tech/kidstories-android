package com.dragonlegend.kidstories;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
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
import com.dragonlegend.kidstories.Adapters.CommentAdapter;
import com.dragonlegend.kidstories.Adapters.StoryListingAdapter;
import com.dragonlegend.kidstories.Api.ApiInterface;
import com.dragonlegend.kidstories.Api.Client;
import com.dragonlegend.kidstories.Api.Responses.BaseResponse;
import com.dragonlegend.kidstories.Api.Responses.BookmarkResponse;
import com.dragonlegend.kidstories.Api.Responses.StoryReactionResponse;
import com.dragonlegend.kidstories.Api.Responses.StoryResponse;
import com.dragonlegend.kidstories.Database.Contracts.FavoriteContract;
import com.dragonlegend.kidstories.Database.Helper.BedTimeDbHelper;
import com.dragonlegend.kidstories.Model.Comment;
import com.dragonlegend.kidstories.Model.Story;
import com.dragonlegend.kidstories.Utils.MainAplication;
import com.pixplicity.easyprefs.library.Prefs;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.support.design.widget.Snackbar.LENGTH_LONG;
import static android.support.design.widget.Snackbar.make;

public class StoryDetail extends AppCompatActivity implements View.OnClickListener {
    public static final String STORY_ID = "story_id";
    ImageView mStoryImage;
    TextView mTitle, mDetail,mStoryAge,mPremiumMessage, likes, dislikes;
    ImageButton mBookmark,mCommentSend, likeButton, dislikeButton;
    EditText mCommentField;
    Button mAddComment;
    String title, content, image;
    ProgressBar mProgressBar;
    BedTimeDbHelper helper;
    Long date;
    Cursor c;
    LinearLayout mLinearLayout,mCommentLayout;
    InputMethodManager imm;
    ScrollView mScrollView;
    CommentAdapter mCommentAdapter;
    RecyclerView mCommentRv;
    List<Comment> mComments = new ArrayList<>();
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

        //add db helper
        helper = new BedTimeDbHelper(this);

        mCommentAdapter = new CommentAdapter(this,mComments);

        imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);


        initViews();
        if (getIntent().getExtras().getString("type")!= null && getIntent().getExtras().getString("type").equals("fav")){
            mTitle.setText(getIntent().getExtras().getString("title"));
            Glide.with(this).load(getIntent().getExtras().getString("image")).into(mStoryImage);
            mDetail.setText(getIntent().getExtras().getString("content"));
            mBookmark.setVisibility(View.INVISIBLE);
            mScrollView.setVisibility(View.VISIBLE);

        }else {
            Client.getInstance().create(ApiInterface.class).getStory(Prefs.getInt("story_id", 0))
                    .enqueue(new Callback<StoryResponse>() {
                        @Override
                        public void onResponse(Call<StoryResponse> call, Response<StoryResponse> response) {
                            Log.d("TAG", "detailsResponse: -> " +response.message());
                            if(response.isSuccessful()){

                                Story story = response.body().getData();
                                title = story.getTitle();
                                content = story.getBody();
                                likes.setText(String.valueOf(story.getLikesCount()));
                                dislikes.setText(String.valueOf(story.getDislikesCount()));
                                image = story.getImageUrl();
                                if(image != null) {
                                    Glide.with(StoryDetail.this)
                                            .load(image)
                                            .into(mStoryImage);
                                }
                                mTitle.setText(title);
                                mDetail.setText(content);
                                mStoryAge.setText("For Kids " +story.getAge() +" years");
                                mScrollView.setVisibility(View.VISIBLE);
//                                mCommentAdapter.setComment(story.getComments().getComments());


                            }else if(response.code() !=200){
                                mScrollView.setVisibility(View.GONE);
                                mPremiumMessage.setVisibility(View.VISIBLE);
                            }
                            else {
                                validate("We are having trouble fetching the story, please try again");
                            }

                            mProgressBar.setVisibility(View.GONE);

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

        }


    }


    private void addFavorite(String title, String story, String image, String time) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues value = new ContentValues();
        value.put(FavoriteContract.FavoriteColumn.COLUMN_TITLE, title);
        value.put(FavoriteContract.FavoriteColumn.COLUMN_CONTENT, story);
        value.put(FavoriteContract.FavoriteColumn.COLUMN_TIME, time);
        value.put(FavoriteContract.FavoriteColumn.COLUMN_IMAGE, image);

        long idRow = db.insert(FavoriteContract.FavoriteColumn.TABLE_NAME, null, value);
        Log.v("IdRow", "Id Count" + idRow);

        Toast.makeText(this, "Successfully Added", Toast.LENGTH_SHORT).show();


    }

    public void initViews(){
        mStoryImage = findViewById(R.id.detail_image);
        mTitle = findViewById(R.id.detail_title);
        mStoryAge = findViewById(R.id.story_age_range);
        mDetail = findViewById(R.id.story);
        mBookmark = findViewById(R.id.bookmark_button);
        mPremiumMessage = findViewById(R.id.premium_message);
        mAddComment = findViewById(R.id.add_comment);
        mProgressBar = findViewById(R.id.progress);
        likes = findViewById(R.id.likes);
        dislikes = findViewById(R.id.dislikes);
        likeButton = findViewById(R.id.like_button);
        dislikeButton = findViewById(R.id.dislike_button);
        mLinearLayout = findViewById(R.id.story_ll);
        mCommentField = findViewById(R.id.comment_box);
        mCommentSend = findViewById(R.id.comment_send);
        mCommentLayout = findViewById(R.id.comment_layout);
        mScrollView = findViewById(R.id.detail_scroll);
        mCommentRv  = findViewById(R.id.comment_rv);

        String reaction = Prefs.getString("reactionStatus", "nil");
        Toast.makeText(this, reaction, Toast.LENGTH_SHORT).show();
        switch (reaction){
            case "0":{
                dislikeButton.setSelected(true);
                likeButton.setSelected(false);
                break;
            }

            case "1":{
                likeButton.setSelected(true);
                dislikeButton.setSelected(false);
                break;
            }
            default:{
                likeButton.setSelected(false);
                dislikeButton.setSelected(false);
                break;
            }

        }

        //mCommentRv.setNestedScrollingEnabled(false);
//        mCommentRv.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        mCommentRv.setLayoutManager(new LinearLayoutManager(this));
        mCommentRv.setAdapter(mCommentAdapter);
//        mScrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
//            @Override
//            public void onScrollChanged() {
//                 imm.hideSoftInputFromWindow(mCommentField.getWindowToken(), 0);
//                if(mScrollView.getScrollY() == 0){
//                    mCommentLayout.setVisibility(View.GONE);
//                }
//            }
//        });
        //Set onClickListeners on Views with actions
        mAddComment.setOnClickListener(this);
        mBookmark.setOnClickListener(this);
        mCommentSend.setOnClickListener(this);

        likeButton.setOnClickListener(v -> {
            if (Prefs.getBoolean("isLoggedIn", false)) {
                reactToStory(true, String.valueOf(String.valueOf(Prefs.getInt("story_id", 0))));
//                    if (holdProgress != null) holdProgress.setVisibility(View.GONE);
//                    holdProgress = ((StoryHolder) holder).reactionProgress;
//                storyHolder.reactionProgress.setVisibility(View.VISIBLE);
//            int addDislike = Integer.parseInt(storyHolder.dislikes.getText().toString()) + 1;
//            storyHolder.dislikes.setText(String.valueOf(addDislike));
            } else
                Toast.makeText(StoryDetail.this, "You must be logged in to perform this operation", Toast.LENGTH_SHORT).show();
        });
        dislikeButton.setOnClickListener(v -> {
            if (Prefs.getBoolean("isLoggedIn", false)) {
                reactToStory(false, String.valueOf(String.valueOf(Prefs.getInt("story_id", 0))));
//                    if (holdProgress != null) holdProgress.setVisibility(View.GONE);
//                    holdProgress = ((StoryHolder) holder).reactionProgress;
//                storyHolder.reactionProgress.setVisibility(View.VISIBLE);
//            int addDislike = Integer.parseInt(storyHolder.dislikes.getText().toString()) + 1;
//            storyHolder.dislikes.setText(String.valueOf(addDislike));
            } else
                Toast.makeText(StoryDetail.this, "You must be logged in to perform this operation", Toast.LENGTH_SHORT).show();
        });

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.add_comment:
                mCommentLayout.setVisibility(View.VISIBLE);
                if(mCommentField.hasFocus()){
                    imm.showSoftInput(mCommentField, InputMethodManager.SHOW_FORCED);

                }
               // mCommentField.requestFocus();
                break;
            case R.id.comment_send:
                addComment(mCommentField.getText().toString().trim());
                Toast.makeText(this, mCommentField.getText().toString().trim(),Toast.LENGTH_LONG).show();
                break;
            case R.id.bookmark_button:
                date = Calendar.getInstance().getTimeInMillis();
                String time = date.toString();
                //Toast.makeText(this, time, Toast.LENGTH_SHORT).show();
                //check if bookmark already exists
                if (storyExist(title)){
                    Toast.makeText(this, "Story already added to Favorite", Toast.LENGTH_SHORT).show();
                    return;
                }
                addFavoriteOnline(Prefs.getString("token", " "), getIntent().getIntExtra(StoryDetail.STORY_ID, 1), title, content, image, time);
                //addFavorite(title, content, image, time);
                Log.d("check", "onClick: id is:"+ getIntent().getIntExtra(StoryDetail.STORY_ID, 1));
            default:
                break;
        }
    }

    //Add the story online first
    private void addFavoriteOnline(String token, int id, final String title, final String story, final String image, final String time) {

        Log.d("token", "token: "+ Prefs.getString("token", ""));
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Adding to bookmarks");
        dialog.show();

        MainAplication.getApiInterface().addBookmark(getIntent().getIntExtra(StoryDetail.STORY_ID, 1)).enqueue(new Callback<BookmarkResponse>() {
            @Override
            public void onResponse(Call<BookmarkResponse> call, Response<BookmarkResponse> response) {
                Log.d("code", "onResponse: "+ String.valueOf(response.code()));
                if (response.isSuccessful()){
                    if (response.code()== 200 || response.code() == 201){
                        //if successfull add story offline
                        dialog.dismiss();
                        addFavorite(title, story, image, time);
                    }else{
                        dialog.dismiss();
                        Toast.makeText(StoryDetail.this, "Oops! Try again", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    dialog.dismiss();
                    Toast.makeText(StoryDetail.this, "Failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BookmarkResponse> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(StoryDetail.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("failed", "onFailure: "+ t.getMessage());
            }
        });

    }


    private boolean storyExist(String title){
        SQLiteDatabase db =  helper.getReadableDatabase();
        String query = "select * from " + FavoriteContract.FavoriteColumn.TABLE_NAME + " where title=?";


        c  = db.rawQuery(query, new String[]{title});
        if (c.moveToFirst()){
            return true;
        }else {
            return false;
        }
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

    private void addComment(String comment){
        MainAplication.getApiInterface().addComment(comment, String.valueOf(getIntent().getIntExtra(StoryDetail.STORY_ID, 1)))
                .enqueue(new Callback<BaseResponse>() {
                    @Override
                    public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                        if (response.isSuccessful()){
                            Toast.makeText(StoryDetail.this, "Successful upload", Toast.LENGTH_SHORT).show();

                            mCommentField.setText("");
                            imm.hideSoftInputFromWindow(mCommentField.getWindowToken(), 0);
                            mCommentLayout.setVisibility(View.INVISIBLE);
                            mScrollView.setVisibility(View.VISIBLE);
                        }else{
                            Toast.makeText(StoryDetail.this, "Failed", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<BaseResponse> call, Throwable t) {
                        Toast.makeText(StoryDetail.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void reactToStory(boolean isLike, String storyId) {

        String action = isLike ? "like" : "dislike";
        MainAplication.getApiInterface().reactToStory(action, storyId).enqueue(new Callback<StoryReactionResponse>() {
            @Override
            public void onResponse(Call<StoryReactionResponse> call, Response<StoryReactionResponse> response) {
//                storyHolder.reactionProgress.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    StoryReactionResponse reactionResponse = response.body();
                    likes.setText(String.valueOf(reactionResponse.getLikesCount()));
                    dislikes.setText(String.valueOf(reactionResponse.getDislikesCount()));
                    Toast.makeText(StoryDetail.this, reactionResponse.getAction(), Toast.LENGTH_SHORT).show();
                    if (isLike) {
                        likeButton.setSelected(!likeButton.isSelected());
                        dislikeButton.setSelected(false);
                    } else {
                        dislikeButton.setSelected(!dislikeButton.isSelected());
                        likeButton.setSelected(false);
                    }


                } else Toast.makeText(StoryDetail.this, response.message(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<StoryReactionResponse> call, Throwable t) {
//                holdProgress.setVisibility(View.GONE);
                Toast.makeText(StoryDetail.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


}