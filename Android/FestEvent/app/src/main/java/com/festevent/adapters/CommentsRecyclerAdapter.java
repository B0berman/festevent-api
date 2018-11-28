package com.festevent.adapters;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.festevent.R;
import com.festevent.api.Client;
import com.festevent.api.CustomCallback;
import com.festevent.beans.Comment;
import com.festevent.beans.Media;
import com.festevent.beans.Publication;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * Created by walbecq on 22/04/18.
 */

public class CommentsRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final Activity activity;
    private List<Comment> comments;
    private boolean updateEnable = true;
    private String  publicationId;

    public CommentsRecyclerAdapter(Activity context, List<Comment> list, String id) {
        activity = context;
        comments = list;
        publicationId = id;
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
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Comment comment = null;
        if (comments != null && !comments.isEmpty() && position >= 1)
            comment = comments.get(position - 1);
        Media picture = null;
        if ((comment != null && (picture = comment.getCommenter().getProfilPicture()) != null) ||
                (picture = Client.getInstance().getUser().getProfilPicture()) != null) {
            Call<ResponseBody> publisherPicCall = Client.getInstance().getUserService().getImage(picture.getId());
            publisherPicCall.enqueue(new CustomCallback<ResponseBody>(activity, 200) {
                @Override
                public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                    super.onResponse(call, response);
                    if (response.code() != 200 || response.body() == null) {

                    } else {
                        Bitmap bmp = BitmapFactory.decodeStream(response.body().byteStream());
                        if (holder.getItemViewType() == 1)
                            ((CommentHolder) holder).commenterImageView.setImageBitmap(bmp);
                        else
                            ((NewCommentHolder) holder).commenterImageView.setImageBitmap(bmp);
                    }
                }
            });
        }
        if (holder.getItemViewType() == 1 && comment != null) {
            ((CommentHolder) holder).commenterNameView.setText(comment.getCommenter().getFirstName() + " " + comment.getCommenter().getLastName());
            ((CommentHolder) holder).commentContentView.setText(comment.getContent());
            ((CommentHolder) holder).commentContentView.requestFocus();
        } else if (holder.getItemViewType() == 0 && comment == null) {
            ((NewCommentHolder) holder).sendButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String content = ((NewCommentHolder) holder).commentContent.getText().toString();
                    ((NewCommentHolder) holder).commentContent.setText("");
                    if (content != null && !content.isEmpty()) {
                        Comment comment = new Comment();
                        comment.setContent(content);
                        Call<Comment> commentPubliCall = Client.getInstance().getPublicationService().commentPublication(publicationId, comment);
                        commentPubliCall.enqueue(new CustomCallback<Comment>(activity, 200) {
                            @Override
                            public void onResponse(Call<Comment> call, retrofit2.Response<Comment> response) {
                                super.onResponse(call, response);
                                if (response.code() == 200) {
                                    Comment newComment = response.body();
                                    comments.add(newComment);
                                    updateContent(comments);
                                }
                            }
                        });
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (comments == null || comments.isEmpty())
            return 1;
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
        public AppCompatImageButton sendButton;
        public EditText commentContent;
        public ImageView commenterImageView;

        public NewCommentHolder(View itemView) {
            super(itemView);

            commenterImageView = itemView.findViewById(R.id.new_comment_image_view);
            commentContent = itemView.findViewById(R.id.new_comment_content_view);
            sendButton = itemView.findViewById(R.id.send_comment_button);
        }
    }
}
