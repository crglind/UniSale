package edu.scranton.lind.unisale;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import edu.scranton.lind.unisale.database_schema.UnisaleDbOpenHelper;
import edu.scranton.lind.unisale.database_schema.UnisaleDbContract.User;


public class UniSale_main extends AppCompatActivity {

    public static final String ARG_USER_ID_MAIN =
            "edu.scranton.lind.unisale.unisalemain.ARG_USER_ID";
    public static final String ARG_SCHOOL_ID =
            "edu.scranton.lind.unisale.unisalemain.ARG_SCHOOL_ID";

    private SQLiteDatabase mReadableDb;
    private int mUID;
    private int mSchoolID;

    public EditText mUsername;
    public EditText mPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uni_sale_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        UnisaleDbOpenHelper dbHelper = new UnisaleDbOpenHelper(this);
        mReadableDb = dbHelper.getReadableDatabase();
        mUsername = (EditText) findViewById(R.id.username_et);
        mPassword = (EditText) findViewById(R.id.password_et);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        mReadableDb = null;
    }

    public void loginClicked(View view){
        String username = mUsername.getText().toString();
        if(username.equals("")){
            Toast.makeText(this, "Username is blank", Toast.LENGTH_SHORT).show();
            return;
        }
        String password = mPassword.getText().toString();
        if (password.equals("")){
            Toast.makeText(this, "Password is blank", Toast.LENGTH_SHORT).show();
            return;
        }
        //Check username against database
        String[] desiredColumns = {User.ID, User.USERNAME, User.PASSWORD, User.SCHOOL_ID};
        String selection = User.USERNAME + " =? ";
        String[] selectionArgs = {username};
        Cursor uCursor = mReadableDb.query(User.TABLE_NAME, desiredColumns,
                selection, selectionArgs, null, null, null);
        uCursor.moveToFirst();
        if(uCursor.isAfterLast()){
            Toast.makeText(this, "Username Invalid", Toast.LENGTH_SHORT).show();
            uCursor.close();
            return;
        }
        //Check password against database
        if(!uCursor.getString(2).equals(password)){
            Toast.makeText(this, "Password Invalid", Toast.LENGTH_SHORT).show();
            uCursor.close();
            return;
        }
        mUID = uCursor.getInt(0);
        Toast.makeText(UniSale_main.this, Integer.toString(mUID), Toast.LENGTH_SHORT).show();
        mSchoolID = uCursor.getInt(3);
        Toast.makeText(UniSale_main.this, Integer.toString(mSchoolID), Toast.LENGTH_SHORT).show();
        uCursor.close();
        Intent intent = new Intent(this, HomeScreen.class);
        intent.putExtra(ARG_USER_ID_MAIN, mUID);
        intent.putExtra(ARG_SCHOOL_ID, mSchoolID);
        startActivity(intent);
    }

    public void newUserClicked(View view){
        Intent intent = new Intent(this, NewUserActivity.class);
        startActivity(intent);
    }
}
