package com.festevent.api;



import com.festevent.beans.Comment;
import com.festevent.beans.Event;
import com.festevent.beans.FriendRequest;
import com.festevent.beans.Group;
import com.festevent.beans.Media;
import com.festevent.beans.Publication;
import com.festevent.beans.Ticket;
import com.festevent.beans.User;

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
    Call<User> signIn(@Header("Authorization") String key);

    @POST("signout")
    Call<ResponseBody> signOut(@Header("token") String token);

    /*
     *  User Services
     *
     */

    @GET("profil")
    Call<User> getUser(@Header("token") String token);

    @PUT("profil")
    @Headers("Content-Type: application/json")
    Call<ResponseBody> setUser(@Body User user, @Header("token") String token);

    @DELETE("profil")
    Call<ResponseBody> deleteUser(@Header("token") String token);

    @POST("profil/research")
    @Headers("Content-Type: application/json")
    Call<List<User>> searchUsers(@Header("token") String token, @Query("keys") List<String> keys, @Query("values") List<String> values);

    @GET("profil/friends")
    Call<List<User>> getUserFriends(@Header("token") String token);

    @GET("profil/pictures")
    Call<List<Media>> getUserPictures(@Header("token") String token);

    @GET("profil/groups")
    Call<List<Group>> getUserGroups(@Header("token") String token);

    @GET("profil/events")
    Call<List<Event>> getUserEvents(@Header("token") String token);

    @GET("profil/tickets")
    Call<List<Ticket>> getUserTickets(@Header("token") String token);

    @GET("profil/profil-image")
    Call<Media> getUserProfilImage(@Header("token") String token);

    @GET("profil/publications")
    Call<List<Publication>> getUserPublications(@Header("token") String token);

    /*
     *  Resource Services
     *
     */

    @GET("festevent-resources/image/{id}")
    Call<ResponseBody> getImage(@Path("id") String id);

    /*
     *  Group Services
     *
     */


    @POST("groups")
    @Headers("Content-Type: application/json")
    Call<Group> createGroup(@Body Group group, @Header("token") String token);

    @PUT("groups/leave")
    Call<ResponseBody> leaveGroup(@Header("token") String token, @Query("id") String id);

    @PUT("groups/member/add")
    Call<ResponseBody> addGroupMember(@Header("token") String token, @Query("id") String id, @Query("email") String email);

    @GET("groups/members")
    Call<List<User>> getGroupMembers(@Header("token") String token, @Query("id") String id);

    /*
    *
    *   Event Service
    *
    *
     */

    @POST("events")
    @Headers("Content-Type: application/json")
    Call<Event> createEvent(@Header("token") String token, @Body Event event);

    @PUT("events")
    @Headers("Content-Type: application/json")
    Call<ResponseBody> modifyEvent(@Header("token") String token, @Body Event event);

    @DELETE("events")
    Call<ResponseBody> deleteEvent(@Header("token") String token, @Query("id") String id);

    @GET("events/participants")
    Call<List<User>> getEventParticipants(@Header("token") String token, @Query("id") String id);

    @GET("events/pictures")
    Call<List<Media>> getEventPictures(@Header("token") String token, @Query("id") String id);

    @GET("events/tickets")
    Call<List<Ticket>> getEventTickets(@Header("token") String token, @Query("id") String id);

    @PUT("events/participate")
    Call<ResponseBody> eventParticipate(@Header("token") String token, @Query("id") String id);

    /*
    *
    *
    *   Publication Service
    *
     */
    @POST("publications/publicate")
    Call<Publication> createPublicaton(@Header("token") String token, @Body Publication publication);

    @POST("publications/event/publicate")
    Call<Publication> createEventPublicaton(@Header("token") String token, @Body Publication publication);

    @PUT("publications")
    Call<ResponseBody> modifyPublication(@Header("token") String token, @Body Publication publication);

    @DELETE("publications")
    Call<ResponseBody> deletePublication(@Header("token") String token, @Query("id") String id);

    @PUT("publicatiosn/like")
    Call<ResponseBody> likePublication(@Header("token") String token, @Query("id") String id);

    @PUT("publications/unlike")
    Call<ResponseBody> unlikePublication(@Header("token") String token, @Query("id") String id);

    @GET("publications/pictures")
    Call<List<Media>> getPublicationPictures(@Header("token") String token, @Query("id") String id);

    @GET("publications/likes")
    Call<List<User>> getPublicationLikes(@Header("token") String token, @Query("id") String id);

    @GET("publications/friends")
    Call<List<Publication>> getFriendsPublications(@Header("token") String token);

    @GET("publications/comments")
    Call<List<Comment>> getPublicationComments(@Header("token") String token, @Query("id") String id);

    @POST("publications/comment")
    Call<Comment> commentPublication(@Header("token") String token, @Query("id") String id, @Body Comment comment);

    /*
     *
     *
     *   Friend Service
     *
     */

    @POST("friends/request")
    Call<ResponseBody> requestFriend(@Header("token") String token, @Query("email") String email);

    @POST("friends/request-answer")
    Call<ResponseBody> friendRequestAnswer(@Header("token") String token, @Query("email") String email, @Query("answer") boolean answer);

    @DELETE("friends")
    Call<ResponseBody> deleteFriend(@Header("token") String token, @Query("email") String email);

    @POST("friends/request-cancel")
    Call<ResponseBody> friendRequestCancel(@Header("token") String token, @Query("email") String email);

    @GET("friends/requests-received")
    Call<List<FriendRequest>> getFriendsRequestsReceived(@Header("token") String token);

    @GET("friends/requests-sent")
    Call<List<FriendRequest>> getFriendsRequestsSent(@Header("token") String token);

    @GET("friends/publications")
    Call<List<Publication>> getFriendPublications(@Header("token") String token, @Query("email") String email);

    @GET("friends/pictures")
    Call<List<Media>> getFriendPictures(@Header("token") String token, @Query("email") String email);

}
