package com.dragonlegend.kidstories.Adapters;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dragonlegend.kidstories.Database.Contracts.FavoriteContract;
import com.dragonlegend.kidstories.Database.Helper.BedTimeDbHelper;
import com.dragonlegend.kidstories.R;
import com.dragonlegend.kidstories.StoryDetail;

import java.util.Calendar;

public class FavAdapter extends CursorAdapter {
    BedTimeDbHelper helper;
    Long minutes_ago;
    int curnent;
    Long min;
    String mins;
    int whole_mins;
    String whole;
    public FavAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.post_single, viewGroup, false);
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        ImageButton mLike,mDislike;
        TextView mTitle,mTime ,mImgTitle;
        ImageView mImage,mAuthor_image;

        //current time algorithm

        String db_time = cursor.getString(cursor.getColumnIndexOrThrow(FavoriteContract.FavoriteColumn.COLUMN_TIME));
        Long d_time = Long.valueOf(db_time);
        Long current_time = Calendar.getInstance().getTimeInMillis();
        minutes_ago = current_time - d_time;




        mImage = view.findViewById(R.id.story_banner);
        mAuthor_image = view.findViewById(R.id.author_image);
        mTitle = view.findViewById(R.id.story_title);
        mImgTitle = view.findViewById(R.id.story_imgtitle);
        mTime = view.findViewById(R.id.story_publish_time);
        mLike = view.findViewById(R.id.like_button);
        mDislike = view.findViewById(R.id.dislike_button);

        final String title = cursor.getString(cursor.getColumnIndexOrThrow(FavoriteContract.FavoriteColumn.COLUMN_TITLE));
        final String image = cursor.getString(cursor.getColumnIndexOrThrow(FavoriteContract.FavoriteColumn.COLUMN_IMAGE));
        final String content = cursor.getString(cursor.getColumnIndexOrThrow(FavoriteContract.FavoriteColumn.COLUMN_CONTENT));

        helper  = new BedTimeDbHelper(context);
        String minn = "minutes";
        String hr = "hours";
        String days = "days";

        if (minutes_ago/60000 < 1){
            mTime.setText("Just now");
        }

        if (whole_mins < 60){
            min = minutes_ago/60000;
            mins = Long.toString(min);
            whole_mins = Integer.parseInt(mins);
            whole = Integer.toString(whole_mins);
            mTime.setText(whole + " " + "minutes ago");
        }
        if (whole_mins >= 60){
            whole_mins = whole_mins/60;
            whole = Integer.toString(whole_mins);
            mTime.setText(whole + " " + hr + " " + "ago");
            if (whole_mins >= 24){
                whole_mins = whole_mins/24;
                whole = Integer.toString(whole_mins);
                mTime.setText(whole + " " + days + " " + "ago");
            }

        }else if (whole_mins > 4320){
            mTime.setText("A few" + days + " " + "ago");
        }
        mTitle.setText(title);
        Glide.with(context).load(image).into(mImage);


        mImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context.getApplicationContext(), StoryDetail.class);
                intent.putExtra("type", "fav");
                intent.putExtra("title", title);
                intent.putExtra("image", image);
                intent.putExtra("content", content);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public Object getItem(int position) {
        return getItem(getCount() - position - 1);
    }
}

