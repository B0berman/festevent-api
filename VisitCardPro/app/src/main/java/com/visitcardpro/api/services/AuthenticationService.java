package com.visitcardpro.api.services;

import com.visitcardpro.api.MyRetrofit;

import okhttp3.ResponseBody;
import retrofit2.Call;

public class AuthenticationService {

    private MyRetrofit retrofit;
    private String     token;

    public AuthenticationService(MyRetrofit r) {
        retrofit = r;
        token = "";
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Call<ResponseBody> signIn(String key)  {
        return retrofit.signIn(key);
    }

    public Call<ResponseBody> signOut() {
        return retrofit.signOut(token);
    }

}
