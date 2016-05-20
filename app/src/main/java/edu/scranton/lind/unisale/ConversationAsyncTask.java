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
import java.util.HashMap;

public class ConversationAsyncTask extends AsyncTask<String, Void, ArrayList<String>> {

    private WeakReference<ConversationFragment> mWeakRefMain;

    public ConversationAsyncTask(ConversationFragment myFrag){
        this.mWeakRefMain = new WeakReference<ConversationFragment>(myFrag);
    }

    @Override
    protected ArrayList<String> doInBackground(String... ids){
        return getMyConversations(Integer.parseInt(ids[0]), Integer.parseInt(ids[1]), Integer.parseInt(ids[2]), ids[3], ids[4], Integer.parseInt(ids[5]));
    }

    @Override
    protected void onPostExecute(ArrayList conversation){
        super.onPostExecute(conversation);
        ConversationFragment frag = mWeakRefMain.get();
        if(frag == null)return; // host disconnect
        frag.showConvo(conversation);
    }

    private ArrayList<String> getMyConversations(int uID, int owner, int listNum, String otherUser, String user, int myId){
        ArrayList convo = new ArrayList();
        JSONArray convoArray = getConvo(myId, uID, listNum);
        try{
            for(int i=0; i<convoArray.length(); i++){
                JSONObject obj = convoArray.getJSONObject(i);
                String message;
                if(obj.getInt("sender_id") == myId) message = user + ": ";
                else message = otherUser + ": ";
                message = message + obj.getString("message");
                convo.add(message);
            }
        }catch (JSONException e){
            return null;
        }
        return convo;
    }

    private JSONArray getConvo(int myID, int uID, int listNum){
        JSONArray jsonArray = null;
        try {
            URL url = new URL
                    ("http://www.cs.scranton.edu/~lindc4/UniSale/unisale.php/convo/" + myID + "/" +
                            uID + "/" + listNum);
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
