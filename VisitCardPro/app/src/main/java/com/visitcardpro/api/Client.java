package com.visitcardpro.api;

import com.visitcardpro.api.services.AuthenticationService;
import com.visitcardpro.api.services.UserService;
import com.visitcardpro.beans.User;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class Client {
    private final String BASE_URI = "http://92.222.82.30:8080/eip/";
    private User user;
    private static Client INSTANCE = new Client();

    public static Client getInstance() {
        return INSTANCE;
    }

    private MyRetrofit retrofit;

    private UserService userService;
    private AuthenticationService authenticationService;

    private Client() {
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URI)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(MyRetrofit.class);

        userService = new UserService(retrofit);
        authenticationService = new AuthenticationService(retrofit);
    }

    public AuthenticationService getAuthenticationService() {
        return authenticationService;
    }

    public UserService getUserService() {
        return userService;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
        String token = user.getAccessToken();
        userService.setToken(token);
        authenticationService.setToken(token);
    }

}
