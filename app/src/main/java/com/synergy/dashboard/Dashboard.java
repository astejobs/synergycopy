
package com.synergy.dashboard;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.synergy.EquipmentSearch.EquipmentSearchActivity;
import com.synergy.R;
import com.synergy.faultReport.FaultReportActivity;
import com.synergy.search.Search;

public class Dashboard extends AppCompatActivity {
    private LinearLayout linearLayoutEquipmentSearch, l_Search, l_Tasksearch, l_Createfault;
    private String workspaceid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        initViews();


    }

    private void initViews() {
        Intent intent = getIntent();
        workspaceid = intent.getStringExtra("workspaceId");
        l_Createfault = findViewById(R.id.linear_create_fault);
        linearLayoutEquipmentSearch = findViewById(R.id.linear_qr_create);
        l_Search = findViewById(R.id.linear_search);
        l_Tasksearch = findViewById(R.id.linear_task_search);
        l_Createfault.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Dashboard.this, FaultReportActivity.class);
                intent1.putExtra("workspaceId", workspaceid);
                startActivity(intent1);
            }
        });
        l_Search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Dashboard.this, Search.class);
                i.putExtra("workspaceId",workspaceid);
                startActivity(i);
            }
        });

        linearLayoutEquipmentSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(Dashboard.this, EquipmentSearchActivity.class);
                intent1.putExtra("workspaceId", workspaceid);
                startActivity(intent1);
            }
        });

    }
}