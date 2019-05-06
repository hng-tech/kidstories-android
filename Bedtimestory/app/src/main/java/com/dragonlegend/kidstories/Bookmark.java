package com.dragonlegend.kidstories;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.dragonlegend.kidstories.Adapters.BookListingAdapter;
import com.dragonlegend.kidstories.Adapters.FavAdapter;
import com.dragonlegend.kidstories.Adapters.StoryListingAdapter;
import com.dragonlegend.kidstories.Api.Responses.BaseResponse;
import com.dragonlegend.kidstories.Database.Contracts.FavoriteContract;
import com.dragonlegend.kidstories.Database.Helper.BedTimeDbHelper;
import com.dragonlegend.kidstories.Model.Story;
import com.dragonlegend.kidstories.R;
import com.dragonlegend.kidstories.Utils.MainAplication;
import com.pixplicity.easyprefs.library.Prefs;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Bookmark extends Fragment {
    BedTimeDbHelper mHelper;
    Cursor c;
    RecyclerView favRec;
    List<Story> stories;
    ProgressBar progressBar;
    BookListingAdapter adapter;
    LinearLayout linearLayout;
    ListView favRec2;
    TextView text;
    Button loog;
    ImageView refresh;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_bookmark, container, false);

        Toolbar toolbar = v.findViewById(R.id.fav);
        progressBar = v.findViewById(R.id.bookmarkProgress);
        //customize custom toolbar
//        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
//        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
//        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
//        ((AppCompatActivity)getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_primary);
//        ((AppCompatActivity)getActivity()).getSupportActionBar().setElevation(0);

        favRec = v.findViewById(R.id.favRec);
        favRec2 = v.findViewById(R.id.favRec2);
        text = v.findViewById(R.id.txt);
        loog = v.findViewById(R.id.looog);
        linearLayout = v.findViewById(R.id.textLogin);
        refresh = v.findViewById(R.id.refresh);
        stories = new ArrayList<>();

        adapter = new BookListingAdapter(getActivity(), stories);
        favRec.setHasFixedSize(true);
        favRec.setLayoutManager(new LinearLayoutManager(getActivity()));
        favRec.setAdapter(adapter);


        if (!Prefs.getBoolean("isLoggedIn", false)){
            favRec.setVisibility(View.INVISIBLE);
            linearLayout.setVisibility(View.VISIBLE);
            refresh.setVisibility(View.INVISIBLE);
            v.findViewById(R.id.looog).setOnClickListener(view -> {
                Intent intent = new Intent(getActivity(), Login.class);
                intent.putExtra("activity", "fav");
                startActivity(intent);
            });
            progressBar.setVisibility(View.GONE);
        }else{
            fetchData();
        }



//        getDatabaseColunms();

        return v;
    }

    private void getDatabaseColunms() {
        mHelper = new BedTimeDbHelper(getActivity());
        SQLiteDatabase db =  mHelper.getReadableDatabase();
        if (!Prefs.getBoolean("isLoggedIn", false)){
            favRec.setVisibility(View.INVISIBLE);
            linearLayout.setVisibility(View.VISIBLE);
            favRec2.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.GONE);
        }else{
            favRec.setVisibility(View.INVISIBLE);
            favRec2.setVisibility(View.VISIBLE);

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

            if (c.moveToFirst()){
                FavAdapter adapter = new FavAdapter(getActivity(), c, 0);

                favRec2.setAdapter(adapter);
            }else{
                text.setText("You have no offline favourites");
                loog.setVisibility(View.GONE);
                linearLayout.setVisibility(View.VISIBLE);
                refresh.setVisibility(View.VISIBLE);
            }


        }


    }

    private void fetchData(){
        Log.d("token", "fetchData: " + Prefs.getString("token", ""));
        MainAplication.getApiInterface().getBookmarks().enqueue(new Callback<BaseResponse<List<Story>>>() {
            @Override
            public void onResponse(Call<BaseResponse<List<Story>>> call, Response<BaseResponse<List<Story>>> response) {
                progressBar.setVisibility(View.GONE);
                Log.d("code", "onResponse: "+ String.valueOf(response.code()));
                if (response.isSuccessful()){
                    assert response.body() != null;
                    List<Story> allStories = response.body().getData();

                    stories.addAll(allStories);
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getActivity(), "Oops!!! An error occurred. Switched to offline favorites", Toast.LENGTH_SHORT).show();
                    getDatabaseColunms();

                }
            }

            @Override
            public void onFailure(Call<BaseResponse<List<Story>>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);

                Toast.makeText(getActivity(), "Oops!!! Request failure. Switched to offline favorites", Toast.LENGTH_SHORT).show();
                getDatabaseColunms();
            }
        });
    }
     public static Bookmark newInstance() {

        Bundle args = new Bundle();

        Bookmark fragment = new Bookmark();
        fragment.setArguments(args);
        return fragment;
    }
}