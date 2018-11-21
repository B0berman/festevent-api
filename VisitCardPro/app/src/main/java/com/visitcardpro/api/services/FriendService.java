package com.visitcardpro.api.services;

import com.visitcardpro.api.MyRetrofit;
import com.visitcardpro.beans.FriendRequest;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;

public class FriendService extends Service {
    public FriendService(MyRetrofit r) {
        super(r);
    }

    public Call<ResponseBody> requestFriend(String email) {
        return retrofit.requestFriend(token, email);
    }

    public Call<ResponseBody> friendRequestAnswer(String email, boolean answer) {
        return retrofit.friendRequestAnswer(token, email, answer);
    }

    public Call<ResponseBody> deleteFriend(String email) {
        return retrofit.deleteFriend(token, email);
    }

    public Call<ResponseBody> friendRequestCancel(String email) {
        return retrofit.friendRequestCancel(token, email);
    }

    public Call<List<FriendRequest>> getFriendsRequestsReceived() {
        return retrofit.getFriendsRequestsReceived(token);
    }

    public Call<List<FriendRequest>> getFriendsRequestsSent() {
        return retrofit.getFriendsRequestsSent(token);
    }
}
