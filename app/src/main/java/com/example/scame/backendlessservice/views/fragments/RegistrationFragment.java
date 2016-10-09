package com.example.scame.backendlessservice.views.fragments;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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
import com.example.scame.backendlessservice.models.BitmapWrapper;
import com.example.scame.backendlessservice.models.UserModel;
import com.example.scame.backendlessservice.presenters.IRegistrationPresenter;
import com.example.scame.backendlessservice.presenters.RegistrationPresenter;
import com.example.scame.backendlessservice.repository.BackendlessRepository;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;


public class RegistrationFragment extends Fragment implements DatePickerFragment.DateListener,
        IRegistrationPresenter.RegistrationView {

    private static final String DATE_FORMAT = "yyyy-MM-dd";

    private static final String DATE_PICKER_FRAG_TAG = "datePickerFrag";

    private static final int PICK_IMAGE_REQUEST = 1;

    @BindView(R.id.name_field)
    EditText nameField;
    @BindView(R.id.email_field)
    EditText emailField;

    @BindView(R.id.password_field)
    EditText passwordField;
    @BindView(R.id.verify_password_field)
    EditText verifyPasswordField;

    @BindView(R.id.profile_img_text)
    TextView profileText;
    @BindView(R.id.profile_image)
    CircleImageView profileImage;

    @BindView(R.id.birth_date_field)
    EditText birthDateField;
    @BindView(R.id.birth_date_image)
    CircleImageView birthDateImage;

    @BindView(R.id.registration_btn)
    Button registerBtn;

    @BindView(R.id.to_login_tv)
    TextView toLoginTv;

    private final SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.US);

    private RegistrationListener registrationListener;

    private Bitmap profileBitmap;

    private ProgressDialog progressDialog;

    private IRegistrationPresenter<IRegistrationPresenter.RegistrationView> registrationPresenter;

    public interface RegistrationListener {

        void onLoginClick();

        void onAuthorizationSuccess();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof RegistrationListener) {
            registrationListener = (RegistrationListener) context;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View registerView = inflater.inflate(R.layout.registration_fragment, container, false);

        ButterKnife.bind(this, registerView);
        setupListeners();

        registrationPresenter = new RegistrationPresenter<>(new BackendlessRepository());
        registrationPresenter.setView(this);

        handleProfileImage(savedInstanceState);
        handleDatePickerListener();

        return registerView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable(getString(R.string.profile_image_key), profileBitmap);
    }

    private void handleProfileImage(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            profileBitmap = savedInstanceState.getParcelable(getString(R.string.profile_image_key));
            if (profileBitmap != null) {
                profileImage.setImageBitmap(profileBitmap);
            }
        }
    }

    private void handleDatePickerListener() {
        DatePickerFragment datePicker = (DatePickerFragment) getChildFragmentManager()
                .findFragmentByTag(DATE_PICKER_FRAG_TAG);

        if (datePicker != null) {
            datePicker.setDateListener(this);
        }
    }

    private void setupListeners() {
        registerBtn.setOnClickListener(v -> register());
        toLoginTv.setOnClickListener(v -> registrationListener.onLoginClick());

        birthDateField.setOnClickListener(v -> pickBirthDate());
        birthDateImage.setOnClickListener(v -> pickBirthDate());

        profileImage.setOnClickListener(v -> pickProfileImage());
        profileText.setOnClickListener(v -> pickProfileImage());
    }

    private void pickBirthDate() {
        DatePickerFragment dateFragment = new DatePickerFragment();
        dateFragment.setDateListener(this);
        dateFragment.show(getChildFragmentManager(), DATE_PICKER_FRAG_TAG);
    }

    @Override
    public void onDateUpdate(Calendar calendar) {
        String formattedDate = dateFormat.format(calendar.getTime());
        birthDateField.setText(formattedDate);
    }

    private void pickProfileImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();
            try {
                profileBitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                profileImage.setImageBitmap(profileBitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void register() {
        if (!isValid()) {
            Toast.makeText(getContext(), "Registration failed [client side]", Toast.LENGTH_LONG).show();
            registerBtn.setEnabled(true);
            return;
        }

        registerBtn.setEnabled(false);
        showProgressDialog();

        registrationPresenter.register(parseFields(), new BitmapWrapper(profileBitmap));
    }

    private void showProgressDialog() {
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();
    }

    private UserModel parseFields() {
        String name = nameField.getText().toString();
        String email = emailField.getText().toString();
        String password = passwordField.getText().toString();

        Date dateOfBirth = null;
        try {
            dateOfBirth = dateFormat.parse(birthDateField.getText().toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        UserModel userModel = new UserModel();
        userModel.setEmail(email);
        userModel.setBirthDate(dateOfBirth);
        userModel.setPassword(password);
        userModel.setName(name);

        return userModel;
    }

    @Override
    public void onSuccessfulRegistration(String email) {
        Toast.makeText(getContext(), "Registered: " + email, Toast.LENGTH_LONG).show();
        progressDialog.hide();
        registerBtn.setEnabled(true);
        registrationListener.onAuthorizationSuccess();
    }

    @Override
    public void onFailedRegistration(String fault) {
        Toast.makeText(getContext(), "Registration failed [server side]: " + fault, Toast.LENGTH_LONG).show();
        progressDialog.hide();
        registerBtn.setEnabled(true);
    }

    // definitely shouldn't be here, but fields mapping requires additional time
    private boolean isValid() {
        boolean valid = true;

        String name = nameField.getText().toString();
        String email = emailField.getText().toString();
        String password = passwordField.getText().toString();
        String birthDate = birthDateField.getText().toString();
        String passwordVerifications = verifyPasswordField.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            nameField.setError("at least 3 characters");
            valid = false;
        } else {
            nameField.setError(null);
        }

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

        if (passwordVerifications.isEmpty() || passwordVerifications.length() < 6 ||
                passwordVerifications.length() > 16 || !(passwordVerifications.equals(password))) {
            verifyPasswordField.setError("Password Do not match");
            valid = false;
        } else {
            verifyPasswordField.setError(null);
        }

        if (birthDate.isEmpty()) {
            birthDateField.setError("Birth date field is empty");
            valid = false;
        } else {
            birthDateField.setError(null);
        }

        if (profileBitmap == null) {
            valid = false;
        }

        return valid;
    }

    @Override
    public void onDestroy() {
        registrationPresenter.destroy();
        super.onDestroy();
    }
}
