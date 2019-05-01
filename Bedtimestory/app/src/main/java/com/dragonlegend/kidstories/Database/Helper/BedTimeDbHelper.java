package com.dragonlegend.kidstories.Database.Helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

import com.dragonlegend.kidstories.Database.Contracts.FavoriteContract;
import com.dragonlegend.kidstories.Database.Contracts.UsersContract;
import com.dragonlegend.kidstories.Model.User;

public class BedTimeDbHelper extends SQLiteOpenHelper {


    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "bedtimestories.db";
    private static final String SQL_CREATE_USER_ENTRY =
            "CREATE TABLE " + UsersContract.UserEntry.TABLE_NAME + " (" +
                    UsersContract.UserEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    UsersContract.UserEntry.EMAIL + " TEXT UNIQUE," +
                    UsersContract.UserEntry.USERID + " TEXT UNIQUE, " +
                    UsersContract.UserEntry.NAME +" TEXT," +
                    UsersContract.UserEntry.IMAGE +" TEXT," +
                    UsersContract.UserEntry.PREMIUM + " TEXT ," +
                    UsersContract.UserEntry.ADMIN + " TEXT ," +
                    UsersContract.UserEntry.LIKED +" INTEGER, " +
                    UsersContract.UserEntry.TOKEN +" TEXT )";

    private static final String SQL_CREATE_FAVORITE =
            "CREATE TABLE " + FavoriteContract.FavoriteColumn.TABLE_NAME + " (" +
                    FavoriteContract.FavoriteColumn._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    FavoriteContract.FavoriteColumn.COLUMN_TITLE +" TEXT," +
                    FavoriteContract.FavoriteColumn.COLUMN_CONTENT +" TEXT," +
                    FavoriteContract.FavoriteColumn.COLUMN_TIME +" TEXT," +
                    FavoriteContract.FavoriteColumn.COLUMN_IMAGE +" TEXT )";

    private static final String SQL_DELETE_USER_ENTRY =
            "DROP TABLE IF EXISTS " + UsersContract.UserEntry.TABLE_NAME;

    public  BedTimeDbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);


    }
    @Override
    public void onCreate(SQLiteDatabase db) {
    db.execSQL(SQL_CREATE_USER_ENTRY);
    db.execSQL(SQL_CREATE_FAVORITE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_USER_ENTRY);
        db.execSQL(SQL_CREATE_FAVORITE);
        onCreate(db);

    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onDowngrade(db, oldVersion, newVersion);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            // Enable foreign key constraints
            db.execSQL("PRAGMA foreign_keys=ON;");
        }
    }

    public void addUser(User user){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = getContentValues(user);


        //insect user
        long cat_id = db.insertWithOnConflict(UsersContract.UserEntry.TABLE_NAME,
                null,values,SQLiteDatabase.CONFLICT_IGNORE);
    }
    public void updateUser(User user){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = getContentValues(user);

        db.update(UsersContract.UserEntry.TABLE_NAME,values,
                UsersContract.UserEntry.EMAIL +" = ?",new String[] {String.valueOf(user.getEmail())});
    }
    public User getUserById(String id){
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM " +UsersContract.UserEntry.TABLE_NAME + " WHERE "+UsersContract.UserEntry.USERID
                + " ='"+id+"'";
        Cursor c = db.rawQuery(query, null);
        User user = new User();
        if (c.moveToFirst()){
            user.setId(c.getString(c.getColumnIndex(UsersContract.UserEntry.USERID)));
            user.setEmail(c.getString(c.getColumnIndex(UsersContract.UserEntry.EMAIL)));
            user.setImage(c.getString(c.getColumnIndex(UsersContract.UserEntry.IMAGE)));
            user.setLiked(c.getInt(c.getColumnIndex(UsersContract.UserEntry.LIKED)));
            user.setName(c.getString(c.getColumnIndex(UsersContract.UserEntry.NAME)));
            user.setToken(c.getString(c.getColumnIndex(UsersContract.UserEntry.TOKEN)));
            user.setPremium(Boolean.getBoolean(c.getString(c.getColumnIndex(UsersContract.UserEntry.PREMIUM))));
            user.setAdmin(Boolean.getBoolean(c.getString(c.getColumnIndex(UsersContract.UserEntry.ADMIN))));

        }
        c.close();
        return user;
    }

    public void deleteUser(User user){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(UsersContract.UserEntry.TABLE_NAME,
                UsersContract.UserEntry.EMAIL +" = ?",
                new String[]{String.valueOf(user.getEmail())});
    }
    @NonNull
    private ContentValues getContentValues(User user) {
        ContentValues values = new ContentValues();
        values.put(UsersContract.UserEntry.EMAIL,user.getEmail());
        values.put(UsersContract.UserEntry.USERID,user.getId());
        values.put(UsersContract.UserEntry.PREMIUM,user.getPremium());
        values.put(UsersContract.UserEntry.ADMIN,user.getAdmin());
        values.put(UsersContract.UserEntry.IMAGE,user.getImage());
        values.put(UsersContract.UserEntry.TOKEN,user.getToken());
        values.put(UsersContract.UserEntry.LIKED,user.getLiked());
        values.put(UsersContract.UserEntry.NAME,user.getName());
        return values;
    }
}
