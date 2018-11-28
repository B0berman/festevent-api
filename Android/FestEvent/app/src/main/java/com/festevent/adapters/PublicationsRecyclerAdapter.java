package com.festevent.adapters;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
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
import com.festevent.activities.EventActivity;
import com.festevent.activities.LoginActivity;
import com.festevent.activities.ProfileModifyActivity;
import com.festevent.activities.PublicateActivity;
import com.festevent.activities.UserActivity;
import com.festevent.api.Client;
import com.festevent.api.CustomCallback;
import com.festevent.beans.Comment;
import com.festevent.beans.Media;
import com.festevent.beans.Publication;
import com.festevent.beans.User;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * Created by walbecq on 22/04/18.
 */

public class PublicationsRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final Activity activity;
    private List<Publication> publications;
    private List<Integer>         likes = Lists.newArrayList();
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
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        if (holder.getItemViewType() == 1) {
            final Publication publication = publications.get(position - 1);
            if (publication.getPublisher() != null) {
                ((PublicationHolder) holder).namePublisher.setText(publication.getPublisher().getFirstName() + " " + publication.getPublisher().getLastName());
                ((PublicationHolder) holder).namePublisher.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!Client.getInstance().getUser().getEmail().equals(publication.getPublisher().getEmail())) {
                            Bundle b = new Bundle();
                            b.putSerializable("user", publication.getPublisher());
                            Intent intent = new Intent(activity, UserActivity.class);
                            intent.putExtras(b);
                            activity.startActivity(intent);
                        }
                    }
                });
                if (publication.getPublisher().getProfilPicture() != null) {
                    Call<ResponseBody> publisherPicCall = Client.getInstance().getUserService().getImage(publication.getPublisher().getProfilPicture().getId());
                    publisherPicCall.enqueue(new CustomCallback<ResponseBody>(activity, 200) {
                        @Override
                        public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                            super.onResponse(call, response);
                            if (response.code() != 200 || response.body() == null) {

                            } else {
                                Bitmap bmp = BitmapFactory.decodeStream(response.body().byteStream());
                                ((PublicationHolder) holder).imagePublisher.setImageBitmap(bmp);
                            }
                        }
                    });
                }
            } else if (publication.getEvent() != null) {
                ((PublicationHolder) holder).namePublisher.setText(publication.getEvent().getTitle());
                ((PublicationHolder) holder).namePublisher.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Bundle b = new Bundle();
                        b.putSerializable("event", publication.getEvent());
                        Intent intent = new Intent(activity, EventActivity.class);
                        intent.putExtras(b);
                        activity.startActivity(intent);
                    }
                });
                Call<ResponseBody> publisherPicCall = Client.getInstance().getUserService().getImage(publication.getEvent().getMainPicture().getId());
                publisherPicCall.enqueue(new CustomCallback<ResponseBody>(activity, 200) {
                    @Override
                    public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                        super.onResponse(call, response);
                        if (response.code() != 200 || response.body() == null) {

                        } else {
                            Bitmap bmp = BitmapFactory.decodeStream(response.body().byteStream());
                            ((PublicationHolder) holder).imagePublisher.setImageBitmap(bmp);
                        }
                    }
                });
            }
            ((PublicationHolder) holder).publicationContent.setText(publication.getContent());

            Call<List<User>> likesCall = Client.getInstance().getPublicationService().getPublicationLikes(publication.getId());
            likesCall.enqueue(new CustomCallback<List<User>>(activity, 200) {
                @Override
                public void onResponse(Call<List<User>> call, retrofit2.Response<List<User>> response) {
                    super.onResponse(call, response);
                    for (User like : response.body()) {
                        if (like.getEmail().equals(Client.getInstance().getUser().getEmail())) {
                            ((PublicationHolder) holder).likeButton.setImageResource(R.drawable.like);
                            likes.add(position);
                        }
                    }
                }
            });
            RecyclerView commentsView = ((PublicationHolder) holder).commentsView;
            final CommentsRecyclerAdapter pAdapter = new CommentsRecyclerAdapter(activity, publication.getComments(), publication.getId());
            RecyclerView.LayoutManager pLayoutManager = new LinearLayoutManager(activity);
            commentsView.setLayoutManager(pLayoutManager);
            commentsView.setItemAnimator(new DefaultItemAnimator());
            commentsView.setAdapter(pAdapter);


            ((PublicationHolder) holder).likeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {
                    Call<ResponseBody> likeCall;
                    if (likes.contains(position))
                        likeCall = Client.getInstance().getPublicationService().unlikePublication(publication.getId());
                    else
                        likeCall = Client.getInstance().getPublicationService().likePublication(publication.getId());
                    likeCall.enqueue(new CustomCallback<ResponseBody>(activity, 200) {
                        @Override
                        public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                            super.onResponse(call, response);
                            if (response.code() == 200) {
                                if (likes.contains(position)) {
                                    ((ImageView)view).setImageResource(R.drawable.unlike);
                                    likes.remove((Integer)position);
                                } else {
                                    likes.add(position);
                                    ((ImageView)view).setImageResource(R.drawable.like);
                                }
                            }
                        }
                    });
                }
            });
            ((PublicationHolder) holder).commentButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (((PublicationHolder) holder).commentsView.getVisibility() == View.GONE) {
                        Call<List<Comment>> commentCall = Client.getInstance().getPublicationService().getPublicationComments(publication.getId());
                        commentCall.enqueue(new CustomCallback<List<Comment>>(activity, 200) {
                            @Override
                            public void onResponse(Call<List<Comment>> call, retrofit2.Response<List<Comment>> response) {
                                super.onResponse(call, response);
                                if (response.code() == 200) {
                                    pAdapter.updateContent(response.body());
                                }
                            }
                        });
                        ((PublicationHolder) holder).commentsView.setVisibility(View.VISIBLE);
                        ((PublicationHolder) holder).commentsView.requestFocus();
                    } else {
                        ((PublicationHolder) holder).commentsView.setVisibility(View.GONE);
                    }
                }
            });
            if (publication.getMedias() != null && !publication.getMedias().isEmpty()) {
                Call<ResponseBody> ppCall = Client.getInstance().getUserService().getImage(publication.getMedias().get(0).getId());
                ppCall.enqueue(new CustomCallback<ResponseBody>(activity, 200) {
                    @Override
                    public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                        super.onResponse(call, response);
                        if (response.code() != 200 || response.body() == null) {

                        } else {
                            Bitmap bmp = BitmapFactory.decodeStream(response.body().byteStream());
                            ((PublicationHolder) holder).imagePublication.setImageBitmap(bmp);
                        }
                    }
                });
            }
        } else {
            ((PublicateHolder) holder).publicateCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(activity, PublicateActivity.class);
                    activity.startActivity(intent);
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
        public ImageView    commentButton;
        public ImageView    likeButton;
        public RecyclerView commentsView;

        public PublicationHolder(View itemView) {
            super(itemView);

            likeButton = itemView.findViewById(R.id.button_like);
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
