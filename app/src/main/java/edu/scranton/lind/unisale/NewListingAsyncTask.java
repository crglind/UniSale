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

public class NewListingAsyncTask extends AsyncTask<Listing,Void, Integer> {

    private WeakReference<NewListing> mWeakRefActivity;

    public NewListingAsyncTask(NewListing main){
        this.mWeakRefActivity = new WeakReference<>(main);
    }

    @Override
    protected Integer doInBackground(Listing... listing){return responseBuilder(listing[0]);}

    @Override
    protected void onPostExecute(Integer response){
        super.onPostExecute(response);
        NewListing main = mWeakRefActivity.get();
        if(main == null) return;
        if(response > 0) main.success(response);
        else if(response == -1) main.failedToInsert();
        else main.failedToConnect();
    }

    private int responseBuilder(Listing listing){
        try {
            URL url = new URL("http://www.cs.scranton.edu/~lindc4/UniSale/unisale.php/new_listing");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            String postParameters =
                    "ID="+ listing.getUID() + "&" +
                    "Title=" + listing.getListTitle() + "&" +
                    "Description=" + listing.getDescription() + "&" +
                    "Price=" + listing.getPrice() + "&" +
                    "Category=" + listing.getCategory() + "&" +
                    "Condition=" + listing.getCondition() + "&" +
                    "SchoolID=" + listing.getSchoolID();

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
            while((inputStr = streamReader.readLine()) != null)
                responseStrBuilder.append(inputStr);
            inputStr = responseStrBuilder.toString();
            System.out.println(inputStr);
            if(inputStr.equals("{'status':'failed'"))return -1;
            else return Integer.parseInt(inputStr);
        }catch(IOException e){
            e.printStackTrace();
        }
        return -2;
    }
}
