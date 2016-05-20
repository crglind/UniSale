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

public class MessageSelectionAsyncTask extends AsyncTask<Integer, Void, ArrayList<Integer[]>>{

    private WeakReference<MessageSelectionFragment> mWeakRefMain;

    public MessageSelectionAsyncTask(MessageSelectionFragment myFrag){
        this.mWeakRefMain = new WeakReference<MessageSelectionFragment>(myFrag);
    }

    @Override
    protected ArrayList<Integer[]> doInBackground(Integer... uID){return getOtherUserIDs(uID[0]);}

    @Override
    protected void onPostExecute(ArrayList infoList){
        super.onPostExecute(infoList);
        MessageSelectionFragment frag = mWeakRefMain.get();
        if(frag == null)return; // host disconnect
        if(infoList == null) frag.noMessages();
        else frag.makeList(infoList);
    }

    private ArrayList<Integer[]> getOtherUserIDs(int uID) {
        ArrayList<Integer[]> messageInfo = new ArrayList<>();
        ArrayList<Integer> previousIDs = new ArrayList<>();
        JSONArray array = getInfo(uID);
        try {
            for (int i = 0; i < array.length(); i++) {
                Integer[] info = new Integer[3];
                JSONObject obj = array.getJSONObject(i);
                if (!previousIDs.contains(obj.getInt("sender_id")) && !previousIDs.contains(obj.getInt("receiver_id"))){
                    if(obj.getInt("sender_id") == uID) {
                        info[0] = obj.getInt("receiver_id");
                        previousIDs.add(obj.getInt("receiver_id"));
                    }
                    else {
                        info[0] = obj.getInt("sender_id");
                        previousIDs.add(obj.getInt("sender_id"));
                    }
                    info[1] = obj.getInt("owner_id");
                    info[2] = obj.getInt("listNum");
                    messageInfo.add(info);
                }
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        return messageInfo;
    }

    private JSONArray getInfo(int uID){
        JSONArray jsonArray;
        try {
            URL url = new URL
                    ("http://www.cs.scranton.edu/~lindc4/UniSale/unisale.php/message_info/" + uID);
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
