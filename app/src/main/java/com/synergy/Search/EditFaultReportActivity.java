package com.synergy.Search;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.AdapterView;
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
import com.synergy.FaultReport.BeforeImage;
import com.synergy.R;
import com.synergy.FaultReport.list;

import org.jetbrains.annotations.NotNull;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.OptionalInt;
import java.util.stream.IntStream;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.synergy.MainActivityLogin.SHARED_PREFS;

public class EditFaultReportActivity extends AppCompatActivity {
    //test

    private FloatingActionButton fab_main, fab_before, fab_after, fab_esc;
    private Boolean isMenuOpen = false;
    private final OvershootInterpolator interpolator = new OvershootInterpolator();
    private Float translationY = 100f;
    private static final String TAG = "EditFault";
    private String workspaceString = "";
    private String timeS, remarksString, techName;
    private String frId;
    private int tHour, tMinute;
    private Spinner statusSpinner, technicianSpinner, costCenterSpinner, mainGrpSpinner,
            faultCategorySpinner, divisionSpinner, locationSpinner, buildingSpinner, prioritySpinner, deptSpinner;
    private TextView timePickerResponse, timePickerStart, timePickerEnd, datePickerResponse, datePickerStart, datePickerEnd;
    private String token;
    private String faultDetailString;
    TextView reqNameEditText;
    private EditText frIdEditText, observationEditText, faultDetailsEditText, locDescEditText,
            requestorNumberEditText, actionTakenEditText, diagnosisEditText, labourHoursEditText;
    private TextView equipmentTextView, equipmentIdTv;
    private TextView dateAddedTextView;
    private TextView timeAddedTextView;
    private Button selectEquipmentButton;
    private Button updateFaultReportButton;
    private LinearLayout mlayout;
    String frid, workSpaceid;
    private int remarksId;
    private EditText editText;
    private TextWatcher textWatcher;
    private List<String> remarksList = new ArrayList<>();
    private List<EditText> editTextList = new ArrayList<>();

    List<list> genralDepList = new ArrayList<list>();
    List<list> genralPriorityList = new ArrayList<list>();
    List<list> genralDivisionList = new ArrayList<list>();
    List<list> genralBuildingList = new ArrayList<list>();
    List<list> genralFaultCatList = new ArrayList<list>();
    List<list> genralMaintGrp = new ArrayList<list>();
    List<list> genralLoaction = new ArrayList<list>();
    List<String> genralStatusList = new ArrayList<String>();
    List<list> genralTechnicalList = new ArrayList<list>();
    List<list> genCostCebterList = new ArrayList<list>();

    int depId, divId, priId, buildId, locId, faultId, maintId, costId, techId;

    private ArrayAdapter<list> costCenterListAdapter;
    private ArrayAdapter<String> statusListAdapter;
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
        progressDialog = new ProgressDialog(EditFaultReportActivity.this);
        Intent i = getIntent();
        frid = i.getStringExtra("frId");
        workSpaceid = i.getStringExtra("workspaceId");

        initViews();
        initializeFab();
        datePicker();
        timePickerMethod();
        spinnerCalls();
        initviewsAndGetInitialData();
    }

    private void spinnerCalls() {


        Call<JsonArray> call = APIClient.getUserServices().getCostCenter(token, workSpaceid);
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                if (response.code() == 200) {
                    JsonArray jsonArrayofCostCenter = response.body();
                    for (int i = 0; i < jsonArrayofCostCenter.size(); i++) {
                        String costName = jsonArrayofCostCenter.get(i).getAsJsonObject().get("costCenterName").getAsString();
                        int costid = jsonArrayofCostCenter.get(i).getAsJsonObject().get("id").getAsInt();
                        genCostCebterList.add(new list(costName, costid));
                        costCenterSpinner.setAdapter(costCenterListAdapter);

                    }
                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {

            }
        });

        Log.d(TAG, "spinnerCalls: work" + workSpaceid);
        Log.d(TAG, "spinnerCalls: token" + token);
        Call<JsonArray> callTechAllGet = APIClient.getUserServices().getTechnicianCall(token, workSpaceid);
        callTechAllGet.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                JsonArray jsonarraytech = response.body();
                Log.d(TAG, "onResponse: jat" + jsonarraytech);
                for (int i = 0; i < jsonarraytech.size(); i++) {
                    String technicianName = jsonarraytech.get(i).getAsJsonObject().get("technicianName").getAsString();
                    int id = jsonarraytech.get(i).getAsJsonObject().get("id").getAsInt();
                    genralTechnicalList.add(new list(technicianName, id));
                    technicianSpinner.setAdapter(technicalListAdapter);
                }
                technicianSpinner.setAdapter(technicalListAdapter);
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(EditFaultReportActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
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
                        genralMaintGrp.add(new list(name, id));
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
        buildingSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                list p = (list) parent.getItemAtPosition(position);
                buildId = p.id;
                progressDialog.setTitle("Loading...");
                Call<JsonArray> call = APIClient.getUserServices().getGenLocation(token, buildId);
                call.enqueue(new Callback<JsonArray>() {
                    @Override
                    public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                        if (response.code() == 200) {
                            genralLoaction.clear();
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


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


//http://192.168.1.117:8082/api/general/technicians
        Log.d(TAG, "spinnerCalls: token" + token);
        Log.d(TAG, "spinnerCalls: work" + workSpaceid);


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

                    }
                    divisionSpinner.setAdapter(divisionAdapter);

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
        Call<JsonArray> call7 = APIClient.getUserServices().getGenDep(workSpaceid, token);
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
        prioritySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                prioritySpinner.setSelection(position);
                list p = (list) parent.getItemAtPosition(position);
                priId = p.id;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        technicianSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                technicianSpinner.setSelection(position);
                list p = (list) parent.getItemAtPosition(position);
                techId = p.id;
                techName = p.name;
            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        costCenterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                costCenterSpinner.setSelection(position);
                list p = (list) parent.getItemAtPosition(position);
                costId = p.id;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mainGrpSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mainGrpSpinner.setSelection(position);
                list p = (list) parent.getItemAtPosition(position);
                maintId = p.id;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        faultCategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                faultCategorySpinner.setSelection(position);
                list p = (list) parent.getItemAtPosition(position);
                faultId = p.id;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        divisionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                divisionSpinner.setSelection(position);
                list p = (list) parent.getItemAtPosition(position);
                divId = p.id;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        locationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                locationSpinner.setSelection(position);
                list p = (list) parent.getItemAtPosition(position);
                locId = p.id;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

/*
        buildingSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                buildingSpinner.setSelection(position);
                list p = (list) parent.getItemAtPosition(position);
                buildId = p.id;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
*/

        deptSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                deptSpinner.setSelection(position);
                list p = (list) parent.getItemAtPosition(position);
                depId = p.id;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }


    private void initViews() {
        equipmentIdTv = findViewById(R.id.eq_id_send);
        updateFaultReportButton = findViewById(R.id.updateFaultReportButton);
        diagnosisEditText = findViewById(R.id.diagnosis);
        actionTakenEditText = findViewById(R.id.actionTaken);
        statusSpinner = findViewById(R.id.statusSpinner);
        technicianSpinner = findViewById(R.id.technicianSpinner);
        costCenterSpinner = findViewById(R.id.costCenter);
        mainGrpSpinner = findViewById(R.id.mainGrp);
        selectEquipmentButton = findViewById(R.id.selectEquipmentButton);
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

        textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                checkFieldsForEmptyValues();
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        };
        datePickerStart.addTextChangedListener(textWatcher);
        datePickerEnd.addTextChangedListener(textWatcher);
        datePickerResponse.addTextChangedListener(textWatcher);
        timePickerEnd.addTextChangedListener(textWatcher);
        timePickerStart.addTextChangedListener(textWatcher);
        timePickerResponse.addTextChangedListener(textWatcher);
        observationEditText.addTextChangedListener(textWatcher);
        actionTakenEditText.addTextChangedListener(textWatcher);
        statusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                checkFieldsForEmptyValues();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        genralFaultCatList.add(new list("Select Fault Category", 0));
        genralMaintGrp.add(new list("Select Maintanence", 0));
        genralBuildingList.add(new list("Select Building", 0));
        genralDepList.add(new list("Select Department", 0));
        genralDivisionList.add(new list("Select Division", 0));
        genralTechnicalList.add(new list("Select Tech", 0));
        genralPriorityList.add(new list("Select Priority", 0));
        genCostCebterList.add(new list("Select CostCenter", 0));

        genralStatusList.add("Select status");
        genralStatusList.add("Open");
        genralStatusList.add("Closed");
        genralStatusList.add("KIV");
        genralStatusList.add("In Progress");

        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        token = sharedPreferences.getString("token", "");

        spinnerSet();
        plusbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mlayout.getChildCount() < 6) {
                    remarksId++;
                    mlayout.addView(createNewEditText(remarksString, remarksId));
                }
            }
        });

        deletebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteRemarks(view);
            }
        });
        selectEquipmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int ActivityTwoRequestCode = 1;

                Intent intent = new Intent(EditFaultReportActivity.this, EquipmentSearchActivityforEdit.class);
                intent.putExtra("token", token);
                intent.putExtra("workspace", workSpaceid);
                startActivityForResult(intent, ActivityTwoRequestCode);

            }
        });
    }

    private void initviewsAndGetInitialData() {

        Call<JsonObject> call = APIClient.getUserServices().getEditfaultDetails(frid, workSpaceid, token);
        call.enqueue(new Callback<JsonObject>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.code() == 200) {
                    JsonObject jsonObject = response.body();
                    Log.d(TAG, "onResponse: json" + jsonObject);
                    assert jsonObject != null;

                    frIdEditText.setText(jsonObject.get("frId").getAsString());
                    reqNameEditText.setText(jsonObject.get("requestorName").getAsString());
                    requestorNumberEditText.setText(jsonObject.get("requestorContactNo").getAsString());
                    if (!(jsonObject.get("equipment").isJsonNull())) {
                        equipmentIdTv.setText(jsonObject.get("equipment").getAsJsonObject().get("id").getAsString());
                        equipmentTextView.setText(jsonObject.get("equipment").getAsJsonObject().get("name").getAsString());
                    }

                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                        String buildingname = jsonObject.get("building").getAsJsonObject().get("name").getAsString();
                        OptionalInt index = IntStream.range(0, genralBuildingList.size())
                                .filter(i -> genralBuildingList.get(i).name.equals(buildingname))
                                .findFirst();
                        if (index.isPresent()) {
                            int realid = index.getAsInt();
                            buildingSpinner.setSelection(realid);
                        }
                    }
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                        if (jsonObject.get("division").isJsonNull()) {
                            divisionSpinner.setSelection(0);

                        } else {
                            String divisionname = jsonObject.get("division").getAsJsonObject().get("name").getAsString();
                            OptionalInt index = IntStream.range(0, genralDivisionList.size())
                                    .filter(i -> genralDivisionList.get(i).name.equals(divisionname))
                                    .findFirst();
                            if (index.isPresent()) {
                                int realid = index.getAsInt();
                                divisionSpinner.setSelection(realid);
                            }
                        }
                    }
                    if (!(jsonObject.get("department").isJsonNull())) {
                        String deptname = jsonObject.get("department").getAsJsonObject().get("name").getAsString();
                        OptionalInt index = IntStream.range(0, genralDepList.size())
                                .filter(i -> genralDepList.get(i).name.equals(deptname))
                                .findFirst();
                        if (index.isPresent()) {
                            int realid = index.getAsInt();
                            Log.d(TAG, "onResponse: dep" + realid);
                            deptSpinner.setSelection(realid);
                        }
                    }
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                        String prirityname = jsonObject.get("priority").getAsJsonObject().get("name").getAsString();
                        OptionalInt index = IntStream.range(0, genralPriorityList.size())
                                .filter(i -> genralPriorityList.get(i).name.equals(prirityname))
                                .findFirst();
                        if (index.isPresent()) {
                            int realid = index.getAsInt();
                            prioritySpinner.setSelection(realid);
                        }
                    }
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                        String maintname = jsonObject.get("maintGrp").getAsJsonObject().get("name").getAsString();
                        OptionalInt index = IntStream.range(0, genralMaintGrp.size())
                                .filter(i -> genralMaintGrp.get(i).name.equals(maintname))
                                .findFirst();
                        if (index.isPresent()) {
                            int realid = index.getAsInt();
                            mainGrpSpinner.setSelection(realid);
                        }
                    }

                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                        String faulname = jsonObject.get("faultCategory").getAsJsonObject().get("name").getAsString();
                        OptionalInt index = IntStream.range(0, genralFaultCatList.size())
                                .filter(i -> genralFaultCatList.get(i).name.equals(faulname))
                                .findFirst();
                        if (index.isPresent()) {
                            int realid = index.getAsInt();
                            Log.d(TAG, "onResponse: fff" + realid);
                            faultCategorySpinner.setSelection(realid);
                        }
                    }
                    if (!(jsonObject.get("attendedBy").isJsonNull())) {
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                            JsonArray ja = jsonObject.get("attendedBy").getAsJsonArray();
                            for (int j = 0; j < ja.size(); j++) {
                                JsonObject jsonObject1 = ja.get(j).getAsJsonObject();
                                if ((jsonObject1.isJsonNull())) {
                                    String techname = ja.get(j).getAsString();
                                    OptionalInt index = IntStream.range(0, genralTechnicalList.size())
                                            .filter(i -> genralTechnicalList.get(i).name.equals(techname))
                                            .findFirst();
                                    if (index.isPresent()) {
                                        int realid = index.getAsInt();
                                        technicianSpinner.setSelection(realid);
                                    }
                                }
                            }
                        }
                    }
                    if (!(jsonObject.get("costCenter").isJsonNull())) {
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                            String costName = jsonObject.get("costCenter").getAsString();
                            OptionalInt index = IntStream.range(0, genCostCebterList.size())
                                    .filter(i -> genCostCebterList.get(i).name.equals(costName))
                                    .findFirst();
                            if (index.isPresent()) {
                                int realid = index.getAsInt();
                                Log.d(TAG, "onResponse: cost" + realid);
                                costCenterSpinner.setSelection(realid);
                            }
                        }

                    }

                    if ((jsonObject.get("location").getAsJsonObject().get("description").getAsString()) != null) {
                        locDescEditText.setText(jsonObject.get("location").getAsJsonObject().get("description").getAsString());
                    }
                    if ((jsonObject.get("faultCategory").getAsJsonObject().get("description").getAsString()) != null) {
                        faultDetailsEditText.setText(jsonObject.get("faultCategory").getAsJsonObject().get("description").getAsString());
                    }
                    if (!(jsonObject.get("labourHrs").isJsonNull())) {
                        labourHoursEditText.setText(jsonObject.get("labourHrs").getAsString());
                    }
                    if (!(jsonObject.get("observation").isJsonNull())) {
                        observationEditText.setText(jsonObject.get("observation").getAsString());
                    }
                    if (!(jsonObject.get("actionTaken").isJsonNull())) {
                        actionTakenEditText.setText(jsonObject.get("actionTaken").getAsString());
                    }
                    if (!(jsonObject.get("reportedTime").isJsonNull())) {
                        timeAddedTextView.setText(jsonObject.get("reportedTime").getAsString());
                    }
                    if (!(jsonObject.get("endTime").isJsonNull())) {
                        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
                        String dateString = formatter.format(new Date(jsonObject.get("endTime").getAsLong()));
                        timePickerEnd.setText(dateString);

                    }
                    if (!(jsonObject.get("startTime").isJsonNull())) {
                        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
                        String dateString = formatter.format(new Date(jsonObject.get("startTime").getAsLong()));
                        timePickerStart.setText(dateString);

                    }
                    if (!(jsonObject.get("reportedDate").isJsonNull())) {
                        Date date = new Date(jsonObject.get("reportedDate").getAsLong());
                        SimpleDateFormat df2 = new SimpleDateFormat("dd/MM/yy");
                        String dateText = df2.format(date);
                        dateAddedTextView.setText(dateText);
                    }
                    if (!(jsonObject.get("startDate").isJsonNull())) {
                        Date date = new Date(jsonObject.get("startDate").getAsLong());
                        SimpleDateFormat df2 = new SimpleDateFormat("dd/MM/yy");
                        String dateText = df2.format(date);
                        datePickerStart.setText(dateText);
                    }
                    if (!(jsonObject.get("endDate").isJsonNull())) {
                        Date date = new Date(jsonObject.get("endDate").getAsLong());
                        SimpleDateFormat df2 = new SimpleDateFormat("dd/MM/yy");
                        String dateText = df2.format(date);
                        datePickerEnd.setText(dateText);
                    }


                    if (!(jsonObject.get("status").isJsonNull())) {
                        String statuscomming = jsonObject.get("status").getAsString();
                        int indeex = 0;
                        if (genralStatusList.contains(statuscomming)) {
                            indeex = statusListAdapter.getPosition(statuscomming);
                            statusSpinner.setSelection(indeex);
                            statusListAdapter.notifyDataSetChanged();
                        }

                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(EditFaultReportActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                Log.d("TAG", "onFailure: " + t.getMessage() + t.getCause());

            }
        });
    }

    private void deleteRemarks(View view) {
        if (!editTextList.isEmpty()) {
            if (mlayout.getChildCount() > 1) {
                if (statusSpinner.getSelectedItem().toString().equals("Closed")) {
                    if (mlayout.getChildCount() > 2) {
                        mlayout.removeViewAt(mlayout.getChildCount() - 1);
                        int index = editTextList.size() - 1;
                        editTextList.remove(index);
                    }
                } else {
                    mlayout.removeViewAt(mlayout.getChildCount() - 1);
                    int index = editTextList.size() - 1;
                    editTextList.remove(index);
                }
            }
        }
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

        maintAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, genralMaintGrp);
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

        costCenterListAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, genCostCebterList);
        costCenterListAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        costCenterSpinner.setAdapter(costCenterListAdapter);


    }

    @NotNull
    private TextView createNewEditText(String remarksString, int remarksId) {
        final LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(LinearLayout
                .LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        editText = new EditText(this);
        editText.setId(remarksId);
        editText.setText(remarksString);
        editText.setLayoutParams(lparams);
        editText.setHint("add remarks");
        editText.setMinHeight(50);
        editText.setMaxWidth(mlayout.getWidth());
        editTextList.add(editText);
        String remarkSt = editText.getText().toString();
        editText.addTextChangedListener(textWatcher);
        return editText;
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
                                calendar.set(0, 0, 0, tHour, tMinute);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                assert data != null;
                String eqip = data.getStringExtra("equipment_code");
                int equipId = data.getIntExtra("equipmentId", 0);
                Log.d(TAG, "onActivityResult: " + equipId);
                equipmentTextView.setText(eqip);
            }
        }
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
                Intent intent = new Intent(EditFaultReportActivity.this, BeforeImage.class);
                intent.putExtra("token", token);
                intent.putExtra("value", "Before");
                intent.putExtra("workspace", workSpaceid);
                intent.putExtra("frId", frId);
                startActivity(intent);
                finish();

            }
        });
        fab_after.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditFaultReportActivity.this, BeforeImage.class);
                intent.putExtra("token", token);
                intent.putExtra("value", "After");
                intent.putExtra("workspace", workSpaceid);
                intent.putExtra("frId", frId);
                startActivity(intent);
                finish();

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

    private void checkFieldsForEmptyValues() {
        updateFaultReportButton.setEnabled(false);
        if (statusSpinner.getSelectedItem().toString().equals("Open")) {
            datePickerStart.setEnabled(false);
            timePickerStart.setEnabled(false);
            datePickerEnd.setEnabled(false);
            timePickerEnd.setEnabled(false);

            buttonEnableMethod();

        } else if (statusSpinner.getSelectedItem().toString().equals("KIV")
                || statusSpinner.getSelectedItem().toString().equals("In Progress")) {

            datePickerStart.setEnabled(true);
            timePickerStart.setEnabled(true);
            timePickerResponse.setEnabled(true);
            datePickerResponse.setEnabled(true);

            if (!actionTakenEditText.getText().toString().isEmpty()
                    && !datePickerStart.getText().toString().isEmpty()
                    && !timePickerStart.getText().toString().isEmpty()) {
                buttonEnableMethod();
            }
        } else if (statusSpinner.getSelectedItem().toString().equals("Closed")) {
            datePickerStart.setEnabled(true);
            timePickerStart.setEnabled(true);
            datePickerEnd.setEnabled(true);
            timePickerEnd.setEnabled(true);
            timePickerResponse.setEnabled(true);
            datePickerResponse.setEnabled(true);
            if (!editTextList.isEmpty()) {

                editText = editTextList.get(0);
                if (!datePickerStart.getText().toString().isEmpty()
                        && !timePickerStart.getText().toString().isEmpty()
                        && !datePickerEnd.getText().toString().isEmpty()
                        && !timePickerEnd.getText().toString().isEmpty()
                        && !observationEditText.getText().toString().isEmpty()
                        && !actionTakenEditText.getText().toString().isEmpty()
                        && !editText.getText().toString().isEmpty()) {
                    buttonEnableMethod();
                }
            }
        }
    }

    private void buttonEnableMethod() {
        updateFaultReportButton.setEnabled(true);
        updateFaultReportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    updateFaultReport();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @SuppressLint("SimpleDateFormat")
    private void updateFaultReport() throws ParseException {

        ProgressDialog mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setTitle("Updating");
        mProgressDialog.show();
        frId = frIdEditText.getText().toString();
        String reqName = reqNameEditText.getText().toString();
        String contactNumber = requestorNumberEditText.getText().toString();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
        long dateRep = 0, dateRes = 0, dateStart = 0, dateEnd = 0;
        if (!dateAddedTextView.getText().toString().isEmpty()) {
            String idateReported = dateAddedTextView.getText().toString();

            SimpleDateFormat s1 = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat s2 = new SimpleDateFormat("ddMMyyyy");
            Date d = s1.parse(idateReported);
            String s3 = s2.format(d);
            dateRep = Long.parseLong(s3);
        }
        if (!datePickerResponse.getText().toString().isEmpty()) {
            String iresponseDate = datePickerResponse.getText().toString();
            SimpleDateFormat f = new SimpleDateFormat("dd-MM-yyyy");
            Date d = f.parse(iresponseDate);
            assert d != null;
            dateRes = d.getTime();
        }
        if (!datePickerStart.getText().toString().isEmpty()) {
            String istartDate = datePickerStart.getText().toString();
            SimpleDateFormat f = new SimpleDateFormat("dd-MM-yyyy");
            Date d = f.parse(istartDate);
            assert d != null;
            dateStart = d.getTime();
        }
        if (!datePickerEnd.getText().toString().isEmpty()) {
            String iendDate = datePickerEnd.getText().toString();
            SimpleDateFormat f = new SimpleDateFormat("dd-MM-yyyy");
            Date d = f.parse(iendDate);
            assert d != null;
            dateEnd = d.getTime();
        }
        long timeRes = 0, timeRep = 0, timeStart = 0, timeEnd = 0;

        if (!timeAddedTextView.getText().toString().isEmpty()) {
            String currentwTime = timeAddedTextView.getText().toString();
            timeRep = new java.text.SimpleDateFormat("HH : mm").
                    parse(currentwTime).getTime() / 1000;
            Log.d(TAG, "updateFaultReport: nn" + timeRep);
        }
        if (!timePickerResponse.getText().toString().isEmpty()) {
            String currentwTime = timePickerResponse.getText().toString();
            timeRes = new java.text.SimpleDateFormat("HH:mm").
                    parse(currentwTime).getTime() / 1000;
            Log.d(TAG, "updateFaultReport: nn" + timeRes);
        }
        if (!timePickerStart.getText().toString().isEmpty()) {
            String currentwTime = timePickerStart.getText().toString();
            timeStart = new java.text.SimpleDateFormat("HH:mm").
                    parse(currentwTime).getTime() / 1000;
            Log.d(TAG, "updateFaultReport: nn" + timeStart);
        }
        if (!timePickerEnd.getText().toString().isEmpty()) {
            String currentwTime = timePickerEnd.getText().toString();
            timeEnd = new java.text.SimpleDateFormat("HH:mm").
                    parse(currentwTime).getTime() / 1000;
            Log.d(TAG, "updateFaultReport: nn" + timeEnd);
        }
        String locationDesc = locDescEditText.getText().toString();
        // String faultCategoryName = faultCategorySpinner.getSelectedItem().toString();
        faultDetailString = faultDetailsEditText.getText().toString();
        String observerString = observationEditText.getText().toString();
        String diagnosesString = diagnosisEditText.getText().toString();
        String actionTakenString = actionTakenEditText.getText().toString();
        String statusString = statusSpinner.getSelectedItem().toString();
        // String equipmentString = equipmentTextView.getText().toString();
        //  String technicianStringId = technicianSpinner.getSelectedItem().toString();

        if (!editTextList.isEmpty()) {
            for (int iRem = 0; iRem < editTextList.size(); iRem++) {
                String remarks1String = editTextList.get(iRem).getText().toString();
                remarksList.add(remarks1String);
            }
        } else remarksList = null;
        int eqId = 0;
        if (!(TextUtils.isEmpty(equipmentIdTv.getText()))) {
            eqId = Integer.parseInt(equipmentIdTv.getText().toString());
        }
        Equipment equipment = new Equipment(eqId);
        Location location = new Location();
        location.setId(locId);
        Building building = new Building();
        building.setId(buildId);
        Department department = new Department();
        department.setId(depId);
        Division division = new Division();
        division.setId(divId);
        FaultCategory faultCategory = new FaultCategory();
        faultCategory.setId(faultId);
        MaintGrp maintGrp = new MaintGrp();
        maintGrp.setId(maintId);
        Priority priority = new Priority();
        priority.setId(priId);
        CostCenter costCenter = new CostCenter(costId);

        AttendedBy attendedBy = new AttendedBy(techId);
        ArrayList<AttendedBy> attendedByArrayList = new ArrayList<AttendedBy>(Collections.singleton(attendedBy));
        EditFaultReportRequest editFaultReportRequest = new EditFaultReportRequest(frId,
                building,
                location,
                reqName,
                department,
                contactNumber,
                dateRep,
                timeRep,
                dateRes,
                timeRes,
                dateStart,
                timeStart,
                dateEnd,
                timeEnd,
                locationDesc,
                faultCategory,
                faultDetailString,
                priority,
                maintGrp,
                division,
                observerString,
                diagnosesString,
                actionTakenString,
                costCenter,
                statusString,
                equipment,
                remarksList,
                attendedByArrayList);

        Call<Void> callEditFaultReport = APIClient.getUserServices().updateReport(editFaultReportRequest, token, workspaceString);
        callEditFaultReport.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.code() == 200) {
                    Toast.makeText(EditFaultReportActivity.this, "Fault Report updated successfully", Toast.LENGTH_SHORT).show();

                } else if (response.code() == 500) {
                    AlertDialog.Builder emptyDailog = new AlertDialog.Builder(EditFaultReportActivity.this);
                    emptyDailog.setTitle("Error: " + response.code() + ". Please fill all the required fields!");
                    emptyDailog.setIcon(R.drawable.ic_error);
                    emptyDailog.setPositiveButton("Ok", null);
                    emptyDailog.show();
                } else {
                    AlertDialog.Builder dailog = new AlertDialog.Builder(EditFaultReportActivity.this);
                    dailog.setTitle("Error: " + response.code());
                    dailog.setIcon(R.drawable.ic_error);
                    dailog.setPositiveButton("Ok", null);
                    dailog.show();
                }
                mProgressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getCause());
                Log.d(TAG, "onFailure: " + t.getMessage());
                Toast.makeText(EditFaultReportActivity.this, "Failed to update" + t.getMessage(), Toast.LENGTH_SHORT).show();
                mProgressDialog.dismiss();
            }
        });
    }

}