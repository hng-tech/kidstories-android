package com.dragonlegend.kidstories;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Range;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.dragonlegend.kidstories.Utils.UploadImage;
import com.pixplicity.easyprefs.library.Prefs;

import static android.support.design.widget.Snackbar.LENGTH_LONG;
import static android.support.design.widget.Snackbar.make;

public class AddStoriesContentActivity extends AppCompatActivity implements View.OnClickListener {
    EditText mContentField;
    Button mSaveButton,mDiscardButton;
    String name = "image";
    private String title;
    private String filePath;
    private String content;
    private Integer category;
    private Spinner chooseCategory;
    private String duration;


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
        chooseCategory = findViewById(R.id.choose_category);
        mDiscardButton.setOnClickListener(this);
        mSaveButton.setOnClickListener(this);

        //set color for selected Item on create
        chooseCategory.setSelection(0, true);
        View v = chooseCategory.getSelectedView();
        ((TextView)v).setTextColor(Color.WHITE);

        chooseCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //set color for selected Item

                ((TextView) view).setTextColor(Color.WHITE);

                // Get Selected Class name from the list
                String selectedCategory = adapterView.getItemAtPosition(i).toString();
                    switch (selectedCategory) {
                        case "Fantasy" :
                            //category = Prefs.getString("fantasy", "");
                            category =1;
                            Log.d("TAG", "onItemSelected:-> " + category);
                            break;
                        case "Bedtime stories" :
                            //category = Prefs.getString("bedtime", "");
                            category =2;
                            Log.d("TAG", "onItemSelected:-> " + category);
                            break;
                        case "Morning Stories" :
                           // category = Prefs.getString("morning","");
                            category =3;
                            Log.d("TAG", "onItemSelected:-> " + category);
                            break;
                        case "Jokes" :
                           // category = Prefs.getString("jokes", "");
                            category = 4;
                            break;
                        case "Christmas Stories" :
                            //category = Prefs.getString("christmas", "");
                            category = 5;
                            break;
                        case "Category":
                            category = 0;
                            break;
                    }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.save_content:
                AddStory();
                break;
            default:
                Intent intent = new Intent(this, Home.class);
                startActivity(intent);
                finish();
                break;
        }
    }

    public void getDuration(String text) {
        String [] arr = text.split(" ", 0);
        int length = arr.length;
        int story_length = (int) (length * 0.1);
        duration = Integer.toString(story_length)+":"+"00"+":"+"00";
        Log.d("TAG", "getDuration: -> " + duration);
    }

    private void AddStory() {
        if (Prefs.getBoolean("isLoggedIn", false) == true){
            content = mContentField.getText().toString().trim();
            getDuration(content);
            String age = "1-5";
            Log.d("TAG", "age: -> " +age);
            String author = Prefs.getString("user_profile_name", "");
            Log.d("TAG", "AddStory: " + duration);
            if (content.isEmpty()) {
                ShowSnackbar("Content cannot be empty");
            }
            else {

                title = Prefs.getString("title", "");
                String imageFileUri = Prefs.getString("filePath","");
                Log.d("TAG", "AddStory: " + imageFileUri);
                if (category == 0){
                    ShowSnackbar("Category cannot be empty");
                }else{

                    UploadImage.uploadFile(imageFileUri,name,title,content,this,category,age,duration,author);
                }
            }
        } else {
            Intent i = new Intent(this, Login.class);
            startActivity(i);
            finish();
            ShowSnackbar("Please log in to add story");
        }

    }


    public void ShowSnackbar(String message) {
        Snackbar snackbar = make(findViewById(android.R.id.content),message, LENGTH_LONG);
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

}
