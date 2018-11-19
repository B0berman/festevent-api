package com.visitcardpro.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.visitcardpro.R;
import com.visitcardpro.api.Client;
import com.visitcardpro.api.CustomCallback;
import com.visitcardpro.database.dao.AuthenticationDAO;
import com.visitcardpro.database.dao.UserDAO;
import com.visitcardpro.utils.JobHelper;

import okhttp3.ResponseBody;
import retrofit2.Call;


public class LoginActivity extends AppCompatActivity {


    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.

        checkConnected();
        mEmailView = findViewById(R.id.email);

        mPasswordView = findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        Button noAccountButton = findViewById(R.id.no_account_button);

        noAccountButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }

    private void attemptLogin() {

        mEmailView.setError(null);
        mPasswordView.setError(null);

        final String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password.
        if (TextUtils.isEmpty(password) || !JobHelper.isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!JobHelper.isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            showProgress(true);
            Call<ResponseBody> call = Client.getInstance().getAuthenticationService().signIn(JobHelper.generateAuthorization(email + ":" + password));
            call.enqueue(new CustomCallback<ResponseBody>(LoginActivity.this, 202) {
                @Override
                public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                    super.onResponse(call, response);
                    String token = response.headers().get("accessToken");
                    Authentication auth = new Authentication(email, token);
                    setUser(auth);
                }
            });
        }
    }

    private void setUser(final Authentication auth) {
        Call<User> call = Client.getInstance().getUserService().getUser(auth.getAccessToken());
        call.enqueue(new CustomCallback<User>(LoginActivity.this) {
            @Override
            public void onResponse(Call<User> call, retrofit2.Response<User> response) {
                super.onResponse(call, response);
                User user = response.body();
                user.setAuthentication(auth);
                UserDAO uDao = new UserDAO(LoginActivity.this);
                AuthenticationDAO aDao = new AuthenticationDAO(LoginActivity.this);

                aDao.open();
                uDao.open();

                aDao.add(auth);
                uDao.add(user);

                aDao.close();
                uDao.close();

                Client.getInstance().setUser(user);
                onConnected();
            }
        });
    }

    private void onConnected() {
        Intent launchNextActivity = new Intent(LoginActivity.this, MainActivity.class);
        launchNextActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        launchNextActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        launchNextActivity.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(launchNextActivity);
    }

    private void checkConnected() {
        AuthenticationDAO authenticationDAO = new AuthenticationDAO(LoginActivity.this);
        authenticationDAO.open();
        Authentication auth = authenticationDAO.get();
        if (auth != null) {
            authenticationDAO.close();
            UserDAO uDao = new UserDAO(LoginActivity.this);
            uDao.open();
            User user = uDao.get();
            if (user != null) {
                uDao.close();
                user.setAuthentication(auth);
                Client.getInstance().setUser(user);
                onConnected();
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }
}

