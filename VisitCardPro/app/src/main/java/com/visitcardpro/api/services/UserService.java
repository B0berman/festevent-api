package com.visitcardpro.api.services;

import com.visitcardpro.api.MyRetrofit;

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




}