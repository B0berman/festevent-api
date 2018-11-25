package com.festevent.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.beust.jcommander.internal.Lists;
import com.festevent.R;
import com.festevent.activities.EventsActivity;
import com.festevent.activities.FriendsActivity;
import com.festevent.activities.GroupsActivity;
import com.festevent.activities.ProfileModifyActivity;
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

public class UserFragment extends Fragment {

    private List<Publication> publications = Lists.newArrayList();
    private List<Media> medias = Lists.newArrayList();

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final RecyclerView recyclerView = view.findViewById(R.id.picturesRecyclerView);
        final RecyclerView precyclerView = view.findViewById(R.id.profilPublicationsRecyclerView);
        final TextView      friendsView = view.findViewById(R.id.friends_link);
        final TextView      eventsView = view.findViewById(R.id.events_link);
        final TextView      groupsView = view.findViewById(R.id.groups_link);
        final ImageView     profilImage = view.findViewById(R.id.profil_image_view);
        final ImageButton pmodify_button = view.findViewById(R.id.pmodify_button);

        User user = Client.getInstance().getUser();

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

        Call<List<Media>> mediasCall = Client.getInstance().getUserService().getUserPictures();
        mediasCall.enqueue(new CustomCallback<List<Media>>(getActivity(), 200) {
            @Override
            public void onResponse(Call<List<Media>> call, retrofit2.Response<List<Media>> response) {
                super.onResponse(call, response);
                if (response.code() != 200 || response.body() == null || response.body().isEmpty()) {
/*                    Media media = new Media();
                    Drawable d = ResourcesCompat.getDrawable(getResources(), R.drawable.usr_img, null);
                    Bitmap bitmap = ((BitmapDrawable)d).getBitmap();
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                    media.setBytes(stream.toByteArray());*/
                } else {
                    medias = response.body();
                }
                if (recyclerView.getAdapter() != null)
                    ((PicturesRecyclerAdapter) recyclerView.getAdapter()).updateContent(medias);
            }
        });

        Call<List<Publication>> publicationCall = Client.getInstance().getUserService().getUserPublications();
        publicationCall.enqueue(new CustomCallback<List<Publication>>(getActivity(), 200) {
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
            ppCall.enqueue(new CustomCallback<ResponseBody>(getActivity(), 200) {
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

        pmodify_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ProfileModifyActivity.class);
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
