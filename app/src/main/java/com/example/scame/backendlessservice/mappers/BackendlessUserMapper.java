package com.example.scame.backendlessservice.mappers;


import com.backendless.BackendlessUser;
import com.example.scame.backendlessservice.models.UserModel;

import java.util.HashMap;
import java.util.Map;

public class BackendlessUserMapper {

    public BackendlessUser convert(UserModel userModel) {
        BackendlessUser backendlessUser = new BackendlessUser();
        backendlessUser.setProperties(parseProperties(userModel));

        return backendlessUser;
    }

    private Map<String, Object> parseProperties(UserModel userModel) {
        Map<String, Object> container = new HashMap<>();

        container.put(Constants.EMAIL, userModel.getEmail());
        container.put(Constants.PASSWORD, userModel.getPassword());
        container.put(Constants.BIRTH_DATE, userModel.getBirthDate());
        container.put(Constants.NAME, userModel.getName());
        container.put(Constants.PROFILE_IMG_URL, userModel.getProfileImageUrl());
        container.put(Constants.LOGIN_HISTORY, userModel.getLoginHistory());

        return container;
    }
}
