package com.visitcardpro.database.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.visitcardpro.beans.Authentication;

public class AuthenticationDAO extends BasicDAO {

    public static final String TABLE_NAME = "authentication";
    public static final String ID = "id";
    public static final String LOGIN = "login";
    public static final String TOKEN = "token";
    public static final String TABLE_CREATE = "CREATE TABLE " + TABLE_NAME + " (" + ID + " INTEGER, " +
            LOGIN + " TEXT, " +
            TOKEN + " TEXT);";
    public static final String TABLE_DROP =  "DROP TABLE IF EXISTS " + TABLE_NAME + ";";

    public AuthenticationDAO(Context pContext) {
        super(pContext);
    }

    public void add(Authentication m) {
        ContentValues value = new ContentValues();
        value.put(ID, 1);
        value.put(LOGIN, m.getLogin());
        value.put(TOKEN, m.getAccessToken());
        mDb.insert(TABLE_NAME, null, value);
    }

    public void delete() {
        mDb.delete(TABLE_NAME, ID + " = ?", new String[] {String.valueOf(1)});
    }

    public void set(Authentication m) {
        ContentValues value = new ContentValues();
        value.put(LOGIN, m.getLogin());
        value.put(TOKEN, m.getAccessToken());
        mDb.update(TABLE_NAME, value, ID  + " = ?", new String[] {String.valueOf(1)});
    }

    public Authentication get() {

        Cursor cursor = mDb.rawQuery("select * from " + TABLE_NAME + " where id = ?", new String[]{String.valueOf(1)});
        cursor.moveToFirst();
        int toto = cursor.getCount();
        if (cursor == null || cursor.getCount() == 0)
            return null;
        Authentication auth = new Authentication();
        auth.setLogin(cursor.getString(1));
        auth.setAccessToken(cursor.getString(2));
        cursor.close();
        return auth;
    }

}
