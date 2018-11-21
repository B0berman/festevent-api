package com.visitcardpro.api.services;

import com.visitcardpro.api.MyRetrofit;
import com.visitcardpro.beans.Event;
import com.visitcardpro.beans.Group;
import com.visitcardpro.beans.Media;
import com.visitcardpro.beans.Publication;
import com.visitcardpro.beans.Ticket;
import com.visitcardpro.beans.User;

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
