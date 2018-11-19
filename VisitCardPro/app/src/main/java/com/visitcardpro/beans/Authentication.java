package com.visitcardpro.beans;

import java.io.Serializable;

/**
 * Created by hugo on 15/04/18.
 */

public class Authentication implements Serializable {
    protected String login;
    protected String accessToken;
    protected String password;

    public Authentication() {

    }

    public Authentication(String login, String accessToken) {
        this.login = login;
        this.accessToken = accessToken;

    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

}
