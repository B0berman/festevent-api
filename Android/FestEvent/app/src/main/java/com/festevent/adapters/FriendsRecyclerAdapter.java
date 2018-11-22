package com.festevent.adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.festevent.R;
import com.festevent.beans.User;

import java.util.List;

/**
 * Created by walbecq on 22/04/18.
 */

public class FriendsRecyclerAdapter extends RecyclerView.Adapter<FriendsRecyclerAdapter.FriendHolder> {

    private final Activity activity;
    private List<User> users;
    private boolean updateEnable = true;

    public FriendsRecyclerAdapter(Activity context, List<User> list) {
        activity = context;
        users = list;
    }

    public List<User> getCurrentContent() {
        return users;
    }

    public void updateContent(List<User> users) {
        if (updateEnable) {
            this.users = users;
            notifyDataSetChanged();
        }
    }

    @Override
    public FriendHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View itemView;
        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.friend_item, parent, false);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = (Integer) ((RecyclerView) view.getParent()).getChildLayoutPosition(view);

                // start activity
            }
        });
        return new FriendHolder(itemView);
    }

    @Override
    public void onBindViewHolder(FriendHolder holder, int position) {
        User friend = users.get(position);
        holder.nameFriend.setText(friend.getFirstName() + " " + friend.getLastName());
//        holder.imagePublisher.setImageBitmap(BitmapFactory.decodeByteArray(publication.getPublisher().getProfilPicture().getBytes(), 0,
//                publication.getPublisher().getProfilPicture().getBytes().length));
//        holder.imagePublication.setImageBitmap(BitmapFactory.decodeByteArray(publication.getMedias().get(0).getBytes(), 0, publication.getMedias().get(0).getBytes().length));
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public boolean isUpdateEnable() {
        return updateEnable;
    }

    public void setUpdateEnable(boolean updateEnable) {
        this.updateEnable = updateEnable;
    }

    public class FriendHolder extends ViewHolder {
        public ImageView imageFriend;
        public TextView  nameFriend;
        public TextView  commonNumberFriends;

        public FriendHolder(View itemView) {
            super(itemView);

            imageFriend = itemView.findViewById(R.id.friend_image_view);
            nameFriend = itemView.findViewById(R.id.friend_name_view);
            commonNumberFriends = itemView.findViewById(R.id.friend_commons_view);
        }
    }
}
