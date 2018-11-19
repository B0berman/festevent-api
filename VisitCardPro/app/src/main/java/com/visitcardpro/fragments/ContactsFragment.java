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

import com.visitcardpro.R;
import com.visitcardpro.adapters.CardRecyclerAdapter;
import com.visitcardpro.api.Client;
import com.visitcardpro.api.CustomCallback;
import com.visitcardpro.beans.Card;
import com.visitcardpro.beans.Contact;
import com.visitcardpro.utils.JobHelper;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

public class ContactsFragment extends Fragment {

    public ContactsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final ArrayList<Card> cards = new ArrayList<Card>();
        final ArrayList<Contact> contacts = new ArrayList<Contact>();

        final FloatingActionButton fab = view.findViewById(R.id.add_contact_fab);
        final RecyclerView recyclerView = view.findViewById(R.id.contacts_recycler);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("Enter id");
                builder.setView(R.layout.new_contact_form);
                builder.setCancelable(true);
                builder.setPositiveButton(
                        "Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                EditText editId = ((AlertDialog) dialog).findViewById(R.id.newc_edit_id);

                                Call<ResponseBody> call = Client.getInstance().getContactService().createContact(editId.getText().toString());
                                call.enqueue(new CustomCallback<ResponseBody>(getContext()) {
                                    @Override
                                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                        if (response.code() == 201) {
                                            try {
                                                contacts.add((Contact) JobHelper.getObjectFromBytes(response.body().bytes()));
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            } catch (ClassNotFoundException e) {
                                                e.printStackTrace();
                                            }

                                            if (recyclerView.getAdapter() != null) {
                                                ((CardRecyclerAdapter)recyclerView.getAdapter()).updateContent(cards);
                                            }
                                        }
                                    }

                                });
                                dialog.cancel();
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


        Call<ArrayList<Contact>> call = Client.getInstance().getContactService().getContacts();
        call.enqueue(new CustomCallback<ArrayList<Contact>>(getContext()) {
            @Override
            public void onResponse(Call<ArrayList<Contact>> call, Response<ArrayList<Contact>> response) {
                if (response.code() == 200) {
                    contacts.addAll(response.body());

                    for (Contact c : contacts) {
                        cards.add(c.getCard());
                    }
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
        return inflater.inflate(R.layout.fragment_contacts, container, false);
    }

}
