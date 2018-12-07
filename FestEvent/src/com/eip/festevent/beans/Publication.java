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
@QueriesAllowed(fields = {"created", "like_nb", "content", "comment_nb"}, operators = {"contains", "=", "order", "limit", "offset", "<", ">"})
public class Publication {
    @Id
    protected String id = ObjectId.get().toString();

    protected Date created = new Date();
    protected String		content;
    protected int           like_nb = 0;
    protected int           comment_nb = 0;
    protected List<Media>			medias = Lists.newArrayList();

    @Embedded
    protected Event event = null;
    @Embedded
    protected User publisher;

    @JsonIgnore
    @Reference
    protected List<User> likes = Lists.newArrayList();

    @JsonIgnore
    protected List<Comment> comments = Lists.newArrayList();

    public void addLike(User user) {
        likes.add(user);
        like_nb++;
    }

    public void removeLike(User user) {
        int i = 0;
        for (User profil : likes) {
            if (profil.getEmail().equals(user.getEmail())) {
                likes.remove(i);
                break;
            }
            i++;
        }
        likes.remove(user);
        like_nb--;
    }

    public void setMedias(List<Media> medias) {
        this.medias = medias;
    }

    public int getLike_nb() {
        return like_nb;
    }

    public void addMedia(Media media) {
        medias.add(media);
    }

    public void removeMedia(Media media) {
        medias.remove(media);
    }

    public void addComment(Comment comment) {
        comments.add(comment);
        comment_nb++;
    }

    public void removeComment(Comment comment) {
        comments.remove(comment);
        comment_nb--;
    }

    public List<User> getLikes() {
        return likes;
    }

    public List<Media> getMedias() {
        return medias;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public Date getDate() {
        return created;
    }

    public void setDate(Date date) {
        this.created = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public User getPublisher() {
        return publisher;
    }

    public void setPublisher(User publisher) {
        this.publisher = publisher;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
