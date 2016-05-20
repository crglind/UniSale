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

public class FindUsersAsyncTask extends AsyncTask<ArrayList, Void, ArrayList> {

        private WeakReference<MessageSelectionFragment> mWeakRefMain;

        public FindUsersAsyncTask(MessageSelectionFragment myFrag){
            this.mWeakRefMain = new WeakReference<MessageSelectionFragment>(myFrag);
        }

        @Override
        protected ArrayList<String> doInBackground(ArrayList... ids){return getOtherUsers(ids[0]);}

        @Override
        protected void onPostExecute(ArrayList otherUsers){
            super.onPostExecute(otherUsers);
            MessageSelectionFragment frag = mWeakRefMain.get();
            if(frag == null)return; // host disconnect
            frag.getListings(otherUsers);
        }

        private ArrayList<String> getOtherUsers(ArrayList<Integer> ids) {
            ArrayList<String> otherUsers = new ArrayList<>();
            for(int i=0; i<ids.size(); i++){
                otherUsers.add(getName(ids.get(i)));
            }
            return otherUsers;
        }

        private String getName(int uID){
            String otherUsername;
            try {
                URL url = new URL
                        ("http://www.cs.scranton.edu/~lindc4/UniSale/unisale.php/userid/" + uID);
                HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                conn.connect();
                InputStream is = conn.getInputStream();
                BufferedReader streamReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                StringBuilder responseStringBuilder = new StringBuilder();
                String inputString;
                while((inputString = streamReader.readLine()) != null)
                    responseStringBuilder.append(inputString).append("\n");
                JSONObject obj = new JSONObject(responseStringBuilder.toString());
                otherUsername = obj.getString("username");
            }catch (IOException | JSONException e){
                otherUsername = null;
            }
            return otherUsername;
        }
}
