package com.synergy.Setting;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.synergy.APIClient;
import com.synergy.LogoutClass;
import com.synergy.MainActivityLogin;
import com.synergy.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.synergy.MainActivityLogin.SHARED_PREFS;

public class SettingActivity extends AppCompatActivity {

    String token, workspace, deviceGCM;
    SwitchMaterial notificationSwitch;
    private String TAG, user,role;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        toolbar = findViewById(R.id.toolbarearch);
        setSupportActionBar(toolbar);

        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        token = sharedPreferences.getString("token", "");
        role = sharedPreferences.getString("role", "Role");
        deviceGCM = sharedPreferences.getString("devicetoken", "");


        notificationSwitch = findViewById(R.id.notification_switch);
        Intent intent = getIntent();
        user = intent.getStringExtra("username");
        token = intent.getStringExtra("token");
        workspace = intent.getStringExtra("worklist");

        notificationSwitch.setClickable(false);
     //   deviceGCM = getToken(this);
        Log.d(TAG, "onCreate: bbbb"+deviceGCM);
        notificationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                int zeroOrOne;
                if (b) {
                    getNotifications(1);

                } else {
                    getNotifications(0);
                }
            }
        });

    }

    private void getNotifications(int zeroOrOne) {

        Call<Void> call = APIClient.getUserServices().getNotification(String.valueOf(zeroOrOne), deviceGCM, token, workspace);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.code() == 200) {
                    Toast.makeText(SettingActivity.this, "Notifications Status Changed", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(SettingActivity.this, "Error" + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(SettingActivity.this, "Failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    public static String getToken(Context context) {
        return context.getSharedPreferences("_", MODE_PRIVATE).getString("devicetoken", "empty");
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
            LogoutClass logoutClass = new LogoutClass();
            logoutClass.logout(this);
        }
        return true;
    }

}