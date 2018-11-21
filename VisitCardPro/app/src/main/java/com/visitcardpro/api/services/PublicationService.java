package com.visitcardpro.api.services;

import com.visitcardpro.api.MyRetrofit;
import com.visitcardpro.beans.Comment;
import com.visitcardpro.beans.Media;
import com.visitcardpro.beans.Publication;
import com.visitcardpro.beans.User;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;

public class PublicationService extends Service {

    public PublicationService(MyRetrofit r) {
        super(r);
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Call<Publication> createPublicaton(Publication publication) {
        return retrofit.createPublicaton(token, publication);
    }

    public Call<Publication> createEventPublicaton(Publication publication) {
        return retrofit.createEventPublicaton(token, publication);
    }

    public Call<ResponseBody> modifyPublication(Publication publication) {
        return retrofit.modifyPublication(token, publication);
    }

    public Call<ResponseBody> deletePublication(String id) {
        return retrofit.deletePublication(token, id);
    }

    public Call<ResponseBody> likePublication(String id) {
        return retrofit.likePublication(token, id);
    }

    public Call<ResponseBody> unlikePublication(String id) {
        return retrofit.unlikePublication(token, id);
    }

    public Call<List<Media>> getPublicationPictures(String id) {
        return retrofit.getPublicationPictures(token, id);
    }

    public Call<List<User>> getPublicationLikes(String id) {
        return retrofit.getPublicationLikes(token, id);
    }

    public Call<List<Publication>> getFriendsPublications(String id) {
        return retrofit.getFriendsPublications(token, id);
    }

    public Call<List<Comment>> getPublicationComments(String id) {
        return retrofit.getPublicationComments(token, id);
    }
}
