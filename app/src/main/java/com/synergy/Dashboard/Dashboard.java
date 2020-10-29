
package com.synergy.Dashboard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.synergy.EquipmentSearch.EquipmentSearchActivity;
import com.synergy.MainActivityLogin;
import com.synergy.R;
import com.synergy.FaultReport.FaultReportActivity;
import com.synergy.Search.Search;

import static com.synergy.MainActivityLogin.SHARED_PREFS;

public class Dashboard extends AppCompatActivity {
    private LinearLayout linearEquipmentSearch, linearSearch, linearTaskSearch, linearCreateReport;
    private String workspaceId;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Intent intent = getIntent();
        workspaceId = intent.getStringExtra("workspaceId");
        linearCreateReport = findViewById(R.id.linear_create_fault);
        linearEquipmentSearch = findViewById(R.id.linear_qr_create);
        linearSearch = findViewById(R.id.linear_search);
        toolbar = findViewById(R.id.toolbar_dashboard);
        setSupportActionBar(toolbar);
        linearTaskSearch = findViewById(R.id.linear_task_search);

        linearCreateReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentMethod(FaultReportActivity.class);
            }
        });
        linearSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentMethod(Search.class);
            }
        });

        linearEquipmentSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intentMethod(EquipmentSearchActivity.class);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        MenuItem item = (MenuItem) menu.findItem(R.id.admin).setTitle("Hello");
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

    private void intentMethod(Class cla) {

        Intent intent = new Intent(getApplicationContext(), cla);
        intent.putExtra("workspaceId", workspaceId);
        startActivity(intent);

    }
}