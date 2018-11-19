package com.visitcardpro.api.services;

import com.visitcardpro.api.MyRetrofit;
import com.visitcardpro.beans.Contact;

import java.util.ArrayList;
import java.util.Date;


import okhttp3.ResponseBody;
import retrofit2.Call;

public class ContactService {

    private MyRetrofit retrofit;
    private String     token;

    public ContactService(MyRetrofit r, String t) {
        retrofit = r;
        token = t;
    }

    public ContactService(MyRetrofit r) {
        retrofit = r;
        token = "";
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Call<ResponseBody> createContact(String cardKey) {
        return  retrofit.createContact(token, cardKey);
    }

    public Call<ResponseBody> getContact(String cardKey) {
        return retrofit.getContact(token, cardKey);
    }

    public Call<ArrayList<Contact>> getContacts() {
        return retrofit.getContacts(token);
    }

    public Call<ResponseBody> deleteContact(String cardKey) {
        return retrofit.deleteContact(token, cardKey);
    }

    public Call<ResponseBody> getContactUpdate(Date lastUpdate) {
        return retrofit.getContactUpdate(token, lastUpdate);
    }

}
