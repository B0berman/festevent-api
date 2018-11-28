package com.festevent.adapters;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.festevent.R;
import com.festevent.activities.UserActivity;
import com.festevent.api.Client;
import com.festevent.api.CustomCallback;
import com.festevent.beans.User;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;

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
                Bundle b = new Bundle();
                b.putSerializable("user", users.get(pos));
                Intent intent = new Intent(activity, UserActivity.class);
                intent.putExtras(b);
                activity.startActivity(intent);
                // start activity
            }
        });
        return new FriendHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final FriendHolder holder, int position) {
        User friend = users.get(position);
        holder.nameFriend.setText(friend.getFirstName() + " " + friend.getLastName());
        if (friend.getProfilPicture() != null) {
            Call<ResponseBody> publisherPicCall = Client.getInstance().getUserService().getImage(friend.getProfilPicture().getId());
            publisherPicCall.enqueue(new CustomCallback<ResponseBody>(activity, 200) {
                @Override
                public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                    super.onResponse(call, response);
                    if (response.code() != 200 || response.body() == null) {

                    } else {
                        Bitmap bmp = BitmapFactory.decodeStream(response.body().byteStream());
                        holder.imageFriend.setImageBitmap(bmp);
                    }
                }
            });
        }
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
