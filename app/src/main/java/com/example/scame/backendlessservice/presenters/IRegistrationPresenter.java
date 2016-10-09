package com.example.scame.backendlessservice.presenters;


import com.example.scame.backendlessservice.models.BitmapWrapper;
import com.example.scame.backendlessservice.models.UserModel;

public interface IRegistrationPresenter<T> extends Presenter<T> {

    interface RegistrationView {

        void onSuccessfulRegistration(String email);

        void onFailedRegistration(String fault);
    }

    void register(UserModel userModel, BitmapWrapper bitmapWrapper);
}
