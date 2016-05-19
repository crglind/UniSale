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

import edu.scranton.lind.unisale.database_schema.UnisaleDbContract;

public class LoginAsyncTask extends AsyncTask<String, Void, Integer[]>{

    private WeakReference<UniSale_main> mWeakRefActivity;

    public LoginAsyncTask(UniSale_main main){
        this.mWeakRefActivity = new WeakReference<UniSale_main>(main);
    }

    @Override
    protected Integer[] doInBackground(String... nameAndPassword){
        return checkAgainstDB(nameAndPassword[0], nameAndPassword[1]);
    }

    @Override
    protected void onPostExecute(Integer[] ids){
        super.onPostExecute(ids);
        UniSale_main main = mWeakRefActivity.get();
        if(main == null) return;
        if(ids == null) main.loginUnsuccessful();
        else main.loginSuccessful(ids);
    }

    private Integer[] checkAgainstDB(String username, String password){
        JSONObject json = loadUser(username);
        Integer[] ids = new Integer[2];
        try {
            if(!(json.getString("password") == null)){
                if(password.equals(json.getString("password"))){
                    ids[0] = json.getInt("id");
                    ids[1] = json.getInt("schoolID");
                }
                else {
                    ids[0] = -1;
                }
            }
            else {
                return null;
            }
        }catch (JSONException e){
            return null;
        }
        return ids;
    }

    private JSONObject loadUser(String username){
        JSONObject jsonObject = null;
        try {
            URL url = new URL
                    ("http://www.cs.scranton.edu/~lindc4/UniSale/unisale.php/user/" + username);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.connect();
            InputStream is = conn.getInputStream();
            BufferedReader streamReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            StringBuilder responseStringBuilder = new StringBuilder();
            String inputString;
            while((inputString = streamReader.readLine()) != null)
                responseStringBuilder.append(inputString).append("\n");
            jsonObject = new JSONObject(responseStringBuilder.toString());
        }catch (IOException | JSONException e){
            jsonObject = null;
        }
        return jsonObject;
    }
}
