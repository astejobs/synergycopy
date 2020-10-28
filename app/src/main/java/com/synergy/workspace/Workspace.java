package com.synergy.workspace;

import androidx.appcompat.app.AppCompatActivity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.synergy.APIClient;
import com.synergy.R;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.synergy.MainActivityLogin.SHARED_PREFS;

public class Workspace extends AppCompatActivity {
    private static final String TAG = "Message";
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workspace);
        recyclerView = findViewById(R.id.recycler_view_workspace);
        progressDialog = new ProgressDialog(Workspace.this);
        progressDialog.setTitle("Loading");


        callForWorkspace();

    }

    private void callForWorkspace() {
        progressDialog.show();
        Call<JsonArray> call = APIClient.getUserServices().getWorkspace();
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                if (response.code() == 200) {
                    progressDialog.dismiss();
                    JsonArray jsonArray = response.body();
                    ArrayList<CardDetails> cardDetailsArrayList = new ArrayList<>();

                    for (int i = 0; i < jsonArray.size(); i++) {
                        JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
                        String workspacetItemId = jsonObject.get("workspaceId").getAsString();
                        String buildingDescription = jsonObject.get("buildingDescription").getAsString();
                        cardDetailsArrayList.add(new CardDetails(workspacetItemId, buildingDescription));
                    }
                    recyclerView.setHasFixedSize(true);
                    mLayoutManager = new LinearLayoutManager(Workspace.this);
                    mAdapter = new CardAdapter(cardDetailsArrayList);
                    recyclerView.setLayoutManager(mLayoutManager);
                    recyclerView.setAdapter(mAdapter);
                    mAdapter.notifyDataSetChanged();
                } else progressDialog.dismiss();
                Log.d(TAG, "onResponse: "+response.message());
                Log.d(TAG, "onResponse: "+response.code());
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(Workspace.this, "Failed : " + t.getCause(), Toast.LENGTH_SHORT).show();
                Toast.makeText(Workspace.this, "Failed : " + t.getMessage(), Toast.LENGTH_LONG).show();
                Log.d(TAG, "onFailure:  " + t.getCause());
                Log.d(TAG, "onFailure: " + t.getMessage());
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
            finishAffinity();
        }
        return true;
    }
}