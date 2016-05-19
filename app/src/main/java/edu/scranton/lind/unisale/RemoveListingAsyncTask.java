package edu.scranton.lind.unisale;


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

public class RemoveListingAsyncTask extends AsyncTask<Integer, Void, Integer> {

    private WeakReference<MyListingsFragment> mWeakRefMain;

    public RemoveListingAsyncTask(MyListingsFragment myFrag){
        this.mWeakRefMain = new WeakReference<MyListingsFragment>(myFrag);
    }

    @Override
    protected void onPreExecute(){super.onPreExecute();}

    @Override
    protected Integer doInBackground(Integer... ids){
        return removeListing(ids[0], ids[1]);
    }

    @Override
    protected void onPostExecute(Integer response){
        super.onPostExecute(response);
        MyListingsFragment frag = mWeakRefMain.get();
        if(frag == null)return; // host disconnect
        if(response == 1) frag.deleted();
        else if(response == -1) frag.error();
        else frag.failedToConnect();
    }

    private Integer removeListing(int uID, int listNum){
        try {
            URL url = new URL
                    ("http://www.cs.scranton.edu/~lindc4/UniSale/unisale.php/delete/"
                            + uID + "/" + listNum);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.connect();
            InputStream is = conn.getInputStream();
            BufferedReader streamReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            StringBuilder responseStringBuilder = new StringBuilder();
            String inputString;
            while((inputString = streamReader.readLine()) != null)
                responseStringBuilder.append(inputString);
            inputString = responseStringBuilder.toString();
            if(inputString.equals("failure"))return -1;
            else return Integer.parseInt(inputString);
        }catch (IOException e){
            e.printStackTrace();
        }
        return -2;
    }
}
