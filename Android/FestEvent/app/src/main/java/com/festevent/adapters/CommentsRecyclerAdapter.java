package com.festevent.adapters;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.festevent.R;
import com.festevent.beans.Comment;
import com.festevent.beans.Media;
import com.festevent.beans.Publication;

import java.util.List;

/**
 * Created by walbecq on 22/04/18.
 */

public class CommentsRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final Activity activity;
    private List<Comment> comments;
    private boolean updateEnable = true;

    public CommentsRecyclerAdapter(Activity context, List<Comment> list) {
        activity = context;
        comments = list;
    }

    public List<Comment> getCurrentContent() {
        return comments;
    }

    public void updateContent(List<Comment> comments) {
        if (updateEnable) {
            this.comments = comments;
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
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_item, parent, false);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = (Integer) ((RecyclerView) view.getParent()).getChildLayoutPosition(view);

                    // start activity
                }
            });
            return new CommentHolder(itemView);
        } else {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.new_comment_item, parent, false);
            return new NewCommentHolder(itemView);
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (holder.getItemViewType() == 1) {
            Comment comment = comments.get(position - 1);
            ((CommentHolder) holder).commenterNameView.setText(comment.getCommenter().getFirstName() + " " + comment.getCommenter().getLastName());
            ((CommentHolder) holder).commentContentView.setText(comment.getContent());

//        holder.imagePublisher.setImageBitmap(BitmapFactory.decodeByteArray(publication.getPublisher().getProfilPicture().getBytes(), 0,
//                publication.getPublisher().getProfilPicture().getBytes().length));
//        holder.imagePublication.setImageBitmap(BitmapFactory.decodeByteArray(publication.getMedias().get(0).getBytes(), 0, publication.getMedias().get(0).getBytes().length));
        } else {
            ((NewCommentHolder) holder).commentCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    System.out.println("CARD CLICKED");
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return comments.size() + 1;
    }

    public boolean isUpdateEnable() {
        return updateEnable;
    }

    public void setUpdateEnable(boolean updateEnable) {
        this.updateEnable = updateEnable;
    }

    public class CommentHolder extends ViewHolder {
        public ImageView commenterImageView;
        public TextView  commenterNameView;
        public TextView  commentContentView;

        public CommentHolder(View itemView) {
            super(itemView);

            commenterImageView = itemView.findViewById(R.id.commenter_image_view);
            commenterNameView = itemView.findViewById(R.id.commenterNameView);
            commentContentView = itemView.findViewById(R.id.commentContentView);
        }
    }

    public class NewCommentHolder extends ViewHolder {
        public CardView commentCard;

        public NewCommentHolder(View itemView) {
            super(itemView);

            commentCard = itemView.findViewById(R.id.publicate_card);
        }
    }
}
