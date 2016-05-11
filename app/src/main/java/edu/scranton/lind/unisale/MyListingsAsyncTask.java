package edu.scranton.lind.unisale;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import edu.scranton.lind.unisale.database_schema.UnisaleDbContract.Listings;

public class MyListingsAsyncTask extends AsyncTask<Integer, Void, ArrayList<Listing>>{

    private WeakReference<MyListingsFragment> mWeakRefMain;
    private SQLiteDatabase mDatabase;

    public MyListingsAsyncTask(MyListingsFragment myFrag, SQLiteDatabase database){
        this.mWeakRefMain = new WeakReference<MyListingsFragment>(myFrag);
        mDatabase = database;
    }

    @Override
    protected void onPreExecute(){super.onPreExecute();}

    @Override
    protected ArrayList<Listing> doInBackground(Integer... uID){
        return getAllMyListings(uID[0]);
    }

    @Override
    protected void onPostExecute(ArrayList listings){
        super.onPostExecute(listings);
        MyListingsFragment frag = mWeakRefMain.get();
        if(frag == null)return; // host disconnect
        frag.setListings(listings);
    }

    private ArrayList<Listing> getAllMyListings(int uID){
        String[] desiredColumns = {Listings.USER_ID, Listings.LIST_NUM, Listings.TITLE, Listings.DESCRIPTION,
                Listings.PRICE, Listings.CATEGORY, Listings.CONDITION, Listings.FINISHED,
                Listings.SCHOOL};
        String selection = Listings.USER_ID + " = ?"+ " AND " +
                Listings.FINISHED + " = ?";
        String[] selectionArgs = {Integer.toString(uID), "0"};
        Cursor lCursor = mDatabase.query(Listings.TABLE_NAME, desiredColumns, selection,
                selectionArgs, null, null, null);
        ArrayList<Listing> arrayListings = new ArrayList<>();
        for(lCursor.moveToFirst(); !lCursor.isAfterLast(); lCursor.moveToNext()){
            Listing myListing = new Listing();
            myListing.setUID(lCursor.getInt(0));
            myListing.setListNum(lCursor.getInt(1));
            myListing.setListTitle(lCursor.getString(2));
            myListing.setDescription(lCursor.getString(3));
            myListing.setPrice(lCursor.getDouble(4));
            myListing.setCondition(lCursor.getString(5));
            myListing.setCategory(lCursor.getString(6));
            arrayListings.add(myListing);
        }
        lCursor.close();
        return arrayListings;

    }
}
