package com.dragonlegend.kidstories;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.dragonlegend.kidstories.Api.ApiInterface;
import com.dragonlegend.kidstories.Api.Client;
import com.dragonlegend.kidstories.Api.Responses.LoginResponse;
import com.dragonlegend.kidstories.Database.Helper.BedTimeDbHelper;
import com.dragonlegend.kidstories.Model.User;
import com.pixplicity.easyprefs.library.Prefs;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {
    ImageView mImage;
    TextView mUserName,mUserEmail,mUserPhoneNum;
    BedTimeDbHelper mDbHelper;
    User mUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Toolbar toolbar = findViewById(R.id.toolbar);
        //customize custom toolbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setElevation(0);
        mDbHelper = new BedTimeDbHelper(this);
        Intent intent = getIntent();

        mImage = findViewById(R.id.profile_image);
        mUserName = findViewById(R.id.user_name);
        mUserEmail = findViewById(R.id.user_email);
        mUserPhoneNum = findViewById(R.id.userPhoneNum);

        mUserName.setText("Loading...");
        mUserEmail.setText("Loading...");
        mUserPhoneNum.setText("Loading...");

        /*if(intent.hasExtra(Config.USER_ID)){
            mUser = mDbHelper.getUserById(intent.getStringExtra(Config.USER_ID));

            mImage = findViewById(R.id.profile_image);
            mUserName = findViewById(R.id.user_name);
            mUserEmail = findViewById(R.id.user_email);
            mUserName.setText(mUser.getName());
            mUserEmail.setText(mUser.getEmail());
        if(!mUser.getImage().isEmpty()){
            Glide.with(this)
                    .load(R.drawable.story_banner)
                    .apply(RequestOptions.circleCropTransform())
                    .into(mImage);
            }
        }*/

        loadUserProfile();

    }

    private void loadUserProfile(){
        Client.getInstance().create(ApiInterface.class).getProfile("Bearer "+Prefs.getString("token",null)).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if(response.isSuccessful()){
                    mUser = response.body().getUser();
                    Log.e("TAG",mUser.getId());
                    String name = mUser.getFirstName()+" "+mUser.getLastName();
                    mUserName.setText(name);
                    mUserEmail.setText(mUser.getEmail());
                    String phone = mUser.getPhoneNumber();
                    if(mUser.getPhoneNumber()==null)
                        phone = "No associated phone number";

                    mUserPhoneNum.setText(phone);

                    String imageUrl = mUser.getImage();
                    if(mUser.getImage()==null)
                        imageUrl = "https://res.cloudinary.com/ephaig/image/upload/v1555015808/download.png";

                    Glide.with(getApplicationContext())
                            .load(imageUrl)
                            .apply(RequestOptions.circleCropTransform())
                            .into(mImage);
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Log.e("TAG",t.getMessage());
            }
        });
    }

}
