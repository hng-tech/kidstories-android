package com.example.bedtime;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.bedtime.Utils.UploadImage;
import com.pixplicity.easyprefs.library.Prefs;

import java.io.File;

public class AddStoriesContentActivity extends AppCompatActivity implements View.OnClickListener {
    EditText mContentField;
    Button mSaveButton,mDiscardButton;
    String name = "image";
    private String title;
    private String filePath;
    private String content;

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
         initViews();
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
                AddStory();
                Toast.makeText(this,"Content Saved",Toast.LENGTH_SHORT).show();
                break;
            default:
                finish();
                break;
        }
    }

    private void AddStory() {
        content = mContentField.getText().toString().trim();
        title = Prefs.getString("title", "");
        String imageFileUri = Prefs.getString("filePath","");

//        Uri fileUri = Uri.parse(Prefs.getString("filePath",""));
//        String filePath =
//        String filePath = getImageAbsolutePath(fileUri,this);
        UploadImage.uploadFile(imageFileUri,name,title,content,this);
    }

    private String getImageAbsolutePath(Uri contentURI, Activity activity) {
        Cursor cursor = activity.getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) {
            return contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(index);
        }

    }


}
