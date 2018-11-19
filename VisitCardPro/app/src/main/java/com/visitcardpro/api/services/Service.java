package com.visitcardpro.api.services;

import com.visitcardpro.api.MyRetrofit;

public abstract class Service {

    protected MyRetrofit retrofit;
    protected String     token;

    public Service(MyRetrofit r) {
        retrofit = r;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
