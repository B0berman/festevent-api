package com.festevent.api.services;

import com.festevent.api.MyRetrofit;
import com.festevent.beans.FriendRequest;
import com.festevent.beans.Media;
import com.festevent.beans.Publication;

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

    public Call<List<Publication>> getFriendPublications(String email) {
        return retrofit.getFriendPublications(token, email);
    }

    public Call<List<Media>> getFriendPictures(String email) {
        return retrofit.getFriendPictures(token, email);
    }
}
