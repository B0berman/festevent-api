package com.visitcardpro.database.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.visitcardpro.database.DatabaseHandler;

public abstract class BasicDAO {

    protected final static int VERSION = 1;
    protected final static String NOM = "visitcardpro.db";
    protected SQLiteDatabase mDb = null;
    protected DatabaseHandler mHandler = null;


    public BasicDAO(Context pContext) {
        this.mHandler = new DatabaseHandler(pContext, NOM, null, VERSION);
    }

    public SQLiteDatabase open() {
        mDb = mHandler.getWritableDatabase();
        return mDb;
    }

    public void close() {
        mDb.close();
    }
}