package com.synergy.FaultReport;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.synergy.APIClient;
import com.synergy.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.synergy.MainActivityLogin.SHARED_PREFS;

public class FaultReportActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    private static final String TAG = "";
    String workSpaceid;
    List<list> genralDepList = new ArrayList<list>();
    List<list> genralPriorityList = new ArrayList<list>();
    List<list> genralDivisionList = new ArrayList<list>();
    List<list> genralBuildingList = new ArrayList<list>();
    List<list> genralFaultCatList = new ArrayList<list>();
    List<list> genralFaultMaintGrp = new ArrayList<list>();
    List<list> genralLoaction = new ArrayList<list>();
    ProgressDialog progressDialog;


    private EditText requestorNameEditText, contactNumberEditText, locationDescriptionEditText, faultDescriptionEditText;
    private Spinner departmentSpinner, buildingSpinner, locationSpinner, prioritySpinner, divisionSpinner, faultCategorySpinner, selectMaintenanceSpinner;
    private Button buttonCreateFaultReport;
    private TextView datePickerEdit, timePickerEdit;

    private String requestorName, locDesc, faultDesc,token;
    String contactNo;
    String dateinStr, timeStr;
    int depId, locId, buildId, maintId, priroityId, faultId, divisionid;

    private ArrayAdapter<list> deptListAdapter;
    private ArrayAdapter<list> priAdapter;
    private ArrayAdapter<list> divisionAdapter;
    private ArrayAdapter<list> buildingAdapter;
    private ArrayAdapter<list> faultCatAdapter;
    private ArrayAdapter<list> maintAdapter;
    private ArrayAdapter<list> locationAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fault_report);
        progressDialog = new ProgressDialog(FaultReportActivity.this);
        progressDialog.setTitle("Loading");
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        token=sharedPreferences.getString("token", "");


        Intent intent = getIntent();
        workSpaceid = intent.getStringExtra("workspaceId");
        initViews();
        callSpinerGenDep();
        callspinnerGenPriority();
        callSpinnerGenDivision();
        callSpinnerBuilding();
        callSpinnerFaultCat();
        callSpinnerMaintGrp();


        deptListAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, genralDepList);
        deptListAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        departmentSpinner.setAdapter(deptListAdapter);
        departmentSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                departmentSpinner.setSelection(position);
                list p = (list) parent.getItemAtPosition(position);
                depId = p.id;


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        priAdapter = new ArrayAdapter<list>(this, android.R.layout.simple_spinner_dropdown_item, genralPriorityList);
        priAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        prioritySpinner.setAdapter(priAdapter);
        prioritySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                prioritySpinner.setSelection(position);
                list p = (list) parent.getItemAtPosition(position);
                priroityId = p.id;
                Log.d(TAG, "onItemSelected: prity id " + priroityId);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        divisionAdapter = new ArrayAdapter<list>(this, android.R.layout.simple_spinner_dropdown_item, genralDivisionList);
        divisionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        divisionSpinner.setAdapter(divisionAdapter);
        divisionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                list p = (list) parent.getItemAtPosition(position);
                divisionid = p.id;
                Log.d(TAG, "onItemSelected: division  id " + divisionid);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        buildingAdapter = new ArrayAdapter<list>(this, android.R.layout.simple_spinner_dropdown_item, genralBuildingList);
        buildingAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        buildingSpinner.setAdapter(buildingAdapter);
        buildingSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                list p = (list) parent.getItemAtPosition(position);
                buildId = p.id;
                callGenLoaction(buildId);
                Log.d(TAG, "onItemSelected: building id " + buildId);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        faultCatAdapter = new ArrayAdapter<list>(this, android.R.layout.simple_spinner_dropdown_item, genralFaultCatList);
        faultCatAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        faultCategorySpinner.setAdapter(faultCatAdapter);
        faultCategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                list p = (list) parent.getItemAtPosition(position);
                faultId = p.id;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        maintAdapter = new ArrayAdapter<list>(this, android.R.layout.simple_spinner_dropdown_item, genralFaultMaintGrp);
        maintAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        selectMaintenanceSpinner.setAdapter(maintAdapter);
        selectMaintenanceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                list p = (list) parent.getItemAtPosition(position);
                maintId = p.id;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        locationAdapter = new ArrayAdapter<list>(this, android.R.layout.simple_spinner_dropdown_item, genralLoaction);
        locationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        locationSpinner.setAdapter(locationAdapter);
        locationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                list p = (list) parent.getItemAtPosition(position);
                locId = p.id;

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

    }

    private void showDatePickerDailog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    private void callGenLoaction(int buildId) {

        Call<JsonArray> call = APIClient.getUserServices().getGenLocation(token,buildId);
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
                Toast.makeText(FaultReportActivity.this, "Failed : " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    private void callSpinnerMaintGrp() {

        Call<JsonArray> call = APIClient.getUserServices().getGenMaintGrp(token);
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                if (response.code() == 200) {
                    progressDialog.dismiss();
                    JsonArray jsonArrayofGenFaultMaintGrp = response.body();
                    for (int i = 0; i < jsonArrayofGenFaultMaintGrp.size(); i++) {
                        String name = jsonArrayofGenFaultMaintGrp.get(i).getAsJsonObject().get("name").getAsString();
                        int id = jsonArrayofGenFaultMaintGrp.get(i).getAsJsonObject().get("id").getAsInt();
                        genralFaultMaintGrp.add(new list(name, id));
                        selectMaintenanceSpinner.setAdapter(maintAdapter);
                    }

                } else
                    progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(FaultReportActivity.this, "Failed : " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    private void callSpinnerFaultCat() {
        Call<JsonArray> call = APIClient.getUserServices().getGenFaultCat(token);
        call.enqueue(new Callback<JsonArray>() {
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
                Toast.makeText(FaultReportActivity.this, "Failed : " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    private void callSpinnerBuilding() {
        Call<JsonArray> call = APIClient.getUserServices().getGenBuildings(token);
        call.enqueue(new Callback<JsonArray>() {
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
                Toast.makeText(FaultReportActivity.this, "Failed : " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    private void callSpinnerGenDivision() {
        //http://192.168.1.106:8080/lsme/api/divisions/1
        Call<JsonArray> call = APIClient.getUserServices().getGenDivisions(token);
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                if (response.code() == 200) {
                    progressDialog.dismiss();

                    JsonArray jsonArrayofGenDiv = response.body();
                    for (int i = 0; i < jsonArrayofGenDiv.size(); i++) {
                        String name = jsonArrayofGenDiv.get(i).getAsJsonObject().get("name").getAsString();
                        int id = jsonArrayofGenDiv.get(i).getAsJsonObject().get("id").getAsInt();
                        genralDivisionList.add(new list(name, id));
                        divisionSpinner.setAdapter(divisionAdapter);
                    }

                } else
                    progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(FaultReportActivity.this, "Failed : " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    private void callspinnerGenPriority() {

        Call<JsonArray> call = APIClient.getUserServices().getGenproirity(token);
        call.enqueue(new Callback<JsonArray>() {
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
                Toast.makeText(FaultReportActivity.this, "Failed : " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });

    }


    private void callSpinerGenDep() {
        progressDialog.show();
        Call<JsonArray> call = APIClient.getUserServices().getGenDep(workSpaceid,token);
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                if (response.code() == 200) {
                    progressDialog.dismiss();

                    JsonArray jsonArrayofGenDep = response.body();
                    for (int i = 0; i < jsonArrayofGenDep.size(); i++) {
                        String name = jsonArrayofGenDep.get(i).getAsJsonObject().get("name").getAsString();
                        int id = jsonArrayofGenDep.get(i).getAsJsonObject().get("id").getAsInt();
                        genralDepList.add(new list(name, id));
                        departmentSpinner.setAdapter(deptListAdapter);

                    }

                } else
                    progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(FaultReportActivity.this, "Failed : " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });

    }

    private void initViews() {

        requestorNameEditText = findViewById(R.id.requestorNameEditText);
        contactNumberEditText = findViewById(R.id.contactNumber_fault);
        locationDescriptionEditText = findViewById(R.id.LocationDescriptionEditText);
        faultDescriptionEditText = findViewById(R.id.FaultDescriptionEditText);
        departmentSpinner = findViewById(R.id.departmentSpinner);
        buildingSpinner = findViewById(R.id.buildingSpinner);
        locationSpinner = findViewById(R.id.locationSpinner);
        prioritySpinner = findViewById(R.id.prioritySpinner);
        divisionSpinner = findViewById(R.id.divisionSpinner);
        faultCategorySpinner = findViewById(R.id.faultCategorySpinner);
        selectMaintenanceSpinner = findViewById(R.id.selectMaintenanceSpinner);
        datePickerEdit = findViewById(R.id.datePickerFault);
        timePickerEdit = findViewById(R.id.timePickerFault);
        buttonCreateFaultReport = findViewById(R.id.buttonCreateFaultReport);

        genralDepList.add(new list("Select dept", 0));
        genralPriorityList.add(new list("Select Priority", 0));
        genralDivisionList.add(new list("Select Division", 0));
        genralBuildingList.add(new list("Select Building", 0));
        genralFaultCatList.add(new list("Select Fault Categories", 0));
        genralFaultMaintGrp.add(new list("Select Maintainence", 0));
        genralLoaction.add(new list("Select Location", 0));

        datePickerEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDailog();

            }
        });


        timePickerEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i = 0, i1 = 0;
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        FaultReportActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                                i = i;
                                i1 = i1;
                                Calendar calendar = Calendar.getInstance();
                                calendar.set(0, 0, 0, i, i1);
                                timePickerEdit.setText(DateFormat.format("HH:mm", calendar));
                            }
                        }, 12, 0, true
                );
                timePickerDialog.updateTime(i, i1);
                timePickerDialog.show();
            }
        });

        buttonCreateFaultReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                try {
                    createFaultReport();
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    private void createFaultReport() throws ParseException {
        timeStr = timePickerEdit.getText().toString();
        dateinStr = datePickerEdit.getText().toString();
        requestorName = requestorNameEditText.getText().toString();
        faultDesc = faultDescriptionEditText.getText().toString();
        locDesc = locationDescriptionEditText.getText().toString();
        contactNo = contactNumberEditText.getText().toString();
        long dateStr = 0;
        SimpleDateFormat f = new SimpleDateFormat("dd-MM-yyyy");

        Date d = f.parse(dateinStr);
        assert d != null;
        dateStr = d.getTime();

        if (TextUtils.isEmpty(contactNumberEditText.getText())
                || buildId == 0 || locId == 0 || maintId == 0 ||
                priroityId == 0 || depId == 0 || faultId == 0 ||
                TextUtils.isEmpty(locationDescriptionEditText.getText())
                || TextUtils.isEmpty(timePickerEdit.getText()) ||
                TextUtils.isEmpty(datePickerEdit.getText())
                || TextUtils.isEmpty(requestorNameEditText.getText())) {
            Toast.makeText(this, "Please Select All Feilds", Toast.LENGTH_SHORT).show();
        } else if (contactNo.length() < 8) {
            Toast.makeText(this, "Contact Number not Valid", Toast.LENGTH_SHORT).show();
        } else
           // Log.d(TAG, "createFaultReport: " + timePickerEdit.getText().toString());
        {
            Location location = new Location();
            location.setId(locId);
            Building building = new Building();
            building.setId(buildId);
            Priority priority = new Priority();
            priority.setId(priroityId);
            MaintGrp maintGrp = new MaintGrp();
            maintGrp.setId(priroityId);
            FaultCategory faultCategory = new FaultCategory();
            faultCategory.setId(faultId);
            Department department = new Department();
            department.setId(depId);
            Division division = new Division();
            division.setId(divisionid);


            CreateFaultRequestPojo createFaultRequestPojo = new CreateFaultRequestPojo(requestorName, timeStr,
                    building, location, contactNo, dateStr, priority, maintGrp
                    , faultCategory, department, faultDesc, locDesc, division);

            Call<JsonObject> call = APIClient.getUserServices().createFault(createFaultRequestPojo, workSpaceid,token);
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    if (response.code() == 200) {
                        JsonObject jo=response.body();
                        String frid=jo.get("frId").getAsString();
                        Intent intent = new Intent(FaultReportActivity.this, BeforeImage.class);
                        intent.putExtra("frId",frid);
                        intent.putExtra("value", "Before");
                        intent.putExtra("workspace",workSpaceid);
                        intent.putExtra("token",token);
                        startActivity(intent);
                        Toast.makeText(FaultReportActivity.this, "Fault Report Created Successfully",
                                Toast.LENGTH_SHORT).show();
                        finish();
                    } else
                        Toast.makeText(FaultReportActivity.this, "Error : " + response.code(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {

                    Toast.makeText(FaultReportActivity.this, "Failed : " + t.getMessage(), Toast.LENGTH_LONG).show();
                    Log.d(TAG, "onFailure:  " + t.getCause());
                    Log.d(TAG, "onFailure: " + t.getMessage());
                }
            });
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Date date = new Date(year - 1900, month, dayOfMonth);
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        String cdate = formatter.format(date);
        datePickerEdit.setText(cdate);


    }
}