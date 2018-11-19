package com.visitcardpro.api.services;

import com.visitcardpro.api.MyRetrofit;
import com.visitcardpro.beans.User;
import java.util.Date;

import okhttp3.ResponseBody;
import retrofit2.Call;

public class UserService {
    private MyRetrofit retrofit;
    private String     token;

    public UserService(MyRetrofit r, String t) {
        token = t;
        retrofit = r;
    }

    public UserService(MyRetrofit r) {
        token = "";
        retrofit = r;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Call<ResponseBody> createUser(User user) {
        return retrofit.createUser(user);
    }

    public Call<User> getUser(String token) {
        return retrofit.getUser(token);
    }

    public Call<ResponseBody> setUser(User user) {
        return retrofit.setUser(user, token);
    }

    public Call<ResponseBody> deleteUser() {
        return retrofit.deleteUser(token);
    }

    public Call<ResponseBody> getUserUpdate(Date lastUpdate) {
        return retrofit.getUserUpdate(token, lastUpdate);
    }


}
