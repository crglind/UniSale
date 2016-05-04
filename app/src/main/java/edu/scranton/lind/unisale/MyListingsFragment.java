package edu.scranton.lind.unisale;

import android.support.v4.app.ListFragment;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import edu.scranton.lind.unisale.database_schema.UnisaleDbContract.Listings;


public class MyListingsFragment extends ListFragment {

    public interface DbProvider{SQLiteDatabase getReadableDb();}

    public static final String USER = "edu.scranton.lind.mylistingsfragment.USER";

    private String ONE = Integer.toString(1);
    private SQLiteDatabase mDatabase;
    private DbProvider provider;
    private int mUID;
    private List<HashMap<String, String>> mListings;

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        mDatabase = provider.getReadableDb();
        Bundle bundle = getArguments();
        mUID = bundle.getInt(USER);
        getAllMyListings();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        provider = (DbProvider)getActivity();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        provider = null;
    }

    private void getAllMyListings(){
        String[] desiredColumns = {Listings.USER_ID, Listings.TITLE, Listings.DESCRIPTION,
                Listings.PRICE, Listings.CATEGORY, Listings.CONDITION, Listings.FINISHED,
                Listings.SCHOOL};
        String selection = Listings.USER_ID + " = ?"+ " AND " +
                Listings.FINISHED + " = ?";
        String[] selectionArgs = {Integer.toString(mUID), ONE};
        Cursor lCursor = mDatabase.query(Listings.TABLE_NAME, desiredColumns, selection,
                selectionArgs, null, null, null);
        mListings = new ArrayList<>();
        for(lCursor.moveToFirst(); !lCursor.isAfterLast(); lCursor.moveToNext()){
            HashMap<String, String> hm = new HashMap<>();
            hm.put("title", lCursor.getString(1));
            hm.put("desc", lCursor.getString(2));
            hm.put("price", "Price: " + lCursor.getString(3));
            hm.put("cat", "Category: " + lCursor.getString(4));
            hm.put("cond", "Condition: " + lCursor.getString(5));
            mListings.add(hm);
        }
        lCursor.close();
        String[] from = {"title", "desc", "price", "cat", "cond"};
        int[] to = {R.id.listing_title, R.id.listing_description,
                R.id.listing_price, R.id.listing_category, R.id.listing_condition};
        SimpleAdapter adapter = new SimpleAdapter
                (getActivity().getBaseContext(), mListings, R.layout.my_listings_row_layout, from, to);
        setListAdapter(adapter);
    }
}
