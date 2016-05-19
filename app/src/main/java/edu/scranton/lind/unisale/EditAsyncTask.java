package edu.scranton.lind.unisale;


import android.os.AsyncTask;
import android.support.annotation.IntegerRes;

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

public class EditAsyncTask extends AsyncTask<Listing,Void, Integer> {

    private WeakReference<EditFragment> mWeakRefActivity;

    public EditAsyncTask(EditFragment main){
        this.mWeakRefActivity = new WeakReference<>(main);
    }

    @Override
    protected Integer doInBackground(Listing... listing){return responseBuilder(listing[0]);}

    @Override
    protected void onPostExecute(Integer response){
        super.onPostExecute(response);
        EditFragment main = mWeakRefActivity.get();
        if(main == null) return;
        if(response == 1) main.success();
        else if (response == -1) main.failureToUpdate();
        else main.failedToConnect();
    }

    private int responseBuilder(Listing listing){
        try {
            URL url = new URL
                    ("http://www.cs.scranton.edu/~lindc4/UniSale/unisale.php/edit_listing/" +
                    listing.getUID() + "/" +
                    listing.getListNum() + "/" +
                    listing.getListTitle() + "/" +
                    removeSpaces(listing.getDescription()) + "/" +
                    listing.getPrice() + "/" +
                    removeSpaces(listing.getCategory()) + "/" +
                    listing.getCondition());
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.connect();
            InputStream is = conn.getInputStream();
            BufferedReader streamReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            StringBuilder responseStringBuilder = new StringBuilder();
            String inputString;
            while((inputString = streamReader.readLine()) != null)
                responseStringBuilder.append(inputString);
            /*URL url = new URL("http://www.cs.scranton.edu/~lindc4/UniSale/unisale.php/edit_listing");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("PUT");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            String putParameters =
                    "ID=" + listing.getUID() + "&" +
                    "ListNum=" + listing.getListNum() + "&" +
                    "Title=" + listing.getListTitle() + "&" +
                    "Description=" + listing.getDescription() + "&" +
                    "Price=" + listing.getPrice() + "&" +
                    "Category=" + listing.getCategory() + "&" +
                    "Condition=" + listing.getCondition();

            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(putParameters);
            writer.flush();
            writer.close();
            os.close();
            conn.connect();
            InputStream is = conn.getInputStream();
            BufferedReader streamReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            StringBuilder responseStrBuilder = new StringBuilder();
            String inputStr;
            while((inputStr = streamReader.readLine()) != null)
                responseStrBuilder.append(inputStr);*/
            inputString = responseStringBuilder.toString();
            if (inputString.equals("failure")){
                return -1;
            }
            else {
                int rowsAffected = Integer.parseInt(inputString);
                return rowsAffected;
            }

        }catch(IOException e){
            e.printStackTrace();
        }
        return -2;
    }

    private String removeSpaces(String string){
        return string.replaceAll(" ", "%20");
    }
}

