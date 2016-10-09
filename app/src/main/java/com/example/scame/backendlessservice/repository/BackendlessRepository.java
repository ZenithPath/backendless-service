package com.example.scame.backendlessservice.repository;


import android.graphics.Bitmap;
import android.util.Log;

import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.files.BackendlessFile;
import com.backendless.persistence.BackendlessDataQuery;
import com.backendless.persistence.QueryOptions;
import com.example.scame.backendlessservice.mappers.BackendlessUserMapper;
import com.example.scame.backendlessservice.mappers.Constants;
import com.example.scame.backendlessservice.mappers.HistoryListMapper;
import com.example.scame.backendlessservice.models.BitmapWrapper;
import com.example.scame.backendlessservice.models.UserHistoryModel;
import com.example.scame.backendlessservice.models.UserModel;

public class BackendlessRepository implements IBackendlessRepository {

    private static final String LOGIN_TYPE_NORMAL = "login";

    private static final String LOGIN_TYPE_REGISTRATION = "registration";

    private static final String PROFILE_FOLDER = "profiler_folder";

    private static final int QUALITY = 50;

    private RegistrationListener registrationListener;

    private ProfileImageListener profileImageListener;

    private HistoryListener historyListener;

    private LoginListener loginListener;

    @Override
    public void register(UserModel userModel) {
        BackendlessUserMapper userMapper = new BackendlessUserMapper();
        BackendlessUser backendlessUser = userMapper.convert(userModel);

        Backendless.UserService.register(backendlessUser, new AsyncCallback<BackendlessUser>() {
            @Override
            public void handleResponse(BackendlessUser response) {
                updateLoginHistory(response, LOGIN_TYPE_REGISTRATION);

                if (registrationListener != null) {
                    registrationListener.onRegistrationSuccess(response.getEmail());
                }
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                if (registrationListener != null) {
                    registrationListener.onRegistrationError(fault.toString());
                }
            }
        });
    }

    @Override
    public void login(String email, String password) {
        Backendless.UserService.login(email, password, new AsyncCallback<BackendlessUser>() {
            @Override
            public void handleResponse(BackendlessUser response) {
                updateLoginHistory(response, LOGIN_TYPE_NORMAL);

                if (loginListener != null) {
                    loginListener.onLoginSuccess(response.getEmail());
                }
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                if (loginListener != null) {
                    loginListener.onLoginError(fault.toString());
                }
            }
        });
    }

    @Override
    public void saveProfileImage(BitmapWrapper profileBitmap, UserModel userModel) {
        Backendless.Files.Android.upload(profileBitmap.getBitmap(), Bitmap.CompressFormat.PNG,
                QUALITY, produceIdentifier(userModel.getEmail()), PROFILE_FOLDER, new AsyncCallback<BackendlessFile>() {

                    @Override
                    public void handleResponse(BackendlessFile response) {
                        if (profileImageListener != null) {
                            userModel.setProfileImageUrl(response.getFileURL());
                            profileImageListener.onSuccessUpload(userModel);
                        }
                    }

                    @Override
                    public void handleFault(BackendlessFault fault) {
                        if (profileImageListener != null) {
                            profileImageListener.onFailedUpload(fault.toString());
                        }
                    }
                });
    }

    @Override
    public void fetchHistory() {
        QueryOptions queryOptions = new QueryOptions(Constants.ORDER_BY_QUERY);
        queryOptions.addRelated(Constants.RELATED_COLUMN);

        BackendlessDataQuery dataQuery = new BackendlessDataQuery(queryOptions);

        Backendless.Data.of(UserModel.class).find(dataQuery, new AsyncCallback<BackendlessCollection<UserModel>>() {
            @Override
            public void handleResponse(BackendlessCollection<UserModel> response) {
                if (historyListener != null) {
                    HistoryListMapper historyMapper = new HistoryListMapper();
                    historyListener.onSuccessfulRequest(historyMapper.convert(response.getData()));
                }
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                if (historyListener != null) {
                    historyListener.onFailedRequest(fault.toString());
                }
            }
        });
    }

    private void updateLoginHistory(BackendlessUser backendlessUser, String loginType) {
        UserHistoryModel userHistoryModel = new UserHistoryModel();
        userHistoryModel.setLoginType(loginType);
        UserHistoryModel[] castedLoginHistory = {userHistoryModel};

        backendlessUser.setProperty(Constants.LOGIN_HISTORY, castedLoginHistory);

        Backendless.UserService.update(backendlessUser, new AsyncCallback<BackendlessUser>() {
            @Override
            public void handleResponse(BackendlessUser response) {
                Log.i("onxHistorySuccess", response.getProperties().toString());
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Log.i("onxHistoryFail", fault.toString());
            }
        });
    }

    public void setRegistrationListener(RegistrationListener registrationListener) {
        this.registrationListener = registrationListener;
    }


    public void setLoginListener(LoginListener loginListener) {
        this.loginListener = loginListener;
    }


    public void setProfileImageListener(ProfileImageListener profileImageListener) {
        this.profileImageListener = profileImageListener;
    }

    public void setHistoryListener(HistoryListener historyListener) {
        this.historyListener = historyListener;
    }

    private String produceIdentifier(String email) {
        return email.substring(0, email.indexOf("@"));
    }
}
