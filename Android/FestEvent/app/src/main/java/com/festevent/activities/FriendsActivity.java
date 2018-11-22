package com.festevent.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.beust.jcommander.internal.Lists;
import com.festevent.R;
import com.festevent.adapters.FriendsRecyclerAdapter;
import com.festevent.adapters.PublicationsRecyclerAdapter;
import com.festevent.beans.User;

import java.util.List;

public class FriendsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        List<User> users = Lists.newArrayList();
        User user = new User();
        user.setFirstName("Toto");
        user.setLastName("Toto");
        users.add(user);
        users.add(user);
        users.add(user);
        users.add(user);
        users.add(user);
        users.add(user);
        users.add(user);
        users.add(user);

        final RecyclerView recyclerView = findViewById(R.id.friendsRecyclerView);
        FriendsRecyclerAdapter pAdapter = new FriendsRecyclerAdapter(this, users);
        RecyclerView.LayoutManager pLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(pLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(pAdapter);
        ((FriendsRecyclerAdapter) recyclerView.getAdapter()).updateContent(users);
    }

}
