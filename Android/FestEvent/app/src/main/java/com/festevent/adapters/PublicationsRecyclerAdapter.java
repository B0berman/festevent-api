package com.festevent.adapters;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.festevent.R;
import com.festevent.beans.Media;
import com.festevent.beans.Publication;

import java.util.List;

/**
 * Created by walbecq on 22/04/18.
 */

public class PublicationsRecyclerAdapter extends RecyclerView.Adapter<PublicationsRecyclerAdapter.PublicationHolder> {

    private final Activity activity;
    private List<Publication> publications;
    private boolean updateEnable = true;

    public PublicationsRecyclerAdapter(Activity context, List<Publication> list) {
        activity = context;
        publications = list;
    }

    public List<Publication> getCurrentContent() {
        return publications;
    }

    public void updateContent(List<Publication> publications) {
        if (updateEnable) {
            this.publications = publications;
            notifyDataSetChanged();
        }
    }

    @Override
    public PublicationHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View itemView;
        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.publication_item, parent, false);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = (Integer) ((RecyclerView) view.getParent()).getChildLayoutPosition(view);

                // start activity
            }
        });
        return new PublicationHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PublicationHolder holder, int position) {
        Publication publication = publications.get(position);
        holder.namePublisher.setText(publication.getPublisher().getFirstName() + " " + publication.getPublisher().getLastName());
        holder.publicationContent.setText(publication.getContent());
//        holder.imagePublisher.setImageBitmap(BitmapFactory.decodeByteArray(publication.getPublisher().getProfilPicture().getBytes(), 0,
//                publication.getPublisher().getProfilPicture().getBytes().length));
//        holder.imagePublication.setImageBitmap(BitmapFactory.decodeByteArray(publication.getMedias().get(0).getBytes(), 0, publication.getMedias().get(0).getBytes().length));
    }

    @Override
    public int getItemCount() {
        return publications.size();
    }

    public boolean isUpdateEnable() {
        return updateEnable;
    }

    public void setUpdateEnable(boolean updateEnable) {
        this.updateEnable = updateEnable;
    }

    public class PublicationHolder extends ViewHolder {
        public ImageView imagePublication;
        public ImageView imagePublisher;
        public TextView  namePublisher;
        public TextView  publicationContent;
        private boolean isImageFitToScreen = false;

        public PublicationHolder(View itemView) {
            super(itemView);

            imagePublication = itemView.findViewById(R.id.publicationImageView);
            imagePublisher = itemView.findViewById(R.id.publisher_image_view);
            publicationContent = itemView.findViewById(R.id.publicationContentView);
            namePublisher = itemView.findViewById(R.id.publisherNameView);
        }
    }
}
