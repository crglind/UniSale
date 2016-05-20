package edu.scranton.lind.unisale;

import android.os.AsyncTask;

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
import java.util.List;

public class FindListingsAsyncTask extends AsyncTask<ArrayList, Void, ArrayList>{

    private WeakReference<MessageSelectionFragment> mWeakRefMain;

    public FindListingsAsyncTask(MessageSelectionFragment myFrag){
        this.mWeakRefMain = new WeakReference<MessageSelectionFragment>(myFrag);
    }

    @Override
    protected ArrayList<String> doInBackground(ArrayList... keys){return getItems(keys[0]);}

    @Override
    protected void onPostExecute(ArrayList titles){
        super.onPostExecute(titles);
        MessageSelectionFragment frag = mWeakRefMain.get();
        if(frag == null)return; // host disconnect
        frag.setup(titles);
    }

    private ArrayList<String> getItems(ArrayList<Integer[]> keys){
        ArrayList<String> discussedListings = new ArrayList<>();
        for(int i=0; i<keys.size(); i++){
            discussedListings.add(getListing(keys.get(i)));
        }
        return discussedListings;
    }

    private String getListing(Integer[] keys){
        JSONObject jsonObject = null;
        String title = null;
        try {
            URL url = new URL
                    ("http://www.cs.scranton.edu/~lindc4/UniSale/unisale.php/get_title/" + keys[0]
                    + "/" + keys[1]);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.connect();
            InputStream is = conn.getInputStream();
            BufferedReader streamReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            StringBuilder responseStringBuilder = new StringBuilder();
            String inputString;
            while((inputString = streamReader.readLine()) != null)
                responseStringBuilder.append(inputString).append("\n");
            jsonObject = new JSONObject(responseStringBuilder.toString());
            title = jsonObject.getString("title");
        }catch (IOException | JSONException e){
            e.printStackTrace();
        }
        return title;
    }
}
