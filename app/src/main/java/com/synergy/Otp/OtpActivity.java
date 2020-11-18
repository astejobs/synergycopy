package com.synergy.Otp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.synergy.R;

public class OtpActivity extends AppCompatActivity {
    EditText otp_textbox_one, otp_textbox_two, otp_textbox_three, otp_textbox_four;
    Button verify_otp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

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


        verify_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = otp_textbox_one.getText().toString() + otp_textbox_two.getText().toString() +
                        otp_textbox_three.getText().toString() + otp_textbox_four.getText().toString();
                Toast.makeText(getApplicationContext(), "Login Successfull" + text, Toast.LENGTH_SHORT).show();
                //
                //text

            }
        });


    }


}