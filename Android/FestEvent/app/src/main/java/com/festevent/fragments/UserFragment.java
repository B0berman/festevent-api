package com.festevent.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.beust.jcommander.internal.Lists;
import com.festevent.R;
import com.festevent.activities.EventsActivity;
import com.festevent.activities.FriendsActivity;
import com.festevent.activities.GroupsActivity;
import com.festevent.adapters.PicturesRecyclerAdapter;
import com.festevent.adapters.PublicationsRecyclerAdapter;
import com.festevent.api.Client;
import com.festevent.beans.Media;
import com.festevent.beans.Publication;

import java.io.ByteArrayOutputStream;
import java.util.List;

public class UserFragment extends Fragment {
    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        final RecyclerView recyclerView = view.findViewById(R.id.picturesRecyclerView);
        final RecyclerView precyclerView = view.findViewById(R.id.profilPublicationsRecyclerView);
        final TextView      friendsView = view.findViewById(R.id.friends_link);
        final TextView      eventsView = view.findViewById(R.id.events_link);
        final TextView      groupsView = view.findViewById(R.id.groups_link);

        List<Media> medias = Lists.newArrayList();
        Media media = new Media();
        Drawable d = ResourcesCompat.getDrawable(getResources(), R.drawable.usr_img, null);
        Bitmap bitmap = ((BitmapDrawable)d).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        media.setBytes(stream.toByteArray());

        medias.add(media);
        medias.add(media);
        medias.add(media);
        medias.add(media);
        medias.add(media);

        List<Publication> publications = Lists.newArrayList();
        Publication publication = new Publication();
        publication.setPublisher(Client.getInstance().getUser());
        publication.setContent("Coucou les gens ceci est ujne publication de test pour test les publications");
        publications.add(publication);
        publications.add(publication);
        publications.add(publication);
        publications.add(publication);
        publications.add(publication);

        PicturesRecyclerAdapter mAdapter = new PicturesRecyclerAdapter(getActivity(), medias);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        ((PicturesRecyclerAdapter) recyclerView.getAdapter()).updateContent(medias);

        PublicationsRecyclerAdapter pAdapter = new PublicationsRecyclerAdapter(getActivity(), publications);
        RecyclerView.LayoutManager pLayoutManager = new LinearLayoutManager(getActivity());
        precyclerView.setLayoutManager(pLayoutManager);
        precyclerView.setItemAnimator(new DefaultItemAnimator());
        precyclerView.setAdapter(pAdapter);
        ((PublicationsRecyclerAdapter) precyclerView.getAdapter()).updateContent(publications);

        friendsView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), FriendsActivity.class);
                startActivity(intent);
            }
        });
        eventsView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), EventsActivity.class);
                startActivity(intent);
            }
        });
        groupsView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), GroupsActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user, container, false);
    }

}
