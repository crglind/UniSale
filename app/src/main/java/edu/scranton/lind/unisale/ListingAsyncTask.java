package edu.scranton.lind.unisale;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import edu.scranton.lind.unisale.database_schema.UnisaleDbContract.Listings;

public class ListingAsyncTask extends AsyncTask<Integer, Void, ArrayList<Listing>>{

    private WeakReference<ListingFragment> mWeakRefMain;
    private SQLiteDatabase mDatabase;

    public ListingAsyncTask(ListingFragment myFrag, SQLiteDatabase database){
        this.mWeakRefMain = new WeakReference<ListingFragment>(myFrag);
        mDatabase = database;
    }

    @Override
    protected void onPreExecute(){super.onPreExecute();}

    @Override
    protected ArrayList<Listing> doInBackground(Integer... ids){
        return getAllListings(ids[0], ids[1]);
    }

    @Override
    protected void onPostExecute(ArrayList listings){
        super.onPostExecute(listings);
        ListingFragment frag = mWeakRefMain.get();
        if(frag == null)return; // host disconnect
        frag.setListings(listings);
    }

    public ArrayList<Listing> getAllListings(int uID, int schoolID){
        String[] desiredColumns = {Listings.USER_ID, Listings.LIST_NUM, Listings.TITLE, Listings.DESCRIPTION,
                Listings.PRICE, Listings.CONDITION, Listings.CATEGORY, Listings.FINISHED,
                Listings.SCHOOL};
        String selection = Listings.USER_ID + " != ? AND " +
                Listings.FINISHED + " = ? AND " + Listings.SCHOOL + " = ?";
        String[] selectionArgs = {Integer.toString(uID), "0", Integer.toString(schoolID)};
        Cursor lCursor = mDatabase.query(Listings.TABLE_NAME, desiredColumns, selection,
                selectionArgs, null, null, null);
        ArrayList<Listing> listings= new ArrayList<>();
        for(lCursor.moveToLast(); !lCursor.isBeforeFirst(); lCursor.moveToPrevious()){
            Listing newListing = new Listing();
            newListing.setUID(lCursor.getInt(0));
            newListing.setListNum(lCursor.getInt(1));
            newListing.setListTitle(lCursor.getString(2));
            newListing.setDescription(lCursor.getString(3));
            newListing.setPrice(lCursor.getDouble(4));
            newListing.setCondition(lCursor.getString(5));
            newListing.setCategory(lCursor.getString(6));
            listings.add(newListing);
        }
        lCursor.close();
        return listings;
    }

}
