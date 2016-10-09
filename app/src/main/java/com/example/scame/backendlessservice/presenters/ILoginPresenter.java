package com.example.scame.backendlessservice.presenters;


public interface ILoginPresenter<T> extends Presenter<T> {

    interface LoginView {

        void onSuccessfulLogin(String email);

        void onFailedLogin(String fault);
    }

    void login(String email, String password);
}
