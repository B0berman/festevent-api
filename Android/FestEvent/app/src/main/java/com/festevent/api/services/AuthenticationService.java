package com.festevent.api.services;

import com.festevent.api.MyRetrofit;
import com.festevent.beans.User;

import okhttp3.ResponseBody;
import retrofit2.Call;

public class AuthenticationService extends Service {

    public AuthenticationService(MyRetrofit r) {
        super(r);

    }

    public Call<User> signIn(String key)  {
        return retrofit.signIn(key);
    }

    public Call<ResponseBody> signOut() {
        return retrofit.signOut(token);
    }

}
