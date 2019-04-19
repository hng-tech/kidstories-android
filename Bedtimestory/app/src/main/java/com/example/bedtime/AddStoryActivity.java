package com.example.bedtime;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class AddStoryActivity extends AppCompatActivity {
    EditText mTitleField;
    Button mImageUploadButton, mTypeContentButton;
    TextView mImagePath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_story);
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
        initViews();


    }

    public void initViews(){
        mImageUploadButton = findViewById(R.id.upload_image_button);
        mTypeContentButton = findViewById(R.id.type_content);
        mTitleField = findViewById(R.id.title_input);
        mImagePath = findViewById(R.id.image_path_text);

        mTypeContentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddStoryActivity.this,AddStoriesContentActivity.class);
                startActivity(intent);
            }
        });
    }
}
