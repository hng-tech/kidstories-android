package com.dragonlegend.kidstories;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.dragonlegend.kidstories.Adapters.CategoriesAdapter;
import com.dragonlegend.kidstories.Adapters.StoryListingAdapter;
import com.dragonlegend.kidstories.Api.ApiInterface;
import com.dragonlegend.kidstories.Api.Client;
import com.dragonlegend.kidstories.Api.Responses.CategoryAllResponse;
import com.dragonlegend.kidstories.Api.Responses.LoginResponse;
import com.dragonlegend.kidstories.Api.Responses.StoryAllResponse;
import com.dragonlegend.kidstories.Database.Helper.BedTimeDbHelper;
import com.dragonlegend.kidstories.Fragments.BottomMenuFragment;
import com.dragonlegend.kidstories.Model.Category;
import com.dragonlegend.kidstories.Model.Story;
import com.dragonlegend.kidstories.Model.User;
import com.dragonlegend.kidstories.Utils.BottomNavigationViewHelper;
import com.dragonlegend.kidstories.Utils.MainAplication;
import com.pixplicity.easyprefs.library.Prefs;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.support.design.widget.Snackbar.LENGTH_LONG;
import static android.support.design.widget.Snackbar.make;

public class Home extends AppCompatActivity implements View.OnClickListener, SearchView.OnQueryTextListener {
    RecyclerView mStoriesRecycler;
    RecyclerView mCategoriesRecycler;
    StoryListingAdapter mAdapter;
    ProgressBar storiesProgress;
    List<Story> mStories;
    List<Category> mCategories;
    CategoriesAdapter mCategoriesAdapter;
    BottomNavigationView bottomNavigationView;
    User mUser;
    ImageView mAddNew,userImage;
    ProgressBar mProgressBar;
    boolean isLoggedIn;
    TextView date,userName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        isLoggedIn = Prefs.getBoolean("isLoggedIn", false);
        Log.e("TAG", isLoggedIn + "");

        //customize custom toolbar
        setSupportActionBar(toolbar);
        /*getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);*/

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setElevation(0);
        Intent intent = getIntent();
//        if (intent.hasExtra(Config.USER_ID)) {
//            String id = intent.getStringExtra(Config.USER_ID);
//            if (!id.isEmpty()) {
//                BedTimeDbHelper dbHelper = new BedTimeDbHelper(this);
//                mUser = dbHelper.getUserById(id);
//            }
//        }

        date=findViewById(R.id.date);
        Calendar calender = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("HH:mm:ss");
        String  strDate = DateFormat.getDateTimeInstance()
                .format(new Date());
        date.setText(strDate);
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(0);
        menuItem.setChecked(true);

        mStoriesRecycler = findViewById(R.id.stories_rv);
        mCategoriesRecycler = findViewById(R.id.cat_rv);
        mProgressBar = findViewById(R.id.progressBar);
        storiesProgress = findViewById(R.id.storiesProgress);
        mAddNew = findViewById(R.id.btn_addnew);
        userName = findViewById(R.id.user_name);
        userImage = findViewById(R.id.user_prof_img);

        if(isLoggedIn){
            userToolbarUpdate();

        }

        mAddNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Prefs.getBoolean("isLoggedIn", false)) {
                    Intent i = new Intent(Home.this, AddStoryActivity.class);
                    startActivity(i);
                } else {
                    Toast.makeText(Home.this, "Please log in", Toast.LENGTH_SHORT).show();
                    Intent intent1 = new Intent(Home.this, Login.class);
                    startActivity(intent1);
                }

            }
        });
        mStories = new ArrayList<>();
        storiesProgress.setVisibility(View.VISIBLE);
        mCategories = new ArrayList<>();
        mCategoriesAdapter = new CategoriesAdapter(this, mCategories, "home");
        mCategoriesRecycler.setAdapter(mCategoriesAdapter);
        mAdapter = new StoryListingAdapter(this, mStories);
//        mStoriesRecycler.setLayoutManager(new LinearLayoutManager(this));
        mStoriesRecycler.setAdapter(mAdapter);

//        mStoriesRecycler.showShimmerAdapter();

//        //checking if user is logged in
//        checkUser();


        loadData();


        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                 Handle navigation view item clicks here.
                int id = item.getItemId();
                if (id == R.id.home) {
                    //start Home activity .
                    Intent i = new Intent(getBaseContext(), Home.class);
                    startActivity(i);
                    Prefs.putString("activity", "home");
                }else if(id == R.id.action_favorites) {
                   openFragment(new Bookmark(), "bookmark");
                   Prefs.putString("activity", "bookmark");
                    //Toast.makeText(Home.this, Prefs.getString("activity", ""), Toast.LENGTH_SHORT).show();


//                    startActivity(new Intent(getBaseContext(), Bookmark.class));
////              ShowSnackbar();
                    return true;
                }
                else if(id == R.id.more) {
                    showBottomMenu();
                    return true;
                }

                else {
                }

                return true;


            }
        });

    }

    private void userToolbarUpdate(){

        String name = Prefs.getString("USER_NAMES",null);
        userName.setText(name);

        String imageUrl = Prefs.getString("USER_IMAGE","https://res.cloudinary.com/ephaig/image/upload/v1555015808/download.png");
        if(!imageUrl.isEmpty()){
            Glide.with(getApplicationContext())
                    .load(imageUrl)
                    .apply(RequestOptions.circleCropTransform())
                    .into(userImage);
        }

    }

    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.cat_activity:
                //start category activity .
            Intent i = new Intent(getBaseContext(), CategoriesActivity.class);
            startActivity(i);

                break;
            case R.id.moreactivity:
                //start category activity .
                Intent m = new Intent(getBaseContext(), MoreActivity.class);
                startActivity(m);

                break;
            case R.id.profile_activity:
                //start Profile activity .
            if(mUser !=null ){
                Intent p = new Intent(getBaseContext(), ProfileActivity.class);
                p.putExtra(Config.USER_ID,mUser.getId());
                startActivity(p);
            }
                break;
            case R.id.add_story_activity:
                //start addstory activity .
            if (isLoggedIn){
                Intent a = new Intent(getBaseContext(), AddStoryActivity.class);
                startActivity(a);
            }else {
                validate("Please Log in to add story !!!");
            }
                break;
            case R.id.donate_url:
                //do ur code;
                String url = "https://paystack.com/pay/kidstoriesapp";
                Intent d = new Intent(Intent.ACTION_VIEW);
                d.setData(Uri.parse(url));
                startActivity(d);
                break;
            case R.id.signout:
                //do ur code;
                Prefs.putString("USER_NAMES",null);
                Prefs.putString("USER_IMAGE",null);
            if (!isLoggedIn){
                ShowSnackbar("You have never logged In");
            }
            else {
                validate("Logging you out!!!!");
            }
            default:
                //do ur code;
        }
    }


    //method to call bottommenu Fragment
    public void showBottomMenu(){


        /**
         * showing bottom sheet dialog
         */

//        BottomMenuFragment bottomSheetFragment = new BottomMenuFragment();
//        bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());




        BottomSheetDialog dialog = new BottomSheetDialog(this);
        View view = getLayoutInflater().inflate(R.layout.bottom_menu_dialog, null);
        dialog.setContentView(view);
        dialog.show();

        if (isLoggedIn){
            view.findViewById(R.id.signout).setVisibility(View.VISIBLE);
            view.findViewById(R.id.login).setVisibility(View.GONE);
            view.findViewById(R.id.pro).setVisibility(View.VISIBLE);

            view.findViewById(R.id.signout).setOnClickListener(view15 -> {
                validate("Logging you out!!!!");
                Prefs.putBoolean("isLoggedIn", false);
                Prefs.putString("token", "");
                view.findViewById(R.id.signout).setVisibility(View.GONE);
                dialog.dismiss();
                view.findViewById(R.id.login).setVisibility(View.VISIBLE);
                view.findViewById(R.id.pro).setVisibility(View.GONE);
                startActivity(new Intent(Home.this, Home.class));
                finish();
            });

        }else{
            view.findViewById(R.id.login).setVisibility(View.VISIBLE);
            view.findViewById(R.id.signout).setVisibility(View.GONE);
            view.findViewById(R.id.pro).setVisibility(View.GONE);

            view.findViewById(R.id.login).setOnClickListener(view15 -> {
                dialog.dismiss();
                startActivity(new Intent(getBaseContext(), Login.class));
            });
        }
        //start categories
        view.findViewById(R.id.cat).setOnClickListener(view1 -> {
            dialog.dismiss();
            startActivity(new Intent(getBaseContext(), CategoriesActivity.class));
        });
        //start add story
        view.findViewById(R.id.moreactivity).setOnClickListener(view13 -> {
            dialog.dismiss();
            startActivity(new Intent(getBaseContext(), MoreActivity.class));
        });
        //start profile
        view.findViewById(R.id.pro).setOnClickListener(view12 -> {
            dialog.dismiss();
            startActivity(new Intent(getBaseContext(), ProfileActivity.class));
        });
        //start add story
        view.findViewById(R.id.story_add).setOnClickListener(view13 -> {
            dialog.dismiss();
            startActivity(new Intent(getBaseContext(), AddStoryActivity.class));
        });
        //start donate
        view.findViewById(R.id.donate_url).setOnClickListener(view14 -> {
            dialog.dismiss();
//            //do ur code;
                String url = "https://paystack.com/pay/kidstoriesapp";
                Intent d = new Intent(Intent.ACTION_VIEW);
                d.setData(Uri.parse(url));
                startActivity(d);
        });
    }
  public void checkUser(){
      if (isLoggedIn) {
          bottomNavigationView.getMenu().findItem(R.id.login).setVisible(false);
          bottomNavigationView.getMenu().findItem(R.id.signout).setVisible(true);
          bottomNavigationView.getMenu().findItem(R.id.profile_activity).setVisible(true);
      } else {
          bottomNavigationView.getMenu().findItem(R.id.login).setVisible(true);
          bottomNavigationView.getMenu().findItem(R.id.signout).setVisible(false);
          bottomNavigationView.getMenu().findItem(R.id.profile_activity).setVisible(false);
      }

  }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        searchView.setQueryHint(Html.fromHtml("<font color = #000000>" + getResources().getString(R.string.search) + "</font>"));
        searchView.setOnQueryTextListener(this);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_filter) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    @Override
    protected void onSaveInstanceState (Bundle outState){
        super.onSaveInstanceState(outState);
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
//        mProgressBar.setVisibility(View.VISIBLE);
        Client.getInstance().create(ApiInterface.class).getAllCategories().enqueue(new Callback<CategoryAllResponse>() {
            @Override
            public void onResponse(Call<CategoryAllResponse> call, Response<CategoryAllResponse> response) {
                if (response.isSuccessful()) {
                    CategoryAllResponse categoryList = response.body();
                    List<Category> cl = categoryList.getData();

                    for (int i = 0; i< cl.size(); i++){
                        Log.d("TAG", "onResponse: -> " + cl.get(i).getName());
                    }
                    if (cl != null) {
                        mCategoriesAdapter.addCategories(cl);
                    }
                }
            }

            @Override
            public void onFailure(Call<CategoryAllResponse> call, Throwable t) {
                Log.d("TAG", "onFailure: " + t.getMessage());
                showNetworkError();
            }
        });
        MainAplication.getApiInterface().getAllStories().enqueue(new Callback<StoryAllResponse>() {
            @Override
            public void onResponse(Call<StoryAllResponse> call, Response<StoryAllResponse> response) {
//                mStoriesRecycler.hideShimmerAdapter();
                storiesProgress.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    StoryAllResponse storyAllResponse = response.body();
                    List<Story> story = storyAllResponse.getData();

                    for (int i = 0; i< story.size(); i++){
                        Log.d("TAG", "onResponse: -> " + story.get(i).getBody());
                    }
                    if (story != null) {
                        mAdapter.removeAllStories();
                        mAdapter.addStories(story);


                    }
                }
                mProgressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<StoryAllResponse> call, Throwable t) {
//                mStoriesRecycler.hideShimmerAdapter();
                storiesProgress.setVisibility(View.GONE);
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
//                Intent i = new Intent(getBaseContext(), Login.class);
//                startActivity(i)
                recreate();
            }
        });
// show the snackbar
        snackbar.show();
    }
    private void openFragment(Fragment fragment, String tag){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.cont, fragment, tag);
        transaction.addToBackStack(fragment.getTag());
        Log.d("TAG","fragment tag: "+fragment.getTag());
        transaction.commit();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {

        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        newText = newText.toLowerCase();
        ArrayList<Story> newList = new ArrayList<>();
        for (Story story : mStories){
            String name = story.getTitle().toLowerCase();
            if (name.contains(newText))
                newList.add(story);
        }

        mAdapter.setFilter(newList);
        return true;
    }

    public void filter_activity(MenuItem item) {
        Intent filter = new Intent(Home.this, Filter.class);
        startActivity(filter);
    }
}
