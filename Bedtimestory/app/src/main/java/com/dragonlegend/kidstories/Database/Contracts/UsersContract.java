package com.dragonlegend.kidstories.Database.Contracts;

import android.provider.BaseColumns;

public final class UsersContract  {

    public  static class UserEntry implements BaseColumns {
        public static final String TABLE_NAME = "users";
        public static final String USERID = "user_id";
        public static final String EMAIL = "email";
        public static final String FIRSTNAME = "first_name";
        public static final String LASTNAME = "last_name";
        public static final String TOKEN = "token";
        public static final String PHONE = "phone";
        public static final String ADMIN = "is_admin";
    }
}
