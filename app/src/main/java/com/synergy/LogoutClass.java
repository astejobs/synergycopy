package com.synergy;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;
import static com.synergy.MainActivityLogin.SHARED_PREFS;

public class LogoutClass {
    public void logout(Activity activity) {
        ProgressDialog progressDialog = new ProgressDialog(activity);
        progressDialog.setTitle("Signing out!");
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.show();
        SharedPreferences sharedPreferences = activity.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "");
        Call<Void> callLogout = APIClient.getUserServices().logoutUser(token);
        callLogout.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.code() == 200) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.clear();
                    editor.apply();
                    Intent intent = new Intent(activity, MainActivityLogin.class);
                    activity.startActivity(intent);
                    activity.finishAffinity();
                } else
                    alertDialog("Error: " + response.code() + " Please try again", activity);
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                alertDialog(t.getMessage() + " Please try again", activity);
                progressDialog.dismiss();
            }
        });
    }

    public void alertDialog(String message, Activity activity) {
        new AlertDialog.Builder(activity)
                .setMessage(message)
                .setCancelable(false)
                .setIcon(R.drawable.ic_error)
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }
}
