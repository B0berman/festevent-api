package com.visitcardpro.api.services;

import com.visitcardpro.api.MyRetrofit;

import okhttp3.ResponseBody;
import retrofit2.Call;

public class AuthenticationService extends Service {

    public AuthenticationService(MyRetrofit r) {
        super(r);

    }

    public Call<ResponseBody> signIn(String key)  {
        return retrofit.signIn(key);
    }

    public Call<ResponseBody> signOut() {
        return retrofit.signOut(token);
    }

}
