package com.synergy.Search;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.synergy.APIClient;
import com.synergy.MainActivityLogin;
import com.synergy.R;
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.synergy.MainActivityLogin.SHARED_PREFS;

public class PreviousImagesActivity extends AppCompatActivity {
    private static final String TAG = "";
    private List<String> urlList = new ArrayList<>();
    private String frid, token, value, workspace, role, username;
    private Toolbar toolbar;
    private DotsIndicator dotsIndicator;
    List<InputStream> list = new ArrayList<InputStream>();
    List<Integer> imageIdList = new ArrayList<Integer>();
    List<Bitmap> bitmapList = new ArrayList<Bitmap>();
    ViewPager viewPager;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_previous_images);
        progressDialog = new ProgressDialog(PreviousImagesActivity.this);
        progressDialog.setTitle("Loading...");

        initViews();

        Call<JsonObject> call = APIClient.getUserServices().getBeforeImage(value, frid, workspace, token);
        progressDialog.show();
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.code() == 200) {
                    JsonObject jsonObject = response.body();
                    assert jsonObject != null;
                    JsonArray jsonArray = jsonObject.get("images").getAsJsonArray();
                    for (int i = 0; i < jsonArray.size(); i++) {
                        Call<ResponseBody> calll = APIClient.getUserServices().getImageBase64(jsonArray.get(i).getAsString(), workspace, token);
                        calll.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                if (response.code() == 200) {
                                    progressDialog.dismiss();
                                    list.add(response.body().byteStream());
                                    Bitmap bmp = BitmapFactory.decodeStream(response.body().byteStream());
                                    bitmapList.add(bmp);
                                    //   methodAddImage();
                                  /*  urlList.add(jsonObject1);
                                     ImageAdapter imageAdapter = new ImageAdapter(PreviousImagesActivity.this,urlList);
                                    viewPager.setAdapter(imageAdapter);
                                    dotsIndicator.setViewPager(viewPager);
                                    Log.d(TAG, "onResponse:in "+urlList);*/
                                    ImageAdapter imageAdapter = new ImageAdapter(PreviousImagesActivity.this, bitmapList);
                                    viewPager.setAdapter(imageAdapter);
                                    dotsIndicator.setViewPager(viewPager);
                                }
                                progressDialog.dismiss();
                            }


                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                Toast.makeText(PreviousImagesActivity.this, "Failed : " + t.getMessage(), Toast.LENGTH_SHORT).show();
                                Log.d(TAG, "onFailure: " + t.getCause());
                                progressDialog.dismiss();
                                Log.d(TAG, "onFailure: " + t.getMessage());
                            }
                        });
                    }
                   /* ImageAdapter imageAdapter = new ImageAdapter(PreviousImagesActivity.this, bitmapList);

                    viewPager.setAdapter(imageAdapter);
                    dotsIndicator.setViewPager(viewPager);*/

                } else
                    Toast.makeText(PreviousImagesActivity.this, "Error : " + response.code(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(PreviousImagesActivity.this, "Failed : " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onFailure: " + t.getMessage());
                Log.d(TAG, "onFailure: " + t.getCause());
                progressDialog.dismiss();
            }
        });

    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar_images);
        setSupportActionBar(toolbar);
        Intent intent = getIntent();
        role = intent.getStringExtra("role");
        value = intent.getStringExtra("value");
        frid = intent.getStringExtra("frid");
        token = intent.getStringExtra("token");
        toolbar.setTitle(value + " Previous Images");
        workspace = intent.getStringExtra("workspace");
        dotsIndicator = findViewById(R.id.dot);
        viewPager = findViewById(R.id.view_pager_layout);
        value = value.toLowerCase();
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        username = sharedPreferences.getString("username", "");
    }

    private void methodAddImage() {
        for (int i = 0; i < list.size(); i++) {

            Bitmap bmp = BitmapFactory.decodeStream(list.get(i));
            LinearLayout layout = (LinearLayout) findViewById(R.id.image_layout_linear);
            ImageView image = new ImageView(PreviousImagesActivity.this);
            image.setLayoutParams(new android.view.ViewGroup.LayoutParams(450
                    , 300));
            image.setScaleType(ImageView.ScaleType.MATRIX);
            image.setPadding(2, 2, 2, 2);
            image.setId(i + 1);
            imageIdList.add(image.getId());
            layout.addView(image);
            image.setImageBitmap(bmp);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        MenuItem item = (MenuItem) menu.findItem(R.id.admin).setTitle(username);
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
            Intent in = new Intent(PreviousImagesActivity.this, MainActivityLogin.class);
            startActivity(in);
            finishAffinity();
        }
        return true;
    }
}