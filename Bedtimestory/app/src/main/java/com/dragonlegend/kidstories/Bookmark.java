package com.dragonlegend.kidstories;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;

import com.dragonlegend.kidstories.Adapters.FavAdapter;
import com.dragonlegend.kidstories.Database.Contracts.FavoriteContract;
import com.dragonlegend.kidstories.Database.Helper.BedTimeDbHelper;
import com.dragonlegend.kidstories.R;

public class Bookmark extends AppCompatActivity {
    BedTimeDbHelper mHelper;
    Cursor c;
    ListView favRec;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmark);

        Toolbar toolbar = findViewById(R.id.fav);
        //customize custom toolbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_primary);
        getSupportActionBar().setElevation(0);

        favRec = findViewById(R.id.favRec);

        getDatabaseColunms();

    }
    private void getDatabaseColunms() {
        mHelper = new BedTimeDbHelper(this);
        SQLiteDatabase db =  mHelper.getReadableDatabase();

        String[] projection = {
                FavoriteContract.FavoriteColumn._ID,
                FavoriteContract.FavoriteColumn.COLUMN_TITLE,
                FavoriteContract.FavoriteColumn.COLUMN_CONTENT,
                FavoriteContract.FavoriteColumn.COLUMN_TIME,
                FavoriteContract.FavoriteColumn.COLUMN_IMAGE,
        };
        c  = db.query(
                FavoriteContract.FavoriteColumn.TABLE_NAME,
                projection,
                null, null, null, null, null);



        FavAdapter adapter = new FavAdapter(this, c, 0);

        favRec.setAdapter(adapter);

    }
}
