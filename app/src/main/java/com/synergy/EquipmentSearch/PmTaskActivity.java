package com.synergy.EquipmentSearch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.synergy.APIClient;
import com.synergy.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class PmTaskActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private TextView taskNumberTextView, scheduleNumberTextView, buildingNameTextView,
            locationNameTextView, equipmentNameTextView, briefDescTextView,
            scheduleDateTextView;
    private EditText remarksTextView, nameTextView;
    private Spinner statusSpinner;
    private Button buttonUpdate;
    private TextView datePickerEdit, timePickerEdit;
    private int tHour, tMinute;
    private ProgressDialog mProgress;
    private ArrayAdapter<String> statusSpinnerAdapter;
    private List<String> statusList = new ArrayList<>();
    String timeString, dateString, completedByString, taskNumberString, statusString, remarksString = "";
    private static final String TAG = "PmTaskActivity";
    private ProgressDialog updateProgress;
    private Toolbar toolbar;
    private Button checkListButton;
    private long scheduleDate;
    private int taskId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pm_task);

        taskNumberTextView = findViewById(R.id.textViewTaskNumberPm);
        scheduleNumberTextView = findViewById(R.id.textViewScheduleNumberPm);
        scheduleNumberTextView.setMovementMethod(new ScrollingMovementMethod());
        nameTextView = findViewById(R.id.namePmTasks);
        remarksTextView = findViewById(R.id.remarks_pmTasks);
        buildingNameTextView = findViewById(R.id.textViewBuildingNumberPm);
        locationNameTextView = findViewById(R.id.textViewLocationNumberPm);
        equipmentNameTextView = findViewById(R.id.textViewEquipmentPm);
        briefDescTextView = findViewById(R.id.textViewBriefDescriptionPm);
        scheduleDateTextView = findViewById(R.id.textViewScheduleDatePm);
        datePickerEdit = findViewById(R.id.date_picker_pmtasksPm);
        datePickerEdit.setEnabled(false);
        timePickerEdit = findViewById(R.id.time_picker_pmtasks);
        timePickerEdit.setEnabled(false);
        buttonUpdate = findViewById(R.id.buttonUpdateTaskPm);
        statusSpinner = findViewById(R.id.spinner_status_pmtasks);
        checkListButton = findViewById(R.id.buttonCheckList);

        mProgress = new ProgressDialog(PmTaskActivity.this);
        mProgress.setTitle("Retreiving data...");
        mProgress.setMessage("Please wait...");
        mProgress.setCancelable(false);
        mProgress.setIndeterminate(true);

        updateProgress = new ProgressDialog(this);
        updateProgress.setTitle("Updating...");
        updateProgress.setCancelable(false);
        updateProgress.setIndeterminate(true);

        /*Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String currentDateTimeString = sdf.format(d);
        timePickerEdit.setText(currentDateTimeString);

        SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy");
        datePickerEdit.setText(sdf1.format(new Date()));*/

        statusSpinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, statusList);
        statusSpinner.setAdapter(statusSpinnerAdapter);

        Intent intent = getIntent();
        taskNumberString = intent.getStringExtra("taskNumber");
        taskId = intent.getIntExtra("taskId", 0);

        getPmTask(taskNumberString, taskId);


        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }

    private void getPmTask(String taskNumberString, int taskId) {
        mProgress.show();

        taskNumberTextView.setText(taskNumberString);

        Call<PmTaskResponse> callPmTask = APIClient.getUserServices().getCallPmTask(String.valueOf(taskId));

        callPmTask.enqueue(new Callback<PmTaskResponse>() {
            @Override
            public void onResponse(Call<PmTaskResponse> call, Response<PmTaskResponse> response) {
                mProgress.dismiss();
                if (response.code() == 200) {
                    Toast.makeText(PmTaskActivity.this, "Success", Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(PmTaskActivity.this, "Error: " + response.code(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<PmTaskResponse> call, Throwable t) {
                mProgress.dismiss();
                Toast.makeText(PmTaskActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void showDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (DatePickerDialog.OnDateSetListener) this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

        String date = i2 + "-" + (i1 + 1) + "-" + i;
        datePickerEdit.setText(date);
    }

    private void getTimeAndDatePicker() {
        timePickerEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        PmTaskActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                                tHour = i;
                                tMinute = i1;

                                Calendar calendar = Calendar.getInstance();
                                calendar.set(0, 0, 0, tHour, tMinute);

                                timePickerEdit.setText(DateFormat.format("HH:mm", calendar));
                            }
                        }, 12, 0, true
                );
                timePickerDialog.updateTime(tHour, tMinute);
                timePickerDialog.show();
            }
        });

        datePickerEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog();
            }
        });

    }

}