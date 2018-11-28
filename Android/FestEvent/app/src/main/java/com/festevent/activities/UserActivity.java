package com.festevent.activities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.beust.jcommander.internal.Lists;
import com.festevent.R;
import com.festevent.adapters.PicturesRecyclerAdapter;
import com.festevent.adapters.PublicationsRecyclerAdapter;
import com.festevent.api.Client;
import com.festevent.api.CustomCallback;
import com.festevent.beans.Media;
import com.festevent.beans.Publication;
import com.festevent.beans.User;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;

public class UserActivity extends AppCompatActivity {
    private List<Publication> publications = Lists.newArrayList();
    private List<Media> medias = Lists.newArrayList();
    private RecyclerView precyclerView;
    private TextView userNameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_user);

        final RecyclerView recyclerView = findViewById(R.id.picturesRecyclerView);
        precyclerView = findViewById(R.id.profilPublicationsRecyclerView);
        final ImageView profilImage = findViewById(R.id.profil_image_view);
        userNameView = findViewById(R.id.profil_username_view);

        CardView cardView = findViewById(R.id.profile_perso_cardviaw);
        cardView.setVisibility(View.GONE);
        ImageView pmodify_button = findViewById(R.id.pmodify_button);
        pmodify_button.setVisibility(View.GONE);

        Bundle b = getIntent().getExtras();
        User user = null;
        if (b != null)
            user = (User) b.getSerializable("user");

        userNameView.setText(user.getFirstName() + " " + user.getLastName());

        PicturesRecyclerAdapter mAdapter = new PicturesRecyclerAdapter(this, medias);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this.getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        recyclerView.setVisibility(View.GONE);

        PublicationsRecyclerAdapter pAdapter = new PublicationsRecyclerAdapter(this, publications);
        RecyclerView.LayoutManager pLayoutManager = new LinearLayoutManager(this);
        precyclerView.setLayoutManager(pLayoutManager);
        precyclerView.setItemAnimator(new DefaultItemAnimator());
        precyclerView.setAdapter(pAdapter);

        Call<List<Media>> mediasCall = Client.getInstance().getFriendService().getFriendPictures(user.getEmail());
        mediasCall.enqueue(new CustomCallback<List<Media>>(this, 200) {
            @Override
            public void onResponse(Call<List<Media>> call, retrofit2.Response<List<Media>> response) {
                super.onResponse(call, response);
                if (response.code() != 200 || response.body() == null || response.body().isEmpty()) {

                } else {
                    medias = response.body();
                }
                if (recyclerView.getAdapter() != null && medias.size() > 0) {
                    if (recyclerView.getVisibility() == View.GONE)
                        recyclerView.setVisibility(View.VISIBLE);
                    ((PicturesRecyclerAdapter) recyclerView.getAdapter()).updateContent(medias);
                }
            }
        });

        Call<List<Publication>> publicationCall = Client.getInstance().getFriendService().getFriendPublications(user.getEmail());
        publicationCall.enqueue(new CustomCallback<List<Publication>>(this, 200) {
            @Override
            public void onResponse(Call<List<Publication>> call, retrofit2.Response<List<Publication>> response) {
                super.onResponse(call, response);
                if (response.code() != 200 || response.body() == null || response.body().isEmpty()) {

                } else {
                    publications = response.body();
                }
                if (precyclerView.getAdapter() != null)
                    ((PublicationsRecyclerAdapter) precyclerView.getAdapter()).updateContent(publications);
            }
        });

        if (user.getProfilPicture() != null && user.getProfilPicture().getId() != null && !user.getProfilPicture().getId().isEmpty()) {
            Call<ResponseBody> ppCall = Client.getInstance().getUserService().getImage(user.getProfilPicture().getId());
            ppCall.enqueue(new CustomCallback<ResponseBody>(this, 200) {
                @Override
                public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                    super.onResponse(call, response);
                    if (response.code() != 200 || response.body() == null) {

                    } else {
                        Bitmap bmp = BitmapFactory.decodeStream(response.body().byteStream());
                        profilImage.setImageBitmap(bmp);
                    }
                }
            });
        }
    }
}
