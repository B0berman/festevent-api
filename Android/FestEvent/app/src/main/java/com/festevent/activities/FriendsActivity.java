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
import com.festevent.api.Client;
import com.festevent.api.CustomCallback;
import com.festevent.beans.Comment;
import com.festevent.beans.User;

import java.util.List;

import retrofit2.Call;

public class FriendsActivity extends AppCompatActivity {

    private List<User> users = Lists.newArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

/*        Bundle b = getIntent().getExtras();
        User user = null;
        if (b != null)
            user = (User) b.getSerializable("user");*/

        final RecyclerView recyclerView = findViewById(R.id.friendsRecyclerView);
        final FriendsRecyclerAdapter pAdapter = new FriendsRecyclerAdapter(this, users);
        RecyclerView.LayoutManager pLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(pLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(pAdapter);

        Call<List<User>> friendsCall = Client.getInstance().getUserService().getUserFriends();
        friendsCall.enqueue(new CustomCallback<List<User>>(this, 200) {
            @Override
            public void onResponse(Call<List<User>> call, retrofit2.Response<List<User>> response) {
                super.onResponse(call, response);
                if (response.code() == 200) {
                    users = response.body();
                    pAdapter.updateContent(users);
                }
            }
        });
    }
}
