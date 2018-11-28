package com.festevent.database.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.festevent.beans.User;

public class UserDAO extends BasicDAO {

    private static final String TABLE_NAME = "user";
    private static final String ID = "id";
    private static final String TOKEN = "accessToken";
    private static final String EMAIL = "email";
    private static final String FIRSTNAME = "firstName";
    private static final String LASTNAME = "lastName";
    public static final String TABLE_CREATE = "CREATE TABLE " + TABLE_NAME + " (" + ID + " INTEGER, " +
            EMAIL + " TEXT, " +
            FIRSTNAME + " TEXT, " +
            TOKEN + " TEXT, " +
            LASTNAME + " TEXT);";
    public static final String TABLE_DROP =  "DROP TABLE IF EXISTS " + TABLE_NAME + ";";

    public UserDAO(Context pContext) {
        super(pContext);
    }

    public void add(User m) {
        ContentValues value = new ContentValues();
        value.put(ID, 1);
        value.put(EMAIL, m.getEmail());
        value.put(FIRSTNAME, m.getFirstName());
        value.put(LASTNAME, m.getLastName());
        value.put(TOKEN, m.getAccessToken());

        mDb.insert(TABLE_NAME, null, value);
    }

    public void delete() {
        mDb.delete(TABLE_NAME, ID + " = ?", new String[] {String.valueOf(1)});
    }

    public void set(User m) {
        ContentValues value = new ContentValues();
        value.put(EMAIL, m.getEmail());
        value.put(FIRSTNAME, m.getFirstName());
        value.put(LASTNAME, m.getLastName());
        value.put(TOKEN, m.getAccessToken());
        mDb.update(TABLE_NAME, value, ID  + " = ?", new String[] {String.valueOf(1)});
    }

    public User get() {
        Cursor cursor = mDb.rawQuery("select * from " + TABLE_NAME + " where id = ?", new String[]{"1"});
        cursor.moveToFirst();
        if (cursor == null || cursor.getCount() == 0)
            return null;
        User user = new User();
        user.setFirstName(cursor.getString(2));
        user.setLastName(cursor.getString(4));
        user.setAccessToken(cursor.getString(3));
        user.setEmail(cursor.getString(1));
        cursor.close();
        return user;
    }
}
