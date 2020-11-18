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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.installations.FirebaseInstallations;
import com.google.firebase.messaging.FirebaseMessaging;
import com.synergy.Otp.OtpActivity;
import com.synergy.Workspace.WorkspaceActivity;

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
    private final int STORAGE_PERMISSION_CODE = 1;
    private TextInputEditText editTextName;
    private EditText passwordEdit;
    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String TEXT1 = "text";
    public static final String PASSWORD1 = "password";
    private Button buttonLogin;
    private String nameString, passwordString, deviceToken;
    private static final String TAG = "Tag";
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_login);
        sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        editor = sharedPreferences.edit();
        FirebaseInstallations.getInstance().getToken(true);

        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if (task.isSuccessful()) {
                    deviceToken = task.getResult();
                }
            }
        });
        buttonLogin = findViewById(R.id.btn_login);
        editTextName = findViewById(R.id.editTextUsername);
        passwordEdit = findViewById(R.id.editTextPassword);
        //   deviceToken = getToken(this);


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
            //loadData();
        }
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mProgress = new ProgressDialog(MainActivityLogin.this);
                mProgress.setTitle("Authenticating...");
                mProgress.setCancelable(false);
                mProgress.setIndeterminate(true);
                mProgress.show();

                nameString = editTextName.getText().toString();
                passwordString = passwordEdit.getText().toString();
                UserRequest userRequest = new UserRequest(nameString, passwordString, deviceToken);
                loginUser(userRequest);
            }
        });
    }

    public void loadData() {

        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        nameString = sharedPreferences.getString(TEXT1, "");
        passwordString = sharedPreferences.getString(PASSWORD1, "");
        String userToken = sharedPreferences.getString("token", "");
        UserRequest request = new UserRequest(nameString, passwordString, deviceToken);
        if (!nameString.equals("")) {
            loginUser(request);
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

    public void loginUser(UserRequest userRequest) {

        Call<UserResponse> call = APIClient.getUserServices().saveUser(userRequest);

        call.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.code() == 200) {

                    String username= editTextName.getText().toString();
                    UserResponse userResponse = response.body();
                    String token = userResponse.getToken();
                    String role = userResponse.getRole();
                    String user = userResponse.getUser();
                   editor.putString("token", token);
                    editor.putString("role", role);
                    editor.putString("devicetoken", deviceToken);
                    editor.apply();
                  /*  if (token == null) {
                        Intent intent = new Intent(getApplicationContext(), OtpActivity.class);
                        intent.putExtra("username", username);
                        startActivity(intent);
                        finish();
                    } else {
                        Intent intent = new Intent(getApplicationContext(), WorkspaceActivity.class);
                        intent.putExtra("devicetoken", deviceToken);
                        startActivity(intent);
                        finish();

                    }*/
                    Intent intent = new Intent(getApplicationContext(), WorkspaceActivity.class);
                    intent.putExtra("devicetoken", deviceToken);
                    startActivity(intent);
                    finish();

                } else if (response.code() == 202) {
                    Toast.makeText(MainActivityLogin.this, "Please check the username and password", Toast.LENGTH_LONG).show();
                } else
                    Toast.makeText(MainActivityLogin.this, "Error: " + response.code(), Toast.LENGTH_LONG).show();

                mProgress.dismiss();
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                mProgress.dismiss();
                Toast.makeText(MainActivityLogin.this, "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }


}
