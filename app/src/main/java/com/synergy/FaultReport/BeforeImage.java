package com.synergy.FaultReport;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.synergy.APIClient;
import com.synergy.MainActivityLogin;
import com.synergy.R;
import com.synergy.Dashboard.Dashboard;

import java.io.ByteArrayOutputStream;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.synergy.MainActivityLogin.SHARED_PREFS;

public class BeforeImage extends AppCompatActivity {

    Button takeBtn, uploadBtn, doneBtn;
    ImageView beforeImgPre;
    static final int REQUEST_IMAGE_CAPTURE = 0;
    private Intent takePictureIntent;
    private String token;
    private String workspace;
    private String frId;
    private ProgressDialog progressDialog;
    private String value;
    private Toolbar toolbar;
    private String user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_before_image);

        Intent receivedFromFaultReport = getIntent();
        token = receivedFromFaultReport.getStringExtra("token");
        workspace = receivedFromFaultReport.getStringExtra("workspace");
        frId = receivedFromFaultReport.getStringExtra("frId");
        value = receivedFromFaultReport.getStringExtra("value");
        user = receivedFromFaultReport.getStringExtra("user");

        takeBtn = findViewById(R.id.take_photo_btn);
        uploadBtn = findViewById(R.id.upload_btn);
        uploadBtn.setEnabled(false);
        toolbar = findViewById(R.id.toolbar_globe);
        doneBtn = findViewById(R.id.done_btn);
        beforeImgPre = findViewById(R.id.before_image_preview);
        progressDialog = new ProgressDialog(BeforeImage.this);

        toolbar.setTitle(value + " Image");
        setSupportActionBar(toolbar);

        takeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
            }
        });
        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BeforeImage.this, Dashboard.class);
                intent.putExtra("token", token);
                intent.putExtra("variable", workspace);
                intent.putExtra("username", user);
                startActivity(intent);
                finish();
            }
        });

    }


    private void compressImage(Bitmap bitmap) {
        try {

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 50, baos);
            byte[] b = baos.toByteArray();
            StringBuilder encodedStringBuilder = new StringBuilder().append(Base64.encodeToString(b, Base64.DEFAULT));
            uploadPicture(encodedStringBuilder);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void uploadPicture(StringBuilder basePicture) {

        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);
        progressDialog.setTitle("Uploading");
        progressDialog.show();

        UploadPictureRequest uploadPictureRequest = new UploadPictureRequest(frId, basePicture);
        value = value.toLowerCase();
        Call<Void> uploadImageCall = APIClient.getUserServices().uploadCaptureImage( value,token, uploadPictureRequest);

        uploadImageCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                progressDialog.dismiss();
                if (response.code() == 200) {
                    Toast.makeText(BeforeImage.this, "Image saved successfully", Toast.LENGTH_SHORT).show();
                    uploadBtn.setEnabled(false);
                    new AlertDialog.Builder(BeforeImage.this)
                            .setTitle("Image saved successfully")
                            .setMessage("Wish to upload more picture?")
                            .setIcon(R.drawable.ic_error)
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finish();
                        }
                    })
                            .show();
                }
                if (response.code() == 406) {
                    Toast.makeText(BeforeImage.this, "Cannot add more than 5 pictures", Toast.LENGTH_SHORT).show();
                    new AlertDialog.Builder(BeforeImage.this)
                            .setMessage("Cannot add more than 5 pictures")
                            .setTitle("Alert")
                            .setIcon(R.drawable.ic_error)
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    uploadBtn.setEnabled(false);
                                    takeBtn.setEnabled(false);
                                    finish();
                                }
                            })
                            .show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(BeforeImage.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            beforeImgPre.setVisibility(View.VISIBLE);
            beforeImgPre.setImageBitmap(photo);
            uploadBtn.setEnabled(true);

            uploadBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    compressImage(photo);
                }
            });

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        MenuItem item = (MenuItem) menu.findItem(R.id.admin).setTitle("Hello: " + user);
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
            Intent in = new Intent(BeforeImage.this, MainActivityLogin.class);
            startActivity(in);
            finishAffinity();
        }
        return true;
    }

}
