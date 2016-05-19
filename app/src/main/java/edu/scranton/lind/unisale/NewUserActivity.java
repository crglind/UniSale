package edu.scranton.lind.unisale;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

import edu.scranton.lind.unisale.database_schema.UnisaleDbOpenHelper;
import edu.scranton.lind.unisale.database_schema.UnisaleDbContract.User;
import edu.scranton.lind.unisale.database_schema.UnisaleDbContract.School;

public class NewUserActivity extends AppCompatActivity {

    public static final String ARG_USER_ID_NEW = "edu.scranton.lind.unisale.newuser.ARG_USER_ID";
    public static final String ARG_SCHOOL_ID = "edu.scranton.lind.unisale.newuser.ARG_SCHOOL_ID";
    public static final String ARG_USERNAME = "edu.scranton.lind.unisale.newuser.ARG_USERNAME";

    private SQLiteDatabase mReadableDb;
    private SQLiteDatabase mWritableDb;
    private ArrayList<CharSequence> mSchoolArray = new ArrayList<>();
    private int mUID;
    private int mSchoolID;
    public EditText mUsername;
    public EditText mPassword;
    public EditText mPasswordCheck;
    public AutoCompleteTextView mCollege;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        UnisaleDbOpenHelper dbHelper = new UnisaleDbOpenHelper(this);
        mReadableDb = dbHelper.getReadableDatabase();
        mWritableDb = dbHelper.getWritableDatabase();
        String[] schoolColumns = {School.SCHOOL};
        Cursor schoolCursor = mReadableDb.query(School.TABLE_NAME, schoolColumns,
                "1", null, null, null, null);
        for(schoolCursor.moveToFirst(); !schoolCursor.isAfterLast(); schoolCursor.moveToNext()){
            mSchoolArray.add(schoolCursor.getString(0));
        }
        schoolCursor.close();
        mUsername = (EditText) findViewById(R.id.username_et);
        mPassword = (EditText) findViewById(R.id.password_et);
        mPasswordCheck = (EditText) findViewById(R.id.confirm_password_et);

        ArrayAdapter<CharSequence> collegeListAdapter = new ArrayAdapter<CharSequence>
                (this, android.R.layout.select_dialog_item, mSchoolArray);
        mCollege = (AutoCompleteTextView) findViewById(R.id.college_selection);
        mCollege.setThreshold(1);
        mCollege.setAdapter(collegeListAdapter);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        mReadableDb = null;
        mWritableDb = null;
    }

    public void registerUser(View view){
        String proposedUsername = mUsername.getText().toString();
        String password = mPassword.getText().toString();
        String passwordCheck = mPasswordCheck.getText().toString();
        String usersCollege = mCollege.getText().toString();
        if(!checkEntries(proposedUsername, password, passwordCheck, usersCollege))return;
        for(int i = 0; i < mSchoolArray.size(); i++){
            if(mSchoolArray.get(i).equals(usersCollege)){
                mSchoolID = i;
                break;
            }
            if (i == (mSchoolArray.size() - 1)){
                Toast.makeText(NewUserActivity.this, "Invalid College, try again",
                        Toast.LENGTH_SHORT).show();
                return;
            }
        }
        //async task variables in order of username, password, collegeid
        RegisterUserAsyncTask ra = new RegisterUserAsyncTask(this);
        ra.execute(proposedUsername, password, String.valueOf(mSchoolID));
        //Check proposedUsername against the database
        /*String[] usernameColumn = {User.USERNAME};
        String selection = User.USERNAME + " =? ";
        String[] selectionArgs = {proposedUsername};
        Cursor userCursor = mReadableDb.query(User.TABLE_NAME, usernameColumn, selection,
                selectionArgs, null, null, null);
        if(!userCursor.isAfterLast()){
            Toast.makeText(NewUserActivity.this, "Username Taken", Toast.LENGTH_SHORT).show();
            userCursor.close();
            return;
        }
        userCursor.close();

        //Put everything in the database and go to the next main screen activity
        ContentValues newUser = new ContentValues();
        newUser.put(User.USERNAME, proposedUsername);
        newUser.put(User.PASSWORD, password);
        newUser.put(User.SCHOOL_ID, mSchoolID);
        long newU = mWritableDb.insert(User.TABLE_NAME, null, newUser);
        String[] idColumns = {User.ID, User.USERNAME};
        Cursor idCursor = mReadableDb.query(User.TABLE_NAME, idColumns, selection,
                selectionArgs, null, null, null);
        mUID = idCursor.getInt(0);
        Intent intent = new Intent(this, HomeScreen.class);
        intent.putExtra(ARG_USER_ID_NEW, mUID);
        intent.putExtra(ARG_SCHOOL_ID, mSchoolID);
        intent.putExtra(ARG_USERNAME, proposedUsername);
        startActivity(intent);*/
    }

    public void success(int response){
        Toast.makeText(NewUserActivity.this,
                mUsername.getText().toString() + " - CREATED!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, HomeScreen.class);
        intent.putExtra(ARG_USER_ID_NEW, response);
        intent.putExtra(ARG_SCHOOL_ID, mSchoolID);
        intent.putExtra(ARG_USERNAME, mUsername.getText().toString());
        startActivity(intent);
    }

    public void usernameTaken(){
        Toast.makeText(NewUserActivity.this, "Username Taken!", Toast.LENGTH_SHORT).show();
    }

    public void failedToCreate(){
        Toast.makeText(NewUserActivity.this,
                "Network Error: Failed to Create", Toast.LENGTH_SHORT).show();
    }

    private boolean checkEntries
            (String username, String password, String passwordCheck, String school){
        boolean result = true;
        if(username.equals("")){
            Toast.makeText(this, "Username is blank", Toast.LENGTH_SHORT).show();
            result = false;
        }
        else if (password.equals("")){
            Toast.makeText(this, "Password is blank", Toast.LENGTH_SHORT).show();
            result = false;
        }
        else if(!password.equals(passwordCheck)){
            Toast.makeText(this, "Passwords do not match!",
                    Toast.LENGTH_SHORT).show();
            result = false;
        }
        else if (school.equals("")){
            Toast.makeText(this, "School/College is blank", Toast.LENGTH_SHORT).show();
            result = false;
        }
        return result;
    }
}
