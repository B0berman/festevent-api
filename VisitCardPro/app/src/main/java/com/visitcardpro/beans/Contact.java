package com.visitcardpro.beans;

import java.io.Serializable;
import java.util.Date;

public class Contact implements Serializable {

    protected Card card;
    protected Date lastUpdate;

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
}
