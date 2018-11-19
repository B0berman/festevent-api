package com.visitcardpro.beans;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Reference;

import java.util.Date;

@Entity
public class Appointment {
    @Id
    protected String id = ObjectId.get().toString();

    @Reference
    protected User creator;

    protected String name;
    protected float longitude = 0.0f;
    protected float latitude = 0.0f;
    protected Date created = new Date();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }
}
