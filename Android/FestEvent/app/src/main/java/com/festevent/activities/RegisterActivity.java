package com.festevent.activities;

/**
 * Created by hugo on 19/04/18.
 */

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.festevent.R;
import com.festevent.api.Client;
import com.festevent.api.CustomCallback;
import com.festevent.beans.Media;
import com.festevent.beans.User;
import com.festevent.utils.JobHelper;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;


public class RegisterActivity extends AppCompatActivity {


    private AutoCompleteTextView mEmailView;
    private EditText mLNameView;
    private EditText mFNameView;
    private EditText mPasswordView;
    private EditText mPasswordConfirmView;
    private View mProgressView;
    private View mLoginFormView;
    private String mCurrentPhotoPath;
    private Activity context = this;
    private Media       profil_pic;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        // Set up the login form.
        mEmailView = findViewById(R.id.email_create);

        mPasswordView = findViewById(R.id.password_create);
        mLNameView = findViewById(R.id.lname_create);
        mFNameView = findViewById(R.id.fname_create);
        mPasswordConfirmView = findViewById(R.id.password_confirm);
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

        Button mEmailSignInButton = findViewById(R.id.register_button);
        mEmailSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.register_form);
        mProgressView = findViewById(R.id.register_progress);

        final ImageView profilImage = findViewById(R.id.register_profil_image_view);
        profilImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                android.app.AlertDialog.Builder alert = new android.app.AlertDialog.Builder(view.getContext());
                alert.setTitle(R.string.add_profil_pic);
                alert.setMessage(R.string.src_profil_pic);

                alert.setPositiveButton(R.string.picture_string, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String fileUri = "";
                        File image = null;
                        try {
                            image = JobHelper.createImageFile(context);
                            mCurrentPhotoPath = image.getAbsolutePath();
                            fileUri = FileProvider.getUriForFile(context, "com.festevent", image).toString();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                        startActivityForResult(intent, 1);
                    }});

                alert.setNegativeButton(R.string.cancel_string , new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Canceled.
                    }
                });

                alert.show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        if (resultCode == RESULT_OK) {

            switch (requestCode) {
                case 1:
                    Bundle extras = data.getExtras();
                    Bitmap imageBitmap = (Bitmap) extras.get("data");
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                    byte[] byteArray = stream.toByteArray();
                    profil_pic = new Media();
                    profil_pic.setType(Media.TYPE.IMAGE_PNG);
                    profil_pic.setBytes(byteArray);
                    break;
                default:
                    Toast.makeText(this, "Something went wrong...", Toast.LENGTH_SHORT).show();
                    break;
            }

        }
    }
    private void attemptLogin() {

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();
        String passwordConfirm = mPasswordConfirmView.getText().toString();
        String fname = mFNameView.getText().toString();
        String lname = mLNameView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password) || !JobHelper.isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }
        if (!passwordConfirm.equals(password)) {
            mPasswordView.setError(getString(R.string.error_no_match_password));
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

        if (TextUtils.isEmpty(fname)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mFNameView;
            cancel = true;
        }
        if (TextUtils.isEmpty(lname)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mFNameView;
            cancel = true;
        }
        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            User user = new User();
            user.setEmail(email);
            user.setFirstName(fname);
            user.setLastName(lname);
            user.setPassword(password);
            if (profil_pic != null) {
                user.setProfilPicture(profil_pic);
            }
            Call<ResponseBody> call = Client.getInstance().getUserService().signUp(user);
            call.enqueue(new CustomCallback<ResponseBody>(RegisterActivity.this, 201) {
                @Override
                public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                    super.onResponse(call, response);
                    if (response.code() == 201) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                        builder.setMessage(R.string.email_confirm_sent);
                        builder.setCancelable(true);
                        builder.setPositiveButton(
                                "Ok",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                        finish();
                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.show();
                    } else {
                        showProgress(false);
                    }
                }
            });
        }
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
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
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }
}

