package com.example.scame.backendlessservice.presenters;


import com.example.scame.backendlessservice.models.BitmapWrapper;
import com.example.scame.backendlessservice.models.UserModel;
import com.example.scame.backendlessservice.repository.BackendlessRepository;
import com.example.scame.backendlessservice.repository.IBackendlessRepository;

public class RegistrationPresenter<T extends IRegistrationPresenter.RegistrationView>
        implements IRegistrationPresenter<T>, IBackendlessRepository.RegistrationListener,
        IBackendlessRepository.ProfileImageListener {

    private T view;

    private IBackendlessRepository backendlessRepository;

    public RegistrationPresenter(IBackendlessRepository backendlessRepository) {
        this.backendlessRepository = backendlessRepository;
    }

    // it's not acceptable in a real app that registration fails because, for example, an image was too large
    // but it's still better than letting registration complete without ensuring proper data handling
    // so users won't be surprised that after successful registration their profile image was forever lost

    // what these casts are doing here? I don't consider these kind of setters/getters as a part of interface contract
    // (e.g. someone prefers to pass args different way)

    @Override
    public void register(UserModel userModel, BitmapWrapper bitmapWrapper) {
        ((BackendlessRepository) backendlessRepository).setProfileImageListener(this);
        backendlessRepository.saveProfileImage(bitmapWrapper, userModel);
    }

    @Override
    public void onSuccessUpload(UserModel userModel) { // now user model contains a link to profile image
        ((BackendlessRepository) backendlessRepository).setRegistrationListener(this);
        backendlessRepository.register(userModel);
    }

    @Override
    public void onFailedUpload(String fault) {
        if (view != null) {
            view.onFailedRegistration(fault);
        }
    }

    @Override
    public void onRegistrationSuccess(String email) {
        if (view != null) {
            view.onSuccessfulRegistration(email);
        }
    }

    @Override
    public void onRegistrationError(String fault) {
        if (view != null) {
            view.onFailedRegistration(fault);
        }
    }

    @Override
    public void setView(T view) {
        this.view = view;
    }

    @Override
    public void destroy() {
        view = null;
        ((BackendlessRepository) backendlessRepository).setRegistrationListener(null);
        ((BackendlessRepository) backendlessRepository).setProfileImageListener(null);
    }
}
