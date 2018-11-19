package com.visitcardpro.api.services;

import com.visitcardpro.api.MyRetrofit;
import com.visitcardpro.beans.Card;

import java.util.ArrayList;
import java.util.Date;

import okhttp3.ResponseBody;
import retrofit2.Call;

public class CardService {

    private MyRetrofit retrofit;
    private String     token;

    public CardService(MyRetrofit r, String t) {
        retrofit = r;
        token = t;
    }

    public CardService(MyRetrofit r) {
        retrofit = r;
        token = "";
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Call<Card> createCard(Card card) {
        return retrofit.createCard(card, token);
    }

    public Call<ArrayList<Card>> getCards() {
        return retrofit.getCards(token);
    }

    public Call<ResponseBody> getCard(String cardKey) {
        return retrofit.getCard(token, cardKey);
    }

    public Call<ResponseBody> editCard(Card user, String cardKey) {
        return retrofit.editCard(user, token, cardKey);
    }

    public Call<ResponseBody> deleteCard(String cardKey) {
        return retrofit.deleteCard(token, cardKey);
    }

    public Call<ResponseBody> getCardUpdate(Date lastUpdate) {
        return retrofit.getCardUpdate(token, lastUpdate);
    }


}
