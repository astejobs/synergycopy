package com.synergy.faultReport;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.synergy.R;

public class BeforeImage extends AppCompatActivity {

    private static final String TAG = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_before_image);
        Intent intent=getIntent();
        String FrId=intent.getStringExtra("Frid");
        Log.d(TAG, "onCreate: "+FrId);
    }
}
