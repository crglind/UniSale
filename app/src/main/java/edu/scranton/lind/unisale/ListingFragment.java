package edu.scranton.lind.unisale;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import edu.scranton.lind.unisale.database_schema.UnisaleDbContract.Listings;

public class ListingFragment extends ListFragment implements SwipeRefreshLayout.OnRefreshListener{

    public interface OnListingSelectedListener {void onListingChosen
            (int position, int listingId, int listNum, String title);}
    public interface DbProvider{SQLiteDatabase getReadableDb();}

    public static final String USER = "edu.scranton.lind.listingfragment.USER";
    public static final String SCHOOL = "edu.scranton.lind.listingfragment.SCHOOl";

    private String ZERO = Integer.toString(0);
    private OnListingSelectedListener mListener;
    private Activity mActivity = null;
    private DbProvider provider;
    private SQLiteDatabase mDatabase;
    private ArrayList<HashMap<String, String>> mListings;
    private ArrayList<Integer> mListingIds;
    private ArrayList<String> mListingTitles;
    private int mUID;
    private int mSchoolID;
    //public String[] exTitleArray;
    //public String[] exPriceArray;
    //public String[] exCondArray;

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        mDatabase = provider.getReadableDb();
        Bundle bundle = getArguments();
        mUID = bundle.getInt(USER);
        mSchoolID = bundle.getInt(SCHOOL);
        getAllListings();
        //exTitleArray = getResources().getStringArray(R.array.example_titles);
        //exPriceArray = getResources().getStringArray(R.array.example_prices);
        //exCondArray = getResources().getStringArray(R.array.example_conditions);

    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id){
        try{
            mListener = (OnListingSelectedListener) mActivity;
        }catch (ClassCastException e){
            throw new ClassCastException(mActivity.toString() +
            " must implement OnListingSelectedListener");
        }
        mListener.onListingChosen
                (position, mListingIds.get(position), mListingIds.get(position+1), mListingTitles.get(position));
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mActivity = (Activity) context;
        provider = (DbProvider)getActivity();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        mDatabase = null;
    }

    @Override
    public void onRefresh(){

    }

    public void getAllListings(){
        String[] desiredColumns = {Listings.USER_ID, Listings.LIST_NUM, Listings.TITLE, Listings.DESCRIPTION,
                Listings.PRICE, Listings.CATEGORY, Listings.CONDITION, Listings.FINISHED,
                Listings.SCHOOL};
        String selection = Listings.USER_ID + " != ? AND " +
                Listings.FINISHED + " = ? AND " + Listings.SCHOOL + " = ?";
        String[] selectionArgs = {Integer.toString(mUID), ZERO, Integer.toString(mSchoolID)};
        Cursor lCursor = mDatabase.query(Listings.TABLE_NAME, desiredColumns, selection,
                selectionArgs, null, null, null);
        mListings = new ArrayList<>();
        mListingIds = new ArrayList<>();
        for(lCursor.moveToFirst(); !lCursor.isAfterLast(); lCursor.moveToNext()){
            HashMap<String, String> hm = new HashMap<>();
            mListingIds.add(lCursor.getInt(0));
            mListingIds.add(lCursor.getInt(1));
            mListingTitles.add(lCursor.getString(2));
            hm.put("title", lCursor.getString(2));
            hm.put("desc", lCursor.getString(3));
            hm.put("price", "Price: " + lCursor.getString(4));
            hm.put("cat", "Category: " + lCursor.getString(5));
            hm.put("cond", "Condition: " + lCursor.getString(6));
            mListings.add(hm);
        }
        lCursor.close();
        String[] from = {"title", "desc", "price", "cat", "cond"};
        int[] to = {R.id.listing_title, R.id.listing_description,
                R.id.listing_price, R.id.listing_category, R.id.listing_condition};
        SimpleAdapter adapter = new SimpleAdapter
                (getActivity().getBaseContext(), mListings, R.layout.listing_row_layout, from, to);
        setListAdapter(adapter);
    }
}
