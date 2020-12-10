package com.synergy.Search;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.synergy.APIClient;
import com.synergy.Dashboard.Dashboard;
import com.synergy.LogoutClass;
import com.synergy.MyBaseActivity;
import com.synergy.R;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Intent.ACTION_GET_CONTENT;
import static com.synergy.MainActivityLogin.SHARED_PREFS;

public class UploadFile extends MyBaseActivity {
    TextView uploadFileTv;
    Button uplaodButton;
    Intent uploadFileIntent;
    String frid, token, workspace, role;
    ProgressDialog progressDialog;
    private String user;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View viewLayout = layoutInflater.inflate(R.layout.activity_upload_file, null, false);
        drawer.addView(viewLayout, 0);

        toolbar.setTitle("Upload File");
        setSupportActionBar(toolbar);
        uploadFileTv = findViewById(R.id.upload_file);
        uplaodButton = findViewById(R.id.upload_file_btn);
        uplaodButton.setEnabled(false);
        progressDialog = new ProgressDialog(UploadFile.this);
        progressDialog.setTitle("Uploading File...");
        progressDialog.setCancelable(false);
        Intent intent = getIntent();
        frid = intent.getStringExtra("frid");
        token = intent.getStringExtra("token");
        workspace = intent.getStringExtra("workspace");
        user = intent.getStringExtra("role");
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        role = sharedPreferences.getString("role", "");

        role = "Technician";
        uploadFileTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                uploadFileIntent = new Intent(ACTION_GET_CONTENT);
                uploadFileIntent.setType("application/pdf");

                startActivityForResult(Intent.createChooser(uploadFileIntent, "Select file"), 10);
            }
        });


    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10 && resultCode == RESULT_OK && data != null) {
            assert data != null;
            uplaodButton.setEnabled(true);
            uploadFileTv.setText(data.getData().toString());
            Uri uri = data.getData();

            try {
                InputStream inputStream = this.getContentResolver().openInputStream(uri);
                byte[] pdfInBytes = new byte[inputStream.available()];
                inputStream.read(pdfInBytes);
                String path = android.util.Base64.encodeToString(pdfInBytes, android.util.Base64.DEFAULT);
                uplaodButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        uploadFileMethod(path, frid);
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

  /*  @RequiresApi(api = Build.VERSION_CODES.O)
    private void callUpload(String uri) throws IOException {
       *//* File pdfFile = new File(uri);
        byte[] encoded = Files.readAllBytes(Paths.get(pdfFile.getAbsolutePath()));*//*

        byte[] input_file = Files.readAllBytes(Paths.get(uri));
        byte[] encodedBytes = Base64.getEncoder().encode(input_file);
        String encodedString = new String(encodedBytes);


    }*/

    private void uploadFileMethod(String encodedString, String frid) {


        UploadFileRequest uploadFileRequest = new UploadFileRequest(encodedString, frid);
        progressDialog.show();
        Call<ResponseBody> call = APIClient.getUserServices().uploadFilePdf(uploadFileRequest, token, workspace, role);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (response.code() == 200) {
                    progressDialog.dismiss();
                    Toast.makeText(UploadFile.this, "successfully uploaded", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(UploadFile.this, Dashboard.class);
                    intent.putExtra("workspaceId", workspace);
                    startActivity(intent);
                    finish();
                    Toast.makeText(UploadFile.this, "Successfully Uploaded File", Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(UploadFile.this, "Error: " + response.code(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(UploadFile.this, "Fialed : " + t.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });

    }
}