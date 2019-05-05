package com.dragonlegend.kidstories.Database.Contracts;

import android.provider.BaseColumns;

public final class FavoriteContract {
    public FavoriteContract() {
    }
    public static abstract class FavoriteColumn implements BaseColumns {
        public static final String TABLE_NAME = "favorite";
        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_CONTENT = "story";
        public static final String COLUMN_IMAGE = "image";
        public static final String COLUMN_TIME = "time";
    }
}


