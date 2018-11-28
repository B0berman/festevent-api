package com.festevent.api.services;

import com.festevent.api.MyRetrofit;
import com.festevent.beans.Event;
import com.festevent.beans.Media;
import com.festevent.beans.Publication;
import com.festevent.beans.Ticket;
import com.festevent.beans.User;


import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;

public class EventService extends Service {

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

    public Call<List<Publication>> getEventPublications(String id) {
        return retrofit.getEventPublications(token, id);
    }

    public Call<List<Event>> getAll() {
        return retrofit.getAllEvents();
    }
}
