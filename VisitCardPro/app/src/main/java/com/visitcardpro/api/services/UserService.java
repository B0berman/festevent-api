package com.visitcardpro.api.services;

import com.visitcardpro.api.MyRetrofit;
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

    public Call<ResponseBody> getUser() {
        return retrofit.getUser(token);
    }

    public Call<ResponseBody> setUser(User user) {
        return retrofit.setUser(user, token);
    }

    public Call<ResponseBody> deleteUser() {
        return retrofit.deleteUser(token);
    }

    public Call<ResponseBody> getUserUpdate() {
        return retrofit.getUserUpdate(token);
    }

    public Call<ResponseBody> searchUsers(List<String> keys, List<String> values) {
        return retrofit.searchUsers(token, keys, values);
    }

    public Call<ResponseBody> getUserFriends() {
        return retrofit.getUserFriends(token);
    }

    public Call<ResponseBody> getUserEvents() {
        return retrofit.getUserEvents(token);
    }

    public Call<ResponseBody> getUserTickets() {
        return retrofit.getUserTickets(token);
    }

    public Call<ResponseBody> getUserPictures() {
        return retrofit.getUserPictures(token);
    }

    public Call<ResponseBody> getUserGroups() {
        return retrofit.getUserGroups(token);
    }

    public Call<ResponseBody> getUserProfilImage() {
        return retrofit.getUserProfilImage(token);
    }

    public Call<ResponseBody> getUserPublications() {
        return retrofit.getUserPublications(token);
    }

}
