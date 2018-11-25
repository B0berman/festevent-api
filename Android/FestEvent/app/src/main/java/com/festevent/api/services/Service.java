package com.festevent.api.services;

import com.festevent.api.MyRetrofit;

public abstract class Service {

    protected MyRetrofit retrofit;
    protected String     token;

    public Service(MyRetrofit r) {
        retrofit = r;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
