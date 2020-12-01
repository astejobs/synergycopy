package com.synergy.Otp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.synergy.APIClient;
import com.synergy.LogoutClass;
import com.synergy.R;
import com.synergy.UserResponse;
import com.synergy.Workspace.WorkspaceActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.synergy.MainActivityLogin.SHARED_PREFS;

public class OtpActivity extends AppCompatActivity {
    private static final String TAG = "OtpActivity";
    EditText otp_textbox_one, otp_textbox_two, otp_textbox_three, otp_textbox_four;
    Button verify_otp, resendBtn;
    String username, token, deviceToken;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Toolbar toolbar;
    ProgressDialog progressDialog;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        toolbar = findViewById(R.id.toolbar_otp);
        setSupportActionBar(toolbar);
        progressDialog=new ProgressDialog(OtpActivity.this);
        progressDialog.setTitle("verifying...");
        sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        editor = sharedPreferences.edit();

        //username = sharedPreferences.getString("role", "");
        deviceToken = sharedPreferences.getString("devicetoken", "");
        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        //   resendBtn = findViewById(R.id.resendbtn);
        otp_textbox_one = findViewById(R.id.otp_edit_box1);
        otp_textbox_two = findViewById(R.id.otp_edit_box2);
        otp_textbox_three = findViewById(R.id.otp_edit_box3);
        otp_textbox_four = findViewById(R.id.otp_edit_box4);
        verify_otp = findViewById(R.id.verify_otp_btn);


        EditText[] edit = {otp_textbox_one, otp_textbox_two, otp_textbox_three, otp_textbox_four};

        otp_textbox_one.addTextChangedListener(new GenericTextWatcher(otp_textbox_one, edit));
        otp_textbox_two.addTextChangedListener(new GenericTextWatcher(otp_textbox_two, edit));
        otp_textbox_three.addTextChangedListener(new GenericTextWatcher(otp_textbox_three, edit));
        otp_textbox_four.addTextChangedListener(new GenericTextWatcher(otp_textbox_four, edit));

/*
        resendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
        callResendMethod();
            }
        });
*/
        verify_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String code = otp_textbox_one.getText().toString() + otp_textbox_two.getText().toString() +
                        otp_textbox_three.getText().toString() + otp_textbox_four.getText().toString();
                if (code.length() < 4) {
                    Toast.makeText(getApplicationContext(), "Please Enter Four Digit OTP", Toast.LENGTH_SHORT).show();

                } else {
                    OtpRequest otpRequest = new OtpRequest(username, code, deviceToken);
                    progressDialog.show();
                    Call<UserResponse> call = APIClient.getUserServices().callOtp(otpRequest);
                    call.enqueue(new Callback<UserResponse>() {
                        @Override
                        public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                            if (response.code() == 200) {
                                progressDialog.dismiss();
                                token = response.body().getToken();
                                editor.putString("token", token);
                                editor.apply();
                                Toast.makeText(getApplicationContext(), "Login Successfull", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(OtpActivity.this, WorkspaceActivity.class);
                                intent.putExtra("token", token);
                                startActivity(intent);
                                finish();
                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(OtpActivity.this, "Error :" + response.code(), Toast.LENGTH_SHORT).show();
                            }

                        }

                        @Override
                        public void onFailure(Call<UserResponse> call, Throwable t) {
                            progressDialog.dismiss();
                            Toast.makeText(OtpActivity.this, "Failed ", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "onFailure: " + t.getCause());
                            Log.d(TAG, "onFailure: " + t.getMessage());
                        }
                    });
                }
            }
        });


    }

    private void callResendMethod() {
        ResendOtpRequest resendOtpRequest = new ResendOtpRequest(username);
        Call<Void> call = APIClient.getUserServices().callResendOtp(resendOtpRequest);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.code() == 200) {
                    Toast.makeText(OtpActivity.this, "Code Has Been Send to Registered E-mail", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(OtpActivity.this, "Error :" + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
                Toast.makeText(OtpActivity.this, "Failed", Toast.LENGTH_SHORT).show();

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