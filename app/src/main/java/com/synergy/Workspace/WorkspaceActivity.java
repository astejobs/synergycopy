package com.synergy.Workspace;

import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.synergy.APIClient;
import com.synergy.LogoutClass;
import com.synergy.MainActivityLogin;
import com.synergy.R;
import com.synergy.Services.MyFirebaseInstanceService;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.synergy.MainActivityLogin.SHARED_PREFS;
import static com.synergy.MainActivityLogin.getToken;

public class WorkspaceActivity extends AppCompatActivity {
    private static final String TAG = "Message";
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ProgressDialog progressDialog;
    private LinearLayout linearLayout;
    private String user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workspace);
        Toolbar toolbar = findViewById(R.id.toolbar_workspace);
        setSupportActionBar(toolbar);
        linearLayout = findViewById(R.id.workLinear);
        linearLayout.setVisibility(View.GONE);

        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "");
        Log.d(TAG, "onCreate: workspace mai token :" + token);
        user = sharedPreferences.getString("role", "");

        recyclerView = findViewById(R.id.recycler_view_workspace);
        progressDialog = new ProgressDialog(WorkspaceActivity.this);
        progressDialog.setTitle("Loading");
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);

        callForWorkspace(token);

    }

    private void callForWorkspace(String token) {
        linearLayout.setVisibility(View.VISIBLE);
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
/*                if (t.getMessage().substring(0, 5).equals("Faile")) {
                    new AlertDialog.Builder(WorkspaceActivity.this)
                            .setTitle("Failed to connect to internet.")
                            .setMessage("Please check the connection")
                            .setCancelable(false)
                            .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Intent intent = new Intent(Intent.ACTION_MAIN);
                                    intent.addCategory(Intent.CATEGORY_HOME);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                    finish();
                                    System.exit(0);
                                }
                            }).show();
                } else*/
                SharedPreferences preferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.clear();
                editor.apply();
                Intent intent = new Intent(getApplicationContext(), MainActivityLogin.class);
                startActivity(intent);
                finish();
                Toast.makeText(WorkspaceActivity.this, "Failed: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        MenuItem item = menu.findItem(R.id.admin).setTitle(user);
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