package com.visitcardpro.api.services;

import com.visitcardpro.api.MyRetrofit;
import com.visitcardpro.beans.Event;
import com.visitcardpro.beans.Media;
import com.visitcardpro.beans.Ticket;
import com.visitcardpro.beans.User;


import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;

public class EventService extends Service {

    private MyRetrofit retrofit;
    private String     token;

    public EventService(MyRetrofit r) {
        super(r);
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Call<Event> createEvent(Event event) {
        return retrofit.createEvent(token, event);
    }

    public Call<ResponseBody> modifyEvent(Event event) {
        return retrofit.modifyEvent(token, event);
    }

    public Call<ResponseBody> deleteEvent(String id) {
        return retrofit.deleteEvent(token, id);
    }

    public Call<List<User>> getEventParticipants(String id) {
        return retrofit.getEventParticipants(token, id);
    }

    public Call<List<Media>> getEventPictures(String id) {
        return retrofit.getEventPictures(token, id);
    }

    public Call<List<Ticket>> getEventTickets(String id) {
        return retrofit.getEventTickets(token, id);
    }

    public Call<ResponseBody> eventParticipate(String id) {
        return retrofit.eventParticipate(token, id);
    }
}
