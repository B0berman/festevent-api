package com.visitcardpro.fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.visitcardpro.R;
import com.visitcardpro.adapters.CardRecyclerAdapter;
import com.visitcardpro.api.Client;
import com.visitcardpro.api.CustomCallback;
import com.visitcardpro.beans.Card;
import com.visitcardpro.database.dao.CardDAO;

import java.util.ArrayList;
import java.util.Date;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by hugo on 18/04/18.
 */

public class CardsFragment extends Fragment {


    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final ArrayList<Card> cards = new ArrayList<Card>();
        final RecyclerView recyclerView = view.findViewById(R.id.cards_recycler);

        final FloatingActionButton fab = view.findViewById(R.id.add_card_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("New Card");
                builder.setView(R.layout.new_card_form);
                builder.setCancelable(true);
                builder.setPositiveButton(
                        "Create",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                EditText et_name = ((AlertDialog)dialog).findViewById(R.id.newc_edit_name);
                                EditText et_title = ((AlertDialog)dialog).findViewById(R.id.newc_edit_title);
                                EditText et_desc = ((AlertDialog)dialog).findViewById(R.id.newc_edit_desc);
                                EditText et_email = ((AlertDialog)dialog).findViewById(R.id.newc_edit_email);
                                EditText et_phone = ((AlertDialog)dialog).findViewById(R.id.newc_edit_phone);

                                View focusView = null;
                                boolean cancel = false;
                                Card card = new Card();

                                card.setUser(Client.getInstance().getUser());
                                if (et_name.getText().toString().isEmpty()) {
                                    focusView = et_name;
                                    et_name.setError("");
                                    cancel = true;
                                }

                                if (et_title.getText().toString().isEmpty()) {
                                    focusView = et_title;
                                    et_name.setError("");
                                    cancel = true;
                                }

                                if (cancel)
                                    focusView.requestFocus();
                                else {
                                    card.setName(et_name.getText().toString());
                                    card.setTitle(et_title.getText().toString());
                                    card.setEmail(et_email.getText().toString());
                                    card.setPhoneNumber(et_phone.getText().toString());
                                    card.setDescription(et_desc.getText().toString());
//                                    card.setLastUpdate(new Date());
                                    Call<Card> call = Client.getInstance().getCardService().createCard(card);
                                    call.enqueue(new CustomCallback<Card>(getActivity()) {
                                        @Override
                                        public void onResponse(Call<Card> call, Response<Card> response) {
                                            if (response.code() == 201) {
                                                CardDAO dao = new CardDAO(getActivity());

                                                dao.open();
                                                dao.add(response.body(), false);
                                                dao.close();

                                                cards.add(response.body());
                                                ((CardRecyclerAdapter) recyclerView.getAdapter()).updateContent(cards);
                                            }
                                        }
                                        });
                                    dialog.cancel();
                                }
                            }
                        });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();

            }
        });


        Call<ArrayList<Card>> call = Client.getInstance().getCardService().getCards();
        call.enqueue(new CustomCallback<ArrayList<Card>>(getContext()) {
            @Override
            public void onResponse(Call<ArrayList<Card>> call, Response<ArrayList<Card>> response) {
                if (response.code() == 200) {
                    cards.addAll(response.body());

                    if (recyclerView.getAdapter() != null) {
                        ((CardRecyclerAdapter)recyclerView.getAdapter()).updateContent(cards);
                    }
                }
            }
        });
        CardRecyclerAdapter mAdapter = new CardRecyclerAdapter(getActivity(), null);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        ((CardRecyclerAdapter) recyclerView.getAdapter()).updateContent(cards);


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_personnal_cards, container, false);
    }
}
