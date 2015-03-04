package com.lcsc.cs.lurkclient.states;

/**
 * Created by Jake on 3/3/2015.
 */
public enum State {
    LOGIN_FORM("LoginForm"),
    SERVER_INFO_FORM("ServerInfoForm"),
    NULL_STATE("NULL");

    private final String className;

    private State(String className) {
        this.className = className;
    }

    public String getClassName() {
        return this.className;
    }
};