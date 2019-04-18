package com.example.bedtime;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;

import com.example.bedtime.Adapters.CategoriesAdapter;
import com.example.bedtime.Adapters.StoryListingAdapter;
import com.example.bedtime.Api.ApiInterface;
import com.example.bedtime.Api.Client;
import com.example.bedtime.Api.Responses.CategoryAllResponse;
import com.example.bedtime.Api.Responses.StoryAllResponse;
import com.example.bedtime.Model.Category;
import com.example.bedtime.Model.Story;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    RecyclerView mStoriesRecycler,mCategoriesRecycler;
    StoryListingAdapter mAdapter;
    List<Story> mStories;
    List<Category> mCategories;
    CategoriesAdapter mCategoriesAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        getSupportActionBar().setElevation(0);

        mStoriesRecycler = findViewById(R.id.stories_rv);
        mCategoriesRecycler = findViewById(R.id.cat_rv);
        mStories = new ArrayList<>();
        mCategories = new ArrayList<>();
        mCategoriesAdapter = new CategoriesAdapter(this,mCategories,"home");
        mCategoriesRecycler.setAdapter(mCategoriesAdapter);
        mAdapter = new StoryListingAdapter(this,mStories);
        mStoriesRecycler.setLayoutManager(new LinearLayoutManager(this));
        mStoriesRecycler.setAdapter(mAdapter);
        loadData();

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close){
            @Override
            public boolean onOptionsItemSelected(MenuItem item) {
//                drawer.openDrawer(Gravity.START);
                return super.onOptionsItemSelected(item);
            }
        };
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        final NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        ImageButton close = navigationView.getHeaderView(0).findViewById(R.id.nav_close_button);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.closeDrawers();
            }
        });
        navigationView.setNavigationItemSelectedListener(this);


    }

//    @Override
//    public void onBackPressed() {
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        if (drawer.isDrawerOpen(GravityCompat.START)) {
//            drawer.closeDrawer(GravityCompat.START);
//        } else {
//            super.onBackPressed();
//        }
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.home, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
        } else if (id == R.id.nav_bookmarks) {

        } else if (id == R.id.nav_categories) {

            //start category activity .
            Intent i = new Intent(getBaseContext(), CategoriesActivity.class);
            startActivity(i);

        } else if (id == R.id.nav_bookmarks) {

        } else if (id == R.id.nav_profile) {

            //start Profile activity .
            Intent i = new Intent(getBaseContext(), ProfileActivity.class);
            startActivity(i);
        }
        else if (id == R.id.nav_login) {
            //start Login activity .
            Intent i = new Intent(getBaseContext(), Login.class);
            startActivity(i);

        } else if (id == R.id.nav_addstory) {

            //start addstory activity .
            Intent i = new Intent(getBaseContext(), AddStoryActivity.class);
            startActivity(i);

        }

//        DrawerLayout drawer = findViewById(R.id.drawer_layout);
//        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void loadData(){
        Client.getInstance().create(ApiInterface.class).getAllCategories().enqueue(new Callback<CategoryAllResponse>() {
            @Override
            public void onResponse(Call<CategoryAllResponse> call, Response<CategoryAllResponse> response) {
                if(response.isSuccessful()){
                    CategoryAllResponse  categoryList = response.body();
                    List<Category> cl= categoryList.getData();
                    if(cl !=null){
                        mCategoriesAdapter.addCategories(cl);
                    }
                }
            }

            @Override
            public void onFailure(Call<CategoryAllResponse> call, Throwable t) {

            }
        });
        Client.getInstance().create(ApiInterface.class).getAllStories().enqueue(new Callback<StoryAllResponse>() {
            @Override
            public void onResponse(Call<StoryAllResponse> call, Response<StoryAllResponse> response) {
                if(response.isSuccessful()){
                    StoryAllResponse  storyAllResponse = response.body();
                    List<Story> story= storyAllResponse.getData().getStories();
                    if(story !=null){
                        mAdapter.addStories(story);
                    }
                }
            }

            @Override
            public void onFailure(Call<StoryAllResponse> call, Throwable t) {
                Log.e("Story = " , t.toString());
            }
        });

    }
}

