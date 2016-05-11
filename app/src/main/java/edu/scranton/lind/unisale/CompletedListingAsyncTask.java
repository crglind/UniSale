package edu.scranton.lind.unisale;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import edu.scranton.lind.unisale.database_schema.UnisaleDbContract;

public class CompletedListingAsyncTask extends AsyncTask<Integer, Void, ArrayList<Listing>> {

    private WeakReference<CompletedListings> mWeakRefMain;
    private SQLiteDatabase mDatabase;

    public CompletedListingAsyncTask(CompletedListings myFrag, SQLiteDatabase database){
        this.mWeakRefMain = new WeakReference<CompletedListings>(myFrag);
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
        CompletedListings frag = mWeakRefMain.get();
        if(frag == null)return; // host disconnect
        frag.setListings(listings);
    }

    private ArrayList<Listing> getAllMyListings(int uID){
        String[] desiredColumns = {UnisaleDbContract.Listings.USER_ID, UnisaleDbContract.Listings.LIST_NUM, UnisaleDbContract.Listings.TITLE, UnisaleDbContract.Listings.DESCRIPTION,
                UnisaleDbContract.Listings.PRICE, UnisaleDbContract.Listings.CATEGORY, UnisaleDbContract.Listings.CONDITION, UnisaleDbContract.Listings.FINISHED,
                UnisaleDbContract.Listings.SCHOOL};
        String selection = UnisaleDbContract.Listings.USER_ID + " = ?"+ " AND " +
                UnisaleDbContract.Listings.FINISHED + " = ?";
        String[] selectionArgs = {Integer.toString(uID), "1"};
        Cursor lCursor = mDatabase.query(UnisaleDbContract.Listings.TABLE_NAME, desiredColumns, selection,
                selectionArgs, null, null, null);
        ArrayList<Listing> cListings = new ArrayList<>();
        for(lCursor.moveToFirst(); !lCursor.isAfterLast(); lCursor.moveToNext()){
            Listing myListing = new Listing();
            myListing.setUID(lCursor.getInt(0));
            myListing.setListNum(lCursor.getInt(1));
            myListing.setListTitle(lCursor.getString(2));
            myListing.setDescription(lCursor.getString(3));
            myListing.setPrice(lCursor.getDouble(4));
            myListing.setCondition(lCursor.getString(5));
            myListing.setCategory(lCursor.getString(6));
            cListings.add(myListing);
        }
        lCursor.close();
        return cListings;

    }

}
