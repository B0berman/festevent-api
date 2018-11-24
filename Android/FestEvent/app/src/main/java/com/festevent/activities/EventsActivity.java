package com.festevent.activities;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.beust.jcommander.internal.Lists;
import com.festevent.R;
import com.festevent.adapters.EventsRecyclerAdapter;
import com.festevent.beans.Event;

import java.util.Date;
import java.util.List;

public class EventsActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {
    private SearchView searchView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_events);


        final RecyclerView precyclerView = findViewById(R.id.eventsRecyclerView);

        List<Event> events = Lists.newArrayList();
        Event event = new Event();
        event.setStart(new Date());
        event.setTitle("Le meilleur Event !");
        events.add(event);
        events.add(event);
        events.add(event);
        events.add(event);
        events.add(event);

        EventsRecyclerAdapter pAdapter = new EventsRecyclerAdapter(this, events);
        RecyclerView.LayoutManager pLayoutManager = new LinearLayoutManager(this);
        precyclerView.setLayoutManager(pLayoutManager);
        precyclerView.setItemAnimator(new DefaultItemAnimator());
        precyclerView.setAdapter(pAdapter);
        ((EventsRecyclerAdapter) precyclerView.getAdapter()).updateContent(events);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.research, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchManager searchManager = (SearchManager)this.getSystemService(Context.SEARCH_SERVICE);

        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
        }
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(this.getComponentName()));
            searchView.setOnQueryTextListener(this);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                // Not implemented here
                return false;
            default:
                break;
        }
        searchView.setOnQueryTextListener(this);
        return super.onOptionsItemSelected(item);
    }}
