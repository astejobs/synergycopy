package com.synergy.EquipmentSearch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.zxing.Result;
import com.synergy.APIClient;
import com.synergy.R;

public class EquipmentSearchActivity extends AppCompatActivity {

    private CodeScanner codeScanner;
    private CodeScannerView codeScannerView;
    private TextView scanTextView;
    private Button btn;
    private String token, workspace;
    private ProgressDialog mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equipment_search);

        Toolbar toolbar = findViewById(R.id.toolbar_qr);
        setSupportActionBar(toolbar);

        btn = findViewById(R.id.qr_btn_click);
        scanTextView = findViewById(R.id.scan_tv);
        codeScannerView = findViewById(R.id.qr_btn);

        mProgress = new ProgressDialog(EquipmentSearchActivity.this);
        mProgress.setTitle("Searching Equipment...");
        mProgress.setMessage("Please wait...");
        mProgress.setCancelable(false);
        mProgress.setIndeterminate(true);

        Intent intent = getIntent();
        workspace = intent.getStringExtra("workspaceId");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.CAMERA}, 1);
            }
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    btn.setVisibility(View.INVISIBLE);
                    codeScannerView.setVisibility(View.VISIBLE);
                    codeScanner = new CodeScanner(EquipmentSearchActivity.this, codeScannerView);
                    codeScanner.startPreview();
                    codeScanner.setDecodeCallback(new DecodeCallback() {
                        @Override
                        public void onDecoded(@NonNull Result result) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    codeScannerView.setVisibility(View.INVISIBLE);
                                    callQrCodeSearch(result.getText());
                                }
                            });
                        }
                    });
                }
            });
        }
    }

    private void callQrCodeSearch(String result) {
        mProgress.show();

        Log.d("TAG", "callQrCodeSearch: " + result);
        Call<EquipmentSearchResponse> callEquipment = APIClient.getUserServices().getCallEquipment(result);
        callEquipment.enqueue(new Callback<EquipmentSearchResponse>() {
            @Override
            public void onResponse(Call<EquipmentSearchResponse> call, Response<EquipmentSearchResponse> response) {
                Toast.makeText(EquipmentSearchActivity.this, "Success: " + response.code(), Toast.LENGTH_SHORT).show();

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

                } else
                    Toast.makeText(EquipmentSearchActivity.this, "Error: " + response.code(), Toast.LENGTH_LONG).show();
                mProgress.dismiss();
            }

            @Override
            public void onFailure(Call<EquipmentSearchResponse> call, Throwable t) {
                Toast.makeText(EquipmentSearchActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                mProgress.dismiss();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}