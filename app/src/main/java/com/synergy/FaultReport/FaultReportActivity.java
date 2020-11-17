package com.synergy.FaultReport;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.synergy.APIClient;
import com.synergy.MainActivityLogin;
import com.synergy.R;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.synergy.MainActivityLogin.SHARED_PREFS;

public class FaultReportActivity extends AppCompatActivity {

    private static final String TAG = "";
    private String workSpaceid, role;
    private List<list> genralDepList = new ArrayList<list>();
    private List<list> genralPriorityList = new ArrayList<list>();
    private List<list> genralDivisionList = new ArrayList<list>();
    private List<list> genralBuildingList = new ArrayList<list>();
    private List<list> genralFaultCatList = new ArrayList<list>();
    private List<list> genralFaultMaintGrp = new ArrayList<list>();
    private List<list> genralLoaction = new ArrayList<list>();
    private List<list> genralEquipment = new ArrayList<list>();
    private List<list> genraltechlist = new ArrayList<list>();

    private ProgressDialog progressDialog;


    private EditText requestorNameEditText, remarksEt, contactNumberEditText, locationDescriptionEditText, faultDescriptionEditText;
    private Spinner departmentSpinner, buildingSpinner, equipmentSpinner, locationSpinner, prioritySpinner,
            divisionSpinner, faultCategorySpinner,
            selectMaintenanceSpinner; //spinnerTechnician;

    private Button buttonCreateFaultReport, selectTech;
    private TextView datePickerEdit, timePickerEdit, techTv;

    private String requestorName, locDesc, faultDesc, token;
    private String contactNo;
    private int depId, locId, buildId, maintId, priroityId, faultId, divisionid, equipId, techId;

    private ArrayAdapter<list> deptListAdapter;
    private ArrayAdapter<list> priAdapter;
    private ArrayAdapter<list> divisionAdapter;
    private ArrayAdapter<list> buildingAdapter;
    private ArrayAdapter<list> faultCatAdapter;
    private ArrayAdapter<list> maintAdapter;
    private ArrayAdapter<list> locationAdapter;
    private ArrayAdapter<list> equipmentAdapter;
    //  private ArrayAdapter<list> technicianAdapter;
    Toolbar toolbar;


    boolean[] checkedItems;
    ArrayList<Integer> mUserItems = new ArrayList<>();
    List<String> stockList = new ArrayList<>();
    String[] listItems;
    List attendedByIdsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fault_report);
        toolbar = findViewById(R.id.toolbar_fault);
        setSupportActionBar(toolbar);
        attendedByIdsList = new ArrayList();


        progressDialog = new ProgressDialog(FaultReportActivity.this);
        progressDialog.setTitle("Loading");

        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        token = sharedPreferences.getString("token", "");
        role = sharedPreferences.getString("role", "");
        Intent intent = getIntent();
        workSpaceid = intent.getStringExtra("workspaceId");

        initViews();
        callSpinerGenDep();
        callspinnerGenPriority();
        callSpinnerGenDivision();
        callSpinnerBuilding();
        callSpinnerFaultCat();
        callSpinnerMaintGrp();
        callTechSpinner();
        selectTech.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(FaultReportActivity.this, "dialog", Toast.LENGTH_SHORT).show();
                AlertDialog.Builder mbuilder = new AlertDialog.Builder(FaultReportActivity.this);
                mbuilder.setTitle("Select Technicians");
                mbuilder.setMultiChoiceItems(listItems, checkedItems, (dialog, position, isChecked) -> {
                    if (isChecked) {
                        if (!mUserItems.contains(position)) {
                            mUserItems.add(position);
                        }
                    } else if (mUserItems.contains(position)) {
                        mUserItems.remove(position);

                    }
                });
                mbuilder.setCancelable(false);
                mbuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String item = "";
                        for (int i = 0; i < mUserItems.size(); i++) {
                            item = item + listItems[mUserItems.get(i)];
                            if (i != mUserItems.size() - 1) {
                                item = item + ", ";
                            }
                        }
                        techTv.setText(item);
                    }
                });
                mbuilder.show();
                mbuilder.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });


            }

        });

/*

        technicianAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, genraltechlist);
        technicianAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTechnician.setAdapter(technicianAdapter);
        spinnerTechnician.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinnerTechnician.setSelection(position);
                list p = (list) parent.getItemAtPosition(position);
                techId = p.id;


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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
        equipmentAdapter = new ArrayAdapter<list>(this, android.R.layout.simple_spinner_dropdown_item, genralEquipment);
        equipmentAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        equipmentSpinner.setAdapter(equipmentAdapter);
        equipmentSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                list p = (list) parent.getItemAtPosition(position);
                equipId = p.id;
                Log.d(TAG, "onItemSelected: equpid" + equipId);

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
                callSpinnerEquipment(locId);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

    }

    private void callTechSpinner() {


        Call<JsonArray> jsonArrayCall = APIClient.getUserServices().getTechnicianCall(token, workSpaceid);
        jsonArrayCall.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                if (response.code() == 200) {
                    JsonArray jsonArray = response.body();
                    for (int i = 0; i < jsonArray.size(); i++) {
                        String eqName = jsonArray.get(i).getAsJsonObject().get("name").getAsString();
                        int eqId = jsonArray.get(i).getAsJsonObject().get("id").getAsInt();
                        genraltechlist.add(new list(eqName, eqId));
                        //   spinnerTechnician.setAdapter(technicianAdapter);
                        stockList.add(eqName);
                    }
                    Log.d(TAG, "onResponse: geb" + genraltechlist);
                    listItems = new String[stockList.size()];
                    listItems = stockList.toArray(listItems);
                    checkedItems = new boolean[listItems.length];
                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
                Toast.makeText(FaultReportActivity.this, "Failed : " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void callSpinnerEquipment(int buildId) {
        Call<JsonArray> jsonArrayCall = APIClient.getUserServices().getGenEquip(token, role, workSpaceid, buildId, locId);
        jsonArrayCall.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                if (response.code() == 200) {
                    JsonArray jsonArray = response.body();
                    for (int i = 0; i < jsonArray.size(); i++) {
                        String eqName = jsonArray.get(i).getAsJsonObject().get("name").getAsString();
                        int eqId = jsonArray.get(i).getAsJsonObject().get("id").getAsInt();
                        genralEquipment.add(new list(eqName, eqId));
                        equipmentSpinner.setAdapter(equipmentAdapter);
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
                Toast.makeText(FaultReportActivity.this, "Failed : " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }


    private void callGenLoaction(int buildId) {

        Call<JsonArray> call = APIClient.getUserServices().getGenLocation(token, buildId);
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
                    // callSpinnerEquipment(buildId);


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

        Call<JsonArray> call = APIClient.getUserServices().getGenMaintGrp(token, workSpaceid);
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
        Call<JsonArray> call = APIClient.getUserServices().getGenFaultCat(token, workSpaceid);
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
        Call<JsonArray> call = APIClient.getUserServices().getGenBuildings(token, workSpaceid);
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

        Call<JsonArray> call = APIClient.getUserServices().getGenDivisions(token, workSpaceid);
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

        Call<JsonArray> call = APIClient.getUserServices().getGenproirity(token, workSpaceid);
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
        Call<JsonArray> call = APIClient.getUserServices().getGenDep(workSpaceid, token, workSpaceid);
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
        selectTech = findViewById(R.id.selecttech);
        techTv = findViewById(R.id.techtv);

        requestorNameEditText = findViewById(R.id.requestorNameEditText);
        contactNumberEditText = findViewById(R.id.contactNumber_fault);
        locationDescriptionEditText = findViewById(R.id.LocationDescriptionEditText);
        faultDescriptionEditText = findViewById(R.id.FaultDescriptionEditText);
        departmentSpinner = findViewById(R.id.departmentSpinner);
        buildingSpinner = findViewById(R.id.buildingSpinner);
        //  spinnerTechnician = findViewById(R.id.techSpinner);
        locationSpinner = findViewById(R.id.locationSpinner);
        prioritySpinner = findViewById(R.id.prioritySpinner);
        divisionSpinner = findViewById(R.id.divisionSpinner);
        faultCategorySpinner = findViewById(R.id.faultCategorySpinner);
        selectMaintenanceSpinner = findViewById(R.id.selectMaintenanceSpinner);
        buttonCreateFaultReport = findViewById(R.id.buttonCreateFaultReport);
        equipmentSpinner = findViewById(R.id.equipmentSpinner);

        genralDepList.add(new list("Select dept", 0));
        genralEquipment.add(new list("Select Equipment", 0));
        genralPriorityList.add(new list("Select Priority", 0));
        genralDivisionList.add(new list("Select Division", 0));
        genralBuildingList.add(new list("Select Building", 0));
        genralFaultCatList.add(new list("Select Fault Categories", 0));
        genralFaultMaintGrp.add(new list("Select Maintainence", 0));
        genralLoaction.add(new list("Select Location", 0));
        genraltechlist.add(new list("Select Technician", 0));
        buttonCreateFaultReport.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {


                try {
                    buttonCreateFaultReport.setEnabled(false);
                    createFaultReport();

                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void createFaultReport() throws ParseException {
        requestorName = requestorNameEditText.getText().toString();
        faultDesc = faultDescriptionEditText.getText().toString();
        locDesc = locationDescriptionEditText.getText().toString();
        contactNo = contactNumberEditText.getText().toString();

        String attendedbyString = techTv.getText().toString();
        List<String> attendedbylist = Arrays.asList(attendedbyString.split(", "));

        for (int j = 0; j < attendedbylist.size(); j++) {
            String techincian = attendedbylist.get(j);
            for (list list : genraltechlist) {
                if (list.getName().equals(techincian)) {
                    Integer idd = list.id;
                    attendedByIdsList.add(idd);
                }
            }
        }
        if (TextUtils.isEmpty(contactNumberEditText.getText())
                || buildId == 0 || locId == 0 || maintId == 0 ||
                priroityId == 0 || depId == 0 || faultId == 0 ||
                equipId == 0 || attendedByIdsList.isEmpty() ||
                TextUtils.isEmpty(locationDescriptionEditText.getText())
                || TextUtils.isEmpty(requestorNameEditText.getText())) {
            Toast.makeText(this, "Please Select All Feilds", Toast.LENGTH_SHORT).show();
        } else if (contactNo.length() < 8) {
            Toast.makeText(this, "Contact Number not Valid", Toast.LENGTH_SHORT).show();
        } else {
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
            Equipment equipment = new Equipment();
            equipment.setId(equipId);
            Log.d(TAG, "createFaultReport: jk" + attendedByIdsList);

            ArrayList<AttendedBy> attendedBy = new ArrayList<>();

            for (int j = 0; j < attendedByIdsList.size(); j++) {
                AttendedBy attendedbyObject = new AttendedBy();
                attendedbyObject.setId((Integer) attendedByIdsList.get(j));
                attendedBy.add(attendedbyObject);

            }

            CreateFaultRequestPojo createFaultRequestPojo = new CreateFaultRequestPojo(requestorName,
                    building, location, contactNo, priority, maintGrp
                    , faultCategory, department, faultDesc, locDesc, division, equipment, attendedBy);
            progressDialog.show();
            Call<JsonObject> call = APIClient.getUserServices().createFault(createFaultRequestPojo, workSpaceid, token, role);
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    if (response.code() == 200) {
                        progressDialog.dismiss();
                        JsonObject jo = response.body();
                        String frid = jo.get("frId").getAsString();
                        Intent intent = new Intent(FaultReportActivity.this, BeforeImage.class);
                        intent.putExtra("frId", frid);
                        intent.putExtra("value", "Before");
                        intent.putExtra("workspace", workSpaceid);
                        intent.putExtra("token", token);
                        startActivity(intent);
                        Toast.makeText(FaultReportActivity.this, "Fault Report Created Successfully",
                                Toast.LENGTH_SHORT).show();
                        finish();
                    } else
                        Log.d(TAG, "onResponse: hi"+response.message());
                    Log.d(TAG, "onResponse: hl"+response.raw());
                        progressDialog.dismiss();
                    Toast.makeText(FaultReportActivity.this, "Error : " + response.code(), Toast.LENGTH_LONG).show();
                    //finish();
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    progressDialog.dismiss();

                    Toast.makeText(FaultReportActivity.this, "Failed : " + t.getMessage(), Toast.LENGTH_LONG).show();
                    Log.d(TAG, "onFailure:  " + t.getCause());
                    Log.d(TAG, "onFailure: " + t.getMessage());
                    //finish();
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        MenuItem item = menu.findItem(R.id.admin).setTitle(role);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.logoutmenu) {
            SharedPreferences preferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.clear();
            editor.apply();
            Intent intent = new Intent(this, MainActivityLogin.class);
            startActivity(intent);
            finishAffinity();
        }
        return true;
    }


}