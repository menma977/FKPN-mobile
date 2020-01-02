package com.mobile.fkpn.model;

import android.content.Context;
import org.json.JSONObject;

public class Token {
    private String username;
    private String auth;
    private Account account;

    public Token(Context context) {
        account = new Account(context);
        try {
            JSONObject getAuth = account.get();
            setUsername((String) getAuth.get("username"));
            setAuth((String) getAuth.get("auth"));
        }catch (Exception e) {
            setUsername("");
            setAuth("");
        }
    }

    public void set(String value) {
        account.set(value);
    }

    public void clear() {
        account.remove();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAuth() {
        return auth;
    }

    public void setAuth(String auth) {
        this.auth = auth;
    }
}
