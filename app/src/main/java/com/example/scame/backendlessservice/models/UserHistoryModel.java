package com.example.scame.backendlessservice.models;


import java.util.Date;

public class UserHistoryModel {

    private Date created;

    private String loginType;

    public void setLoginType(String loginType) {
        this.loginType = loginType;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getCreated() {
        return created;
    }

    public String getLoginType() {
        return loginType;
    }
}
