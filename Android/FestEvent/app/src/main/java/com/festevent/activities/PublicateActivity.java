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
import android.os.StrictMode;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.festevent.R;
import com.festevent.api.Client;
import com.festevent.api.CustomCallback;
import com.festevent.beans.Media;
import com.festevent.beans.Publication;
import com.festevent.beans.User;
import com.festevent.utils.JobHelper;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;


public class PublicateActivity extends AppCompatActivity {


    private EditText publicateContentView;
    private View mProgressView;
    private View mLoginFormView;
    private ImageButton publicateButton;
    private String mCurrentPhotoPath;
    private Activity context = this;
    private Media       profil_pic;
    private ImageView profilImage;
    File image = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publicate);
        // Set up the login form.

        publicateContentView = findViewById(R.id.publicate_content_view);
        publicateButton = findViewById(R.id.publicate_button);

        publicateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.publicate_form);
        mProgressView = findViewById(R.id.publicate_progress);

        profilImage = findViewById(R.id.publicate_profil_image_view);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        if (resultCode == RESULT_OK) {

            switch (requestCode) {
                case 100:
                    Bundle extras = data.getExtras();
                    Bitmap imageBitmap = (Bitmap) extras.get("data");
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                    byte[] byteArray = stream.toByteArray();
                    profil_pic = new Media();
                    profil_pic.setType(Media.TYPE.IMAGE_PNG);
                    profil_pic.setBytes(byteArray);
                    profilImage.setImageBitmap(imageBitmap);
                    break;
                default:
                    Toast.makeText(this, "Something went wrong...", Toast.LENGTH_SHORT).show();
                    break;
            }

        }
    }
    private void attemptLogin() {

        // Reset errors.
        publicateContentView.setError(null);

        // Store values at the time of the login attempt.
        String content = publicateContentView.getText().toString();


        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (content == null || TextUtils.isEmpty(content)) {
            publicateContentView.setError(getString(R.string.error_invalid_password));
            focusView = publicateContentView;
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

            Publication publication = new Publication();
            publication.setContent(content);
            if (profil_pic != null) {
                // set publication pictures
            }
            Call<Publication> call = Client.getInstance().getPublicationService().createPublicaton(publication);
            call.enqueue(new CustomCallback<Publication>(PublicateActivity.this, 201) {
                @Override
                public void onResponse(Call<Publication> call, retrofit2.Response<Publication> response) {
                    super.onResponse(call, response);
                    if (response.code() == 201) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(PublicateActivity.this);
                        builder.setMessage(R.string.publicate_success);
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

