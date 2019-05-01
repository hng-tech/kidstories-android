package com.dragonlegend.kidstories;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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
import com.dragonlegend.kidstories.Database.Contracts.FavoriteContract;
import com.dragonlegend.kidstories.Database.Helper.BedTimeDbHelper;
import com.dragonlegend.kidstories.Model.Story;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StoryDetail extends AppCompatActivity implements View.OnClickListener {
    public static final String STORY_ID = "story_id";
    ImageView mStoryImage;
    TextView mTitle, mDetail,mStoryAge;
    ImageButton mBookmark,mCommentSend;
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


        imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

        initViews();
        if (getIntent().getExtras().getString("type")!= null && getIntent().getExtras().getString("type").equals("fav")){
            mTitle.setText(getIntent().getExtras().getString("title"));
            Glide.with(this).load(getIntent().getExtras().getString("image")).into(mStoryImage);
            mDetail.setText(getIntent().getExtras().getString("content"));
            mBookmark.setVisibility(View.INVISIBLE);

        }else {
            Client.getInstance().create(ApiInterface.class).getStory(getIntent().getStringExtra(STORY_ID))
                    .enqueue(new Callback<StoryResponse>() {
                        @Override
                        public void onResponse(Call<StoryResponse> call, Response<StoryResponse> response) {
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
                            mProgressBar.setVisibility(View.GONE);
                            mLinearLayout.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onFailure(Call<StoryResponse> call, Throwable t) {
t.toString();
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
        Toast.makeText(this, "Successfully Added to Cart", Toast.LENGTH_SHORT).show();

    }

    public void initViews(){
        mStoryImage = findViewById(R.id.detail_image);
        mTitle = findViewById(R.id.detail_title);
        mStoryAge = findViewById(R.id.story_age_range);
        mDetail = findViewById(R.id.story);
        mBookmark = findViewById(R.id.bookmark_button);
        mAddComment = findViewById(R.id.add_comment);
        mProgressBar = findViewById(R.id.progress);
        mLinearLayout = findViewById(R.id.story_ll);
        mCommentField = findViewById(R.id.comment_box);
        mCommentSend = findViewById(R.id.comment_send);
        mCommentLayout = findViewById(R.id.comment_layout);
        mScrollView = findViewById(R.id.detail_scroll);
        mScrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                imm.hideSoftInputFromWindow(mCommentField.getWindowToken(), 0);
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
                mCommentField.requestFocus();
                imm.showSoftInput(mCommentField, InputMethodManager.SHOW_IMPLICIT);
                break;
            case R.id.comment_send:
                Toast.makeText(this, mCommentField.getText().toString().trim(),Toast.LENGTH_LONG).show();
                break;
            case R.id.bookmark_button:
                date = Calendar.getInstance().getTimeInMillis();
                String time = date.toString();
                //Toast.makeText(this, time, Toast.LENGTH_SHORT).show();
                if (storyExist(title)){
                    Toast.makeText(this, "Story already added to Favorite", Toast.LENGTH_SHORT).show();
                    return;
                }
                addFavorite(title, content, image, time);
            default:
               break;
        }
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
}
