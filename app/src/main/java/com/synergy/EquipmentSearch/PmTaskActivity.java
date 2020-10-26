package com.synergy.EquipmentSearch;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PmTaskActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private TextView taskNumberTextView;
    private TextView scheduleNumberTextView;
    private TextView buildingNameTextView;
    private TextView locationNameTextView;
    private TextView equipmentNameTextView;
    private TextView briefDescTextView;
    private TextView scheduleDateTextView;
    private EditText remarksTextView, nameTextView;
    private Spinner statusSpinner;
    private Button buttonUpdate;
    private TextView datePickerEdit;
    private TextView timePickerEdit;
    private int tHour, tMinute;
    private ProgressDialog mProgress;
    private ArrayAdapter<String> statusSpinnerAdapter;
    private final List<String> statusList = new ArrayList<>();
    private String taskNumberString;
    private ProgressDialog updateProgress;
    private Button checkListButton;
    private long scheduleDate;

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
        buttonUpdate.setEnabled(false);
        statusSpinner = findViewById(R.id.spinner_status_pmtasks);
        checkListButton = findViewById(R.id.buttonCheckList);
        checkListButton.setEnabled(false);

        mProgress = new ProgressDialog(PmTaskActivity.this);
        mProgress.setTitle("Retrieving data...");
        mProgress.setMessage("Please wait...");
        mProgress.setCancelable(false);
        mProgress.setIndeterminate(true);

        updateProgress = new ProgressDialog(this);
        updateProgress.setTitle("Updating...");
        updateProgress.setCancelable(false);
        updateProgress.setIndeterminate(true);

        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String currentDateTimeString = sdf.format(d);
        timePickerEdit.setText(currentDateTimeString);

        SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy");
        datePickerEdit.setText(sdf1.format(new Date()));

        statusSpinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, statusList);
        statusSpinner.setAdapter(statusSpinnerAdapter);

        Intent intent = getIntent();
        taskNumberString = intent.getStringExtra("taskNumber");
        int taskId = intent.getIntExtra("taskId", 0);

        getPmTask(taskNumberString, taskId);


        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    updateTaskMethod();
                }
            }
        });

        checkListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent setIntent = new Intent(getApplicationContext(), CheckListActivity.class);
                setIntent.putExtra("taskId", taskId);
                startActivity(setIntent);
            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void updateTaskMethod() {
        String timeString = timePickerEdit.getText().toString();
        String dateString = datePickerEdit.getText().toString();
        String completedByString = nameTextView.getText().toString();
        String status = statusSpinner.getSelectedItem().toString();
        String remarksString = remarksTextView.getText().toString();

        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        Date newDate = null;
        try {
            newDate = format.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        format = new SimpleDateFormat("yyyy-MM-dd");
        String date = format.format(newDate);

        GetUpdatePmTaskRequest getUpdatePmTaskRequest = new GetUpdatePmTaskRequest(taskNumberString, status, date, timeString, completedByString, remarksString);

        if (!nameTextView.getText().toString().isEmpty() && !remarksTextView.getText().toString().isEmpty()) {
            LocalDate now = Instant.ofEpochMilli((long) scheduleDate).atZone(ZoneId.systemDefault()).toLocalDate();
            if (!now.isBefore(LocalDate.now())) {
                updatePmTaskService(getUpdatePmTaskRequest);
            } else
                Toast.makeText(PmTaskActivity.this, "Overdue tasks cannot be updated.", Toast.LENGTH_SHORT).show();
        } else
            Toast.makeText(PmTaskActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
    }

    private void getPmTask(String taskNumberString, int taskId) {
        mProgress.show();
        taskNumberTextView.setText(taskNumberString);

        Call<GetPmTaskItemsResponse> callPmTask = APIClient.getUserServices().getCallPmTask(String.valueOf(taskId));
        callPmTask.enqueue(new Callback<GetPmTaskItemsResponse>() {
            @Override
            public void onResponse(Call<GetPmTaskItemsResponse> call, Response<GetPmTaskItemsResponse> response) {
                mProgress.dismiss();
                if (response.code() == 200) {

                    GetPmTaskItemsResponse getPmTaskItemsResponse = response.body();
                    if (getPmTaskItemsResponse.getTaskNumber() != null) {
                        taskNumberTextView.setText(getPmTaskItemsResponse.getTaskNumber());
                    }
                    if (getPmTaskItemsResponse.getPmScheduleNo() != null) {
                        scheduleNumberTextView.setText(getPmTaskItemsResponse.getPmScheduleNo());
                    }
                    if (getPmTaskItemsResponse.getBriefDescription() != null) {
                        briefDescTextView.setText(getPmTaskItemsResponse.getBriefDescription());
                    }
                    if (getPmTaskItemsResponse.getEquipmentBuilding() != null) {
                        buildingNameTextView.setText(getPmTaskItemsResponse.getEquipmentBuilding());
                    }
                    if (getPmTaskItemsResponse.getEquipmentCode() != null) {
                        equipmentNameTextView.setText(getPmTaskItemsResponse.getEquipmentCode());
                    }
                    if (getPmTaskItemsResponse.getEquipmentLocation() != null) {
                        locationNameTextView.setText(getPmTaskItemsResponse.getEquipmentLocation());
                    }
                    if (getPmTaskItemsResponse.getScheduleDate() != 0) {
                        scheduleDate = (long) getPmTaskItemsResponse.getScheduleDate();
                        LocalDateTime date = null;
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                            date = Instant.ofEpochMilli((long) getPmTaskItemsResponse.getScheduleDate()).atZone(ZoneId.systemDefault()).toLocalDateTime();
                        }
                        String dateStr = null;
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                            dateStr = date.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
                        }
                        scheduleDateTextView.setText(dateStr);
                    }
                    if (getPmTaskItemsResponse.getStatus() != null) {
                        statusList.add(getPmTaskItemsResponse.getStatus());
                        if (getPmTaskItemsResponse.getStatus().equals("OPEN")) {
                            statusList.add("CLOSED");
                        } else statusList.add("OPEN");
                        statusSpinner.setAdapter(statusSpinnerAdapter);
                    } else {
                        statusList.add("OPEN");
                        statusList.add("CLOSED");
                        statusSpinner.setAdapter(statusSpinnerAdapter);
                    }

                    if (getPmTaskItemsResponse.getRemarks() != null) {
                        remarksTextView.setText(getPmTaskItemsResponse.getRemarks());
                    }

                    if (getPmTaskItemsResponse.getCompletedBy() != null) {
                        nameTextView.setText(getPmTaskItemsResponse.getCompletedBy());
                    }

                    buttonUpdate.setEnabled(true);
                    checkListButton.setEnabled(true);
                } else
                    Toast.makeText(PmTaskActivity.this, "Error: " + response.code(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<GetPmTaskItemsResponse> call, Throwable t) {
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

    private void updatePmTaskService(GetUpdatePmTaskRequest getUpdatePmTaskRequest) {
        updateProgress.show();

        Call<GetUpdatePmTaskResponse> callTaskUpdate = APIClient.getUserServices().postPmTaskUpdate(getUpdatePmTaskRequest);

        callTaskUpdate.enqueue(new Callback<GetUpdatePmTaskResponse>() {
            @Override
            public void onResponse(Call<GetUpdatePmTaskResponse> call, Response<GetUpdatePmTaskResponse> response) {
                updateProgress.dismiss();
                if (response.code() == 200) {
                    Toast.makeText(PmTaskActivity.this, "Task Updated", Toast.LENGTH_LONG).show();
                    finish();
                } else
                    Toast.makeText(PmTaskActivity.this, "Error: " + response.code(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<GetUpdatePmTaskResponse> call, Throwable t) {
                Toast.makeText(PmTaskActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                updateProgress.dismiss();
            }
        });
    }

}