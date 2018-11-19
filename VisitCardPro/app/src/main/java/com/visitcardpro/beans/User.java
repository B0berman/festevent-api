package com.visitcardpro.beans;

import java.io.Serializable;

/**
 * Created by root on 17/03/18.
 */
public class User implements Serializable {
    protected String firstName;
    protected String lastName;
    protected Authentication authentication;

    public User() {

    }

    public Authentication getAuthentication() {
        return authentication;
    }

    public void setAuthentication(Authentication authentication) {
        this.authentication = authentication;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
