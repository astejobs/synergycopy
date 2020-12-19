package com.synergy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.JsonArray;
import com.synergy.EquipmentSearch.PmTaskActivity;
import com.synergy.Search.EditFaultOnSearchActivity;
import com.synergy.Search.EditFaultReportActivity;
import com.synergy.Workspace.WorkspaceActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY;
import static com.synergy.MainActivityLogin.SHARED_PREFS;

public class SplashScreenActivity extends AppCompatActivity {
    private FusedLocationProviderClient client;
    private double latitude, longitude;
    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        client = LocationServices.getFusedLocationProviderClient(SplashScreenActivity.this);
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        token = sharedPreferences.getString("token", "");
        checkForGps();

    }

    private void networkCall() {

        Call<JsonArray> call = APIClient.getUserServices().getWorkspace(token);
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                if (response.code() == 200) {
                    Bundle bundle = new Bundle();
                    bundle = getIntent().getExtras();

                    if (bundle != null) {
                        if (bundle.get("id") != null) {
                            String workspace = bundle.get("workspace").toString();
                            String click_action = bundle.get("click_action").toString();
                            String id = bundle.get("id").toString();
                            String equipCode = "";
                            String taskNumber = "";
                            String afterImage = "";
                            String beforeImage = "";
                            String source = "";
                            Intent intent = null;
                            startActivity(new Intent(SplashScreenActivity.this, WorkspaceActivity.class));
                            finish();

                            if (click_action.equals(Constants.EDITFAULTREPORT_ACTIVITY_NOTIFICATION)) {
                                intent = new Intent(getApplicationContext(), EditFaultOnSearchActivity.class);
                                intent.putExtra("frId", id);
                                intent.putExtra("latitude", latitude);
                                intent.putExtra("longitude", longitude);
                                intent.putExtra("workspace", workspace);

                                bundle.clear();
                            } else if (click_action.equals(Constants.PMTASK_ACTIVITY_NOTIFICATION)) {
                                intent = new Intent(getApplicationContext(), PmTaskActivity.class);
                                intent.putExtra("taskId", Integer.parseInt(id));
                                intent.putExtra("taskNumber", taskNumber);
                                intent.putExtra("afterImage", afterImage);
                                intent.putExtra("beforeImage", beforeImage);
                                intent.putExtra("source", source);
                                bundle.clear();
                            }
                            intent.putExtra("workspace", workspace);
                            startActivity(intent);
                            finish();

                        } else {
                            startActivity(new Intent(SplashScreenActivity.this, WorkspaceActivity.class));
                            finish();
                        }
                    } else {
                        startActivity(new Intent(SplashScreenActivity.this, WorkspaceActivity.class));
                        finish();
                    }
                } else if (response.code() == 401) {
                    startActivity(new Intent(SplashScreenActivity.this, MainActivityLogin.class));
                    finish();
                } else {
                    new AlertDialog.Builder(SplashScreenActivity.this)
                            .setMessage("Check your internet connection")
                            .setCancelable(false)
                            .setIcon(R.drawable.ic_error)
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    finish();
                                }
                            }).show();
                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                new AlertDialog.Builder(SplashScreenActivity.this)
                        .setMessage("Check your internet connection")
                        .setCancelable(false)
                        .setIcon(R.drawable.ic_error)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                finish();
                            }
                        }).show();
            }
        });
    }


    @SuppressLint("MissingPermission")
    private void locationMethodPriority() {
        LocationManager locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);

        client.getCurrentLocation(PRIORITY_HIGH_ACCURACY, null).addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {

                Location location = task.getResult();
                if (location != null) {
                    longitude = location.getLongitude();
                    latitude = location.getLatitude();

                }
            }
        });
    }

    private void checkForGps() {
        final LocationManager manager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);

        if (ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 108);
            }

        } else {

            locationMethodPriority();
            networkCall();
        }



     /*   if (!(manager.isProviderEnabled(LocationManager.GPS_PROVIDER))) {
            showSettingsAlert();

        } else
            locationMethodPriority();*/


    }

    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(SplashScreenActivity.this);
        alertDialog.setTitle("GPS  settings");
        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");
        alertDialog.setPositiveButton("Settings",
                (dialog, which) -> {
                    Intent intent = new Intent(
                            Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                    locationMethodPriority();
                    networkCall();
                });
        alertDialog.setNegativeButton("Cancel",
                (dialog, which) -> dialog.cancel());
        alertDialog.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 108) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                locationMethodPriority();
                networkCall();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

}
