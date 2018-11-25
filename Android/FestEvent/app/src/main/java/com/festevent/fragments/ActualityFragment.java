package com.festevent.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beust.jcommander.internal.Lists;
import com.festevent.R;
import com.festevent.adapters.PublicationsRecyclerAdapter;
import com.festevent.api.Client;
import com.festevent.beans.Publication;

import java.util.List;

public class ActualityFragment extends Fragment {
    private static int firstVisibleInListview = 0;

    public ActualityFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final RecyclerView precyclerView = view.findViewById(R.id.publicationsRecyclerView);

        List < Publication > publications = Lists.newArrayList();
        Publication publication = new Publication();
        publication.setPublisher(Client.getInstance().getUser());
        publication.setContent("Coucou les gens ceci est ujne publication de test pour test les publications");
        publications.add(publication);
        publications.add(publication);
        publications.add(publication);
        publications.add(publication);
        publications.add(publication);

        PublicationsRecyclerAdapter pAdapter = new PublicationsRecyclerAdapter(getActivity(), publications);
        RecyclerView.LayoutManager pLayoutManager = new LinearLayoutManager(getActivity());
        precyclerView.setLayoutManager(pLayoutManager);
        precyclerView.setItemAnimator(new DefaultItemAnimator());
        precyclerView.setAdapter(pAdapter);
        ((PublicationsRecyclerAdapter) precyclerView.getAdapter()).updateContent(publications);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_actuality, container, false);
    }

}
