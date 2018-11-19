package com.visitcardpro.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.visitcardpro.R;
import com.visitcardpro.activities.LoginActivity;
import com.visitcardpro.api.Client;
import com.visitcardpro.api.CustomCallback;
import com.visitcardpro.database.dao.AuthenticationDAO;
import com.visitcardpro.database.dao.CardDAO;
import com.visitcardpro.database.dao.UserDAO;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

public class UserFragment extends Fragment {
    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button signoutButton = view.findViewById(R.id.signout_button);
        signoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Call<ResponseBody> call = Client.getInstance().getAuthenticationService().signOut();
                call.enqueue(new CustomCallback<ResponseBody>(getContext(), 200) {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        super.onResponse(call, response);
                        UserDAO userDAO = new UserDAO(getContext());
                        userDAO.open();
                        userDAO.delete();
                        userDAO.close();
                        AuthenticationDAO authenticationDAO = new AuthenticationDAO(getContext());
                        authenticationDAO.open();
                        authenticationDAO.delete();
                        authenticationDAO.close();
                        CardDAO cardDAO = new CardDAO(getContext());
                        cardDAO.open();
                        cardDAO.clear();
                        cardDAO.close();
                        Intent launchNextActivity = new Intent(getActivity(), LoginActivity.class);
                        launchNextActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        launchNextActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        launchNextActivity.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(launchNextActivity);
                    }
                });
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user, container, false);
    }

}
