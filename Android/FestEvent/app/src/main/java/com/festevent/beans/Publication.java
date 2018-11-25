package com.festevent.beans;

import com.beust.jcommander.internal.Lists;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Publication implements Serializable {

    protected String id;
    protected String date;
    protected String		content;
//    protected int           like_nb = 0;
//    protected int           comment_nb = 0;
    protected List<Media>			medias = Lists.newArrayList();

    protected Event event;
    protected User publisher;

    protected List<User> likes = Lists.newArrayList();

    protected List<Comment> comments = Lists.newArrayList();

    public void addLike(User user) {
        likes.add(user);
//        like_nb++;
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
//        like_nb--;
    }

    public void setMedias(List<Media> medias) {
        this.medias = medias;
    }

//    public int getLike_nb() {
//        return like_nb;
//    }

    public void addMedia(Media media) {
        medias.add(media);
    }

    public void removeMedia(Media media) {
        medias.remove(media);
    }

    public void addComment(Comment comment) {
        comments.add(comment);
//        comment_nb++;
    }

    public void removeComment(Comment comment) {
        comments.remove(comment);
//        comment_nb--;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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

}
