package com.synergy.EquipmentSearch;

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

public class EquipmentSearchActivity extends MyBaseActivity {

    private CodeScannerView codeScannerView;
    private TextView scanTextView;
    private ProgressDialog mProgress;
    private String workspace, role, token, username;

    private FusedLocationProviderClient client;
    private  double latitude,longitude;

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
        client = LocationServices.getFusedLocationProviderClient(EquipmentSearchActivity.this);
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

        mProgress = new ProgressDialog(EquipmentSearchActivity.this);
        mProgress.setTitle("Searching Equipment...");
        mProgress.setMessage("Please wait...");
        mProgress.setCancelable(false);
        mProgress.setIndeterminate(true);

        Intent intent = getIntent();
        workspace = intent.getStringExtra("workspaceId");
        String value = intent.getStringExtra("value");
        if (value.equals("Fault")) {
            toolbar.setTitle("Scan Fault Report");
        } else toolbar.setTitle("Scan Tasks");
        setSupportActionBar(toolbar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.CAMERA}, 1);
            }

            codeScannerView.setVisibility(View.VISIBLE);
            locationMethod();

            CodeScanner codeScanner = new CodeScanner(EquipmentSearchActivity.this, codeScannerView);
            codeScanner.startPreview();
            codeScanner.setDecodeCallback(new DecodeCallback() {
                @Override
                public void onDecoded(@NonNull Result result) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            codeScannerView.setVisibility(View.GONE);
                            scanTextView.setVisibility(View.VISIBLE);
                            scanTextView.setText(result.getText() + latitude +longitude);
                            if (value.equals("Fault")) {
                                Intent intent1 = new Intent(EquipmentSearchActivity.this,
                                        EditFaultReportActivity.class);
                                intent1.putExtra("equipcode", result.getText());
                                intent1.putExtra("workspaceId", workspace);
                                intent1.putExtra("lat",latitude);
                                intent1.putExtra("long",longitude);
                                startActivity(intent1);
                                finish();
                            } else {
                                //dialog();
                                callQrCodeSearch(String.valueOf(scanTextView.getText()), "Open");
                            }
                        }
                    });
                }
            });
        }
    }

    @SuppressLint("MissingPermission")
    private void locationMethod() {
            LocationManager locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        client.getCurrentLocation(PRIORITY_HIGH_ACCURACY,null).addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                Location location=task.getResult();
                if (location!=null){
                    longitude=location.getLongitude();
                    latitude=location.getLatitude();
                }
                Log.d("TAG", "onComplete: hey "+location.getLatitude());
                Log.d("TAG", "onComplete: hey"+location.getLongitude());
            }
        });
        }

    private void callQrCodeSearch(String result, String status) {
        mProgress.show();

        Call<List<EquipmentSearchResponse>> callEquipment = APIClient.getUserServices().getEquipmentTask(result, role, token, workspace);
        callEquipment.enqueue(new Callback<List<EquipmentSearchResponse>>() {
            @Override
            public void onResponse(Call<List<EquipmentSearchResponse>> call, Response<List<EquipmentSearchResponse>> response) {
                if (response.code() == 200) {

                    scanTextView.setVisibility(View.GONE);
                    List<EquipmentSearchResponse> equipmentSearchResponse = response.body();
                    if (equipmentSearchResponse.isEmpty()) {
                        Toast.makeText(EquipmentSearchActivity.this, "No tasks available!", Toast.LENGTH_SHORT).show();
                    }
                    RecyclerView recyclerView = findViewById(R.id.recycler_view_equip);

                    ArrayList<EquipmentSearchCard> equipmentSearchCardArrayList = new ArrayList<>();
                    String source = "scan";
                    for (int i = 0; i < equipmentSearchResponse.size(); i++) {
                        String taskNumber = equipmentSearchResponse.get(i).getTaskNumber();
                        int taskId = equipmentSearchResponse.get(i).getTaskId().intValue();
                        String buildingName = equipmentSearchResponse.get(i).getBuildingName();
                        String locationName = equipmentSearchResponse.get(i).getLocationName();
                        long scheduleDate = equipmentSearchResponse.get(i).getScheduleDate();
                        String status = equipmentSearchResponse.get(i).getStatus();
                        String afterImage = equipmentSearchResponse.get(i).getAfterImage();
                        String beforeImage = equipmentSearchResponse.get(i).getBeforeImage();
                        String equipName = equipmentSearchResponse.get(i).getEquipmentName();


                        equipmentSearchCardArrayList.add(new EquipmentSearchCard(taskId, taskNumber, workspace, status, buildingName,
                                locationName, scheduleDate, afterImage, beforeImage, source, equipName));
                    }
                    recyclerView.setHasFixedSize(true);
                    EquipmentSearchAdapter mAdapter = new EquipmentSearchAdapter(equipmentSearchCardArrayList);
                    recyclerView.setLayoutManager(new LinearLayoutManager(EquipmentSearchActivity.this));
                    recyclerView.setAdapter(mAdapter);
                } else if (response.code() == 401) {
                    Toast.makeText(EquipmentSearchActivity.this, Constants.ERROR_CODE_401_MESSAGE, Toast.LENGTH_SHORT).show();
                    LogoutClass logoutClass = new LogoutClass();
                    logoutClass.logout(EquipmentSearchActivity.this);
                } else if (response.code() == 500) {
                    Toast.makeText(EquipmentSearchActivity.this, Constants.ERROR_CODE_500_MESSAGE, Toast.LENGTH_SHORT).show();
                } else if (response.code() == 404) {
                    Toast.makeText(EquipmentSearchActivity.this, Constants.ERROR_CODE_404_MESSAGE, Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(EquipmentSearchActivity.this, "Error: " + response.code(), Toast.LENGTH_LONG).show();
                mProgress.dismiss();

            }

            @Override
            public void onFailure(Call<List<EquipmentSearchResponse>> call, Throwable t) {
                Toast.makeText(EquipmentSearchActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                mProgress.dismiss();
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    private void dialog() {
        AlertDialog alertDialog = new AlertDialog.Builder(EquipmentSearchActivity.this).create(); //Read Update
        LayoutInflater layoutInflater = this.getLayoutInflater();
        View radioLayoutView = layoutInflater.inflate(R.layout.custom_dialog_radio_layout_qr, null);
        RadioGroup radioGroup = radioLayoutView.findViewById(R.id.radio_grp_id);

        alertDialog.setView(radioLayoutView);
        alertDialog.setTitle("Select Type of Tasks");

        alertDialog.setButton(Dialog.BUTTON_POSITIVE, "Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int selectedId = radioGroup.getCheckedRadioButtonId();
                RadioButton radioSelectedButton = radioGroup.findViewById(selectedId);

                String status = radioSelectedButton.getText().toString();
                status = status.substring(0, status.length() - 6);
                //callQrCodeSearch(String.valueOf(scanTextView.getText()), status);
            }
        });
        alertDialog.setButton(Dialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }


}