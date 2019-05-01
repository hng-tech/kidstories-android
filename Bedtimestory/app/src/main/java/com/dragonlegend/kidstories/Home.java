package com.dragonlegend.kidstories;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.dragonlegend.kidstories.Adapters.CategoriesAdapter;
import com.dragonlegend.kidstories.Adapters.StoryListingAdapter;
import com.dragonlegend.kidstories.Api.ApiInterface;
import com.dragonlegend.kidstories.Api.Client;
import com.dragonlegend.kidstories.Api.Responses.CategoryAllResponse;
import com.dragonlegend.kidstories.Api.Responses.StoryAllResponse;
import com.dragonlegend.kidstories.Database.Helper.BedTimeDbHelper;
import com.dragonlegend.kidstories.Model.Category;
import com.dragonlegend.kidstories.Model.Story;
import com.dragonlegend.kidstories.Model.User;
import com.pixplicity.easyprefs.library.Prefs;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.support.design.widget.Snackbar.LENGTH_LONG;
import static android.support.design.widget.Snackbar.make;

public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    RecyclerView mStoriesRecycler, mCategoriesRecycler;
    StoryListingAdapter mAdapter;
    List<Story> mStories;
    List<Category> mCategories;
    CategoriesAdapter mCategoriesAdapter;
    User mUser;
    ImageView mAddNew;
    ProgressBar mProgressBar;
    boolean isLoggedIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        isLoggedIn = Prefs.getBoolean("isLoggedIn",false);
        Log.e("TAG",isLoggedIn+"");

        //customize custom toolbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setElevation(0);
        Intent intent = getIntent();
        if (intent.hasExtra(Config.USER_ID)) {
            String id = intent.getStringExtra(Config.USER_ID);
            if (!id.isEmpty()) {
                BedTimeDbHelper dbHelper = new BedTimeDbHelper(this);
                mUser = dbHelper.getUserById(id);
            }
        }
        mStoriesRecycler = findViewById(R.id.stories_rv);
        mCategoriesRecycler = findViewById(R.id.cat_rv);
        mProgressBar = findViewById(R.id.progressBar);
        mAddNew = findViewById(R.id.btn_addnew);
        mAddNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Prefs.getBoolean("isLoggedIn", false)){
                    Intent i = new Intent(Home.this, AddStoryActivity.class);
                    startActivity(i);
                }
                else {
                    Toast.makeText(Home.this, "Please log in", Toast.LENGTH_SHORT).show();
                    Intent intent1 = new Intent(Home.this, Login.class);
                    startActivity(intent1);
                }

            }
        });
        mStories = new ArrayList<>();
        mCategories = new ArrayList<>();
        mCategoriesAdapter = new CategoriesAdapter(this, mCategories, "home");
        mCategoriesRecycler.setAdapter(mCategoriesAdapter);
        mAdapter = new StoryListingAdapter(this, mStories);
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
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
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

        if(isLoggedIn){
            navigationView.getMenu().findItem(R.id.nav_login).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_signout).setVisible(true);
            navigationView.getMenu().findItem(R.id.nav_profile).setVisible(true);
        }else{
            navigationView.getMenu().findItem(R.id.nav_login).setVisible(true);
            navigationView.getMenu().findItem(R.id.nav_signout).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_profile).setVisible(false);
        }

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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            //start Home activity .
            Intent i = new Intent(getBaseContext(), Home.class);
            startActivity(i);
        } else if (id == R.id.nav_bookmarks) {

                startActivity(new Intent(getBaseContext(), Bookmark.class));
//              ShowSnackbar();


        } else if (id == R.id.nav_categories) {

            //start category activity .
            Intent i = new Intent(getBaseContext(), CategoriesActivity.class);
            startActivity(i);

        } else if (id == R.id.nav_bookmarks) {

         }
        else if (id == R.id.nav_donate) {
          //redirects user to Donate Form
            String url = "https://paystack.com/pay/kidstoriesapp";

            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);


        }
         else if (id == R.id.nav_profile) {
//
//            //start Profile activity .
            if(mUser !=null ){
                Intent i = new Intent(getBaseContext(), ProfileActivity.class);
                i.putExtra(Config.USER_ID,mUser.getId());
                startActivity(i);
            }
//
// else if (id == R.id.nav_donate) {
//
//            String url = "https://paystack.com/pay/kidstoriesapp";
//
//            Intent i = new Intent(Intent.ACTION_VIEW);
//            i.setData(Uri.parse(url));
//            startActivity(i);
//
//        }
            //ShowSnackbar("comming soon");

        }
        else if (id == R.id.nav_login) {
            //start Login activity .
            Intent i = new Intent(getBaseContext(), Login.class);
            startActivity(i);

        }else if (id == R.id.nav_signout){
            Prefs.putBoolean("isLoggedIn", false);
            recreate();
        }
        else if (id == R.id.nav_addstory) {

//            start addstory activity .
            if (Prefs.getBoolean("isLoggedIn", false)){

                Intent i = new Intent(getBaseContext(), AddStoryActivity.class);
                startActivity(i);
            }else{
                validate("Please Log in to add story !!!");
            }


        }

//        DrawerLayout drawer = findViewById(R.id.drawer_layout);
//        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    public void ShowSnackbar(String message) {
        Snackbar snackbar = make(findViewById(android.R.id.content), message, LENGTH_LONG);
// get snackbar view
        View mView = snackbar.getView();
// get textview inside snackbar view
        TextView mTextView = (TextView) mView.findViewById(android.support.design.R.id.snackbar_text);
// set text to center
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1)
            mTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        else
            mTextView.setGravity(Gravity.CENTER_HORIZONTAL);
// show the snackbar
        snackbar.show();
    }

    public void loadData() {
        mProgressBar.setVisibility(View.VISIBLE);
        Client.getInstance().create(ApiInterface.class).getAllCategories().enqueue(new Callback<CategoryAllResponse>() {
            @Override
            public void onResponse(Call<CategoryAllResponse> call, Response<CategoryAllResponse> response) {
                if (response.isSuccessful()) {
                    CategoryAllResponse categoryList = response.body();
                    List<Category> cl = categoryList.getData();
                    if (cl != null) {
                        mCategoriesAdapter.addCategories(cl);
                    }
                }
            }

            @Override
            public void onFailure(Call<CategoryAllResponse> call, Throwable t) {
                showNetworkError();
            }
        });
        Client.getInstance().create(ApiInterface.class).getAllStories().enqueue(new Callback<StoryAllResponse>() {
            @Override
            public void onResponse(Call<StoryAllResponse> call, Response<StoryAllResponse> response) {
                if (response.isSuccessful()) {
                    StoryAllResponse storyAllResponse = response.body();
                    List<Story> story = storyAllResponse.getData();
                    if (story != null) {
                        mAdapter.addStories(story);


                    }
                }
                mProgressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<StoryAllResponse> call, Throwable t) {
                Log.e("Story = ", t.toString());
                showNetworkError();
            }
        });
    }

    public void showNetworkError() {
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

    //exit the app onBackPressed
    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub

        new AlertDialog.Builder(Home.this)
//                .setTitle("Title")
                .setMessage("Do you really want to exit?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.finishAffinity(Home.this);
                        finish();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();

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
                Prefs.putBoolean("isLoggedIn", false);
                Intent i = new Intent(getBaseContext(), Login.class);
                startActivity(i);
            }
        });
// show the snackbar
        snackbar.show();
    }


}

