package com.festevent.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.beust.jcommander.internal.Lists;
import com.festevent.R;
import com.festevent.adapters.EventsRecyclerAdapter;
import com.festevent.adapters.PublicationsRecyclerAdapter;
import com.festevent.api.Client;
import com.festevent.beans.Event;
import com.festevent.beans.Publication;

import java.util.Date;
import java.util.List;

public class EventsFragment extends Fragment {

    public EventsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final RecyclerView precyclerView = view.findViewById(R.id.evntsRecyclerView);

        List<Event> events = Lists.newArrayList();
        Event event = new Event();
        event.setStart(new Date());
        event.setTitle("Le meilleur Event !");
        events.add(event);
        events.add(event);
        events.add(event);
        events.add(event);
        events.add(event);

        EventsRecyclerAdapter pAdapter = new EventsRecyclerAdapter(getActivity(), events);
        RecyclerView.LayoutManager pLayoutManager = new LinearLayoutManager(getActivity());
        precyclerView.setLayoutManager(pLayoutManager);
        precyclerView.setItemAnimator(new DefaultItemAnimator());
        precyclerView.setAdapter(pAdapter);
        ((EventsRecyclerAdapter) precyclerView.getAdapter()).updateContent(events);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_events, container, false);
    }

}
