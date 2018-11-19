package com.visitcardpro.beans;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Lists;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Reference;

import java.util.Date;
import java.util.List;

@Entity
public class Group {
    @Id
    protected String id = ObjectId.get().toString();

    protected String name;

    @Reference
    protected Event event;

    @Reference
    protected User creator;
    protected Date created = new Date();

    @JsonIgnore
    @Reference
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
