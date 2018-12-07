package com.eip.festevent.beans;

import com.eip.festevent.dao.morphia.QueriesAllowed;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Lists;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Reference;

import java.util.Date;
import java.util.List;

@Entity
@QueriesAllowed(fields = {"start", "end", "title", "valid"}, operators = {"<", ">", "contains", "=", "order", "limit", "offset"})
public class Event {

    @Id
    protected String id = ObjectId.get().toString();

    protected String   title;
    protected String   description;
    protected Date start;
    protected Date     end;
    protected int	   edition = 1;
    protected String   address;
    protected boolean valid = false;
    protected Media     mainPicture;

    @Reference
    protected User creator;

    @JsonIgnore
    @Embedded
    protected List<Media> pictures = Lists.newArrayList();

    public void setPictures(List<Media> pictures) {
        this.pictures = pictures;
    }

    // STAFF
    // ALERTS
    public void addPicture(Media media) {
        pictures.add(media);
    }

    public void removePicture(Media media) {
        pictures.remove(media);
    }


    public Media getMainPicture() {
        return mainPicture;
    }

    public void setMainPicture(Media picture) {
        this.mainPicture = picture;
    }

    public List<Media> getPictures() {
        return pictures;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public int getEdition() {
        return edition;
    }

    public void setEdition(int edition) {
        this.edition = edition;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }
}
