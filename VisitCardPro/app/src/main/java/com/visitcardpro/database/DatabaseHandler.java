package com.visitcardpro.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.visitcardpro.database.dao.AuthenticationDAO;
import com.visitcardpro.database.dao.CardDAO;
import com.visitcardpro.database.dao.UserDAO;

public class DatabaseHandler extends SQLiteOpenHelper {

    public DatabaseHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(AuthenticationDAO.TABLE_CREATE);
        db.execSQL(CardDAO.TABLE_CREATE);
        db.execSQL(UserDAO.TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL(AuthenticationDAO.TABLE_DROP);
        db.execSQL(UserDAO.TABLE_DROP);
        db.execSQL(CardDAO.TABLE_DROP);
        onCreate(db);
    }

}