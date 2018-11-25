package com.festevent.api;

import com.festevent.activities.LoginActivity;
import com.festevent.api.services.AuthenticationService;
import com.festevent.api.services.EventService;
import com.festevent.api.services.FriendService;
import com.festevent.api.services.GroupService;
import com.festevent.api.services.PublicationService;
import com.festevent.api.services.UserService;
import com.festevent.beans.User;
import com.festevent.database.dao.UserDAO;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;


import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;

public class Client {
    private final String BASE_URI = "http://92.222.82.30:8080/eip/";
    private User user;
    private static Client INSTANCE = new Client();

    public static Client getInstance() {
        return INSTANCE;
    }

    private UserService userService;
    private EventService eventService;
    private PublicationService publicationService;
    private GroupService groupService;
    private AuthenticationService authenticationService;
    private FriendService friendService;

    private Client() {
        MyRetrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URI)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(MyRetrofit.class);

        userService = new UserService(retrofit);
        eventService = new EventService(retrofit);
        groupService = new GroupService(retrofit);
        publicationService = new PublicationService(retrofit);
        authenticationService = new AuthenticationService(retrofit);
        friendService = new FriendService(retrofit);
    }

    public void signout(Context context, Activity activity) {
        UserDAO userDAO = new UserDAO(context);
        userDAO.open();
        userDAO.delete();
        userDAO.close();
        Intent launchNextActivity = new Intent(activity, LoginActivity.class);
        launchNextActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        launchNextActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        launchNextActivity.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        activity.startActivity(launchNextActivity);
    }

    public FriendService getFriendService() {
        friendService.setToken(user.getAccessToken());
        return friendService;
    }

    public EventService getEventService() {
        eventService.setToken(user.getAccessToken());
        return eventService;
    }

    public PublicationService getPublicationService() {
        publicationService.setToken(user.getAccessToken());
        return publicationService;
    }

    public GroupService getGroupService() {
        groupService.setToken(user.getAccessToken());
        return groupService;
    }

    public AuthenticationService getAuthenticationService() {
        return authenticationService;
    }

    public UserService getUserService() {
        userService.setToken(user.getAccessToken());
        return userService;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;;
    }

}
