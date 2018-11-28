package com.festevent.beans;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Event implements Serializable {


    protected String   title;
    protected String   description;
    protected String start;
    protected String     end;
    protected int	   edition = 1;
    protected String   address;
    protected boolean valid = false;
    protected Media     mainPicture;
    protected String    id;
    protected User creator;

    protected List<Media> pictures;

    // STAFF


    public String getId() {
        return id;
    }

    public void setPictures(List<Media> pictures) {
        this.pictures = pictures;
    }

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

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
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
