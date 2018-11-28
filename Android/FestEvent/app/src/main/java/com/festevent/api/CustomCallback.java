package com.festevent.api;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.festevent.beans.Media;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomCallback<T> implements Callback<T> {

    private Context mContext;
    private int     mSuccessCode;

    public CustomCallback(Context context, int code) {
        mContext = context;
        mSuccessCode = code;
    }

    public CustomCallback(Context context) {
        mContext = context;
        mSuccessCode = 200;
    }

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        if (!response.isSuccessful() || response.code() != mSuccessCode) {
            System.out.println("HEEEEEELOOOOOOO\n\n\n");
            try {
                System.out.println(new String(response.errorBody().bytes()));
            } catch (IOException e) {
                e.printStackTrace();
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setMessage(response.code() + " : " + response.message());
            builder.setCancelable(true);
            builder.setPositiveButton(
                    "Ok",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setMessage(t.getCause() + " - " + t.getMessage());
        builder.setCancelable(true);
        builder.setPositiveButton(
                "Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();

    }
}
