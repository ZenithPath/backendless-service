package com.example.scame.backendlessservice.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.backendless.Backendless;
import com.example.scame.backendlessservice.R;
import com.example.scame.backendlessservice.mappers.Constants;
import com.example.scame.backendlessservice.models.UserHistoryModel;
import com.example.scame.backendlessservice.models.UserModel;
import com.example.scame.backendlessservice.views.fragments.LoginFragment;
import com.example.scame.backendlessservice.views.fragments.RegistrationFragment;

public class BackendlessMain extends AppCompatActivity implements LoginFragment.LoginListener,
        RegistrationFragment.RegistrationListener {

    private static final String REGISTER_FRAGMENT_TAG = "regFragment";

    private static final String LOGIN_FRAGMENT_TAG = "loginFragment";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.backendless_main);

        initBackendlessService();
        instantiateFragment();
    }

    private void initBackendlessService() {
        String appVersion = "v1";
        String appId = getResources().getString(R.string.APP_ID);
        String secret = getResources().getString(R.string.SECRET_KEY);

        Backendless.initApp(this, appId, secret, appVersion);
        Backendless.Data.mapTableToClass(Constants.USERS_TABLE, UserModel.class);
        Backendless.Data.mapTableToClass(Constants.USER_HISTORY_TABLE, UserHistoryModel.class);
    }

    private void instantiateFragment() {
        if (getSupportFragmentManager().findFragmentByTag(REGISTER_FRAGMENT_TAG) == null) {
            replaceFragment(LOGIN_FRAGMENT_TAG, new LoginFragment());
        }
    }

    @Override
    public void onLoginClick() {
        replaceFragment(LOGIN_FRAGMENT_TAG, new LoginFragment());
    }

    @Override
    public void onRegisterClick() {
        replaceFragment(REGISTER_FRAGMENT_TAG, new RegistrationFragment());
    }

    @Override
    public void onAuthorizationSuccess() {
        startActivity(new Intent(this, HistoryListActivity.class));
    }

    private void replaceFragment(String TAG, Fragment fragment) {
        if (getSupportFragmentManager().findFragmentByTag(TAG) == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container_fl, fragment, TAG)
                    .commit();
        }
    }
}
