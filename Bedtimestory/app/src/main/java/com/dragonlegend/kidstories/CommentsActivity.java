package com.dragonlegend.kidstories;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class CommentsActivity extends AppCompatActivity {
    EditText name;
    EditText addComment;
    SharedPreferences sharedPreferences;
    TextView post;
    RecyclerView list;
    static final String mypref="mypref";
    static final String namepref="namepref";
    static final String comentpref="commentpref";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
        name=findViewById(R.id.username);
        addComment=findViewById(R.id.add_comment);
        list=findViewById(R.id.recyclerview);

        post=findViewById(R.id.post);
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String n=name.getText().toString();
               String a=addComment.getText().toString();
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.putString(namepref,n);
                editor.putString(comentpref,a);


                sharedPreferences=getSharedPreferences(mypref,Context.MODE_PRIVATE);
                if(sharedPreferences.contains(namepref)){
                    addComment.setText(sharedPreferences.getString(namepref,""));

//                    Intent intent=new Intent(CommentsActivity.this,StoryDetail.class);
//                    //intent.putExtra("abc",s);
//                    startActivity(intent);
                }
            }
        });


        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Comments");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}

