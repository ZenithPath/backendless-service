package com.example.scame.backendlessservice.models;


import java.util.Date;

public class UserModel {

    private UserHistoryModel[] loginHistory;

    private String name;

    private String password;

    private String email;

    private String profileImageUrl;

    private Date birthDate;

    private String objectId;

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public void setLoginHistory(UserHistoryModel[] loginHistory) {
        this.loginHistory = loginHistory;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public String getObjectId() {
        return objectId;
    }

    public UserHistoryModel[] getLoginHistory() {
        return loginHistory;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public Date getBirthDate() {
        return birthDate;
    }
}
