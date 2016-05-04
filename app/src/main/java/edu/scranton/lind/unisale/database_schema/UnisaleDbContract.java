package edu.scranton.lind.unisale.database_schema;


import android.provider.BaseColumns;

public final class UnisaleDbContract {

    public UnisaleDbContract(){}

    public static abstract class User implements BaseColumns{
        public static final String TABLE_NAME = "users";
        public static final String ID = "u_id";
        public static final String USERNAME = "username";
        public static final String PASSWORD = "password";
        public static final String SCHOOL_ID = "school_id";
    }

    public static abstract class School implements BaseColumns{
        public static final String TABLE_NAME = "schools";
        public static final String ID = "s_id";
        public static final String SCHOOL = "school";
    }

    public static abstract class Listings implements BaseColumns{
        public static final String TABLE_NAME = "listings";
        public static final String USER_ID = "u_id";
        public static final String LIST_NUM = "list_num";
        public static final String TITLE = "title";
        public static final String DESCRIPTION = "description";
        public static final String PRICE = "price";
        public static final String CATEGORY = "category";
        public static final String CONDITION = "condition";
        public static final String IMAGE = "image";
        public static final String FINISHED = "finished";
        public static final String SCHOOL = "school";
    }

    public static abstract class Converstions implements BaseColumns {
        public static final String TABLE_NAME = "conversations";
        public static final String USER_ID = "user_id";
        public static final String OTHER_U_ID = "ou_id";
        public static final String OWNER_ID = "owner_id";
        public static final String LIST_NUM = "list_num";
        public static final String DATE = "date";
        public static final String MESSAGES = "messages";
    }
}
