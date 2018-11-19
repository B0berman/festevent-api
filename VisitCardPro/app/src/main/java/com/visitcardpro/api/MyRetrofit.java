package com.visitcardpro.api;


import com.visitcardpro.beans.Card;
import com.visitcardpro.beans.Contact;
import com.visitcardpro.beans.User;

import java.util.ArrayList;
import java.util.Date;

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

 /*
  *
  *  Created by walbecq on 02/12/17.
  */

public interface MyRetrofit {

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

    @POST("user")
    @Headers("Content-Type: application/json")
    Call<ResponseBody> createUser(@Body User user);

    @GET("user")
    Call<User> getUser(@Header("accessToken") String token);

    @PUT("user")
    Call<ResponseBody> setUser(@Body User user, @Header("accessToken") String token);

    @DELETE("user")
    Call<ResponseBody> deleteUser(@Header("accessToken") String token);

    @GET("user/update")
    @Headers("Content-Type: application/json")
    Call<ResponseBody> getUserUpdate(@Header("accessToken") String token, @Body Date lastUpdate);

    /*
     *  Contact Services
     *
     */

    @POST("user/contacts")
    @Headers("Content-Type: application/json")
    Call<ResponseBody> createContact(@Header("accessToken") String token, @Body String cardKey);

    @GET("user/contacts/{cardKey}")
    Call<ResponseBody> getContact(@Header("accessToken") String token, @Path("cardKey") String cardKey);

    @GET("user/contacts")
    @Headers("Content-Type: application/json")
    Call<ArrayList<Contact>> getContacts(@Header("accessToken") String token);

    @DELETE("user/contacts/{cardKey}")
    Call<ResponseBody> deleteContact(@Header("accessToken") String token, @Path("cardKey") String cardKey);

    @GET("user/contacts/update")
    @Headers("Content-Type: application/json")
    Call<ResponseBody> getContactUpdate(@Header("accessToken") String token, @Body Date lastUpdate);


    /*
     *  Card Services
     *
     */

    @POST("user/cards")
    @Headers("Content-Type: application/json")
    Call<Card> createCard(@Body Card card, @Header("accessToken") String token);

    @GET("user/cards")
    Call<ArrayList<Card>> getCards(@Header("accessToken") String token);

    @GET("user/cards/{cardKey}")
    Call<ResponseBody> getCard(@Header("accessToken") String token, @Path("cardKey") String cardKey);

    @PUT("user/cards/{cardKey}")
    @Headers("Content-Type: application/json")
    Call<ResponseBody> editCard(@Body Card user, @Header("accessToken") String token, @Path("cardKey") String cardKey);

    @DELETE("user/cards/{cardKey}")
    Call<ResponseBody> deleteCard(@Header("accessToken") String token, @Path("cardKey") String cardKey);

    @GET("user/cards/update")
    @Headers("Content-Type: application/json")
    Call<ResponseBody> getCardUpdate(@Header("accessToken") String token, @Body Date lastUpdate);

}
