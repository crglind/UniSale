package edu.scranton.lind.unisale;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.Toast;

import java.util.ArrayList;

import edu.scranton.lind.unisale.database_schema.UnisaleDbContract.Listings;

public class ListingFragment extends ListFragment implements SwipeRefreshLayout.OnRefreshListener{

    public interface DbProvider{SQLiteDatabase getReadableDb();}

    public static final String USER = "edu.scranton.lind.listingfragment.USER";
    public static final String SCHOOL = "edu.scranton.lind.listingfragment.SCHOOl";

    private DbProvider provider;
    private SQLiteDatabase mDatabase;
    private int mUID;
    private int mSchoolID;

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        mDatabase = provider.getReadableDb();
        Bundle bundle = getArguments();
        mUID = bundle.getInt(USER);
        mSchoolID = bundle.getInt(SCHOOL);
        ListingAsyncTask la = new ListingAsyncTask(this);
        la.execute(mUID, mSchoolID);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        provider = (DbProvider)getActivity();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        mDatabase = null;
    }

    @Override
    public void onRefresh(){

    }

    public void setListings(ArrayList<Listing> listings){
        String layout;
        if(getActivity().findViewById(R.id.small_container) != null) layout = "small";
        else layout = "large";
        ListingAdapter adapter =
                new ListingAdapter(getActivity().getBaseContext(), listings, this, layout);
        setListAdapter(adapter);
    }

}
