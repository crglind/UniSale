package edu.scranton.lind.unisale;

import android.support.v4.app.ListFragment;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import java.util.ArrayList;

import edu.scranton.lind.unisale.database_schema.UnisaleDbContract.Listings;


public class MyListingsFragment extends ListFragment implements MyListingAdapter.MLAInterface{

    public interface DbProvider{SQLiteDatabase getReadableDb();}

    public static final String USER = "edu.scranton.lind.mylistingsfragment.USER";

    private SQLiteDatabase mDatabase;
    private DbProvider provider;
    private int mUID;
    private ArrayList<Listing> mListings;
    private MyListingAdapter mAdapter;

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        mDatabase = provider.getReadableDb();
        Bundle bundle = getArguments();
        mUID = bundle.getInt(USER);
        MyListingsAsyncTask ma = new MyListingsAsyncTask(this, mDatabase);
        ma.execute(mUID);
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

    public void setListings(ArrayList listings){
        mListings = listings;
        mAdapter = new MyListingAdapter(getActivity().getBaseContext(), listings, this);
        setListAdapter(mAdapter);
    }

    public void removePressed(int position){
        Listing listing = mListings.get(position);
        String selection = Listings.USER_ID + " = " + mUID + " AND " + Listings.LIST_NUM + " = " +
                listing.getListNum();
        mDatabase.delete(Listings.TABLE_NAME, selection, null);
        mListings.remove(position);
        mAdapter.notifyDataSetChanged();
    }

    public void editPressed(int position){

    }
}
