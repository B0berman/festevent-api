package com.eip.festevent.beans;

import com.eip.festevent.dao.morphia.QueriesAllowed;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Reference;

import java.util.Date;

@Entity
@QueriesAllowed(fields = {"start", "end", "title", "valid"}, operators = {"<", ">", "contains", "=", "order", "limit"})
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

    @Reference
    protected User creator;


    // STAFF
    // ALERTS


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
