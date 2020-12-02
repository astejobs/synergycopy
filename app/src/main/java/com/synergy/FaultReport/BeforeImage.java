package com.synergy.FaultReport;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
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
import com.synergy.CheckInternet;
import com.synergy.Constants;
import com.synergy.EquipmentSearch.EquipmentSearchActivity;
import com.synergy.LogoutClass;
import com.synergy.MainActivityLogin;
import com.synergy.R;
import com.synergy.Dashboard.Dashboard;
import com.synergy.Search.PreviousImagesActivity;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.synergy.MainActivityLogin.SHARED_PREFS;

public class BeforeImage extends AppCompatActivity {

    private static final String TAG = "before";
    Button takeBtn, uploadBtn, doneBtn, previousImagesbtn;
    ImageView beforeImgPre;
    public static final int REQUEST_IMAGE_CAPTURE = 0;
    public static final int REQUEST_IMAGEBEFORE_CAPTURE = 3;
    public static final int REQUEST_IMAGEAFTER_CAPTURE = 4;
    private Intent takePictureIntent;
    private String token;
    private String workspace;
    private String frId;
    private ProgressDialog progressDialog;
    private String value;
    private Toolbar toolbar;
    private String user;
    private String status;
    private String checkForFrid;
    String managingagent = "ManagingAgent";

    private final CheckInternet checkInternet = new CheckInternet();

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(checkInternet, intentFilter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(checkInternet);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_before_image);

        Intent intent = getIntent();
        token = intent.getStringExtra("token");
        workspace = intent.getStringExtra("workspace");
        frId = intent.getStringExtra("frId");
        value = intent.getStringExtra("value");
        user = intent.getStringExtra("role");
        status = "test";
        status = intent.getStringExtra("status");
        checkForFrid = intent.getStringExtra("checkForFrid");
        Log.d(TAG, "onCreate: hi" + user);

        if (checkForFrid == null) {

            takeBtn = findViewById(R.id.take_photo_btn);
            uploadBtn = findViewById(R.id.upload_btn);
            uploadBtn.setEnabled(false);
            toolbar = findViewById(R.id.toolbar_globe);
            doneBtn = findViewById(R.id.done_btn);
            previousImagesbtn = findViewById(R.id.previous_images);
            beforeImgPre = findViewById(R.id.before_image_preview);
            progressDialog = new ProgressDialog(BeforeImage.this);
            setSupportActionBar(toolbar);
            toolbar.setTitle(value + " Image");

            previousImagesbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(BeforeImage.this, PreviousImagesActivity.class);
                    intent.putExtra("token", token);
                    intent.putExtra("workspace", workspace);
                    intent.putExtra("frid", frId);
                    intent.putExtra("role", user);
                    intent.putExtra("value", value);
                    startActivity(intent);
                }
            });

            takeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                    }
                }
            });
/*
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
*/
        }

        if (user.equals("Technician")) {
            previousImagesbtn = findViewById(R.id.previous_images);
            takeBtn = findViewById(R.id.take_photo_btn);
            uploadBtn = findViewById(R.id.upload_btn);
            doneBtn = findViewById(R.id.done_btn);
            takeBtn.setVisibility(View.VISIBLE);
            uploadBtn.setVisibility(View.VISIBLE);
            doneBtn.setVisibility(View.INVISIBLE);
            toolbar = findViewById(R.id.toolbar_globe);
            setSupportActionBar(toolbar);
            toolbar.setTitle(value + " Image");
            previousImagesbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(BeforeImage.this, PreviousImagesActivity.class);
                    intent.putExtra("token", token);
                    intent.putExtra("workspace", workspace);
                    intent.putExtra("frid", frId);
                    intent.putExtra("role", user);
                    intent.putExtra("value", value);
                    startActivity(intent);
                }
            });


        }
        if (user.equals(managingagent)) {
            previousImagesbtn = findViewById(R.id.previous_images);
            takeBtn = findViewById(R.id.take_photo_btn);
            uploadBtn = findViewById(R.id.upload_btn);
            doneBtn = findViewById(R.id.done_btn);
            takeBtn.setVisibility(View.INVISIBLE);
            uploadBtn.setVisibility(View.INVISIBLE);
            doneBtn.setVisibility(View.INVISIBLE);
            toolbar = findViewById(R.id.toolbar_globe);
            setSupportActionBar(toolbar);
            toolbar.setTitle(value + " Image");
            //
            takeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                    }
                }
            });

            previousImagesbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(BeforeImage.this, PreviousImagesActivity.class);
                    intent.putExtra("token", token);
                    intent.putExtra("workspace", workspace);
                    intent.putExtra("frid", frId);
                    intent.putExtra("role", user);
                    intent.putExtra("value", value);
                    startActivity(intent);
                }
            });


        } else {
            previousImagesbtn = findViewById(R.id.previous_images);
            takeBtn = findViewById(R.id.take_photo_btn);
            uploadBtn = findViewById(R.id.upload_btn);
            doneBtn = findViewById(R.id.done_btn);
            takeBtn.setVisibility(View.VISIBLE);
            uploadBtn.setVisibility(View.VISIBLE);
            doneBtn.setVisibility(View.INVISIBLE);
            toolbar = findViewById(R.id.toolbar_globe);
            setSupportActionBar(toolbar);
            toolbar.setTitle(value + " Image");

            takeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                    }
                }
            });

            previousImagesbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(BeforeImage.this, PreviousImagesActivity.class);
                    intent.putExtra("token", token);
                    intent.putExtra("workspace", workspace);
                    intent.putExtra("frid", frId);
                    intent.putExtra("role", user);
                    intent.putExtra("value", value);
                    startActivity(intent);
                }
            });


        }

    }


    private void compressImage(Bitmap bitmap) {

        try {

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 80, baos);
            byte[] b = baos.toByteArray();
            // String encodedStringBuilder = Base64.encodeToString(b, Base64.DEFAULT);

            StringBuilder encodedStringBuilder = new StringBuilder()
                    .append(Base64.encodeToString(b, Base64.DEFAULT));
/*
            byte[] decodedString = Base64.decode(String.valueOf(encodedStringBuilder), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            ImageView imageView = findViewById(R.id.decoded_img);
            imageView.setVisibility(View.VISIBLE);
            imageView.setImageBitmap(decodedByte);*/

            uploadPicture(encodedStringBuilder);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void uploadPicture(StringBuilder encodedStringBuilder) {

        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);
        progressDialog.setTitle("Uploading");
        progressDialog.show();
        UploadPictureRequest uploadPictureRequest = new UploadPictureRequest(frId, encodedStringBuilder);
        value = value.toLowerCase();
        Call<Void> uploadImageCall = APIClient.getUserServices().uploadCaptureImage(value, token, workspace, uploadPictureRequest);

        uploadImageCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                progressDialog.dismiss();
                if (response.code() == 200) {
                    Toast.makeText(BeforeImage.this, "Image saved successfully", Toast.LENGTH_SHORT).show();
                    uploadBtn.setEnabled(false);
                    new AlertDialog.Builder(BeforeImage.this)
                            .setTitle("Image saved successfully")
                            .setMessage("Want to upload more pictures ?")
                            .setIcon(R.drawable.ic_error)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
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
                } else if (response.code() == 401) {
                    Toast.makeText(BeforeImage.this, Constants.ERROR_CODE_401_MESSAGE, Toast.LENGTH_SHORT).show();
                    LogoutClass logoutClass = new LogoutClass();
                    logoutClass.logout(BeforeImage.this);
                } else if (response.code() == 500) {
                    Toast.makeText(BeforeImage.this, Constants.ERROR_CODE_500_MESSAGE, Toast.LENGTH_SHORT).show();
                } else if (response.code() == 404) {
                    Toast.makeText(BeforeImage.this, Constants.ERROR_CODE_404_MESSAGE, Toast.LENGTH_SHORT).show();
                } else if (response.code() == 406) {
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
            assert data != null;
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
        MenuItem item = (MenuItem) menu.findItem(R.id.admin).setTitle(user);
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
