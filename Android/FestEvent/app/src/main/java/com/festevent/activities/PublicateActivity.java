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
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.beust.jcommander.internal.Lists;
import com.festevent.R;
import com.festevent.adapters.PicturesRecyclerAdapter;
import com.festevent.api.Client;
import com.festevent.api.CustomCallback;
import com.festevent.beans.Media;
import com.festevent.beans.Publication;
import com.festevent.utils.JobHelper;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;


public class PublicateActivity extends AppCompatActivity {


    private EditText publicateContentView;
    private View mProgressView;
    private View mLoginFormView;
    private ImageButton publicateButton;
    private ImageButton pictureButton;
    private String mCurrentPhotoPath;
    private Activity context = this;
    private Media       profil_pic;
    private ImageView profilImage;
    private File image = null;
    private RecyclerView recyclerView;
    private List<Media> medias = Lists.newArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publicate);
        // Set up the login form.
        recyclerView = findViewById(R.id.picturesRecyclerView);

        publicateContentView = findViewById(R.id.publicate_content_view);
        publicateButton = findViewById(R.id.publicate_button);
        pictureButton = findViewById(R.id.publicate_picture_button);

        PicturesRecyclerAdapter mAdapter = new PicturesRecyclerAdapter(this, medias);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this.getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        recyclerView.setVisibility(View.GONE);

        publicateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });
        pictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                android.app.AlertDialog.Builder alert = new android.app.AlertDialog.Builder(view.getContext());
                alert.setTitle(R.string.add_profil_pic);
                alert.setMessage(R.string.src_profil_pic);
                final Uri fileUri;

                try {
                    image = JobHelper.createImageFile();
                    mCurrentPhotoPath = image.getAbsolutePath();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                fileUri = Uri.fromFile(image);//FileProvider.getUriForFile(context, "com.festevent", image).toString();

                alert.setPositiveButton(R.string.picture_string, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                        StrictMode.setVmPolicy(builder.build());
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri.toString());
                        intent.putExtra("outputFormat",
                                Bitmap.CompressFormat.JPEG.toString());
                        startActivityForResult(intent, 100);
                    }});

                alert.setNegativeButton("form_galery", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        Intent photoPickerIntent = new Intent(
                                Intent.ACTION_PICK,
                                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        photoPickerIntent.setType("image/*");
                        photoPickerIntent.putExtra("crop", "true");
                        photoPickerIntent.putExtra("outputX", 150);
                        photoPickerIntent.putExtra("outputY", 150);
                        photoPickerIntent.putExtra("aspectX", 1);
                        photoPickerIntent.putExtra("aspectY", 1);
                        photoPickerIntent.putExtra("scale", true);
                        photoPickerIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri.toString());
                        photoPickerIntent.putExtra("outputFormat",
                                Bitmap.CompressFormat.JPEG.toString());
                        startActivityForResult(photoPickerIntent, 100);
                        // Canceled.
                    }
                });

                alert.show();
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
                    Media media = new Media();
                    media.setType(Media.TYPE.IMAGE_JPG);
                    media.setBytes(byteArray);
                    if (recyclerView.getVisibility() == View.GONE)
                        recyclerView.setVisibility(View.VISIBLE);
                    medias.add(media);
                    ((PicturesRecyclerAdapter) recyclerView.getAdapter()).updateContent(medias);
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
            publicateContentView.setError(getString(R.string.error_empty_field));
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
            if (medias != null && !medias.isEmpty()) {
                publication.setMedias(medias);
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

