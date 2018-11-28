package com.festevent.adapters;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.festevent.R;
import com.festevent.api.Client;
import com.festevent.api.CustomCallback;
import com.festevent.beans.Media;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * Created by walbecq on 22/04/18.
 */

public class PicturesRecyclerAdapter extends RecyclerView.Adapter<PicturesRecyclerAdapter.PictureHolder> {

    private final Activity activity;
    private List<Media> pictures;
    private boolean updateEnable = true;

    public PicturesRecyclerAdapter(Activity context, List<Media> list) {
        activity = context;
        pictures = list;
    }

    public List<Media> getCurrentContent() {
        return pictures;
    }

    public void updateContent(List<Media> pictures) {
        if (updateEnable) {
            this.pictures = pictures;
            notifyDataSetChanged();
        }
    }

    @Override
    public PictureHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View itemView;
        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.picture_item, parent, false);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = (Integer) ((RecyclerView) view.getParent()).getChildLayoutPosition(view);

                // start activity
            }
        });
        return new PictureHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final PictureHolder holder, int position) {
        Media picture = pictures.get(position);
        if (picture.getBytes() != null && picture.getBytes().length > 0) {
            holder.image.setImageBitmap(BitmapFactory.decodeByteArray(picture.getBytes(), 0, picture.getBytes().length));
        } else if (picture.getId() != null && !picture.getId().isEmpty()) {
            Call<ResponseBody> ppCall = Client.getInstance().getUserService().getImage(picture.getId());
            ppCall.enqueue(new CustomCallback<ResponseBody>(activity, 200) {
                @Override
                public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                    super.onResponse(call, response);
                    if (response.code() != 200 || response.body() == null) {

                    } else {
                        Bitmap bmp = BitmapFactory.decodeStream(response.body().byteStream());
                        holder.image.setImageBitmap(bmp);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return pictures.size();
    }

    public boolean isUpdateEnable() {
        return updateEnable;
    }

    public void setUpdateEnable(boolean updateEnable) {
        this.updateEnable = updateEnable;
    }

public class PictureHolder extends ViewHolder {
        public ImageView image;

        public PictureHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.carouselimageView);
        }
    }
}
