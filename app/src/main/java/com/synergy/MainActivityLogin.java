package com.synergy;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivityLogin extends AppCompatActivity {

    private ProgressDialog mProgress;
    private int STORAGE_PERMISSION_CODE = 1;
    private TextInputEditText editTextName;
    private EditText passwordEdit;
    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String TEXT1 = "text";
    public static final String PASSWORD1 = "password";
    private Button buttonLogin;
    private String userTokenReceived, nameString, passwordString, userName;
    private ArrayList<String> workSpacelistReceived = new ArrayList<String>();
    private static final String TAG = "Tag";
    private static final String CHANNEL_ID = "channel Id";
    private static final String CHANNEL_NAME = "channel Name";
    private static final String CHANNEL_DESC = "channel Desc";
    private String deviceGCM;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_login);

        buttonLogin = findViewById(R.id.btn_login);
        editTextName = findViewById(R.id.editTextUsername);
        passwordEdit = findViewById(R.id.editTextPassword);

        deviceGCM = getToken(this);

        if (!(ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_NOTIFICATION_POLICY) == PackageManager.PERMISSION_GRANTED)) {
            requestStoragePermission();
            buttonLogin.setEnabled(false);
        } else {
            buttonLogin.setEnabled(true);
            loadData();
        }
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nameString = editTextName.getText().toString();
                passwordString = passwordEdit.getText().toString();

                Toast.makeText(MainActivityLogin.this, ""+nameString   + passwordString, Toast.LENGTH_SHORT).show();
                UserRequest userRequest = new UserRequest(nameString, passwordString, deviceGCM);
                saveUser(userRequest);
                saveData(userTokenReceived);
            }
        });
    }

    public void saveData(String userTokenReceived) {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(TEXT1, editTextName.getText().toString());
        editor.putString(PASSWORD1, passwordEdit.getText().toString());
        editor.putString("token", userTokenReceived);
        editor.apply();
    }

    public void loadData() {

        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        nameString = sharedPreferences.getString(TEXT1, "");
        passwordString = sharedPreferences.getString(PASSWORD1, "");
        String userToken = sharedPreferences.getString("token", "");
        UserRequest request = new UserRequest(nameString, passwordString, deviceGCM);
        if (!nameString.equals("")) {
            saveUser(request);
        }
    }

    private void requestStoragePermission() {

        new AlertDialog.Builder(this)
                .setTitle("Permission needed")
                .setMessage("This permission is needed for the application to run properly")
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions(MainActivityLogin.this,
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                                        Manifest.permission.ACCESS_FINE_LOCATION,
                                        Manifest.permission.CAMERA,
                                        Manifest.permission.ACCESS_NOTIFICATION_POLICY}, STORAGE_PERMISSION_CODE);
                    }
                })
                .create().show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED
                    && grantResults[2] == PackageManager.PERMISSION_GRANTED
                    && grantResults[3] == PackageManager.PERMISSION_GRANTED) {
                buttonLogin.setEnabled(true);
            } else {
                new AlertDialog.Builder(this)
                        .setTitle("Permission Denied")
                        .setMessage("Please enable the permissions in settings")
                        .setPositiveButton("ok", null)
                        .create().show();
            }
        }
    }

    public static String getToken(Context context) {
        return context.getSharedPreferences("_", MODE_PRIVATE).getString("fcm_token", "empty");
    }

    public void saveUser(UserRequest userRequest) {

/*
        mProgress = new ProgressDialog(MainActivityLogin.this);
        mProgress.setTitle("Authenticating...");
        mProgress.setCancelable(false);
        mProgress.setIndeterminate(true);
        mProgress.show();

        Call<UserResponse> call = APIClient.getUserServices().saveUser(userRequest);

        call.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                mProgress.dismiss();
                if (response.code() == 200) {
                  String workspace=response.body().getWorkspaceId();
                    Toast.makeText(MainActivityLogin.this, "hi : "+workspace, Toast.LENGTH_SHORT).show();
                } else if (response.code() == 202) {
                    Toast.makeText(MainActivityLogin.this, "Please check the username and password", Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(MainActivityLogin.this, "Error: " + response.code(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                mProgress.dismiss();
                Toast.makeText(MainActivityLogin.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
*/
    }
}
