package com.synergy.UploadPdf;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.synergy.APIClient;
import com.synergy.Dashboard.Dashboard;
import com.synergy.MyBaseActivity;
import com.synergy.R;
import com.synergy.Search.UploadFileRequest;

import java.io.IOException;
import java.io.InputStream;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Intent.ACTION_GET_CONTENT;
import static com.synergy.MainActivityLogin.SHARED_PREFS;

public class UploadPdf extends MyBaseActivity {
    private FloatingActionButton browsePdfBtn, uploadPdfBtn;
    Intent uploadFileIntent;
    private PDFView pdfView;
    String frid, token, workspace, role;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View viewLayout = layoutInflater.inflate(R.layout.activity_upload_pdf, null, false);
        drawer.addView(viewLayout, 0);

       // setContentView(R.layout.activity_upload_pdf);
        browsePdfBtn = findViewById(R.id.getpdf);
        uploadPdfBtn = findViewById(R.id.uploadpdf);
        pdfView = findViewById(R.id.pdfView);
        toolbar.setTitle("Upload PDF");
        setSupportActionBar(toolbar);

        uploadPdfBtn.setEnabled(false);
        progressDialog = new ProgressDialog(UploadPdf.this);
        progressDialog.setTitle("Uploading File...");
        progressDialog.setCancelable(false);



        Intent intent = getIntent();
        frid = intent.getStringExtra("frid");
        token = intent.getStringExtra("token");
        workspace = intent.getStringExtra("workspace");
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        role = sharedPreferences.getString("role", "");

        browsePdfBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPdfFromPhone();
            }
        });
    }

    private void getPdfFromPhone() {
        uploadFileIntent = new Intent(ACTION_GET_CONTENT);
        uploadFileIntent.setType("application/pdf");

        startActivityForResult(Intent.createChooser(uploadFileIntent, "Select file"), 10);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10 && resultCode == RESULT_OK && data != null) {
            assert data != null;
            uploadPdfBtn.setEnabled(true);
            // uploadFileTv.setText(data.getData().toString());
            Uri uri = data.getData();

            try {


                InputStream inputStream = this.getContentResolver().openInputStream(uri);
                byte[] pdfInBytes = new byte[inputStream.available()];

                pdfView.recycle();
                pdfView.fromBytes(pdfInBytes).load();

                inputStream.read(pdfInBytes);
                String path = android.util.Base64.encodeToString(pdfInBytes, android.util.Base64.DEFAULT);
                uploadPdfBtn.setOnClickListener(new View.OnClickListener() {
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


    private void uploadFileMethod(String encodedString, String frid) {
        progressDialog.show();

        UploadFileRequest uploadFileRequest = new UploadFileRequest(encodedString, frid);

        Call<ResponseBody> call = APIClient.getUserServices().uploadFilePdf(uploadFileRequest, token, workspace, role);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (response.code() == 200) {
                    progressDialog.dismiss();
                    Toast.makeText(UploadPdf.this, "successfully uploaded", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(UploadPdf.this, Dashboard.class);
                    intent.putExtra("workspaceId", workspace);
                    startActivity(intent);
                    finish();
                    Toast.makeText(UploadPdf.this, "Successfully Uploaded File", Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(UploadPdf.this, "Error: " + response.code(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(UploadPdf.this, "Fialed : " + t.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}


