package com.example.bedtime;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.bedtime.Database.Helper.BedTimeDbHelper;
import com.example.bedtime.Model.User;

public class ProfileActivity extends AppCompatActivity {
    ImageView mImage;
    TextView mUserName,mUserEmail;
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
        if(intent.hasExtra(Config.USER_ID)){
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
        }

    }
}
