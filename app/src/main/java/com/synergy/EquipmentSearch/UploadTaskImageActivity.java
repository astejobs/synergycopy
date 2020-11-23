package com.synergy.EquipmentSearch;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.synergy.APIClient;
import com.synergy.LogoutClass;
import com.synergy.MainActivityLogin;
import com.synergy.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.synergy.FaultReport.BeforeImage.REQUEST_IMAGEAFTER_CAPTURE;
import static com.synergy.FaultReport.BeforeImage.REQUEST_IMAGEBEFORE_CAPTURE;
import static com.synergy.MainActivityLogin.SHARED_PREFS;

public class UploadTaskImageActivity extends AppCompatActivity {

    private String role;
    private String taskId;
    private String token;
    private ImageView beforeImage, afterImage;
    public static final int TAKE_PHOTO_GALLERY = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_task_image);

        Toolbar toolbar = findViewById(R.id.toolbar_UploadTask);
        setSupportActionBar(toolbar);
        beforeImage = findViewById(R.id.beforeImageTask);
        afterImage = findViewById(R.id.afterImageTask);

        Intent intent = getIntent();
        taskId = intent.getStringExtra("taskId");

        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        role = sharedPreferences.getString("role", "");
        token = sharedPreferences.getString("token", "");

        beforeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage(UploadTaskImageActivity.this, REQUEST_IMAGEBEFORE_CAPTURE);
            }
        });

        afterImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage(UploadTaskImageActivity.this, REQUEST_IMAGEAFTER_CAPTURE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String imageViewValue;

        if (requestCode == Integer.valueOf(String.valueOf(0) + String.valueOf(REQUEST_IMAGEBEFORE_CAPTURE)) ||
                requestCode == Integer.valueOf(String.valueOf(1) + String.valueOf(REQUEST_IMAGEBEFORE_CAPTURE)) &&
                        resultCode == Activity.RESULT_OK) {
            assert data != null;
            String encodedStringBuilder = null;
            imageViewValue = "before";
            if (requestCode == Integer.valueOf(String.valueOf(0) + String.valueOf(REQUEST_IMAGEBEFORE_CAPTURE))) {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                try {
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    photo.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);
                    byte[] byteArray = byteArrayOutputStream.toByteArray();

                    encodedStringBuilder = Base64.encodeToString(byteArray, Base64.DEFAULT);
                    uploadPicture(encodedStringBuilder, photo, beforeImage, imageViewValue);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Uri selectedImage = data.getData();

                Bitmap photo1 = null;
                try {
                    photo1 = MediaStore.Images.Media.getBitmap(getApplication().getContentResolver(), selectedImage);
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    photo1.compress(Bitmap.CompressFormat.JPEG, 30, byteArrayOutputStream);
                    byte[] byteArray = byteArrayOutputStream.toByteArray();

                    encodedStringBuilder = Base64.encodeToString(byteArray, Base64.DEFAULT);
                    uploadPicture(encodedStringBuilder, photo1, beforeImage, imageViewValue);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

        } else {
            assert data != null;
            String encodedStringBuilder = null;
            imageViewValue = "after";
            if (requestCode == Integer.valueOf(String.valueOf(0) + String.valueOf(REQUEST_IMAGEAFTER_CAPTURE))) {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                try {
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    photo.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);
                    byte[] byteArray = byteArrayOutputStream.toByteArray();

                    encodedStringBuilder = Base64.encodeToString(byteArray, Base64.DEFAULT);
                    uploadPicture(encodedStringBuilder, photo, afterImage, imageViewValue);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Uri selectedImage = data.getData();

                Bitmap photoBmp = null;
                try {
                    photoBmp = MediaStore.Images.Media.getBitmap(getApplication().getContentResolver(), selectedImage);
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    photoBmp.compress(Bitmap.CompressFormat.JPEG, 30, byteArrayOutputStream);
                    byte[] byteArray = byteArrayOutputStream.toByteArray();

                    encodedStringBuilder = Base64.encodeToString(byteArray, Base64.DEFAULT);
                    uploadPicture(encodedStringBuilder, photoBmp, afterImage, imageViewValue);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }

    private void uploadPicture(String encodedStringBuilder, Bitmap photo, ImageView imageView, String imageViewValue) {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);
        progressDialog.setTitle("Uploading");
        progressDialog.show();
        UploadImageRequest uploadImageRequest = new UploadImageRequest(taskId, encodedStringBuilder);

        Call<Void> callUploadPicture = APIClient.getUserServices().taskImageUpload(role, token, imageViewValue, uploadImageRequest);
        callUploadPicture.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.code() == 200) {
                    imageView.setBackground(null);
                    imageView.setImageBitmap(photo);
                } else
                    Toast.makeText(UploadTaskImageActivity.this, "Error: " + response.code(), Toast.LENGTH_LONG).show();

                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(UploadTaskImageActivity.this, "Error: " + t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }
        });
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

    private void selectImage(Context context, int REQUEST_CODE) {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Choose picture");

        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals("Take Photo")) {
                    Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(takePicture, Integer.valueOf(String.valueOf(0) + String.valueOf(REQUEST_CODE)));

                } else if (options[item].equals("Choose from Gallery")) {
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto, Integer.valueOf(String.valueOf(1) + String.valueOf(REQUEST_CODE)));

                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    public void startActivityForSecondResult(Intent data, int requestCode, String imageViewValue, int galleryOrCamera, int CONSTANTVALUES) {

        assert data != null;
        String encodedStringBuilder = null;
        if (requestCode == Integer.valueOf(String.valueOf(galleryOrCamera) + String.valueOf(CONSTANTVALUES))) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            try {
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                photo.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
                byte[] byteArray = byteArrayOutputStream.toByteArray();

                encodedStringBuilder = Base64.encodeToString(byteArray, Base64.DEFAULT);
                uploadPicture(encodedStringBuilder, photo, beforeImage, imageViewValue);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath = cursor.getString(columnIndex);
                Bitmap photo1 = (BitmapFactory.decodeFile(picturePath));
                try {
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    photo1.compress(Bitmap.CompressFormat.PNG, 30, byteArrayOutputStream);
                    byte[] byteArray = byteArrayOutputStream.toByteArray();

                    encodedStringBuilder = Base64.encodeToString(byteArray, Base64.DEFAULT);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                uploadPicture(encodedStringBuilder, photo1, beforeImage, imageViewValue);
                cursor.close();
            }

        }
    }


}