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
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import edu.scranton.lind.unisale.database_schema.UnisaleDbContract.Listings;

public class ListingAsyncTask extends AsyncTask<Integer, Void, ArrayList<Listing>>{

    private WeakReference<ListingFragment> mWeakRefMain;

    public ListingAsyncTask(ListingFragment myFrag){
        this.mWeakRefMain = new WeakReference<ListingFragment>(myFrag);
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
        JSONArray jsonListings = loadListings(uID, schoolID);
        ArrayList<Listing> listings = new ArrayList<>();
        /*String[] desiredColumns = {Listings.USER_ID, Listings.LIST_NUM, Listings.TITLE, Listings.DESCRIPTION,
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
                listings.add(newListing);
            }

        }catch (JSONException e){
            return null;
        }
        return listings;
    }

    private JSONArray loadListings(int uID, int schoolID){
        JSONArray jsonArray = null;
        try {
            URL url = new URL
                    ("http://www.cs.scranton.edu/~lindc4/UniSale/unisale.php/listings/"
                            + uID + "/" + schoolID);
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
