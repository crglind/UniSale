package edu.scranton.lind.unisale;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.ListFragment;

import java.util.ArrayList;

import edu.scranton.lind.unisale.database_schema.UnisaleDbContract.Listings;


public class CompletedListings extends ListFragment {

    public interface DbProvider{SQLiteDatabase getReadableDb();}

    public static final String USER = "edu.scranton.lind.completedlisting.USER";

    private SQLiteDatabase mDatabase;
    private int mUID;
    private DbProvider provider;
    private CompletedListingAdapter mAdapter;

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        mDatabase = provider.getReadableDb();
        Bundle bundle = getArguments();
        mUID = bundle.getInt(USER);
        CompletedListingAsyncTask ca = new CompletedListingAsyncTask(this, mDatabase);
        ca.execute(mUID);
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
        mAdapter = new
                CompletedListingAdapter(getActivity().getBaseContext(), listings);
        setListAdapter(mAdapter);
    }
}
