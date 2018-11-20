package com.visitcardpro.api;

import com.visitcardpro.api.services.AuthenticationService;
import com.visitcardpro.api.services.EventService;
import com.visitcardpro.api.services.FriendService;
import com.visitcardpro.api.services.GroupService;
import com.visitcardpro.api.services.PublicationService;
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

    public FriendService getFriendService() {
        return friendService;
    }

    public EventService getEventService() {
        return eventService;
    }

    public PublicationService getPublicationService() {
        return publicationService;
    }

    public GroupService getGroupService() {
        return groupService;
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
