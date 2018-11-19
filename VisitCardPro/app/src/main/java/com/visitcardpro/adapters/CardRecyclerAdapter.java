package com.visitcardpro.adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.visitcardpro.R;
import com.visitcardpro.beans.Card;

import java.util.ArrayList;

/**
 * Created by walbecq on 22/04/18.
 */

public class CardRecyclerAdapter extends RecyclerView.Adapter<CardRecyclerAdapter.CardHolder> {

    private final Activity activity;
    private ArrayList<Card> cards;
    private boolean updateEnable = true;

    public CardRecyclerAdapter(Activity context, ArrayList<Card> list) {
        activity = context;
        cards = list;
    }

    public ArrayList<Card> getCurrentContent() {
        return cards;
    }
    public void updateContent(ArrayList<Card> cards) {
        if (updateEnable) {
            this.cards = cards;
            notifyDataSetChanged();
        }
    }

    @Override
    public CardHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View itemView;
        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item, parent, false);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = (Integer) ((RecyclerView) view.getParent()).getChildLayoutPosition(view);

                // start activity
            }
        });
        return new CardHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CardHolder holder, int position) {
        Card card = cards.get(position);

        holder.title.setText(card.getTitle());
    }

    @Override
    public int getItemCount() {
        return cards.size();
    }

    public boolean isUpdateEnable() {
        return updateEnable;
    }

    public void setUpdateEnable(boolean updateEnable) {
        this.updateEnable = updateEnable;
    }

public class CardHolder extends ViewHolder {
        public TextView title;

        public CardHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.card_item_title);
        }
    }
}
