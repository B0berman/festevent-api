package com.festevent.adapters.custom;

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

public class CustomRecyclerAdapter<T> extends RecyclerView.Adapter<ViewHolder> {

    private final Activity activity;
    private List<T> data;
    private boolean updateEnable = true;

    public CustomRecyclerAdapter(Activity context, List<T> list) {
        activity = context;
        data = list;
    }

    public List<T> getCurrentContent() {
        return data;
    }

    public void updateContent(List<T> data) {
        if (updateEnable) {
            this.data = data;
            notifyDataSetChanged();
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
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
    public void onBindViewHolder(ViewHolder holder, int position) {

    }

    @Override
    public int getItemViewType(int position) {
        // Just as an example, return 0 or 2 depending on position
        // Note that unlike in ListView adapters, types don't have to be contiguous
        if (position == 0)
            return 0;
        return 1;
    }

    @Override
    public int getItemCount() {
        return data.size();
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
