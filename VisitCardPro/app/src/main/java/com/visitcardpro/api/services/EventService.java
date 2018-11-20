package com.visitcardpro.api.services;

import com.visitcardpro.api.MyRetrofit;
import com.visitcardpro.beans.Event;

import java.util.ArrayList;
import java.util.Date;


import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public class EventService extends Service {

    private MyRetrofit retrofit;
    private String     token;

    public EventService(MyRetrofit r) {
        super(r);
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Call<ResponseBody> createEvent(Event event) {
        return retrofit.createEvent(token, event);
    }

    public Call<ResponseBody> modifyEvent(Event event) {
        return retrofit.modifyEvent(token, event);
    }

    public Call<ResponseBody> deleteEvent(String id) {
        return retrofit.deleteEvent(token, id);
    }

    public Call<ResponseBody> getEventParticipants(String id) {
        return retrofit.getEventParticipants(token, id);
    }

    public Call<ResponseBody> getEventPictures(String id) {
        return retrofit.getEventPictures(token, id);
    }

    public Call<ResponseBody> getEventTickets(String id) {
        return retrofit.getEventTickets(token, id);
    }

    public Call<ResponseBody> eventParticipate(String id) {
        return retrofit.eventParticipate(token, id);
    }
}
