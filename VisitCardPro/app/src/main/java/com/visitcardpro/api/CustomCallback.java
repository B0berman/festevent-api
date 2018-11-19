package com.visitcardpro.api;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

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
