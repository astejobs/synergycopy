package com.synergy;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.synergy.EquipmentSearch.PmTaskActivity;
import com.synergy.Search.EditFaultReportActivity;
import com.synergy.Workspace.WorkspaceActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.synergy.MainActivityLogin.SHARED_PREFS;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);


        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "");

        Call<JsonArray> call = APIClient.getUserServices().getWorkspace(token);
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                if (response.code() == 200) {
                    Bundle bundle = new Bundle();
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
                                intent = new Intent(getApplicationContext(), EditFaultReportActivity.class);
                                intent.putExtra("equipcode", equipCode);
                                intent.putExtra("frId", id);
                                bundle.clear();
                            } else if (click_action.equals(Constants.PMTASK_ACTIVITY_NOTIFICATION)) {
                                intent = new Intent(getApplicationContext(), PmTaskActivity.class);
                                intent.putExtra("taskId", Integer.parseInt(id));
                                intent.putExtra("taskNumber", taskNumber);
                                intent.putExtra("afterImage", afterImage);
                                intent.putExtra("beforeImage", beforeImage);
                                intent.putExtra("source", source);
                                bundle.clear();
                            }
                            intent.putExtra("workspace", workspace);
                            startActivity(intent);
                            finish();
                        } else {
                            startActivity(new Intent(SplashScreenActivity.this, WorkspaceActivity.class));
                            finish();
                        }
                    } else {
                        startActivity(new Intent(SplashScreenActivity.this, WorkspaceActivity.class));
                        finish();
                    }
                } else if (response.code() == 401) {
                    startActivity(new Intent(SplashScreenActivity.this, MainActivityLogin.class));
                    finish();
                } else {
                    new AlertDialog.Builder(SplashScreenActivity.this)
                            .setMessage("Check your internet connection")
                            .setCancelable(false)
                            .setIcon(R.drawable.ic_error)
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    finish();
                                }
                            }).show();
                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                new AlertDialog.Builder(SplashScreenActivity.this)
                        .setMessage("Check your internet connection")
                        .setCancelable(false)
                        .setIcon(R.drawable.ic_error)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                finish();
                            }
                        }).show();
            }
        });
    }
}