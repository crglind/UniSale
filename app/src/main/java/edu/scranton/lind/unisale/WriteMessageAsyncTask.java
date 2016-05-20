package edu.scranton.lind.unisale;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class WriteMessageAsyncTask extends AsyncTask<String, Void, Integer> {

    private WeakReference<ConversationFragment> mWeakRefMain;

    public WriteMessageAsyncTask(ConversationFragment myFrag) {
        this.mWeakRefMain = new WeakReference<ConversationFragment>(myFrag);
    }

    @Override
    protected Integer doInBackground(String... message) {
        return sendMessage(message[0], Integer.parseInt(message[1]), Integer.parseInt(message[2]), Integer.parseInt(message[3]), Integer.parseInt(message[4]));
    }

    @Override
    protected void onPostExecute(Integer response) {
        super.onPostExecute(response);
        ConversationFragment frag = mWeakRefMain.get();
        if (frag == null) return; // host disconnect
        if(response > 0)frag.success();
        else if(response == -1)frag.unsuccessful();
        else frag.unableToConnect();

    }

    private int sendMessage(String message, int otherID, int ownerID, int listNum, int myID) {
        try {
            URL url = new URL("http://www.cs.scranton.edu/~lindc4/UniSale/unisale.php/write_message");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            String postParameters =
                    "myID=" + myID + "&" +
                            "oID" + otherID + "&" +
                            "Message" + message + "&" +
                            "Owner" + ownerID + "&" +
                            "ListNum" + listNum;

            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(postParameters);
            writer.flush();
            writer.close();
            os.close();
            conn.connect();
            InputStream is = conn.getInputStream();
            BufferedReader streamReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            StringBuilder responseStrBuilder = new StringBuilder();
            String inputStr;
            while ((inputStr = streamReader.readLine()) != null)
                responseStrBuilder.append(inputStr);
            inputStr = responseStrBuilder.toString();
            System.out.println(inputStr);
            if (inputStr.equals("{'status':'failed'")) return -1;
            else return Integer.parseInt(inputStr);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return -2;
    }
}
