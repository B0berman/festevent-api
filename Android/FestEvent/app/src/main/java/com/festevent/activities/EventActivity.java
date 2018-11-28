package com.festevent.activities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.beust.jcommander.internal.Lists;
import com.festevent.R;
import com.festevent.adapters.PicturesRecyclerAdapter;
import com.festevent.adapters.PublicationsRecyclerAdapter;
import com.festevent.api.Client;
import com.festevent.api.CustomCallback;
import com.festevent.beans.Event;
import com.festevent.beans.Media;
import com.festevent.beans.Publication;
import com.festevent.beans.User;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;

public class EventActivity extends AppCompatActivity {

    private Event event;
    private ImageView eventCoverView;
    private List<Publication> publications = Lists.newArrayList();
    private TextView eventNameView;
    private TextView eventDateView;
    private TextView eventDetailsView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        Bundle b = getIntent().getExtras();
        if (b != null)
            event = (Event) b.getSerializable("event");

        eventCoverView = findViewById(R.id.event_cover_image_view);
        eventNameView = findViewById(R.id.eventNameView);
        eventDateView = findViewById(R.id.eventDateTextView);
        eventDetailsView = findViewById(R.id.eventDetailView);

        final RecyclerView recyclerView = findViewById(R.id.eventPicturesRecyclerView);
        final RecyclerView precyclerView = findViewById(R.id.eventPublicationsRecyclerView);


        PicturesRecyclerAdapter mAdapter = new PicturesRecyclerAdapter(this, event.getPictures());
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

        Call<ResponseBody> coverCall = Client.getInstance().getUserService().getImage(event.getMainPicture().getId());
        coverCall.enqueue(new CustomCallback<ResponseBody>(this, 200) {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                super.onResponse(call, response);
                if (response.code() != 200 || response.body() == null) {

                } else {
                    Bitmap bmp = BitmapFactory.decodeStream(response.body().byteStream());
                    eventCoverView.setImageBitmap(bmp);
                }
            }
        });

        Call<List<Media>> mediasCall = Client.getInstance().getUserService().getUserPictures();
        mediasCall.enqueue(new CustomCallback<List<Media>>(this, 200) {
            @Override
            public void onResponse(Call<List<Media>> call, retrofit2.Response<List<Media>> response) {
                super.onResponse(call, response);
                if (response.code() != 200 || response.body() == null || response.body().isEmpty()) {

                } else {
                    event.setPictures(response.body());
                }
                if (recyclerView.getAdapter() != null && event.getPictures().size() > 0) {
                    if (recyclerView.getVisibility() == View.GONE)
                        recyclerView.setVisibility(View.VISIBLE);
                    ((PicturesRecyclerAdapter) recyclerView.getAdapter()).updateContent(event.getPictures());
                }
            }
        });

        Call<List<Publication>> publicationCall = Client.getInstance().getEventService().getEventPublications(event.getId());
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

        eventNameView.setText(event.getTitle());
        eventDateView.setText(event.getStart());
        eventDetailsView.setText(event.getAddress());

    }
}
