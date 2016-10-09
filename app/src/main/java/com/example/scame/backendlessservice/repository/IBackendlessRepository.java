package com.example.scame.backendlessservice.repository;


import com.example.scame.backendlessservice.models.BitmapWrapper;
import com.example.scame.backendlessservice.models.HistoryAdapterItem;
import com.example.scame.backendlessservice.models.UserModel;

import java.util.List;

public interface IBackendlessRepository {

    interface RegistrationListener {

        void onRegistrationSuccess(String email);

        void onRegistrationError(String fault);
    }

    interface LoginListener {

        void onLoginSuccess(String email);

        void onLoginError(String fault);
    }

    interface ProfileImageListener {

        void onSuccessUpload(UserModel userModel);

        void onFailedUpload(String fault);
    }

    interface HistoryListener {

        void onSuccessfulRequest(List<HistoryAdapterItem> historyItems);

        void onFailedRequest(String fault);
    }

    void register(UserModel userModel);

    void login(String email, String password);

    void saveProfileImage(BitmapWrapper profileBitmap, UserModel userModel);

    void fetchHistory();
}
