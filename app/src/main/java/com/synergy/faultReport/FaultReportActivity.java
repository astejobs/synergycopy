package com.synergy.faultReport;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.synergy.APIClient;
import com.synergy.R;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

        Intent intent = getIntent();
        workSpaceid = intent.getStringExtra("workspaceId");
        initViews();
        callSpinerGenDep();
        callspinnerGenPriority();
        callSpinnerGenDivision();
        callSpinnerBuilding();
        callSpinnerFaultCat();
        callSpinnerMaintGrp();
        callGenLoaction();

/*
        datePickerEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        FaultReportActivity.this, FaultReportActivity.this, startYear, starthMonth, startDay);

            }
        });
*/
        deptListAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, genralDepList);
        deptListAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        departmentSpinner.setAdapter(deptListAdapter);
        departmentSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                departmentSpinner.setSelection(position);
                list p = (list) parent.getItemAtPosition(position);
                int i = p.id;
                Log.d(TAG, "onItemSelected: Dept  " + i);

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
                int idd = p.id;
                Log.d(TAG, "onItemSelected: prity id " + idd);
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
                int idd = p.id;
                Log.d(TAG, "onItemSelected: division  id " + idd);

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
                int idd = p.id;
                Log.d(TAG, "onItemSelected: building id " + idd);
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
                int idd = p.id;
                Log.d(TAG, "onItemSelected: Fault  id " + idd);
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
                int idd = p.id;
                Log.d(TAG, "onItemSelected: Maint id " + idd);
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
                int idd = p.id;
                Log.d(TAG, "onItemSelected :Location id " + idd);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }



    private void callGenLoaction() {

        Call<JsonArray> call = APIClient.getUserServices().getGenLocation();
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
                Toast.makeText(FaultReportActivity.this, "Error : " + response.message(), Toast.LENGTH_SHORT).show();
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

        Call<JsonArray> call = APIClient.getUserServices().getGenMaintGrp();
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
                Toast.makeText(FaultReportActivity.this, "Error : " + response.message(), Toast.LENGTH_SHORT).show();
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
        Call<JsonArray> call = APIClient.getUserServices().getGenFaultCat();
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                if (response.code() == 200) {
                    progressDialog.dismiss();

                    JsonArray jsonArrayofGenFaultCat = response.body();
                    Toast.makeText(FaultReportActivity.this, "Success", Toast.LENGTH_SHORT).show();
                    for (int i = 0; i < jsonArrayofGenFaultCat.size(); i++) {
                        String name = jsonArrayofGenFaultCat.get(i).getAsJsonObject().get("name").getAsString();
                        int id = jsonArrayofGenFaultCat.get(i).getAsJsonObject().get("id").getAsInt();
                        genralFaultCatList.add(new list(name, id));
                        faultCategorySpinner.setAdapter(faultCatAdapter);
                    }

                } else
                    progressDialog.dismiss();
                Toast.makeText(FaultReportActivity.this, "Error : " + response.message(), Toast.LENGTH_SHORT).show();
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
        Call<JsonArray> call = APIClient.getUserServices().getGenBuildings();
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                if (response.code() == 200) {
                    progressDialog.dismiss();

                    JsonArray jsonArrayofGenBuilding = response.body();
                    Toast.makeText(FaultReportActivity.this, "Success", Toast.LENGTH_SHORT).show();
                    for (int i = 0; i < jsonArrayofGenBuilding.size(); i++) {
                        String name = jsonArrayofGenBuilding.get(i).getAsJsonObject().get("name").getAsString();
                        int id = jsonArrayofGenBuilding.get(i).getAsJsonObject().get("id").getAsInt();
                        genralBuildingList.add(new list(name, id));
                        buildingSpinner.setAdapter(buildingAdapter);
                    }

                } else
                    progressDialog.dismiss();
                Toast.makeText(FaultReportActivity.this, "Error : " + response.message(), Toast.LENGTH_SHORT).show();
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
        Call<JsonArray> call = APIClient.getUserServices().getGenDivisions();
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                if (response.code() == 200) {
                    progressDialog.dismiss();

                    JsonArray jsonArrayofGenDiv = response.body();
                    Toast.makeText(FaultReportActivity.this, "Success", Toast.LENGTH_SHORT).show();
                    for (int i = 0; i < jsonArrayofGenDiv.size(); i++) {
                        String name = jsonArrayofGenDiv.get(i).getAsJsonObject().get("name").getAsString();
                        int id = jsonArrayofGenDiv.get(i).getAsJsonObject().get("id").getAsInt();
                        genralDivisionList.add(new list(name, id));
                        divisionSpinner.setAdapter(divisionAdapter);
                    }

                } else
                    progressDialog.dismiss();
                Toast.makeText(FaultReportActivity.this, "Error : " + response.message(), Toast.LENGTH_SHORT).show();
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

        Call<JsonArray> call = APIClient.getUserServices().getGenproirity();
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                if (response.code() == 200) {
                    progressDialog.dismiss();

                    JsonArray jsonArrayofGenPriorty = response.body();
                    Toast.makeText(FaultReportActivity.this, "Success", Toast.LENGTH_SHORT).show();
                    for (int i = 0; i < jsonArrayofGenPriorty.size(); i++) {
                        String name = jsonArrayofGenPriorty.get(i).getAsJsonObject().get("name").getAsString();
                        int id = jsonArrayofGenPriorty.get(i).getAsJsonObject().get("id").getAsInt();
                        genralPriorityList.add(new list(name, id));
                        prioritySpinner.setAdapter(priAdapter);

                    }

                } else
                    progressDialog.dismiss();
                Toast.makeText(FaultReportActivity.this, "Error : " + response.message(), Toast.LENGTH_SHORT).show();
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
        Call<JsonArray> call = APIClient.getUserServices().getGenDep(workSpaceid);
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                if (response.code() == 200) {
                    progressDialog.dismiss();

                    JsonArray jsonArrayofGenDep = response.body();
                    Toast.makeText(FaultReportActivity.this, "Success", Toast.LENGTH_SHORT).show();
                    for (int i = 0; i < jsonArrayofGenDep.size(); i++) {
                        String name = jsonArrayofGenDep.get(i).getAsJsonObject().get("name").getAsString();
                        int id = jsonArrayofGenDep.get(i).getAsJsonObject().get("id").getAsInt();
                        genralDepList.add(new list(name, id));
                        departmentSpinner.setAdapter(deptListAdapter);

                    }

                } else
                    progressDialog.dismiss();
                Toast.makeText(FaultReportActivity.this, "Error : " + response.message(), Toast.LENGTH_SHORT).show();
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

    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {


    }
}