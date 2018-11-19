package com.visitcardpro.database.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.visitcardpro.beans.Card;

import java.util.ArrayList;
import java.util.Date;

public class CardDAO extends BasicDAO {

    public static final String TABLE_NAME = "card";
    public static final String NAME = "name";
    public static final String DESCRIPTION = "decription";
    public static final String EMAIL = "email";
    public static final String TITLE = "title";
    public static final String KEY = "cardKey";
    public static final String LASTUPDATE = "lastUpdate";
    public static final String PHONENUMBER = "phoneNumber";
    public static final String CONTACT = "contact";
    public static final String TABLE_CREATE = "CREATE TABLE " + TABLE_NAME + " (" +
            NAME + " TEXT, " +
            DESCRIPTION + " TEXT, " +
            EMAIL + " TEXT, " +
            TITLE + " TEXT, " +
            KEY + " TEXT, " +
            LASTUPDATE + " INTEGER, " +
            CONTACT + " INTEGER, " +
            PHONENUMBER + " TEXT);";
    public static final String TABLE_DROP =  "DROP TABLE IF EXISTS " + TABLE_NAME + ";";

    public CardDAO(Context pContext) {
        super(pContext);
    }

    public void add(Card m, boolean isContact) {
        ContentValues value = new ContentValues();
        value.put(NAME, m.getName());
        value.put(DESCRIPTION, m.getDescription());
        value.put(EMAIL, m.getEmail());
        value.put(TITLE, m.getTitle());
        value.put(KEY, m.getKey());
        value.put(LASTUPDATE, m.getLastUpdate().getTime());
        value.put(PHONENUMBER, m.getPhoneNumber());
        value.put(CONTACT, isContact);
        mDb.insert(TABLE_NAME, null, value);
    }

    public void delete(String key) {
        mDb.delete(TABLE_NAME, KEY + " = ?", new String[] {String.valueOf(key)});
    }

    public void set(Card m) {
        ContentValues value = new ContentValues();
        value.put(NAME, m.getName());
        value.put(DESCRIPTION, m.getDescription());
        value.put(EMAIL, m.getEmail());
        value.put(TITLE, m.getTitle());
        value.put(LASTUPDATE, m.getLastUpdate().getTime());
        value.put(PHONENUMBER, m.getPhoneNumber());
        mDb.update(TABLE_NAME, value, KEY  + " = ?", new String[] {String.valueOf(m.getKey())});
    }

    public void clear() {
        mDb.execSQL("DELETE FROM " + TABLE_NAME);
    }

    public ArrayList<Card> get(boolean isContact) {
        String query = isContact ? "1" : "0";
        Cursor cursor = mDb.rawQuery("select * from " + TABLE_NAME + " where contact = ?", new String[]{query});
        cursor.moveToFirst();
        if (cursor == null || cursor.getCount() == 0)
            return null;
        ArrayList<Card> cards = new ArrayList<>();
        for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            Card card = new Card();
            card.setName(cursor.getString(1));
            card.setDescription(cursor.getString(2));
            card.setEmail(cursor.getString(3));
            card.setTitle(cursor.getString(4));
            card.setKey(cursor.getString(5));
            Date lastUpdate = new Date();
            lastUpdate.setTime(cursor.getInt(6));
            card.setLastUpdate(lastUpdate);
            card.setPhoneNumber(cursor.getString(7));
            cards.add(card);
        }
        cursor.close();
        return cards;
    }

    public Card get(String key) {
        Cursor cursor = mDb.rawQuery("select * from " + TABLE_NAME + " where "+ KEY +" = ?", new String[]{key});
        cursor.moveToFirst();
        if (cursor == null || cursor.getCount() == 0)
            return null;
        Card card = new Card();
        card.setName(cursor.getString(1));
        card.setDescription(cursor.getString(2));
        card.setEmail(cursor.getString(3));
        card.setTitle(cursor.getString(4));
        card.setKey(cursor.getString(5));
        Date lastUpdate = new Date();
        lastUpdate.setTime(cursor.getInt(6));
        card.setLastUpdate(lastUpdate);
        card.setPhoneNumber(cursor.getString(7));
        cursor.close();
        return card;
    }
}
