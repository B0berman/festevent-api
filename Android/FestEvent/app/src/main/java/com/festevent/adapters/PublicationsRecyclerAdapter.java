package com.festevent.adapters;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beust.jcommander.internal.Lists;
import com.festevent.R;
import com.festevent.api.Client;
import com.festevent.beans.Comment;
import com.festevent.beans.Media;
import com.festevent.beans.Publication;

import java.util.List;

/**
 * Created by walbecq on 22/04/18.
 */

public class PublicationsRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

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
    public int getItemViewType(int position) {
        // Just as an example, return 0 or 2 depending on position
        // Note that unlike in ListView adapters, types don't have to be contiguous
        if (position == 0)
            return 0;
        return 1;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View itemView;
        if (viewType == 1) {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.publication_item, parent, false);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = (Integer) ((RecyclerView) view.getParent()).getChildLayoutPosition(view);

                    // start activity
                }
            });
            return new PublicationHolder(itemView);
        } else {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.publicate_item, parent, false);
            return new PublicateHolder(itemView);
        }
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        if (holder.getItemViewType() == 1) {
            Publication publication = publications.get(position - 1);
            ((PublicationHolder) holder).namePublisher.setText(publication.getPublisher().getFirstName() + " " + publication.getPublisher().getLastName());
            ((PublicationHolder) holder).publicationContent.setText(publication.getContent());

            Comment c = new Comment();
            c.setCommenter(Client.getInstance().getUser());
            c.setContent("Bonjour je suis un commentaire");
            List<Comment> comments = Lists.newArrayList();
            comments.add(c);
            comments.add(c);
            comments.add(c);
            comments.add(c);
            comments.add(c);

            RecyclerView commentsView = ((PublicationHolder) holder).commentsView;
            CommentsRecyclerAdapter pAdapter = new CommentsRecyclerAdapter(activity, comments);
            RecyclerView.LayoutManager pLayoutManager = new LinearLayoutManager(activity);
            commentsView.setLayoutManager(pLayoutManager);
            commentsView.setItemAnimator(new DefaultItemAnimator());
            commentsView.setAdapter(pAdapter);

            ((PublicationHolder) holder).commentButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (((PublicationHolder) holder).commentsView.getVisibility() == View.GONE) {
                        ((PublicationHolder) holder).commentsView.setVisibility(View.VISIBLE);
                    } else {
                        ((PublicationHolder) holder).commentsView.setVisibility(View.GONE);
                    }
                }
            });
//        holder.imagePublisher.setImageBitmap(BitmapFactory.decodeByteArray(publication.getPublisher().getProfilPicture().getBytes(), 0,
//                publication.getPublisher().getProfilPicture().getBytes().length));
//        holder.imagePublication.setImageBitmap(BitmapFactory.decodeByteArray(publication.getMedias().get(0).getBytes(), 0, publication.getMedias().get(0).getBytes().length));
        } else {
            ((PublicateHolder) holder).publicateCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    System.out.println("CARD CLICKED");
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return publications.size() + 1;
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
        public Button    commentButton;
        public RecyclerView commentsView;

        public PublicationHolder(View itemView) {
            super(itemView);

            commentsView = itemView.findViewById(R.id.comment_recycler_view);
            imagePublication = itemView.findViewById(R.id.publicationImageView);
            imagePublisher = itemView.findViewById(R.id.publisher_image_view);
            publicationContent = itemView.findViewById(R.id.publicationContentView);
            namePublisher = itemView.findViewById(R.id.publisherNameView);
            commentButton = itemView.findViewById(R.id.comment_button);
        }
    }

    public class PublicateHolder extends ViewHolder {
        public CardView publicateCard;

        public PublicateHolder(View itemView) {
            super(itemView);

            publicateCard = itemView.findViewById(R.id.publicate_card);
        }
    }
}
