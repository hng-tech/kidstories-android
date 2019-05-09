package com.dragonlegend.kidstories;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.dragonlegend.kidstories.Api.ApiInterface;
import com.dragonlegend.kidstories.Api.Client;
import com.dragonlegend.kidstories.Api.Responses.LoginResponse;
import com.dragonlegend.kidstories.Database.Helper.BedTimeDbHelper;
import com.dragonlegend.kidstories.Model.User;
import com.dragonlegend.kidstories.Model.UserData;
import com.dragonlegend.kidstories.Utils.UploadImage;
import com.pixplicity.easyprefs.library.Prefs;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {
    ImageView mImage;
    EditText mUserName,mUserEmail,mUserPhoneNum;
    BedTimeDbHelper mDbHelper;
    UserData mUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Toolbar toolbar = findViewById(R.id.toolbar);
        //customize custom toolbar
        setSupportActionBar(toolbar);
        /*getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);*/
        getSupportActionBar().setElevation(0);
        mDbHelper = new BedTimeDbHelper(this);
        Intent intent = getIntent();

        mImage = findViewById(R.id.profile_image);
        mUserName = findViewById(R.id.user_name);
        mUserEmail = findViewById(R.id.user_email);
        mUserPhoneNum = findViewById(R.id.userPhoneNum);

        mUserName.setEnabled(false);
        mUserEmail.setEnabled(false);
        mUserPhoneNum.setEnabled(false);

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
        Client.getInstance().create(ApiInterface.class).getProfile(Prefs.getString("token",null)).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if(response.isSuccessful()){
                    mUser = response.body().getData();
                    Log.e("TAG",mUser.getId());
                    String name = mUser.getFirst_name()+" "+mUser.getLast_name();
                    mUserName.setText(name);
                    mUserEmail.setText(mUser.getEmail());
                    String phone = mUser.getPhone();
                    if(phone==null)
                        phone = "No associated phone number";

                    mUserPhoneNum.setText(phone);

                    String imageUrl = mUser.getImage();
                    if(imageUrl==null)
                        imageUrl = "https://res.cloudinary.com/ephaig/image/upload/v1555015808/download.png";

                    Glide.with(getApplicationContext())
                            .load(imageUrl)
                            .apply(RequestOptions.circleCropTransform())
                            .into(mImage);

                    //Log.e("TAG",imageUrl);
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Log.e("TAG",t.getMessage());
            }
        });
    }

    private void updateProfile(String fname,String lname,String email,String phoneNumber){
Client.getInstance().create(ApiInterface.class).updateProfile(Prefs.getString("token",null),fname,lname,email,phoneNumber).enqueue(new Callback<LoginResponse>() {
    @Override
    public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
        if(response.isSuccessful())
            Toast.makeText(ProfileActivity.this,"Profile Updated",Toast.LENGTH_LONG).show();
    }

    @Override
    public void onFailure(Call<LoginResponse> call, Throwable t) {

    }
});
    }

    public void save(View view) {
        String fName = mUserName.getText().toString().substring(0,mUserName.getText().toString().indexOf(" "));
        String lName = mUserName.getText().toString().substring(mUserName.getText().toString().indexOf(" "));
        updateProfile(fName,lName,mUserEmail.getText().toString(),mUserPhoneNum.getText().toString());
        mUserName.setEnabled(false);
        mUserEmail.setEnabled(false);
        mUserPhoneNum.setEnabled(false);
        recreate();
    }

    public void edit(View view) {
        new AlertDialog.Builder(ProfileActivity.this)
                .setTitle("Update profile")
                .setMessage("Are you sure you want to update your profile?")
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mUserName.setEnabled(true);
                        mUserEmail.setEnabled(true);
                        mUserPhoneNum.setEnabled(true);
                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create().show();
    }
}
