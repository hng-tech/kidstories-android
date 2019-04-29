package com.dragonlegend.kidstories;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dragonlegend.kidstories.Utils.PermissionManager;
import com.pixplicity.easyprefs.library.Prefs;

public class AddStoryActivity extends AppCompatActivity {
    EditText mTitleField;
    Button mImageUploadButton, mTypeContentButton;
    TextView mImagePath;
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int STOTAGE_REQUEST_CODE = 2;
    private Uri filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_story);

        //Requesting storage permission
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

        } else {
            requestStoragePermission();

        }



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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == STOTAGE_REQUEST_CODE)  {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission GRANTED", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }
    //this methods makes the storage permission request
    private void requestStoragePermission() {

        PermissionManager.requestPermision(this, STOTAGE_REQUEST_CODE, AddStoryActivity.this);
    }


    //this method Initialize the Views
    public void initViews(){
        mImageUploadButton = findViewById(R.id.upload_image_button);
        mTypeContentButton = findViewById(R.id.type_content);
        mTitleField = findViewById(R.id.title_input);
        mImagePath = findViewById(R.id.image_path_text);

        mTypeContentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if((!mTitleField.getText().toString().trim().isEmpty()) ){

                    String title = mTitleField.getText().toString().trim();
                    Prefs.putString("title", title);
                    Log.d("TAG", "onClick: " + Prefs.getString("filePath",""));
                    Intent intent = new Intent(AddStoryActivity.this, AddStoriesContentActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(AddStoryActivity.this,
                            "Title and Image cannot be empty!!!", Toast.LENGTH_LONG).show();
                }
            }
        });

        mImageUploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PickPicture();
            }
        });
    }

    private void PickPicture() {
        Intent pickPictureIntent = new Intent();
        pickPictureIntent.setType("image/*");
        pickPictureIntent.setAction(Intent.ACTION_PICK);
        startActivityForResult(pickPictureIntent,PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null  && data.getData() != null){

            // Get the Image from data
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            assert cursor != null;
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String filePathString= cursor.getString(columnIndex);
            mImagePath.setText(selectedImage.getPath());
            Prefs.putString("filePath",filePathString);

            cursor.close();

        }
    }
}
