package com.synergy.search;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.synergy.APIClient;
import com.synergy.R;
import com.synergy.faultReport.list;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.synergy.MainActivityLogin.SHARED_PREFS;

public class EditFaultReportActivity extends AppCompatActivity {


    private FloatingActionButton fab_main, fab_before, fab_after, fab_esc;
    private Boolean isMenuOpen = false;
    private final OvershootInterpolator interpolator = new OvershootInterpolator();
    private Float translationY = 100f;
    private static final String TAG = "SearchResponseAdapter";
    private String workspaceString = "";
    private String timeS;
    private String tokenGen;
    private String frId, buildingNameString, costCenterString, departmentString, divisionString, faultString, mainGrpString, priorityString, statusString;
    private int buildingId, unitId;
    private int tHour, tMinute;
    private Spinner statusSpinner, technicianSpinner, costCenterSpinner, mainGrpSpinner,
            faultCategorySpinner, divisionSpinner, locationSpinner, buildingSpinner, prioritySpinner, deptSpinner;
    private TextView timePickerResponse, timePickerStart, timePickerEnd, datePickerResponse, datePickerStart, datePickerEnd;
    private String requestorName,token;
    private String remarksString, startTimeString;
    private String locationString, locDesc, faultDetailString, locationName;
    private long startDateLong;
    TextView reqNameEditText;
    private EditText frIdEditText, observationEditText, faultDetailsEditText, locDescEditText,
            requestorNumberEditText, actionTakenEditText, diagnosisEditText, labourHoursEditText;
    private TextView equipmentTextView;
    private TextView dateAddedTextView;
    private TextView timeAddedTextView;
    private Button selectEquipmentButton;
    private Button updateFaultReportButton;
    private LinearLayout mlayout;
    private Integer buildingIdInt, deptIdInt, faultCatoryId, priorIdInt, mainGrpIdInt, costCenterIdInt, divisionIdInt, locationId;
    private Integer attendedByIdInt = null;

    String frid, workSpaceid;

    List<list> genralDepList = new ArrayList<list>();
    List<list> genralPriorityList = new ArrayList<list>();
    List<list> genralDivisionList = new ArrayList<list>();
    List<list> genralBuildingList = new ArrayList<list>();
    List<list> genralFaultCatList = new ArrayList<list>();
    List<list> genralFaultMaintGrp = new ArrayList<list>();
    List<list> genralLoaction = new ArrayList<list>();
    List<list> genralStatusList = new ArrayList<list>();
    List<list> genralTechnicalList = new ArrayList<list>();

    private ArrayAdapter<list> statusListAdapter;
    private ArrayAdapter<list> technicalListAdapter;
    private ArrayAdapter<list> deptListAdapter;
    private ArrayAdapter<list> priAdapter;
    private ArrayAdapter<list> divisionAdapter;
    private ArrayAdapter<list> buildingAdapter;
    private ArrayAdapter<list> faultCatAdapter;
    private ArrayAdapter<list> maintAdapter;
    private ArrayAdapter<list> locationAdapter;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_fault_report);
        progressDialog=new ProgressDialog(EditFaultReportActivity.this);
        initViews();
        initializeFab();
        datePicker();
        timePickerMethod();

       spinnerCalls();
        initviewsAndGetInitialData();
    }

    private void spinnerCalls() {

        progressDialog.setTitle("Loading...");
            Call<JsonArray> call = APIClient.getUserServices().getGenLocation(token);
            call.enqueue(new Callback<JsonArray>() {
                @Override
                public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                    if (response.code() == 200) {
                        progressDialog.dismiss();
                        JsonArray jsonArrayofGenLocation = response.body();
                        for (int i = 0; i < jsonArrayofGenLocation.size(); i++) {
                            String name = jsonArrayofGenLocation.get(i).getAsJsonObject().get("name").getAsString();
                            int id = jsonArrayofGenLocation.get(i).getAsJsonObject().get("id").getAsInt();
                            genralLoaction.add(new list(name, id));
                            locationSpinner.setAdapter(locationAdapter);
                        }

                    } else
                        progressDialog.dismiss();
                }

                @Override
                public void onFailure(Call<JsonArray> call, Throwable t) {
                    progressDialog.dismiss();
                    Toast.makeText(EditFaultReportActivity.this, "Failed : " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onFailure: " + t.getMessage());
                }
            });




            Call<JsonArray> call2 = APIClient.getUserServices().getGenMaintGrp(token);
            call2.enqueue(new Callback<JsonArray>() {
                @Override
                public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                    if (response.code() == 200) {
                        progressDialog.dismiss();
                        JsonArray jsonArrayofGenFaultMaintGrp = response.body();
                        for (int i = 0; i < jsonArrayofGenFaultMaintGrp.size(); i++) {
                            String name = jsonArrayofGenFaultMaintGrp.get(i).getAsJsonObject().get("name").getAsString();
                            int id = jsonArrayofGenFaultMaintGrp.get(i).getAsJsonObject().get("id").getAsInt();
                            genralFaultMaintGrp.add(new list(name, id));
                            mainGrpSpinner.setAdapter(maintAdapter);
                        }

                    } else
                        progressDialog.dismiss();
                }

                @Override
                public void onFailure(Call<JsonArray> call, Throwable t) {
                    progressDialog.dismiss();
                    Toast.makeText(EditFaultReportActivity.this, "Failed : " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onFailure: " + t.getMessage());
                }
            });



            Call<JsonArray> call3 = APIClient.getUserServices().getGenFaultCat(token);
            call3.enqueue(new Callback<JsonArray>() {
                @Override
                public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                    if (response.code() == 200) {
                        progressDialog.dismiss();

                        JsonArray jsonArrayofGenFaultCat = response.body();
                        for (int i = 0; i < jsonArrayofGenFaultCat.size(); i++) {
                            String name = jsonArrayofGenFaultCat.get(i).getAsJsonObject().get("name").getAsString();
                            int id = jsonArrayofGenFaultCat.get(i).getAsJsonObject().get("id").getAsInt();
                            genralFaultCatList.add(new list(name, id));
                            faultCategorySpinner.setAdapter(faultCatAdapter);
                        }

                    } else
                        progressDialog.dismiss();
                }

                @Override
                public void onFailure(Call<JsonArray> call, Throwable t) {
                    progressDialog.dismiss();
                    Toast.makeText(EditFaultReportActivity.this, "Failed : " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onFailure: " + t.getMessage());
                }
            });



            Call<JsonArray> call4 = APIClient.getUserServices().getGenBuildings(token);
            call4.enqueue(new Callback<JsonArray>() {
                @Override
                public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                    if (response.code() == 200) {
                        progressDialog.dismiss();

                        JsonArray jsonArrayofGenBuilding = response.body();
                        for (int i = 0; i < jsonArrayofGenBuilding.size(); i++) {
                            String name = jsonArrayofGenBuilding.get(i).getAsJsonObject().get("name").getAsString();
                            int id = jsonArrayofGenBuilding.get(i).getAsJsonObject().get("id").getAsInt();
                            genralBuildingList.add(new list(name, id));
                            buildingSpinner.setAdapter(buildingAdapter);
                        }

                    } else
                        progressDialog.dismiss();
                }

                @Override
                public void onFailure(Call<JsonArray> call, Throwable t) {
                    progressDialog.dismiss();
                    Toast.makeText(EditFaultReportActivity.this, "Failed : " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onFailure: " + t.getMessage());
                }
            });



        /*    Call<JsonArray> callTechAllGet = APIClient.getUserServices().technicianCall(token, workspaceString);
            callTechAllGet.enqueue(new Callback<JsonArray>() {
                @Override
                public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {

                    JsonArray jsonarraytech = response.body();

                    for (int i = 0; i < jsonarraytech.size(); i++) {
                        String name = jsonarraytech.get(i).getAsJsonObject().get("name").getAsString();
                        int id = jsonarraytech.get(i).getAsJsonObject().get("id").getAsInt();
                        genralTechnicalList.add(new list(name, id));
                        technicianSpinner.setAdapter(technicalListAdapter);
                    }
                    technicianSpinner.setAdapter(technicalListAdapter);


                }

                @Override
                public void onFailure(Call<JsonArray> call, Throwable t) {
                    progressDialog.dismiss();
                    Toast.makeText(EditFaultReportActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });*/



            //http://192.168.1.106:8080/lsme/api/divisions/1
            Call<JsonArray> call5 = APIClient.getUserServices().getGenDivisions(token);
            call5.enqueue(new Callback<JsonArray>() {
                @Override
                public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                    if (response.code() == 200) {
                        progressDialog.dismiss();

                        JsonArray jsonArrayofGenDiv = response.body();
                        for (int i = 0; i < jsonArrayofGenDiv.size(); i++) {
                            String name = jsonArrayofGenDiv.get(i).getAsJsonObject().get("name").getAsString();
                            int id = jsonArrayofGenDiv.get(i).getAsJsonObject().get("id").getAsInt();
                            genralDivisionList.add(new list(name, id));

                        } divisionSpinner.setAdapter(divisionAdapter);

                    } else
                        progressDialog.dismiss();
                }

                @Override
                public void onFailure(Call<JsonArray> call, Throwable t) {
                    progressDialog.dismiss();
                    Toast.makeText(EditFaultReportActivity.this, "Failed : " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onFailure: " + t.getMessage());
                }
            });



            Call<JsonArray> call6 = APIClient.getUserServices().getGenproirity(token);
            call6.enqueue(new Callback<JsonArray>() {
                @Override
                public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                    if (response.code() == 200) {
                        progressDialog.dismiss();

                        JsonArray jsonArrayofGenPriorty = response.body();
                        for (int i = 0; i < jsonArrayofGenPriorty.size(); i++) {
                            String name = jsonArrayofGenPriorty.get(i).getAsJsonObject().get("name").getAsString();
                            int id = jsonArrayofGenPriorty.get(i).getAsJsonObject().get("id").getAsInt();
                            genralPriorityList.add(new list(name, id));
                            prioritySpinner.setAdapter(priAdapter);

                        }

                    } else
                        progressDialog.dismiss();
                }

                @Override
                public void onFailure(Call<JsonArray> call, Throwable t) {
                    progressDialog.dismiss();
                    Toast.makeText(EditFaultReportActivity.this, "Failed : " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onFailure: " + t.getMessage());
                }
            });





            progressDialog.show();
            Call<JsonArray> call7 = APIClient.getUserServices().getGenDep(workSpaceid,token);
            call7.enqueue(new Callback<JsonArray>() {
                @Override
                public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                    if (response.code() == 200) {
                        progressDialog.dismiss();

                        JsonArray jsonArrayofGenDep = response.body();
                        for (int i = 0; i < jsonArrayofGenDep.size(); i++) {
                            String name = jsonArrayofGenDep.get(i).getAsJsonObject().get("name").getAsString();
                            int id = jsonArrayofGenDep.get(i).getAsJsonObject().get("id").getAsInt();
                            genralDepList.add(new list(name, id));
                            deptSpinner.setAdapter(deptListAdapter);

                        }

                    } else
                        progressDialog.dismiss();
                }

                @Override
                public void onFailure(Call<JsonArray> call, Throwable t) {
                    progressDialog.dismiss();
                    Toast.makeText(EditFaultReportActivity.this, "Failed : " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onFailure: " + t.getMessage());
                }
            });

        }


    private void initViews() {

        updateFaultReportButton = findViewById(R.id.updateFaultReportButton);
        diagnosisEditText = findViewById(R.id.diagnosis);
        actionTakenEditText = findViewById(R.id.actionTaken);
        statusSpinner = findViewById(R.id.statusSpinner);
        technicianSpinner = findViewById(R.id.technicianSpinner);
        costCenterSpinner = findViewById(R.id.costCenter);
        mainGrpSpinner = findViewById(R.id.mainGrp);
        faultCategorySpinner = findViewById(R.id.faultCategory);
        divisionSpinner = findViewById(R.id.divisionNumberSpinner);
        locationSpinner = findViewById(R.id.unitNumber);
        buildingSpinner = findViewById(R.id.buildingNumber);
        prioritySpinner = findViewById(R.id.priorityNumber);
        deptSpinner = findViewById(R.id.dept_number);
        timePickerResponse = findViewById(R.id.timeResponse);
        timePickerEnd = findViewById(R.id.timeEnd);
        timePickerStart = findViewById(R.id.timeStart);
        datePickerEnd = findViewById(R.id.dateEnd);
        datePickerResponse = findViewById(R.id.dateResponse);
        datePickerStart = findViewById(R.id.dateStart);
        requestorNumberEditText = findViewById(R.id.contactNumber);
        frIdEditText = findViewById(R.id.frIdEditText);
        TextView jobTimeTextView = findViewById(R.id.timeJob);
        locDescEditText = findViewById(R.id.locationDescrip);
        labourHoursEditText = findViewById(R.id.labourHours);
        faultDetailsEditText = findViewById(R.id.faultDetails);
        reqNameEditText = findViewById(R.id.reqNameEditTextEditFault);
        equipmentTextView = findViewById(R.id.equipmentTextViewEditFault);
        dateAddedTextView = findViewById(R.id.dateAdded);
        timeAddedTextView = findViewById(R.id.timeAdded);
        Button plusbtn = findViewById(R.id.plusButton);
        Button deletebtn = findViewById(R.id.deleteButton);
        mlayout = findViewById(R.id.layout_remarks);
        TextView textView = new TextView(this);
        observationEditText = findViewById(R.id.observation);
        fab_main = findViewById(R.id.images_id);
        fab_before = findViewById(R.id.before_id);
        fab_after = findViewById(R.id.after_id);
        fab_esc = findViewById(R.id.esc_id);
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        token=sharedPreferences.getString("token", "");

        spinnerSet();
    }

    private void initviewsAndGetInitialData() {
        Intent i = getIntent();
        frid = i.getStringExtra("frId");
        workSpaceid = i.getStringExtra("workspaceId");
        Call<JsonObject> call = APIClient.getUserServices().getEditfaultDetails(frid, workSpaceid,token);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.code() == 200) {
                    Toast.makeText(EditFaultReportActivity.this, "success", Toast.LENGTH_SHORT).show();
                    JsonObject jsonObject=response.body();
                    frIdEditText.setText(jsonObject.get("frId").getAsString());
                    reqNameEditText.setText(jsonObject.get("requestorName").getAsString());
                    requestorNumberEditText.setText(jsonObject.get("requestorContactNo").getAsString());

                   String lname= jsonObject.get("location").getAsJsonObject().get("name").getAsString();
                   if (genralLoaction.equals(lname)){
                       locationSpinner.setSelection(Integer.parseInt(lname));
                   }
                    String buildingname= jsonObject.get("location").getAsJsonObject().get("name").getAsString();
                    if (genralBuildingList.equals(buildingname)){
                        buildingSpinner.setSelection(Integer.parseInt(buildingname));
                    }
                    String divisionname= jsonObject.get("location").getAsJsonObject().get("name").getAsString();
                    if (genralDivisionList.contains(divisionname)){
                        divisionSpinner.setSelection(Integer.parseInt(divisionname));
                    }
                    String deptname= jsonObject.get("location").getAsJsonObject().get("name").getAsString();
                    if (genralDepList.equals(deptname)){
                        deptSpinner.setSelection(Integer.parseInt(deptname));
                    }
                    String prirityname= jsonObject.get("location").getAsJsonObject().get("name").getAsString();
                    if (genralPriorityList.equals(prirityname)){
                        prioritySpinner.setSelection(Integer.parseInt(prirityname));
                    }
                    String maintname= jsonObject.get("location").getAsJsonObject().get("name").getAsString();
                    if (genralFaultMaintGrp.equals(maintname)){
                        mainGrpSpinner.setSelection(Integer.parseInt(maintname));
                    }
                    String faulname= jsonObject.get("location").getAsJsonObject().get("name").getAsString();
                    if (genralFaultCatList.equals(faulname)){
                        faultCategorySpinner.setSelection(Integer.parseInt(faulname));
                    }


                  /*  datePickerEnd.setText(jsonObject.get("endDate").getAsString());
                    datePickerStart.setText(jsonObject.get("startDate").getAsString());
                    timePickerEnd.setText(jsonObject.get("endTime").getAsString());
                    timePickerStart.setText(jsonObject.get("startTime").getAsString());
                    locDescEditText.setText(jsonObject.get("location").getAsJsonObject().get("description").getAsString());
                    faultDetailsEditText.setText(jsonObject.get("faultCategory").getAsJsonObject().get("description").getAsString());
                    labourHoursEditText.setText(jsonObject.get("labourHrs").getAsString());
*/

                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(EditFaultReportActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                Log.d("TAG", "onFailure: " + t.getMessage() + t.getCause());

            }
        });
    }

    private void spinnerSet() {

        deptListAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, genralDepList);
        deptListAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        deptSpinner.setAdapter(deptListAdapter);

        divisionAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, genralDivisionList);
        divisionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        divisionSpinner.setAdapter(divisionAdapter);

        buildingAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, genralBuildingList);
        buildingAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        buildingSpinner.setAdapter(buildingAdapter);

        locationAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, genralLoaction);
        locationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        locationSpinner.setAdapter(locationAdapter);

        maintAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, genralFaultMaintGrp);
        maintAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mainGrpSpinner.setAdapter(maintAdapter);

        priAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, genralPriorityList);
        priAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        prioritySpinner.setAdapter(priAdapter);

        faultCatAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, genralFaultCatList);
        faultCatAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        faultCategorySpinner.setAdapter(faultCatAdapter);

        technicalListAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, genralTechnicalList);
        technicalListAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        technicianSpinner.setAdapter(technicalListAdapter);

        statusListAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, genralStatusList);
        statusListAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statusSpinner.setAdapter(statusListAdapter);




    }
    private void datePicker() {

        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);

        View.OnClickListener showDatePicker = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerDialog datePickerDialog = new DatePickerDialog(EditFaultReportActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                        Date date = new Date(year - 1900, monthOfYear, dayOfMonth);
                        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                        String cdate = formatter.format(date);

                        if (v.getId() == R.id.dateStart) //id of your StartDate button
                        {
                            datePickerStart.setText(cdate);
                        }
                        if (v.getId() == R.id.dateEnd) {
                            datePickerEnd.setText(cdate);
                        }
                        if (v.getId() == R.id.dateResponse) {
                            datePickerResponse.setText(cdate);
                        }

                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        };

        datePickerStart.setOnClickListener(showDatePicker);
        datePickerResponse.setOnClickListener(showDatePicker);
        datePickerEnd.setOnClickListener(showDatePicker);
    }
    public void timePickerMethod() {

        View.OnClickListener showTimePicker = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        EditFaultReportActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                                tHour = i;
                                tMinute = i1;


                                Calendar calendar = Calendar.getInstance();
                                calendar.set(0, 0, 0, tHour,tMinute);
                                timeS = String.valueOf(DateFormat.format("HH:mm", calendar));

                                if (view.getId() == R.id.timeStart) {
                                    timePickerStart.setText(timeS);
                                }
                                if (view.getId() == R.id.timeEnd) {
                                    timePickerEnd.setText(timeS);
                                }
                                if (view.getId() == R.id.timeResponse) {
                                    timePickerResponse.setText(timeS);
                                }


                            }
                        }, 12, 0, true
                );
                timePickerDialog.updateTime(tHour, tMinute);
                timePickerDialog.show();

            }
        };
        timePickerEnd.setOnClickListener(showTimePicker);
        timePickerStart.setOnClickListener(showTimePicker);
        timePickerResponse.setOnClickListener(showTimePicker);
    }


    private void initializeFab() {

        fab_before.setAlpha(0f);
        fab_after.setAlpha(0f);
        fab_esc.setAlpha(0f);

        fab_before.setTranslationY(translationY);
        fab_after.setTranslationY(translationY);
        fab_esc.setTranslationY(translationY);


        fab_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isMenuOpen) {
                    closeMenu();
                } else {
                    openMenu();
                }

            }
        });

        fab_before.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* Intent intent = new Intent(EditFaultReportActivity.this, BeforePictureActivity.class);
                intent.putExtra("token", tokenGen);
                intent.putExtra("workspace", workspaceString);
                intent.putExtra("frId", frId);
                intent.putExtra("value", "Before");
                intent.putExtra("user", user);
                startActivity(intent);*/

            }
        });
        fab_after.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* Intent intent = new Intent(EditFaultReportActivity.this, BeforePictureActivity.class);
                intent.putExtra("token", tokenGen);
                intent.putExtra("workspace", workspaceString);
                intent.putExtra("frId", frId);
                intent.putExtra("value", "After");
                startActivity(intent);*/
//
            }
        });
        fab_esc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeMenu();
            }
        });

    }

    private void openMenu() {
        isMenuOpen = !isMenuOpen;
        fab_main.animate().setInterpolator(interpolator).rotation(45f).setDuration(300).start();
        fab_esc.animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start();
        fab_before.animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start();
        fab_after.animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start();
    }

    private void closeMenu() {
        isMenuOpen = !isMenuOpen;
        fab_main.animate().setInterpolator(interpolator).rotation(0f).setDuration(300).start();
        fab_esc.animate().translationY(translationY).alpha(0f).setInterpolator(interpolator).setDuration(300).start();
        fab_before.animate().translationY(translationY).alpha(0f).setInterpolator(interpolator).setDuration(300).start();
        fab_after.animate().translationY(translationY).alpha(0f).setInterpolator(interpolator).setDuration(300).start();
    }
}