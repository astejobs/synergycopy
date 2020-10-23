package com.synergy.EquipmentSearch;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.synergy.R;

import java.util.ArrayList;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class QrDetails extends AppCompatActivity {

    private static final String TAG = "";
    private TextView codeTV, typeTV, nameTV, buildingTV, locationTV, assetTV;
    private Button searchFaultButton, searchTaskButton;
    private ProgressDialog mProgress;
    private ArrayList<String> frIdList = new ArrayList();
    private String token, workspace;
    private ListView listView, listView_qr;
    private String code;
    private ArrayList<String> tno_list = new ArrayList<>();
    private ArrayAdapter<String> taskListAdapter;
    private ArrayAdapter<String> frIdAdapter;
    private String user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_details);

        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String code = intent.getStringExtra("code");
        String type = intent.getStringExtra("type");
        int buildingId = intent.getIntExtra("bId", 0);
        int locationId = intent.getIntExtra("locationId", 0);
        String locationName = intent.getStringExtra("locationName");
        String buildingName = intent.getStringExtra("building");
        String asset = intent.getStringExtra("asset");

        codeTV = findViewById(R.id.eq_code);
        typeTV = findViewById(R.id.eq_type);
        nameTV = findViewById(R.id.eq_name);
        buildingTV = findViewById(R.id.eq_building);
        locationTV = findViewById(R.id.eq_location);
        assetTV = findViewById(R.id.eq_assetn0);

        codeTV.setText("Equipment code: " + code);
        typeTV.setText("Equipment type: " + type);
        nameTV.setText("Equipment name: " + name);
        buildingTV.setText("Equipment building: " + buildingName);
        locationTV.setText("Equipment location: " + locationName);
        assetTV.setText("Asset No: " + asset);

        listView = findViewById(R.id.list_view_equip);
        searchFaultButton = findViewById(R.id.first_btn);
        searchTaskButton = findViewById(R.id.second_btn);
        Toolbar toolbar = findViewById(R.id.toolbar_QRDetails);
        setSupportActionBar(toolbar);

        listView.setAdapter(null);

        searchFaultButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadFaultReportEquip();
            }
        });

        searchTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog();

            }
        });
    }

    private void loadFaultReportEquip() {
        mProgress.setTitle("Loading FaultReports...");
    }

    private void dialog() {
        AlertDialog alertDialog = new AlertDialog.Builder(QrDetails.this).create(); //Read Update
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

                loadTaskOnEq(status);

            }
        });
        alertDialog.setButton(Dialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }

    private void loadTaskOnEq(String status) {
        mProgress.setTitle("Loading Tasks..");
    }

}
