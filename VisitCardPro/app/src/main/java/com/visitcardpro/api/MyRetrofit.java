package com.visitcardpro.api;



import com.visitcardpro.beans.Event;
import com.visitcardpro.beans.Group;
import com.visitcardpro.beans.Publication;
import com.visitcardpro.beans.User;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/*
  *
  *  Created by walbecq on 02/12/17.
  */

public interface MyRetrofit {


    @POST("signup")
    @Headers("Content-Type: application/json")
    Call<ResponseBody> signUp(@Body User user);

    /*
     *  Auth Services
     *
     */

    @POST("signin")
    Call<ResponseBody> signIn(@Header("Authorization") String key);

    @POST("signout")
    Call<ResponseBody> signOut(@Header("accessToken") String token);

    /*
     *  User Services
     *
     */

    @GET("profil")
    Call<ResponseBody> getUser(@Header("accessToken") String token);

    @PUT("profil")
    @Headers("Content-Type: application/json")
    Call<ResponseBody> setUser(@Body User user, @Header("accessToken") String token);

    @DELETE("profil")
    Call<ResponseBody> deleteUser(@Header("accessToken") String token);

    @GET("profil/friends")
    Call<ResponseBody> getUserUpdate(@Header("accessToken") String token);

    @POST("profil/research")
    @Headers("Content-Type: application/json")
    Call<ResponseBody> searchUsers(@Header("accessToken") String token, @Query("keys") List<String> keys, @Query("values") List<String> values);

    @GET("profil/friends")
    Call<ResponseBody> getUserFriends(@Header("accessToken") String token);

    @GET("profil/pictures")
    Call<ResponseBody> getUserPictures(@Header("accessToken") String token);

    @GET("profil/groups")
    Call<ResponseBody> getUserGroups(@Header("accessToken") String token);

    @GET("profil/events")
    Call<ResponseBody> getUserEvents(@Header("accessToken") String token);

    @GET("profil/tickets")
    Call<ResponseBody> getUserTickets(@Header("accessToken") String token);

    @GET("profil/profil-image")
    Call<ResponseBody> getUserProfilImage(@Header("accessToken") String token);

    @GET("profil/publications")
    Call<ResponseBody> getUserPublications(@Header("accessToken") String token);

    /*
     *  Resource Services
     *
     */

    @GET("resource/image/{id}")
    Call<ResponseBody> getImage(@Path("id") String id);

    /*
     *  Group Services
     *
     */


    @POST("groups")
    @Headers("Content-Type: application/json")
    Call<ResponseBody> createGroup(@Body Group group, @Header("accessToken") String token);

    @PUT("groups/leave")
    Call<ResponseBody> leaveGroup(@Header("accessToken") String token, @Query("id") String id);

    @PUT("groups/member/add")
    Call<ResponseBody> addGroupMember(@Header("accessToken") String token, @Query("id") String id, @Query("email") String email);

    @GET("groups/members")
    Call<ResponseBody> getGroupMembers(@Header("accessToken") String token, @Query("id") String id);

    /*
    *
    *   Event Service
    *
    *
     */

    @POST("events")
    @Headers("Content-Type: application/json")
    Call<ResponseBody> createEvent(@Header("accessToken") String token, @Body Event event);

    @PUT("events")
    @Headers("Content-Type: application/json")
    Call<ResponseBody> modifyEvent(@Header("accessToken") String token, @Body Event event);

    @DELETE("events")
    Call<ResponseBody> deleteEvent(@Header("accessToken") String token, @Query("id") String id);

    @GET("events/participants")
    Call<ResponseBody> getEventParticipants(@Header("accessToken") String token, @Query("id") String id);

    @GET("events/pictures")
    Call<ResponseBody> getEventPictures(@Header("accessToken") String token, @Query("id") String id);

    @GET("events/tickets")
    Call<ResponseBody> getEventTickets(@Header("accessToken") String token, @Query("id") String id);

    @PUT("events/participate")
    Call<ResponseBody> eventParticipate(@Header("accessToken") String token, @Query("id") String id);

    /*
    *
    *
    *   Publication Service
    *
     */
    @POST("publications/publicate")
    Call<ResponseBody> createPublicaton(@Header("accessToken") String token, @Body Publication publication);

    @POST("publications/event/publicate")
    Call<ResponseBody> createEventPublicaton(@Header("accessToken") String token, @Body Publication publication);

    @PUT("publications")
    Call<ResponseBody> modifyPublication(@Header("accessToken") String token, @Body Publication publication);

    @DELETE("publications")
    Call<ResponseBody> deletePublication(@Header("accessToken") String token, @Query("id") String id);

    @PUT("publicatiosn/like")
    Call<ResponseBody> likePublication(@Header("accessToken") String token, @Query("id") String id);

    @PUT("publications/unlike")
    Call<ResponseBody> unlikePublication(@Header("accessToken") String token, @Query("id") String id);

    @GET("publications/pictures")
    Call<ResponseBody> getPublicationPictures(@Header("accessToken") String token, @Query("id") String id);

    @GET("publications/likes")
    Call<ResponseBody> getPublicationLikes(@Header("accessToken") String token, @Query("id") String id);

    @GET("publications/friends")
    Call<ResponseBody> getFriendsPublications(@Header("accessToken") String token, @Query("id") String id);

    @GET("publications/comments")
    Call<ResponseBody> getPublicationComments(@Header("accessToken") String token, @Query("id") String id);

    /*
     *
     *
     *   Friend Service
     *
     */

    @POST("friends/request")
    Call<ResponseBody> requestFriend(@Header("accessToken") String token, @Query("email") String email);

    @POST("friends/request-answer")
    Call<ResponseBody> friendRequestAnswer(@Header("accessToken") String token, @Query("email") String email, @Query("answer") boolean answer);

    @DELETE("friends")
    Call<ResponseBody> deleteFriend(@Header("accessToken") String token, @Query("email") String email);

    @POST("friends/request-cancel")
    Call<ResponseBody> friendRequestCancel(@Header("accessToken") String token, @Query("email") String email);

    @GET("friends/requests-received")
    Call<ResponseBody> getFriendsRequestsReceived(@Header("accessToken") String token);

    @GET("friends/requests-sent")
    Call<ResponseBody> getFriendsRequestsSent(@Header("accessToken") String token);
}
