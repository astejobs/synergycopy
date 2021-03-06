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
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.common.util.CollectionUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.synergy.APIClient;
import com.synergy.CheckInternet;
import com.synergy.Constants;
import com.synergy.LogoutClass;
import com.synergy.MainActivityLogin;
import com.synergy.MyBaseActivity;
import com.synergy.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import static com.synergy.MainActivityLogin.SHARED_PREFS;

public class PmTaskActivity extends MyBaseActivity implements DatePickerDialog.OnDateSetListener {

    private TextInputEditText taskNumberTextView;
    private TextInputEditText scheduleNumberTextView;
    private TextInputEditText buildingNameTextView;
    private TextInputEditText locationNameTextView;
    private TextInputEditText equipmentNameTextView;
    private TextInputEditText briefDescTextView;
    private TextInputEditText scheduleDateTextView;
    private TextInputEditText remarksTextView;
    private TextInputEditText nameTextView;
    private AutoCompleteTextView statusSpinner;
    private Button buttonUpdate;
    private TextInputEditText datePickerEdit;
    private final OvershootInterpolator interpolator = new OvershootInterpolator();
    private Float translationY = 600f;
    private TextInputEditText timePickerEdit;
    private int tHour, tMinute;
    private ProgressDialog mProgress;
    private ArrayAdapter<String> statusSpinnerAdapter;
    private List<String> statusList = new ArrayList<>();
    private ProgressDialog updateProgress;
    private long scheduleDate;
    private String roleTask;
    private int taskId;
    private String workspace;
    private String afterImage, beforeImage;
    private String source = "";
    private FloatingActionButton moreFab, uploadFab, checlistFab;
    private boolean isOpen;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View viewLayout = layoutInflater.inflate(R.layout.activity_pm_task, null, false);
        drawer.addView(viewLayout, 0);

        taskNumberTextView = findViewById(R.id.textViewTaskNumberPm);
        scheduleNumberTextView = findViewById(R.id.textViewScheduleNumberPm);
        scheduleNumberTextView.setMovementMethod(new ScrollingMovementMethod());
        nameTextView = findViewById(R.id.namePmTasks);
        toolbar.setTitle("Pm Task");
        setSupportActionBar(toolbar);
        remarksTextView = findViewById(R.id.remarks_pmTasks);
        buildingNameTextView = findViewById(R.id.textViewBuildingNumberPm);
        locationNameTextView = findViewById(R.id.textViewLocationNumberPm);
        equipmentNameTextView = findViewById(R.id.textViewEquipmentPm);
        briefDescTextView = findViewById(R.id.textViewBriefDescriptionPm);
        scheduleDateTextView = findViewById(R.id.textViewScheduleDatePm);
        datePickerEdit = findViewById(R.id.date_picker_pmtasksPm);
        timePickerEdit = findViewById(R.id.time_picker_pmtasks);
        buttonUpdate = findViewById(R.id.buttonUpdateTaskPm);
        buttonUpdate.setEnabled(false);
        statusSpinner = findViewById(R.id.spinner_status_pmtasks);
        moreFab = findViewById(R.id.more_pmtask);
        checlistFab = findViewById(R.id.checklist_pmtask);
        uploadFab = findViewById(R.id.image_pmtask);

        isOpen = false;

        checlistFab.setAlpha(0f);
        uploadFab.setAlpha(0f);
        checlistFab.setTranslationY(translationY);
        uploadFab.setTranslationY(translationY);


        taskNumberTextView.setEnabled(false);
        scheduleNumberTextView.setEnabled(false);
        buildingNameTextView.setEnabled(false);
        locationNameTextView.setEnabled(false);
        equipmentNameTextView.setEnabled(false);
        briefDescTextView.setEnabled(false);
        scheduleDateTextView.setEnabled(false);
        datePickerEdit.setEnabled(false);
        timePickerEdit.setEnabled(false);
        nameTextView.setEnabled(false);


        mProgress = new ProgressDialog(PmTaskActivity.this);
        mProgress.setMessage("Please wait...");
        mProgress.setCancelable(false);
        mProgress.setIndeterminate(true);

        updateProgress = new ProgressDialog(this);
        updateProgress.setTitle("Updating...");
        updateProgress.setCancelable(false);
        updateProgress.setIndeterminate(true);

        statusSpinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, statusList);
        statusSpinner.setAdapter(statusSpinnerAdapter);

        Intent intent = getIntent();
        String taskNumberString = intent.getStringExtra("taskNumber");
        taskId = intent.getIntExtra("taskId", 0);
        //taskId = 3;
        workspace = intent.getStringExtra("workspace");
        //String workspace = "CMMS-DEMO-112020-001";
        afterImage = intent.getStringExtra("afterImage");
        beforeImage = intent.getStringExtra("beforeImage");
        source = intent.getStringExtra("source");

        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "");
        roleTask = sharedPreferences.getString("role", "");

        getPmTask(taskNumberString, taskId, token);


        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateTaskMethod(taskId, token, workspace);
            }
        });

        /*checkListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent setIntent = new Intent(getApplicationContext(), CheckListActivity.class);
                setIntent.putExtra("taskId", taskId);
                startActivity(setIntent);
            }
        });*/

        moreFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isOpen) {
                    closeMenu();
                } else {
                    openMenu();
                }
            }
        });

    }


    private void updateTaskMethod(int taskId, String token, String workspace) {
        String timeString = timePickerEdit.getText().toString();
        String dateString = datePickerEdit.getText().toString();
        String status = statusSpinner.getText().toString();
        String remarksString = remarksTextView.getText().toString();

        String dateTimeString = dateString + timeString;

        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyyHH:mm");
        Date date = null;
        try {
            date = (Date) formatter.parse(dateTimeString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Long completedDateTime = null;
        if (statusSpinner.getText().equals("Completed")) {
            completedDateTime = date.getTime();
        }
        GetUpdatePmTaskRequest getUpdatePmTaskRequest = new GetUpdatePmTaskRequest(status, remarksString, completedDateTime, completedDateTime, taskId);

        if (!remarksTextView.getText().toString().isEmpty()) {
            updatePmTaskService(getUpdatePmTaskRequest, token, workspace);
        } else
            Toast.makeText(PmTaskActivity.this, "Please add remarks!", Toast.LENGTH_SHORT).show();
    }

    private void getPmTask(String taskNumberString, int taskId, String token) {
        mProgress.show();
        taskNumberTextView.setText(taskNumberString);

        Call<GetPmTaskItemsResponse> callPmTask = APIClient.getUserServices().getCallPmTask(String.valueOf(taskId), token);
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
                        String dateStr = null;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            date = Instant.ofEpochMilli((long) getPmTaskItemsResponse.getScheduleDate()).atZone(ZoneId.systemDefault()).toLocalDateTime();
                            dateStr = date.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
                        } else {
                            Calendar cl = Calendar.getInstance();
                            cl.setTimeInMillis(scheduleDate);
                            dateStr = "" + (cl.get(Calendar.DAY_OF_MONTH)) + "-" + (cl.get(Calendar.MONTH) + 1) + "-" + cl.get(Calendar.YEAR);
                        }
                        scheduleDateTextView.setText(dateStr);
                    }
                    Calendar cal = Calendar.getInstance(Locale.ENGLISH);
                    if (getPmTaskItemsResponse.getCompDate() != null && getPmTaskItemsResponse.getCompTime() != null) {
                        long compDate = getPmTaskItemsResponse.getCompDate();
                        cal.setTimeInMillis(compDate);
                        String date = String.valueOf(DateFormat.format("dd-MM-yyyy", cal));
                        datePickerEdit.setText(date);

                        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
                        String dateString = formatter.format(getPmTaskItemsResponse.getCompTime());
                        timePickerEdit.setText(dateString);

                    } else {
                        Date d = new Date();
                        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                        String currentDateTimeString = sdf.format(d);
                        timePickerEdit.setText(currentDateTimeString);
                        SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy");
                        datePickerEdit.setText(sdf1.format(new Date()));
                    }

                    statusList.add("Open");
                    statusList.add("Closed");
                    statusList.add("Completed");

                    if (getPmTaskItemsResponse.getStatus() != null) {

                        if (roleTask.equals("Technician")) {
                            statusList.add("Completed");
                        } else {
                            statusList.add("Closed");
                        }
                        statusList.add("Open");
                        if (!getPmTaskItemsResponse.getStatus().equals("OPEN")) {
                            statusList.add(getPmTaskItemsResponse.getStatus());
                        }
                        /*Set<String> set1 = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
                        set1.addAll(statusList);
                        statusList.retainAll(set1);*/

                        TreeSet<String> ts1 = new TreeSet<String>();
                        ts1.addAll(statusList);

                        statusList.clear();
                        statusList.addAll(ts1);

                    }
                    statusSpinner.setAdapter(statusSpinnerAdapter);
                    if (!getPmTaskItemsResponse.getStatus().equals("OPEN")) {
                        statusSpinner.setText(getPmTaskItemsResponse.getStatus(), true);
                    } else statusSpinner.setText("Open");
                    if (getPmTaskItemsResponse.getRemarks() != null) {
                        remarksTextView.setText(getPmTaskItemsResponse.getRemarks());
                    }

                    if (getPmTaskItemsResponse.getCompletedBy() != null) {
                        nameTextView.setText(getPmTaskItemsResponse.getCompletedBy());
                    }
                    if (!source.equals("search")) {
                        buttonUpdate.setEnabled(true);
                    }

                    //checkListButton.setEnabled(true);
                } else if (response.code() == 401) {
                    Toast.makeText(PmTaskActivity.this, Constants.ERROR_CODE_401_MESSAGE, Toast.LENGTH_SHORT).show();
                    LogoutClass logoutClass = new LogoutClass();
                    logoutClass.logout(PmTaskActivity.this);
                } else if (response.code() == 500) {
                    Toast.makeText(PmTaskActivity.this, Constants.ERROR_CODE_500_MESSAGE, Toast.LENGTH_SHORT).show();
                } else if (response.code() == 404) {
                    Toast.makeText(PmTaskActivity.this, Constants.ERROR_CODE_404_MESSAGE, Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(PmTaskActivity.this, "Error: " + response.code(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<GetPmTaskItemsResponse> call, Throwable t) {
                mProgress.dismiss();
                LogoutClass logoutClass = new LogoutClass();
                logoutClass.alertDialog("No tasks available! Please try again", PmTaskActivity.this);
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

    private void updatePmTaskService(GetUpdatePmTaskRequest getUpdatePmTaskRequest, String token, String workspace) {
        updateProgress.show();

        Call<GetUpdatePmTaskResponse> callTaskUpdate = APIClient.getUserServices().postPmTaskUpdate(getUpdatePmTaskRequest, token, roleTask, workspace);
        callTaskUpdate.enqueue(new Callback<GetUpdatePmTaskResponse>() {
            @Override
            public void onResponse(Call<GetUpdatePmTaskResponse> call, Response<GetUpdatePmTaskResponse> response) {
                updateProgress.dismiss();
                if (response.code() == 200) {
                    Toast.makeText(PmTaskActivity.this, "Task Updated", Toast.LENGTH_LONG).show();
                    if (roleTask.equals(Constants.ROLE_TECHNICIAN) && statusSpinner.getText().equals("Completed")) {
                        Intent intent = new Intent(getApplicationContext(), UploadTaskImageActivity.class);
                        intent.putExtra("workspace", workspace);
                        intent.putExtra("taskNumber", taskNumberTextView.getText().toString());
                        intent.putExtra("taskId", String.valueOf(taskId));
                        intent.putExtra("afterImage", afterImage);
                        intent.putExtra("beforeImage", beforeImage);
                        startActivity(intent);
                    }
                    finish();
                } else if (response.code() == 401) {
                    Toast.makeText(PmTaskActivity.this, Constants.ERROR_CODE_401_MESSAGE, Toast.LENGTH_SHORT).show();
                    LogoutClass logoutClass = new LogoutClass();
                    logoutClass.logout(PmTaskActivity.this);
                } else if (response.code() == 500) {
                    Toast.makeText(PmTaskActivity.this, Constants.ERROR_CODE_500_MESSAGE, Toast.LENGTH_SHORT).show();
                } else if (response.code() == 404) {
                    Toast.makeText(PmTaskActivity.this, Constants.ERROR_CODE_404_MESSAGE, Toast.LENGTH_SHORT).show();
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

    private void openMenu() {
        isOpen = !isOpen;
        moreFab.animate().setInterpolator(interpolator).rotation(0f).setDuration(300).start();
        uploadFab.animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start();
        checlistFab.animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start();

        uploadFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), UploadTaskImageActivity.class);
                intent.putExtra("workspace", workspace);
                intent.putExtra("taskNumber", taskNumberTextView.getText().toString());
                intent.putExtra("taskId", String.valueOf(taskId));
                intent.putExtra("afterImage", afterImage);
                intent.putExtra("beforeImage", beforeImage);
                startActivity(intent);
            }
        });

        checlistFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent setIntent = new Intent(getApplicationContext(), CheckListActivity.class);
                setIntent.putExtra("taskId", taskId);
                startActivity(setIntent);
            }
        });
    }

    private void closeMenu() {
        isOpen = !isOpen;
        moreFab.animate().setInterpolator(interpolator).rotation(0f).setDuration(300).start();
        uploadFab.animate().translationY(translationY).setInterpolator(interpolator).setDuration(300).start();
        checlistFab.animate().translationY(translationY).setInterpolator(interpolator).setDuration(300).start();
    }
}