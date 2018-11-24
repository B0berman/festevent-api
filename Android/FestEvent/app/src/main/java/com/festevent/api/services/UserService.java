package com.festevent.api.services;

import com.festevent.api.MyRetrofit;
import com.festevent.beans.Event;
import com.festevent.beans.Group;
import com.festevent.beans.Media;
import com.festevent.beans.Publication;
import com.festevent.beans.Ticket;
import com.festevent.beans.User;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;

public class UserService extends Service {

    public UserService(MyRetrofit r) {
        super(r);
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Call<ResponseBody> signUp(User user) {
        return retrofit.signUp(user);
    }

    public Call<User> getUser() {
        return retrofit.getUser(token);
    }

    public Call<ResponseBody> setUser(User user) {
        return retrofit.setUser(user, token);
    }

    public Call<ResponseBody> deleteUser() {
        return retrofit.deleteUser(token);
    }

    public Call<List<User>> searchUsers(List<String> keys, List<String> values) {
        return retrofit.searchUsers(token, keys, values);
    }

    public Call<List<User>> getUserFriends() {
        return retrofit.getUserFriends(token);
    }

    public Call<List<Event>> getUserEvents() {
        return retrofit.getUserEvents(token);
    }

    public Call<List<Ticket>> getUserTickets() {
        return retrofit.getUserTickets(token);
    }

    public Call<List<Media>> getUserPictures() {
        return retrofit.getUserPictures(token);
    }

    public Call<List<Group>> getUserGroups() {
        return retrofit.getUserGroups(token);
    }

    public Call<Media> getUserProfilImage() {
        return retrofit.getUserProfilImage(token);
    }

    public Call<List<Publication>> getUserPublications() {
        return retrofit.getUserPublications(token);
    }

}
