package com.synergy.Workspace;

import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.synergy.APIClient;
import com.synergy.CheckInternet;
import com.synergy.Constants;
import com.synergy.EquipmentSearch.PmTaskActivity;
import com.synergy.LogoutClass;
import com.synergy.MainActivityLogin;
import com.synergy.R;
import com.synergy.Search.EditFaultReportActivity;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.synergy.MainActivityLogin.SHARED_PREFS;

public class WorkspaceActivity extends AppCompatActivity {
    private static final String TAG = "Message";
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ProgressDialog progressDialog;
    private LinearLayout linearLayout;
    private String role,username;
    private final CheckInternet checkInternet = new CheckInternet();

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(checkInternet, intentFilter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(checkInternet);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workspace);
        Toolbar toolbar = findViewById(R.id.toolbar_workspace);
        setSupportActionBar(toolbar);
        linearLayout = findViewById(R.id.workLinear);

        /*Bundle bundle = new Bundle();
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

                if (click_action.equals(Constants.EDITFAULTREPORT_ACTIVITY_NOTIFICATION)) {
                    intent = new Intent(this, EditFaultReportActivity.class);
                    intent.putExtra("equipcode", equipCode);
                    intent.putExtra("frId", id);
                    bundle.clear();
                } else if (click_action.equals(Constants.PMTASK_ACTIVITY_NOTIFICATION)) {
                    intent = new Intent(this, PmTaskActivity.class);
                    intent.putExtra("taskId", Integer.parseInt(id));
                    intent.putExtra("taskNumber", taskNumber);
                    intent.putExtra("afterImage", afterImage);
                    intent.putExtra("beforeImage", beforeImage);
                    intent.putExtra("source", source);
                    bundle.clear();
                }
                intent.putExtra("workspace", workspace);
                startActivity(intent);
            }
        }
*/
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "");
        role = sharedPreferences.getString("role", "");
        username=sharedPreferences.getString("username","");

        recyclerView = findViewById(R.id.recycler_view_workspace);
        progressDialog = new ProgressDialog(WorkspaceActivity.this);
        progressDialog.setTitle("Loading");
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);

        callForWorkspace(token);
    }

    private void callForWorkspace(String token) {
        progressDialog.show();
        Call<JsonArray> call = APIClient.getUserServices().getWorkspace(token);
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                if (response.code() == 200) {
                    JsonArray jsonArray = response.body();
                    ArrayList<CardDetails> cardDetailsArrayList = new ArrayList<>();

                    for (int i = 0; i < jsonArray.size(); i++) {
                        JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
                        String workSpaceItemId = jsonObject.get("workspaceId").getAsString();
                        String buildingDescription = jsonObject.get("buildingDescription").getAsString();
                        cardDetailsArrayList.add(new CardDetails(workSpaceItemId, buildingDescription));
                    }
                    recyclerView.setHasFixedSize(true);
                    mLayoutManager = new LinearLayoutManager(WorkspaceActivity.this);
                    mAdapter = new CardAdapter(cardDetailsArrayList);
                    recyclerView.setLayoutManager(mLayoutManager);
                    recyclerView.setAdapter(mAdapter);
                    mAdapter.notifyDataSetChanged();
                } else {
                    SharedPreferences preferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.clear();
                    editor.apply();
                    Intent intent = new Intent(getApplicationContext(), MainActivityLogin.class);
                    startActivity(intent);
                    finish();
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                progressDialog.dismiss();
                SharedPreferences preferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.clear();
                editor.apply();
                Intent intent = new Intent(getApplicationContext(), MainActivityLogin.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        MenuItem item = menu.findItem(R.id.admin).setTitle(username);
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
}