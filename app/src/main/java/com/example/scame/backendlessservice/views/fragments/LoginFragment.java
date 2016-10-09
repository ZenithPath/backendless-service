package com.example.scame.backendlessservice.views.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.scame.backendlessservice.R;
import com.example.scame.backendlessservice.presenters.ILoginPresenter;
import com.example.scame.backendlessservice.presenters.LoginPresenter;
import com.example.scame.backendlessservice.repository.BackendlessRepository;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginFragment extends Fragment implements ILoginPresenter.LoginView {

    @BindView(R.id.email_field)
    EditText emailField;
    @BindView(R.id.password_field)
    EditText passwordField;
    @BindView(R.id.login_btn)
    Button loginButton;
    @BindView(R.id.to_registration_tv)
    TextView toRegistrationTv;

    private ProgressDialog progressDialog;

    private LoginListener loginListener;

    private ILoginPresenter<ILoginPresenter.LoginView> loginPresenter;

    public interface LoginListener {

        void onRegisterClick();

        void onAuthorizationSuccess();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof LoginListener) {
            this.loginListener = (LoginListener) context;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View loginView = inflater.inflate(R.layout.login_fragment, container, false);
        ButterKnife.bind(this, loginView);

        loginButton.setOnClickListener(v -> login());

        loginPresenter = new LoginPresenter<>(new BackendlessRepository());
        loginPresenter.setView(this);

        toRegistrationTv.setOnClickListener(v -> loginListener.onRegisterClick());

        return loginView;
    }

    private void login() {
        if (!isValid()) {
            Toast.makeText(getContext(), "Login failed [client side]", Toast.LENGTH_LONG).show();
            loginButton.setEnabled(true);
            return;
        }

        loginButton.setEnabled(false);
        showProgressDialog();

        loginPresenter.login(emailField.getText().toString(), passwordField.getText().toString());
    }

    private void showProgressDialog() {
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();
    }

    @Override
    public void onSuccessfulLogin(String email) {
        Toast.makeText(getContext(), "Logged in: " + email, Toast.LENGTH_LONG).show();
        progressDialog.hide();
        loginButton.setEnabled(true);
        loginListener.onAuthorizationSuccess();
    }

    @Override
    public void onFailedLogin(String fault) {
        Toast.makeText(getContext(), "Login failed [server side]: " + fault, Toast.LENGTH_LONG).show();
        progressDialog.hide();
        loginButton.setEnabled(true);
    }

    // shouldn't be here as well
    private boolean isValid() {
        boolean valid = true;

        String email = emailField.getText().toString();
        String password = passwordField.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailField.setError("enter a valid email address");
            valid = false;
        } else {
            emailField.setError(null);
        }

        if (password.isEmpty() || password.length() < 6 || password.length() > 16) {
            passwordField.setError("between 6 and 16 alphanumeric characters");
            valid = false;
        } else {
            passwordField.setError(null);
        }

        return valid;
    }

    @Override
    public void onDestroy() {
        loginPresenter.destroy();
        super.onDestroy();
    }
}
