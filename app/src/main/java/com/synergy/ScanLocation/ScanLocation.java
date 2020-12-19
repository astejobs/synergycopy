package com.synergy.ScanLocation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.zxing.Result;
import com.synergy.APIClient;
import com.synergy.CheckInternet;
import com.synergy.Constants;
import com.synergy.LogoutClass;
import com.synergy.MainActivityLogin;
import com.synergy.MyBaseActivity;
import com.synergy.R;
import com.synergy.Search.EditFaultReportActivity;
import com.synergy.Search.Search;
import com.synergy.Workspace.CardAdapter;
import com.synergy.Workspace.WorkspaceActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY;
import static com.synergy.MainActivityLogin.SHARED_PREFS;

public class ScanLocation extends MyBaseActivity {

    private CodeScannerView codeScannerView;
    private TextView scanTextView, longtxt, lattxt;
    private ProgressDialog mProgress;
    private String workspace, role, token, username;

    private FusedLocationProviderClient client;
    private double latitude, longitude;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        client = LocationServices.getFusedLocationProviderClient(ScanLocation.this);
        locationMethod();
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View viewLayout = layoutInflater.inflate(R.layout.activity_equipment_search, null, false);
        drawer.addView(viewLayout, 0);

        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        token = sharedPreferences.getString("token", "");
        role = sharedPreferences.getString("role", "");
        username = sharedPreferences.getString("username", "");

        scanTextView = findViewById(R.id.scan_tv);

        codeScannerView = findViewById(R.id.qr_btn);

        mProgress = new ProgressDialog(ScanLocation.this);
        mProgress.setTitle("Searching Equipment...");
        mProgress.setMessage("Please wait...");
        mProgress.setCancelable(false);
        mProgress.setIndeterminate(true);

        Intent intent = getIntent();
        workspace = intent.getStringExtra("workspaceId");
        String value = intent.getStringExtra("value");
        toolbar.setTitle("Scan Location");
        setSupportActionBar(toolbar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.CAMERA}, 1);
            }

            codeScannerView.setVisibility(View.VISIBLE);

            CodeScanner codeScanner = new CodeScanner(ScanLocation.this, codeScannerView);
            codeScanner.startPreview();
            codeScanner.setDecodeCallback(new DecodeCallback() {
                @Override
                public void onDecoded(@NonNull Result result) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            codeScannerView.setVisibility(View.GONE);
                            scanTextView.setVisibility(View.VISIBLE);
                            scanTextView.setText(result.getText() + "Latitude :" + latitude + "Longitude :" + longitude);

                        }
                    });
                }
            });
        }
    }

    @SuppressLint("MissingPermission")
    private void locationMethod() {
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

    @Override
    protected void onResume() {
        super.onResume();
    }
}