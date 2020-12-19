package com.synergy.Search;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Path;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.synergy.R;

import java.util.List;

public class ImageAdapter extends PagerAdapter {
    private static final String TAG = "";
    private Context mcontext;
    private List<Bitmap> bitmapList;
    private int imgId = R.drawable.noimg;


    ImageAdapter(Context mcontext, List<Bitmap> bitmapList) {
        this.bitmapList = bitmapList;
        this.mcontext = mcontext;
    }

    @Override
    public int getCount() {
        return bitmapList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        ImageView imageView = new ImageView(mcontext);
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        Log.d(TAG, "instantiateItem: nn" + bitmapList);
        imageView.setImageBitmap(bitmapList.get(position));
        container.addView(imageView);
        final int delPosition = position;
        imageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                bitmapList.remove(position);
                Toast.makeText(mcontext, "Image Deleted Successfully", Toast.LENGTH_SHORT).show();
                notifyDataSetChanged();
                return true;
            }

        });
        return imageView;
    }
    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((ImageView) object);
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return PagerAdapter.POSITION_NONE;
    }
}
