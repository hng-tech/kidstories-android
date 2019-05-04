package com.dragonlegend.kidstories;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.dragonlegend.kidstories.Adapters.FavAdapter;
import com.dragonlegend.kidstories.Adapters.StoryListingAdapter;
import com.dragonlegend.kidstories.Api.Responses.BaseResponse;
import com.dragonlegend.kidstories.Database.Contracts.FavoriteContract;
import com.dragonlegend.kidstories.Database.Helper.BedTimeDbHelper;
import com.dragonlegend.kidstories.Model.Story;
import com.dragonlegend.kidstories.R;
import com.dragonlegend.kidstories.Utils.MainAplication;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Bookmark extends AppCompatActivity {
    BedTimeDbHelper mHelper;
    Cursor c;
    RecyclerView favRec;
    List<Story> stories;
    ProgressBar progressBar;
    StoryListingAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmark);

        Toolbar toolbar = findViewById(R.id.fav);
        progressBar = findViewById(R.id.bookmarkProgress);
        //customize custom toolbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_primary);
        getSupportActionBar().setElevation(0);

        favRec = findViewById(R.id.favRec);
        stories = new ArrayList<>();

        adapter = new StoryListingAdapter(this, stories);
        favRec.setHasFixedSize(true);
        favRec.setLayoutManager(new LinearLayoutManager(this));
        favRec.setAdapter(adapter);

        fetchData();

//        getDatabaseColunms();

    }
    private void getDatabaseColunms() {
        mHelper = new BedTimeDbHelper(this);
        SQLiteDatabase db =  mHelper.getReadableDatabase();

        String[] projection = {
                FavoriteContract.FavoriteColumn._ID,
                FavoriteContract.FavoriteColumn.COLUMN_TITLE,
                FavoriteContract.FavoriteColumn.COLUMN_CONTENT,
                FavoriteContract.FavoriteColumn.COLUMN_TIME,
                FavoriteContract.FavoriteColumn.COLUMN_IMAGE,
        };
        c  = db.query(
                FavoriteContract.FavoriteColumn.TABLE_NAME,
                projection,
                null, null, null, null, null);



//        FavAdapter adapter = new FavAdapter(this, c, 0);

//        favRec.setAdapter(adapter);

    }

    private void fetchData(){
        MainAplication.getApiInterface().getBookmarks().enqueue(new Callback<BaseResponse<List<Story>>>() {
            @Override
            public void onResponse(Call<BaseResponse<List<Story>>> call, Response<BaseResponse<List<Story>>> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()){
                    assert response.body() != null;
                    List<Story> allStories = response.body().getData();

                    stories.addAll(allStories);
                    adapter.notifyDataSetChanged();
                } else Toast.makeText(Bookmark.this, response.message(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<BaseResponse<List<Story>>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(Bookmark.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
