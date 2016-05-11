package edu.scranton.lind.unisale.database_schema;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


import edu.scranton.lind.unisale.database_schema.UnisaleDbContract.User;
import edu.scranton.lind.unisale.database_schema.UnisaleDbContract.School;
import edu.scranton.lind.unisale.database_schema.UnisaleDbContract.Listings;
import edu.scranton.lind.unisale.database_schema.UnisaleDbContract.Converstions;

public class UnisaleDbOpenHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "unisale.db";
    public static final int DATABASE_VERSION = 8;

    private static final String TEXT_TYPE = " TEXT";
    private static final String INTEGER_TYPE = " INTEGER";
    private static final String DOUBLE_TYPE =  " DOUBLE";
    private static final String COMMA = ", ";

    private static final String SQL_CREATE_USER = "CREATE TABLE " + User.TABLE_NAME + "(" +
            User.ID + INTEGER_TYPE + " primary key " + COMMA +
            User.USERNAME + TEXT_TYPE + COMMA +
            User.PASSWORD + TEXT_TYPE + COMMA +
            User.SCHOOL_ID + INTEGER_TYPE + ")";

    private static final String SQL_CREATE_SCHOOL = "CREATE TABLE " + School.TABLE_NAME + "(" +
            School.ID + INTEGER_TYPE + " primary key " + COMMA +
            School.SCHOOL + TEXT_TYPE + ")";

    private static final String SQL_CREATE_LISTINGS = "CREATE TABLE " + Listings.TABLE_NAME + "(" +
            Listings.USER_ID + INTEGER_TYPE + COMMA +
            Listings.LIST_NUM + INTEGER_TYPE + COMMA +
            Listings.TITLE + TEXT_TYPE + COMMA +
            Listings.DESCRIPTION + TEXT_TYPE + COMMA +
            Listings.PRICE + DOUBLE_TYPE + COMMA +
            Listings.CATEGORY + TEXT_TYPE + COMMA +
            Listings.CONDITION + TEXT_TYPE + COMMA +
            Listings.FINISHED + INTEGER_TYPE + COMMA +
            Listings.SCHOOL + INTEGER_TYPE + ")";

    private static final String SQL_CREATE_CONVERSATIONS = "CREATE TABLE " + Converstions.TABLE_NAME
            + "(" + Converstions.USER_ID + INTEGER_TYPE + COMMA +
            Converstions.OTHER_U_ID + INTEGER_TYPE + COMMA +
            Converstions.OWNER_ID + INTEGER_TYPE + COMMA +
            Converstions.LIST_NUM + INTEGER_TYPE + COMMA +
            Converstions.DATE + TEXT_TYPE + COMMA +
            Converstions.MESSAGES + TEXT_TYPE + ")";

    private static final String SQL_DROP_USER = "DROP TABLE IF EXISTS " + User.TABLE_NAME;
    private static final String SQL_DROP_SCHOOL = "DROP TABLE IF EXISTS " + School.TABLE_NAME;
    private static final String SQL_DROP_LISTINGS = "DROP TABLE IF EXISTS " + Listings.TABLE_NAME;
    private static final String SQL_DROP_CONVERSATIONS = "DROP TABLE IF EXISTS "
            + Converstions.TABLE_NAME;

    public UnisaleDbOpenHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(SQL_CREATE_USER);
        db.execSQL(SQL_CREATE_SCHOOL);
        db.execSQL(SQL_CREATE_LISTINGS);
        db.execSQL(SQL_CREATE_CONVERSATIONS);
        initialize(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL(SQL_DROP_USER);
        db.execSQL(SQL_DROP_SCHOOL);
        db.execSQL(SQL_DROP_LISTINGS);
        db.execSQL(SQL_DROP_CONVERSATIONS);
        onCreate(db);
    }

    private void initialize(SQLiteDatabase db){
        //Set example user
        String username = "exampleName";
        String password = "password";
        int u_school = 0;
        ContentValues newUser = new ContentValues();
        newUser.put(User.USERNAME, username);
        newUser.put(User.PASSWORD, password);
        newUser.put(User.SCHOOL_ID, u_school);
        long uID = db.insert(User.TABLE_NAME, null, newUser);
        //Set shrunk school table
        String[] schools = {"University of Scranton",
                "Marywood University", "Keystone College", "Kings College"};
        for(int s=0; s<4; s++){
            ContentValues schoolValues = new ContentValues();
            schoolValues.put(School.ID, s);
            schoolValues.put(School.SCHOOL, schools[s]);
            long sID = db.insert(School.TABLE_NAME, null, schoolValues);
        }
        //Set example listings
        String[] items = {"Couch", "Science Textbook", "Ipod",
                "Literature Book", "Tablet" ,"Silverware"};
        double[] prices = {20.00, 15.00, 10.00, 5.00, 0.00, 3.50};
        String[] condition = {"POOR", "GOOD", "OKAY", "PERFECT", "GREAT", "POOR"};
        String[] category = {"Furniture", "Books", "Electronics", "Books", "Electronics", "Home Goods"};
        int[] school = {0, 0, 1, 2, 0, 3};
        int[] finished = {0, 1, 0, 0, 0, 0};
        String[] descriptions = {"it's a couch", "a science textbook", "great ipod",
                "Lit book", "Nexus 7", "Old silverware"};
        int[] listNum = {0, 0, 0, 0, 0, 0};
        for(int l=0; l<6; l++){
            ContentValues listing = new ContentValues();
            listing.put(Listings.USER_ID, l);
            listing.put(Listings.LIST_NUM, listNum[0]);
            listing.put(Listings.TITLE, items[l]);
            listing.put(Listings.DESCRIPTION, descriptions[l]);
            listing.put(Listings.PRICE, prices[l]);
            listing.put(Listings.CATEGORY, category[l]);
            listing.put(Listings.CONDITION, condition[l]);
            listing.put(Listings.FINISHED, finished[l]);
            listing.put(Listings.SCHOOL, school[l]);
            long lID = db.insert(Listings.TABLE_NAME, null, listing);
        }
    }
}
