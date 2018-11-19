package com.visitcardpro.api.services;

import com.visitcardpro.api.MyRetrofit;

import java.util.ArrayList;
import java.util.Date;

import okhttp3.ResponseBody;
import retrofit2.Call;

public class PublicationService extends Service {

    private MyRetrofit retrofit;
    private String     token;

    public PublicationService(MyRetrofit r) {
        super(r);
    }

    public void setToken(String token) {
        this.token = token;
    }

}
