package com.festevent.database.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.festevent.beans.Media;
import com.festevent.beans.User;

public class MediaDAO extends BasicDAO {

    private static final String TABLE_NAME = "media";
    private static final String ID = "id";
    private static final String CODE = "code";
    private static final String URL = "url";
    public static final String TABLE_CREATE = "CREATE TABLE " + TABLE_NAME + " (" + ID + " INTEGER, " +
            CODE + " TEXT, " +
            URL + " TEXT);";
    public static final String TABLE_DROP =  "DROP TABLE IF EXISTS " + TABLE_NAME + ";";

    public MediaDAO(Context pContext) {
        super(pContext);
    }

    public void add(Media m) {
        ContentValues value = new ContentValues();
        value.put(ID, 1);
        value.put(CODE, m.getId());
        value.put(URL, m.getUrl());

        mDb.insert(TABLE_NAME, null, value);
    }

    public void delete() {
        mDb.delete(TABLE_NAME, ID + " = ?", new String[] {String.valueOf(1)});
    }

    public void set(Media m) {
        ContentValues value = new ContentValues();
        value.put(ID, 1);
        value.put(CODE, m.getId());
        value.put(URL, m.getUrl());
        mDb.update(TABLE_NAME, value, ID  + " = ?", new String[] {String.valueOf(1)});
    }

    public Media get() {
        Cursor cursor = mDb.rawQuery("select * from " + TABLE_NAME + " where id = ?", new String[]{"1"});
        cursor.moveToFirst();
        if (cursor == null || cursor.getCount() == 0)
            return null;
        Media media = new Media();
        media.setId(cursor.getString(1));
        media.setUrl(cursor.getString(2));
        cursor.close();
        return media;
    }
}
