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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.festevent.R;
import com.festevent.activities.EventActivity;
import com.festevent.activities.FriendsActivity;
import com.festevent.activities.UserActivity;
import com.festevent.api.Client;
import com.festevent.api.CustomCallback;
import com.festevent.beans.Event;
import com.festevent.beans.Media;
import com.festevent.beans.Publication;
import com.festevent.utils.JobHelper;

import java.sql.Date;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * Created by walbecq on 22/04/18.
 */

public class EventsRecyclerAdapter extends RecyclerView.Adapter<EventsRecyclerAdapter.EventHolder> {

    private final Activity activity;
    private List<Event> events;
    private boolean updateEnable = true;

    public EventsRecyclerAdapter(Activity context, List<Event> list) {
        activity = context;
        events = list;
    }

    public List<Event> getCurrentContent() {
        return events;
    }

    public void updateContent(List<Event> publications) {
        if (updateEnable) {
            this.events = publications;
            notifyDataSetChanged();
        }
    }

    @Override
    public EventHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View itemView;
        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_item, parent, false);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = (Integer) ((RecyclerView) view.getParent()).getChildLayoutPosition(view);
                Bundle b = new Bundle();
                b.putSerializable("event", events.get(pos));
                Intent intent = new Intent(activity, EventActivity.class);
                intent.putExtras(b);
                activity.startActivity(intent);
                // start activity
            }
        });
        return new EventHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final EventHolder holder, int position) {
        Event event = events.get(position);
        holder.eventTitle.setText(event.getTitle());
        holder.eventDate.setText("27\nNOV");    //JobHelper.formatDate(Date.valueOf(event.getStart()), "dd\nMMM"));

        Call<ResponseBody> coverCall = Client.getInstance().getUserService().getImage(event.getMainPicture().getId());
        coverCall.enqueue(new CustomCallback<ResponseBody>(activity, 200) {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                super.onResponse(call, response);
                if (response.code() != 200 || response.body() == null) {

                } else {
                    Bitmap bmp = BitmapFactory.decodeStream(response.body().byteStream());
                    holder.eventImage.setImageBitmap(bmp);
                }
            }
        });
//        holder.imagePublisher.setImageBitmap(BitmapFactory.decodeByteArray(publication.getPublisher().getProfilPicture().getBytes(), 0,
//                publication.getPublisher().getProfilPicture().getBytes().length));
//        holder.imagePublication.setImageBitmap(BitmapFactory.decodeByteArray(publication.getMedias().get(0).getBytes(), 0, publication.getMedias().get(0).getBytes().length));
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public boolean isUpdateEnable() {
        return updateEnable;
    }

    public void setUpdateEnable(boolean updateEnable) {
        this.updateEnable = updateEnable;
    }

    public class EventHolder extends ViewHolder {
        public ImageView eventImage;
        public TextView  eventDetails;
        public TextView  eventDate;
        public TextView  eventTitle;

        public EventHolder(View itemView) {
            super(itemView);

            eventDate = itemView.findViewById(R.id.eventItemDateTextView);
            eventDetails = itemView.findViewById(R.id.eventItemDetailView);
            eventImage = itemView.findViewById(R.id.eventItemImageView);
            eventTitle = itemView.findViewById(R.id.eventItemNameView);
        }
    }
}
