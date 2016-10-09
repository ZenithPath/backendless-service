package com.example.scame.backendlessservice.presenters;


import com.example.scame.backendlessservice.repository.BackendlessRepository;
import com.example.scame.backendlessservice.repository.IBackendlessRepository;

public class LoginPresenter<T extends ILoginPresenter.LoginView> implements ILoginPresenter<T>,
        IBackendlessRepository.LoginListener {

    private T view;

    private IBackendlessRepository backendlessRepository;

    public LoginPresenter(IBackendlessRepository backendlessRepository) {
        this.backendlessRepository = backendlessRepository;
    }

    @Override
    public void login(String email, String password) {
        ((BackendlessRepository) backendlessRepository).setLoginListener(this);
        backendlessRepository.login(email, password);
    }

    @Override
    public void setView(T view) {
        this.view = view;
    }

    @Override
    public void onLoginSuccess(String email) {
        if (view != null) {
            view.onSuccessfulLogin(email);
        }
    }

    @Override
    public void onLoginError(String fault) {
        if (view != null) {
            view.onFailedLogin(fault);
        }
    }

    @Override
    public void destroy() {
        view = null;
        ((BackendlessRepository) backendlessRepository).setLoginListener(null);
    }
}
