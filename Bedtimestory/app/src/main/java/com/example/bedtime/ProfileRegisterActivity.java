package com.example.bedtime;

import android.content.Intent;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

public class ProfileRegisterActivity extends AppCompatActivity {
    public static final String REGISTER_EMAIL = "register_email";
    public static final String REGISTER_PASSWORD = "register_password";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_register);
        Toolbar toolbar = findViewById(R.id.toolbar);
        //customize custom toolbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setElevation(0);

        Button btn = findViewById(R.id.btn_register);
        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // onclicking Create Account redirects to Home Activity
                Intent i = new Intent(getBaseContext(), Home.class);
                startActivity(i);
            }
        });
    }
}
