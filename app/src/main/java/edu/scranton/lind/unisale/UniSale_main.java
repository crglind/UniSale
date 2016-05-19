package edu.scranton.lind.unisale;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;



public class UniSale_main extends AppCompatActivity {

    public static final String ARG_USER_ID_MAIN =
            "edu.scranton.lind.unisale.unisalemain.ARG_USER_ID";
    public static final String ARG_SCHOOL_ID =
            "edu.scranton.lind.unisale.unisalemain.ARG_SCHOOL_ID";
    public static final String ARG_USERNAME = "edu.scranton.lind.unisale.unisalemain.ARG_USERNAME";

    private String mUser;

    public EditText mUsername;
    public EditText mPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uni_sale_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK)
                != Configuration.SCREENLAYOUT_SIZE_LARGE) {
            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        mUsername = (EditText) findViewById(R.id.username_et);
        mPassword = (EditText) findViewById(R.id.password_et);
    }

    public void loginClicked(View view){
        mUser = mUsername.getText().toString();
        if(mUser.equals("")){
            Toast.makeText(this, "Username is blank", Toast.LENGTH_SHORT).show();
            return;
        }
        String password = mPassword.getText().toString();
        if (password.equals("")){
            Toast.makeText(this, "Password is blank", Toast.LENGTH_SHORT).show();
            return;
        }
        LoginAsyncTask la = new LoginAsyncTask(this);
        la.execute(mUser, password);
    }

    public void loginSuccessful(Integer[] ids){
        if(ids[0].intValue() == -1){
            Toast.makeText(UniSale_main.this, "Password Incorrect", Toast.LENGTH_SHORT).show();
        }
        else {
            Intent intent = new Intent(this, HomeScreen.class);
            intent.putExtra(ARG_USER_ID_MAIN, ids[0]);
            intent.putExtra(ARG_SCHOOL_ID, ids[1]);
            intent.putExtra(ARG_USERNAME, mUser);
            startActivity(intent);
        }
    }

    public void loginUnsuccessful(){
        Toast.makeText(UniSale_main.this, "Username Invalid", Toast.LENGTH_SHORT).show();
    }

    public void newUserClicked(View view){
        Intent intent = new Intent(this, NewUserActivity.class);
        startActivity(intent);
    }
}
