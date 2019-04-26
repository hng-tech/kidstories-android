package com.dragonlegend.kidstories;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddStoriesContentActivity extends AppCompatActivity implements View.OnClickListener {
    EditText mContentField;
    Button mSaveButton,mDiscardButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_stories_content);
        Toolbar toolbar = findViewById(R.id.toolbar);
        //customize custom toolbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setDisplayShowHomeEnabled(false);   //disable back button
        getSupportActionBar().setHomeButtonEnabled(false);
//        TextView back = toolbar.findViewById(R.id.toolbar_back);
//        back.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
//        initViews();
    }

    public void initViews(){
        mContentField = findViewById(R.id.story_content_field);
        mDiscardButton = findViewById(R.id.discard_content);
        mSaveButton = findViewById(R.id.save_content);
        mDiscardButton.setOnClickListener(this);
        mSaveButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.save_content:
                Toast.makeText(this,"Content Saved",Toast.LENGTH_SHORT).show();
                break;
            default:
                finish();
                break;
        }
    }
}
