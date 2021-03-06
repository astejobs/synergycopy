
package com.synergy.Dashboard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.synergy.EquipmentSearch.EquipmentSearchActivity;
import com.synergy.LogoutClass;
import com.synergy.R;
import com.synergy.FaultReport.FaultReportActivity;
import com.synergy.Search.LocationManager_check;
import com.synergy.Search.Search;
import com.synergy.SearchTasks.SearchTaskActivity;
import com.synergy.Setting.SettingActivity;

import static com.synergy.MainActivityLogin.SHARED_PREFS;

public class Dashboard extends AppCompatActivity {
    private LinearLayout linearEquipmentFaultSearch, linearEquipmentTaskSearch,
            linearLayoutDashboard, linearSearchOnFrid, linearTaskSearch,
            linearCreateFaultReport, lineraSetting, layout_m1, layout_m2, layout_m3;
    private String workspaceId, role, username;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        SharedPreferences preferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        role = preferences.getString("role", "");
        username = preferences.getString("username", "");
        Intent intent = getIntent();
        workspaceId = intent.getStringExtra("workspaceId");
        LocationManager_check locationManagerCheck = new LocationManager_check(Dashboard.this);
        Location location = null;
        if (locationManagerCheck.isLocationServiceAvailable()) {

            if (locationManagerCheck.getProviderType() == 1)
                if (ActivityCompat.checkSelfPermission(Dashboard.this,
                        Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(Dashboard.this
                        , Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                    return;
                } else if (locationManagerCheck.getProviderType() == 2)

                    location = locationManagerCheck.locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            location = locationManagerCheck.locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            //  double lat = location.getLongitude();

        } else {
            locationManagerCheck.createLocationServiceError(Dashboard.this);
        }
        linearLayoutDashboard = findViewById(R.id.lay_dashboard);
        linearCreateFaultReport = findViewById(R.id.linear_create_fault);
        linearEquipmentFaultSearch = findViewById(R.id.linear_qr_create);
        linearEquipmentTaskSearch = findViewById(R.id.linear_qr_taskseacr_qr);
        linearSearchOnFrid = findViewById(R.id.linear_search);
        lineraSetting = findViewById(R.id.setting);
        linearTaskSearch = findViewById(R.id.linear_task_search);
        layout_m1 = findViewById(R.id.linear_lay_1_m);
        layout_m2 = findViewById(R.id.linear_lay_2_m);
        layout_m3 = findViewById(R.id.linear_lay_3_m);
        CardView cardViewCreate = findViewById(R.id.card_view_create);
        CardView cardViewSearch = findViewById(R.id.card_view_search);
        CardView cardViewEqFault = findViewById(R.id.card_view_eq_qr_fault);
        CardView cardViewEqTask = findViewById(R.id.id_card_view_eq_task);
        CardView cardViewTaskSearch = findViewById(R.id.card_view_tsaksearch);
        CardView cardViewSetting = findViewById(R.id.card_view_setting);


        toolbar = findViewById(R.id.toolbar_dashboard);
        setSupportActionBar(toolbar);

        if (role.equals("ManagingAgent") || role.equals("Technician")) {

            layout_m1.removeAllViews();
            layout_m2.removeAllViews();
            layout_m3.removeAllViews();
            linearLayoutDashboard.removeView(layout_m1);
            linearLayoutDashboard.removeView(layout_m2);
            linearLayoutDashboard.removeView(layout_m3);
            linearLayoutDashboard.removeView(cardViewCreate);
            linearLayoutDashboard.removeView(cardViewSearch);
            linearLayoutDashboard.removeView(cardViewEqFault);
            linearLayoutDashboard.removeView(cardViewSetting);
            linearLayoutDashboard.removeView(cardViewTaskSearch);
            linearLayoutDashboard.removeView(cardViewEqTask);

            LinearLayout a = new LinearLayout(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(16, 150, 16, 16);

            params.gravity = Gravity.CENTER_HORIZONTAL;
            a.setLayoutParams(params);
            a.setOrientation(LinearLayout.HORIZONTAL);

            a.addView(cardViewSearch);
            a.addView(cardViewEqFault);

            LinearLayout b = new LinearLayout(this);
            LinearLayout.LayoutParams paramss = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            paramss.setMargins(16, 200, 16, 16);

            paramss.gravity = Gravity.CENTER_HORIZONTAL;
            b.setLayoutParams(paramss);
            b.setWeightSum(2);
            b.setOrientation(LinearLayout.HORIZONTAL);
            b.addView(cardViewEqTask);
            b.addView(cardViewTaskSearch);

            linearLayoutDashboard.addView(a);
            linearLayoutDashboard.addView(b);
        }

        linearTaskSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentMethod(SearchTaskActivity.class, "");
            }
        });
        linearSearchOnFrid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentMethod(Search.class, "");
            }
        });
        linearEquipmentFaultSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intentMethod(EquipmentSearchActivity.class, "Fault");
            }
        });


        linearEquipmentTaskSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentMethod(EquipmentSearchActivity.class, "Task");
            }
        });


        lineraSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentMethod(SettingActivity.class, "");
            }
        });


        linearCreateFaultReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentMethod(FaultReportActivity.class, "");
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        //MenuItem item = (MenuItem) menu.findItem(R.id.admin).setTitle(username);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.logoutmenu) {
            LogoutClass logoutClass = new LogoutClass();
            logoutClass.logout(this);
        }
        return true;
    }

    private void intentMethod(Class cla, String value) {

        Intent intent = new Intent(getApplicationContext(), cla);
        intent.putExtra("workspaceId", workspaceId);
        intent.putExtra("value", value);
        startActivity(intent);

    }
}