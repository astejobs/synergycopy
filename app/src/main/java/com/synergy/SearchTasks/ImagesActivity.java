package com.synergy.SearchTasks;

import com.synergy.Constants;
import com.synergy.MyBaseActivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.synergy.APIClient;
import com.synergy.EquipmentSearch.UploadTaskImageActivity;
import com.synergy.R;
import com.trex.lib.ThumbIndicator;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ImagesActivity extends MyBaseActivity {

    private static final String TAG = "";
    private List<String> mUrls;
    private ViewPager mVpMain;
    private ThumbIndicator mIndicator;
    private int indexToDelete = -1;
    private List<String> base64List = new ArrayList<>();
    private ImageView galleryButton, cameraButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View viewLayout = layoutInflater.inflate(R.layout.activity_images, null, false);
        drawer.addView(viewLayout, 0);

        galleryButton = findViewById(R.id.galleryButton);
        cameraButton = findViewById(R.id.cameraButton);

        def();
        setup();

        Call<JsonObject> callImage = APIClient.getUserServices().getBeforeImage(
                "before",
                "FR-DEMO-122020-00039",
                "lsme-DEMO-112016-001",
                token);
        callImage.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.code() == 200) {
                    JsonObject jsonObject = response.body();
                    assert jsonObject != null;
                    JsonArray jsonArray = jsonObject.get("images").getAsJsonArray();
                    for (int i = 0; i < jsonArray.size(); i++) {
                        Call<ResponseBody> callBase = APIClient.getUserServices().getImageBase64(jsonArray.get(i).getAsString(),
                                "lsme-DEMO-112016-001", token);

                        callBase.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                if (response.code() == 200) {
                                    Bitmap bmp = BitmapFactory.decodeStream(response.body().byteStream());
                                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                    bmp.compress(Bitmap.CompressFormat.PNG, 100, baos);
                                    byte[] b = baos.toByteArray();
                                    String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);
                                    base64List.add(imageEncoded);
                                    adp.notifyDataSetChanged();
                                    mIndicator.notifyDataSetChanged();
                                } else if (response.code() == 401) {
                                    Toast.makeText(ImagesActivity.this, Constants.ERROR_CODE_401_MESSAGE, Toast.LENGTH_SHORT).show();
                                } else if (response.code() == 500) {
                                    Toast.makeText(ImagesActivity.this, Constants.ERROR_CODE_500_MESSAGE, Toast.LENGTH_SHORT).show();
                                } else if (response.code() == 404) {
                                    Toast.makeText(ImagesActivity.this, Constants.ERROR_CODE_404_MESSAGE, Toast.LENGTH_SHORT).show();
                                } else
                                    Toast.makeText(ImagesActivity.this, "Error: " + response.code(), Toast.LENGTH_SHORT).show();

                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                Toast.makeText(ImagesActivity.this, "Error: " + t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(ImagesActivity.this, "Error: " + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        galleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto, 13);
            }
        });

        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(takePicture, 1);
            }
        });
    }

    private void def() {
        mVpMain = findViewById(R.id.vpMain);
        mIndicator = findViewById(R.id.indicator);
        mUrls = base64List;
        //mUrls = getMockImgs();
        mVpMain.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
            }

            @Override
            public void onPageScrollStateChanged(int i) {
                if (indexToDelete != -1 && i == ViewPager.SCROLL_STATE_IDLE) {
                    mUrls.remove(indexToDelete);
                    adp.notifyDataSetChanged();
                    mIndicator.notifyDataSetChanged();
                    if (indexToDelete == 0) {
                        mVpMain.setCurrentItem(indexToDelete, false);
                        mIndicator.setCurrentItem(indexToDelete, false);
                    }
                    indexToDelete = -1;
                }
            }
        });
    }

    private List<String> getMockImgs() {
        List<String> tmp = new ArrayList<>();
        tmp.add("http://api.moneyar.com/APIs/images/23599258923.jpg");
        tmp.add("http://api.moneyar.com/APIs/images/23599524312.jpg");
        tmp.add("http://api.moneyar.com/APIs/images/23599660421.jpg");
        tmp.add("http://api.moneyar.com/APIs/images/23599697411.jpg");
        tmp.add("http://api.moneyar.com/APIs/images/23599792108.jpg");
        return tmp;
    }


    VpAdapter adp;

    private void setup() {
        adp = new VpAdapter();
        mVpMain.setAdapter(adp);
        mIndicator.setupWithViewPager(mVpMain, (ArrayList<String>) mUrls, 70);
    }

    class VpAdapter extends PagerAdapter {
        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, final int position) {
            View v = getLayoutInflater().inflate(R.layout.thumb_item, container, false);
            ImageView imageSlider = v.findViewById(R.id.imgSlider);

            //Glide.with(container.getContext()).load(mUrls.get(position)).into(imageSlider);

            byte[] decodedByte = Base64.decode(mUrls.get(position), 0);
            Bitmap bitmap = BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
            Glide.with(container.getContext()).asBitmap().load(bitmap).into(imageSlider);
            container.addView(v);
            return v;
        }

        @Override
        public int getItemPosition(@NonNull Object object) {
            if (mUrls.indexOf(object) == -1)
                return POSITION_NONE;
            else
                return super.getItemPosition(object);
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
            return view == o;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getCount() {
            return mUrls.size();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                try {
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    photo.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);
                    byte[] byteArray = byteArrayOutputStream.toByteArray();

                    String base = new UploadTaskImageActivity().bmpToString(photo);

                    cameraButton.setImageBitmap(photo);
                } catch (Exception e) {
                    Log.d("TAG", "onActivityResult: " + e.getLocalizedMessage());
                }
            } else {
                Uri selectedImage = data.getData();

                Bitmap photoBmp = null;
                try {
                    photoBmp = MediaStore.Images.Media.getBitmap(getApplication().getContentResolver(), selectedImage);
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    photoBmp.compress(Bitmap.CompressFormat.JPEG, 30, byteArrayOutputStream);
                    byte[] byteArray = byteArrayOutputStream.toByteArray();

                    String uploadBase = new UploadTaskImageActivity().bmpToString(photoBmp);

                    galleryButton.setImageBitmap(photoBmp);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}