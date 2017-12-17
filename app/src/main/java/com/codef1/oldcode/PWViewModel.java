package com.codef1.oldcode;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import com.codef1.oldcode.data.PWDbHelper;
import com.codef1.oldcode.data.PasswordContract;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yuzhu on 2017/12/17.
 */

public class PWViewModel extends AndroidViewModel {
    private PWDbHelper mDbHelper;
    private ArrayList<Password> mData;

    public PWViewModel(@NonNull Application application) {
        super(application);

        mDbHelper = new PWDbHelper(application);
    }

    public List<Password> getData() {
        if (mData == null) {
            mData = new ArrayList<>();
            loadData();
        }
        ArrayList<Password> clonedData = new ArrayList<>(mData.size());
        for (int i = 0; i < mData.size(); i++) {
            clonedData.add(new Password(mData.get(i)));
        }

        return clonedData;
    }

    private void loadData() {
        SQLiteDatabase sqLiteDatabase = mDbHelper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query(PasswordContract.PWEntry.TABLE,
                new String[]{
                        PasswordContract.PWEntry._ID,
                        PasswordContract.PWEntry.COL_PW_NAME,
                        PasswordContract.PWEntry.COL_PW_VALUE,
                }, null, null, null, null, null);
        while (cursor.moveToNext()) {
            int idIndex = cursor.getColumnIndex(PasswordContract.PWEntry._ID);
            int titleIndex = cursor.getColumnIndex(PasswordContract.PWEntry.COL_PW_NAME);
            int valueIndex = cursor.getColumnIndex(PasswordContract.PWEntry.COL_PW_VALUE);
            mData.add(new Password(cursor.getLong(idIndex), cursor.getString(titleIndex), cursor.getString(valueIndex)));
        }
        cursor.close();
        sqLiteDatabase.close();
    }

    public Password addPassword(String title, String value) {
        //Persist
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(PasswordContract.PWEntry.COL_PW_NAME, title);
        values.put(PasswordContract.PWEntry.COL_PW_VALUE, value);
        long id = db.insertWithOnConflict(PasswordContract.PWEntry.TABLE,
                null,
                values,
                SQLiteDatabase.CONFLICT_REPLACE);
        db.close();

        //ViewModel
        Password password = new Password(id, title, value);
        mData.add(password);
        return new Password(password);
    }

    public void removePassword(long id) {
        //Persist
        SQLiteDatabase sqLiteDatabase = mDbHelper.getWritableDatabase();
        sqLiteDatabase.delete(PasswordContract.PWEntry.TABLE, PasswordContract.PWEntry._ID + " = ? ",
                new String[]{Long.toString(id)});
        sqLiteDatabase.close();

        //ViewModel
        int index = -1;
        for (int i = 0; i < mData.size(); i++) {
            Password password = mData.get(i);
            if (password.getID() == id) {
                index = i;
                break;
            }
        }
        if (index != -1) {
            mData.remove(index);
        }
    }
}
