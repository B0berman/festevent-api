package com.visitcardpro.beans;

import java.io.Serializable;

public class  Ticket implements Serializable {


    protected User owner;

    protected Event event;

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }
}
