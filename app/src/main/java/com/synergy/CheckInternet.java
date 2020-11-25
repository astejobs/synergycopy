package com.synergy;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class CheckInternet extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {

        View view = null;
        if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
            boolean noConnectivity = intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);
            if (noConnectivity) {
                Toast.makeText(context, "Disconnected!", Toast.LENGTH_SHORT).show();
                Snackbar snackbar = Snackbar.make(view.findViewById(R.id.layout_dummy), "Message is deleted", Snackbar.LENGTH_LONG)
                        .setAction("UNDO", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Toast.makeText(context,"Undo Clicked",Toast.LENGTH_SHORT).show();
                                    }
                                });

                snackbar.show();
            } else Toast.makeText(context, "Connected!", Toast.LENGTH_SHORT).show();
        }
    }

    private String isNetworkAvailable(Context context) {

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null) {
            return "Connected";
        } else return "Check your internet connection";
    }
}