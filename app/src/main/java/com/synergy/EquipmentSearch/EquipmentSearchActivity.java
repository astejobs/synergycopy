package com.synergy.EquipmentSearch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
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
import com.google.zxing.Result;
import com.synergy.APIClient;
import com.synergy.MainActivityLogin;
import com.synergy.R;
import com.synergy.Search.EditFaultReportActivity;

import static com.synergy.MainActivityLogin.SHARED_PREFS;

public class EquipmentSearchActivity extends AppCompatActivity {

    private CodeScannerView codeScannerView;
    private TextView scanTextView;
    private ProgressDialog mProgress;
    private String workspace, role, token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equipment_search);

        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        token = sharedPreferences.getString("token", "");
        role = sharedPreferences.getString("role", "");

        scanTextView = findViewById(R.id.scan_tv);
        codeScannerView = findViewById(R.id.qr_btn);
        Toolbar toolbar = findViewById(R.id.toolbar_equipmentSearch);
        setSupportActionBar(toolbar);

        mProgress = new ProgressDialog(EquipmentSearchActivity.this);
        mProgress.setTitle("Searching Equipment...");
        mProgress.setMessage("Please wait...");
        mProgress.setCancelable(false);
        mProgress.setIndeterminate(true);

        Intent intent = getIntent();
        workspace = intent.getStringExtra("workspaceId");
        String value = intent.getStringExtra("value");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.CAMERA}, 1);
            }


            codeScannerView.setVisibility(View.VISIBLE);
            CodeScanner codeScanner = new CodeScanner(EquipmentSearchActivity.this, codeScannerView);
            codeScanner.startPreview();
            codeScanner.setDecodeCallback(new DecodeCallback() {
                @Override
                public void onDecoded(@NonNull Result result) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            codeScannerView.setVisibility(View.INVISIBLE);
                            scanTextView.setVisibility(View.VISIBLE);
                            scanTextView.setText(result.getText());
                            if (value.equals("Fault")) {
                                Intent intent1 = new Intent(EquipmentSearchActivity.this, EditFaultReportActivity.class);
                                intent1.putExtra("equipcode", result.getText());
                                intent1.putExtra("workspaceId", workspace);
                                startActivity(intent1);
                            } else {
                                dialog();
                            }
                        }
                    });
                }
            });

        }
    }

    private void callQrCodeSearch(String result, String status) {
        mProgress.show();

        Call<EquipmentSearchResponse> callEquipment = APIClient.getUserServices().getEquipmentTask(result, status, token, workspace);
        callEquipment.enqueue(new Callback<EquipmentSearchResponse>() {
            @Override
            public void onResponse(Call<EquipmentSearchResponse> call, Response<EquipmentSearchResponse> response) {
                if (response.code() == 200) {

                    scanTextView.setText(result);
                    EquipmentSearchResponse equipmentSearchResponse = response.body();

                    Intent intent = new Intent(getApplicationContext(), QrDetails.class);
                    String name = equipmentSearchResponse.name;
                    String eqCode = equipmentSearchResponse.equipmentCode;
                    String eqType = equipmentSearchResponse.equipmentType;
                    String buildingName = equipmentSearchResponse.building.name;
                    int buildingId = equipmentSearchResponse.building.id;
                    int locationId = equipmentSearchResponse.location.id;
                    String locationName = equipmentSearchResponse.location.name;
                    String assetNumber = equipmentSearchResponse.assetNo;

                    intent.putExtra("name", name);
                    intent.putExtra("code", eqCode);
                    intent.putExtra("type", eqType);
                    intent.putExtra("building", buildingName);
                    intent.putExtra("bId", buildingId);
                    intent.putExtra("locationId", locationId);
                    intent.putExtra("locationName", locationName);
                    intent.putExtra("asset", assetNumber);
                    startActivity(intent);
                    finish();

                } else
                    Toast.makeText(EquipmentSearchActivity.this, "Error: " + response.code(), Toast.LENGTH_LONG).show();
                mProgress.dismiss();
                finish();
            }

            @Override
            public void onFailure(Call<EquipmentSearchResponse> call, Throwable t) {
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        MenuItem item = (MenuItem) menu.findItem(R.id.admin).setTitle(role);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.logoutmenu) {
            SharedPreferences preferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.clear();
            editor.apply();
            Intent intent = new Intent(this, MainActivityLogin.class);
            startActivity(intent);
            finishAffinity();
        }
        return true;
    }

    private void dialog() {
        AlertDialog alertDialog = new AlertDialog.Builder(EquipmentSearchActivity.this).create(); //Read Update
        LayoutInflater layoutInflater = this.getLayoutInflater();
        View radioLayoutView = layoutInflater.inflate(R.layout.custom_dialog_radio_layout_qr, null);
        RadioGroup radioGroup = radioLayoutView.findViewById(R.id.radio_grp_id);

        alertDialog.setView(radioLayoutView);
        alertDialog.setTitle("Select Type of Tasks");

        alertDialog.setButton(Dialog.BUTTON_POSITIVE, "ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int selectedId = radioGroup.getCheckedRadioButtonId();
                RadioButton radioSelectedButton = radioGroup.findViewById(selectedId);

                String status = radioSelectedButton.getText().toString();
                status = status.substring(0, status.length() - 6);
                callQrCodeSearch(String.valueOf(scanTextView.getText()), status);
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