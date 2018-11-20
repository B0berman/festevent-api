package com.visitcardpro.beans;

import com.beust.jcommander.internal.Lists;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Group implements Serializable {

    protected String name;

    protected Event event;

    protected User creator;
    protected Date created = new Date();

    protected List<User> members = Lists.newArrayList();

    public List<User> getMembers() {
        return members;
    }

    public void addMember(User p) {
        members.add(p);
    }

    public void removeMember(User p) {
        members.remove(p);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }
}
