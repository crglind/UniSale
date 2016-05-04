package edu.scranton.lind.unisale;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;

public class RetainedDatabase extends Fragment {

    private SQLiteDatabase mReadableDatabase;
    private SQLiteDatabase mWritableDatabase;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    public void setDatabase(SQLiteDatabase readable, SQLiteDatabase writable){
        mReadableDatabase = readable;
        mWritableDatabase = writable;
    }

    public SQLiteDatabase getReadableDatabaseFromRetained(){return mReadableDatabase;}

    public SQLiteDatabase getWritableDatabaseFromRetained(){return mWritableDatabase;}
}
