package com.festevent.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.festevent.database.dao.MediaDAO;
import com.festevent.database.dao.UserDAO;

public class DatabaseHandler extends SQLiteOpenHelper {

    public DatabaseHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(UserDAO.TABLE_CREATE);
        db.execSQL(MediaDAO.TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL(UserDAO.TABLE_DROP);
        db.execSQL(MediaDAO.TABLE_DROP);
        onCreate(db);
    }

}