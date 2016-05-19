package edu.scranton.lind.unisale;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import edu.scranton.lind.unisale.database_schema.UnisaleDbContract.Listings;

public class MyListingsAsyncTask extends AsyncTask<Integer, Void, ArrayList<Listing>>{

    private WeakReference<MyListingsFragment> mWeakRefMain;

    public MyListingsAsyncTask(MyListingsFragment myFrag){
        this.mWeakRefMain = new WeakReference<MyListingsFragment>(myFrag);
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
        JSONArray jsonListings = loadListings(uID);
        ArrayList<Listing> myListings = new ArrayList<>();
        /*String[] desiredColumns = {Listings.USER_ID, Listings.LIST_NUM, Listings.TITLE, Listings.DESCRIPTION,
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
        lCursor.close();*/
        try{
            for(int i=0; i<jsonListings.length(); i++){
                JSONObject obj = jsonListings.getJSONObject(i);
                Listing newListing = new Listing();
                newListing.setUID(obj.getInt("userID"));
                newListing.setListNum(obj.getInt("listNum"));
                newListing.setListTitle(obj.getString("title"));
                newListing.setDescription(obj.getString("description"));
                newListing.setPrice(obj.getDouble("price"));
                newListing.setCategory(obj.getString("category"));
                newListing.setCondition(obj.getString("condition"));
                newListing.setFinished(obj.getInt("finished"));
                newListing.setSchoolID(obj.getInt("schoolID"));
                myListings.add(newListing);
            }

        }catch (JSONException e){
            return null;
        }
        return myListings;

    }

    private JSONArray loadListings(int uID){
        JSONArray jsonArray = null;
        try {
            URL url = new URL
                    ("http://www.cs.scranton.edu/~lindc4/UniSale/unisale.php/listings/" + uID);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.connect();
            InputStream is = conn.getInputStream();
            BufferedReader streamReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            StringBuilder responseStringBuilder = new StringBuilder();
            String inputString;
            while((inputString = streamReader.readLine()) != null)
                responseStringBuilder.append(inputString).append("\n");
            jsonArray = new JSONArray(responseStringBuilder.toString());
        }catch (IOException | JSONException e){
            jsonArray = null;
        }
        return jsonArray;
    }
}
