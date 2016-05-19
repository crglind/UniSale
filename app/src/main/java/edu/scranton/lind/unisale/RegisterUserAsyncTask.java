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

public class RegisterUserAsyncTask extends AsyncTask<String,Void, Integer>{

    private WeakReference<NewUserActivity> mWeakRefActivity;

    public RegisterUserAsyncTask(NewUserActivity main){
        this.mWeakRefActivity = new WeakReference<>(main);
    }

    @Override
    protected Integer doInBackground(String... fields){
        return responseBuilder(fields);
    }

    @Override
    protected void onPostExecute(Integer response){
        super.onPostExecute(response);
        NewUserActivity main = mWeakRefActivity.get();
        if(main == null) return;
        if(response > 0) main.success(response);
        else if(response == -1) main.usernameTaken();
        else main.failedToCreate();
    }

    private int responseBuilder(String[] fields){
        try {
            URL url = new URL("http://www.cs.scranton.edu/~lindc4/UniSale/unisale.php/new_user");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            String postParameters = "userName=" + fields[0] + "&" + "passWord=" + fields[1] + "&" +
                    "schoolid=" + Integer.parseInt(fields[2]);

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
            if(inputStr.equals("Username Exists"))return -1;
            else if(inputStr.equals("{'status':'failed'"))return -2;
            else return Integer.parseInt(inputStr);
        }catch(IOException e){
            e.printStackTrace();
        }
        return -2;
    }
}
