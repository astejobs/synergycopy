package com.synergy.Search;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.synergy.APIClient;
import com.synergy.Dashboard.Dashboard;
import com.synergy.FaultReport.BeforeImage;
import com.synergy.FaultReport.FaultReportActivity;
import com.synergy.LogoutClass;
import com.synergy.MainActivityLogin;
import com.synergy.R;
import com.synergy.FaultReport.list;

import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.OptionalInt;
import java.util.stream.IntStream;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.synergy.MainActivityLogin.SHARED_PREFS;

public class EditFaultReportActivity extends AppCompatActivity {
    //test

    private FloatingActionButton fab_main, fab_before, fab_after;
    private Boolean isMenuOpen = false;
    private final OvershootInterpolator interpolator = new OvershootInterpolator();
    private Float translationY = 100f;
    private static final String TAG = "EditFault";
    private String timeS, remarksString, techName;
    private String frId;
    TextView uploadFileTv;
    Button uploadFileBtn;
    Intent uploadFileIntent;
    private int tHour, tMinute;
    private Spinner statusSpinner, costCenterSpinner, mainGrpSpinner,
            faultCategorySpinner, divisionSpinner, locationSpinner, buildingSpinner, prioritySpinner, deptSpinner;
    private TextView timePickerResponse, timePickerStart, timePickerEnd, datePickerResponse, datePickerStart, datePickerEnd;
    private String token, role;
    private String faultDetailString;
    TextView reqNameEditText;
    private EditText frIdEditText, observationEditText, faultDetailsEditText, locDescEditText,
            requestorNumberEditText, actionTakenEditText, diagnosisEditText, labourHoursEditText;
    private TextView equipmentTextView, equipmentIdTv;
    private TextView activationDate;
    private TextView activationTime;
    private Button selectEquipmentButton, plusbtn, deletebtn;
    private Button updateFaultReportButton;
    private LinearLayout mlayout;
    String frid, workSpaceid, equipCode;
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

    int depId, divId, priId, buildId, locId, faultId, maintId;
    private Integer costId = null;
    private Integer techId = null;
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
    LinearLayout linearLayoutdisable;
    Toolbar toolbar;

    boolean[] checkedItems;
    ArrayList<Integer> mUserItems = new ArrayList<>();
    List<String> stockList = new ArrayList<>();
    String[] listItems;
    List attendedByIdsList;

    private Button selectTech;
    private TextView techTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_fault_report);
        progressDialog = new ProgressDialog(EditFaultReportActivity.this);
        toolbar = findViewById(R.id.toolbar_edit_fault);
        setSupportActionBar(toolbar);

        Intent i = getIntent();
        frid = i.getStringExtra("frId");

        workSpaceid = i.getStringExtra("workspaceId");
        equipCode = i.getStringExtra("equipcode");
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        role = sharedPreferences.getString("role", "");


        linearLayoutdisable = findViewById(R.id.layout_disable);
        if (role.equals("ManagingAgent")) {

            initViews();
            callDisable();
            initializeFab();
            //  calldept();
            //callpriroty();
            //callbuilding();
            //calldivision();
            //callfault();
            //callmaint();
            calltech();
            //callCostCenter();

            if (frid != null) {
                initviewsAndGetInitialData(frid);
            } else {
                initviewsAndGetInitialDataOnEquip(equipCode);

            }
            spinnerSelectionCalls();


        } else {

            initViews();
            initializeFab();
            //  calldept();
            // callpriroty();
            //  callbuilding();
            //  calldivision();
            //   callfault();
            //   callmaint();
            calltech();
            //   callCostCenter();

            if (frid != null) {
                updateFaultReportButton.setVisibility(View.INVISIBLE);
                initviewsAndGetInitialData(frid);
            } else {
                initviewsAndGetInitialDataOnEquip(equipCode);

            }
            spinnerSelectionCalls();
        }
    }

    private void callDisable() {
        techTv.setEnabled(false);
        //   selectTech.setEnabled(false);
        frIdEditText.setEnabled(false);
        deptSpinner.setEnabled(false);
        prioritySpinner.setEnabled(false);
        requestorNumberEditText.setEnabled(false);
        buildingSpinner.setEnabled(false);
        locationSpinner.setEnabled(false);
        divisionSpinner.setEnabled(false);
        locDescEditText.setEnabled(false);
        faultCategorySpinner.setEnabled(false);
        faultDetailsEditText.setEnabled(false);
        mainGrpSpinner.setEnabled(false);
        observationEditText.setEnabled(false);
       // diagnosisEditText.setEnabled(false);
        actionTakenEditText.setEnabled(false);
      //costCenterSpinner.setEnabled(false);
        //  technicianSpinner.setEnabled(false);
        //  selectEquipmentButton.setEnabled(false);
        plusbtn.setEnabled(false);
        deletebtn.setEnabled(false);




/*
        public static void enableDisableViewGroup(ViewGroup viewGroup, boolean enabled) {
            int childCount = viewGroup.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View view = viewGroup.getChildAt(i);
                view.setEnabled(enabled);
                if (view instanceof ViewGroup) {
                    enableDisableViewGroup((ViewGroup) view, enabled);
                }
            }
        }
*/


    }

    /*  @Override
      public boolean dispatchTouchEvent(MotionEvent ev){

          return true;//consume
      }*/
    private void callCostCenter() {
        progressDialog.show();

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
                Toast.makeText(EditFaultReportActivity.this, "Failed" + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onFailure: costcenter" + t.getCause());
                Log.d(TAG, "onFailure: costcenter" + t.getMessage());

            }
        });
    }

    private void calldept() {
        Call<JsonArray> call7 = APIClient.getUserServices().getGenDep(workSpaceid, token, workSpaceid);
        call7.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                if (response.code() == 200) {

                    progressDialog.dismiss();

                    JsonArray jsonArrayofGenDep = response.body();
                    for (int i = 0; i < jsonArrayofGenDep.size(); i++) {
                        String name = jsonArrayofGenDep.get(i).getAsJsonObject().get("name").getAsString();
                        int id = jsonArrayofGenDep.get(i).getAsJsonObject().get("id").getAsInt();
                        list list = new list(name, id);
                        if (!(genralDepList.contains(list))) {
                            genralDepList.add(list);
                            // deptSpinner.setAdapter(deptListAdapter);
                        }
                    }

                } else
                    progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(EditFaultReportActivity.this, "Failed : " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onFailure: department" + t.getMessage());
                Log.d(TAG, "onFailure:department " + t.getCause());
            }
        });
    }

    private void callpriroty() {
        Call<JsonArray> call6 = APIClient.getUserServices().getGenproirity(token, workSpaceid);
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
                Log.d(TAG, "onFailure: priority" + t.getMessage());
                Log.d(TAG, "onFailure: priority" + t.getMessage());
            }
        });
    }

    private void callbuilding() {


        Call<JsonArray> call4 = APIClient.getUserServices().getGenBuildings(token, workSpaceid);
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
                Log.d(TAG, "onFailure:building " + t.getMessage());
                Log.d(TAG, "onFailure: building" + t.getCause());
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
                        Log.d(TAG, "onFailure: location" + t.getMessage());
                        Log.d(TAG, "onFailure:location " + t.getCause());
                    }
                });


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void calldivision() {

        Call<JsonArray> call5 = APIClient.getUserServices().getGenDivisions(token, workSpaceid);
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
                Log.d(TAG, "onFailure: division " + t.getMessage());
                Log.d(TAG, "onFailure: division" + t.getCause());
            }
        });


    }

    private void callfault() {


        Call<JsonArray> call3 = APIClient.getUserServices().getGenFaultCat(token, workSpaceid);
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
                Log.d(TAG, "onFailure: fault" + t.getMessage());
                Log.d(TAG, "onFailure: fault" + t.getCause());
            }
        });
    }

    private void callmaint() {
        Call<JsonArray> call2 = APIClient.getUserServices().getGenMaintGrp(token, workSpaceid);
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
                Log.d(TAG, "onFailure: maintgrp" + t.getMessage());
                Log.d(TAG, "onFailure: maintgrp" + t.getCause());
            }
        });
    }

    private void calltech() {

        Call<JsonArray> callTechAllGet = APIClient.getUserServices().getTechnicianCall(token, workSpaceid);
        callTechAllGet.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                if (response.code() == 200) {

                    JsonArray jsonarraytech = response.body();
                    for (int i = 0; i < jsonarraytech.size(); i++) {
                        String technicianName = jsonarraytech.get(i).getAsJsonObject().get("name").getAsString();
                        int id = jsonarraytech.get(i).getAsJsonObject().get("id").getAsInt();
                        genralTechnicalList.add(new list(technicianName, id));
                        //techTv.setText(technicianName);
                        stockList.add(technicianName);

                        //   technicianSpinner.setAdapter(technicalListAdapter);
                    }
                    listItems = new String[stockList.size()];
                    listItems = stockList.toArray(listItems);
                    checkedItems = new boolean[listItems.length];
                    //  technicianSpinner.setAdapter(technicalListAdapter);
                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                progressDialog.dismiss();
                Log.d(TAG, "onFailure: tech" + t.getCause());
                Log.d(TAG, "onFailure: tech" + t.getMessage());
                Toast.makeText(EditFaultReportActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    private void spinnerSelectionCalls() {

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
/*
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
*/

/*
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
*/

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
                Log.d(TAG, "onItemSelected: hi faz" + faultId);
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
        techTv = findViewById(R.id.techtv);
        //  selectTech = findViewById(R.id.selecttech);
        equipmentIdTv = findViewById(R.id.eq_id_send);
        updateFaultReportButton = findViewById(R.id.updateFaultReportButton);
      //  diagnosisEditText = findViewById(R.id.diagnosis);
        actionTakenEditText = findViewById(R.id.actionTaken);
        statusSpinner = findViewById(R.id.statusSpinner);
        //   technicianSpinner = findViewById(R.id.technicianSpinner);
      //  costCenterSpinner = findViewById(R.id.costCenter);
        mainGrpSpinner = findViewById(R.id.mainGrp);
        //selectEquipmentButton = findViewById(R.id.selectEquipmentButton);
        faultCategorySpinner = findViewById(R.id.faultCategory);
        divisionSpinner = findViewById(R.id.divisionNumberSpinner);
        locationSpinner = findViewById(R.id.unitNumber);
        buildingSpinner = findViewById(R.id.buildingNumber);
        prioritySpinner = findViewById(R.id.priorityNumber);
        deptSpinner = findViewById(R.id.dept_number);
        requestorNumberEditText = findViewById(R.id.contactNumber);
        frIdEditText = findViewById(R.id.frIdEditText);
        locDescEditText = findViewById(R.id.locationDescrip);
        faultDetailsEditText = findViewById(R.id.faultDetails);
        reqNameEditText = findViewById(R.id.reqNameEditTextEditFault);
        equipmentTextView = findViewById(R.id.equipmentTextViewEditFault);
        activationDate = findViewById(R.id.activation_date);
        activationTime = findViewById(R.id.activation_time);
        plusbtn = findViewById(R.id.plusButton);
        deletebtn = findViewById(R.id.deleteButton);
        mlayout = findViewById(R.id.layout_remarks);
        TextView textView = new TextView(this);
        observationEditText = findViewById(R.id.observation);
        fab_main = findViewById(R.id.images_id);
        fab_before = findViewById(R.id.before_id);
        fab_after = findViewById(R.id.after_id);
        // selectTech.setVisibility(View.INVISIBLE);
        //  selectEquipmentButton.setVisibility(View.INVISIBLE);
        //  selectEquipmentButton.setEnabled(false);
        //  selectTech.setEnabled(false);
        locationSpinner.setEnabled(false);
        buildingSpinner.setEnabled(false);
        deptSpinner.setEnabled(false);
        requestorNumberEditText.setEnabled(false);
        prioritySpinner.setEnabled(false);
        divisionSpinner.setEnabled(false);
        locDescEditText.setEnabled(false);
        faultCategorySpinner.setEnabled(false);
        faultDetailsEditText.setEnabled(false);
        mainGrpSpinner.setEnabled(false);


/*
        selectTech.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(EditFaultReportActivity.this, "dialog", Toast.LENGTH_SHORT).show();
                AlertDialog.Builder mbuilder = new AlertDialog.Builder(EditFaultReportActivity.this);
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
*/

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


      /*  genralFaultCatList.add(new list("Select Fault Category", 0));
        genralMaintGrp.add(new list("Select Maintanence", 0));
        genralBuildingList.add(new list("Select Building", 0));
        genralDepList.add(new list("Select Department", 0));
        genralDivisionList.add(new list("Select Division", 0));
        //genralTechnicalList.add(new list("Select Tech", 0));
        genralPriorityList.add(new list("Select Priority", 0));
        genCostCebterList.add(new list("Select CostCenter", 0));*/

        String tech = "Technician";
        if (role.equals(tech)) {
            genralStatusList.add("Select status");
            genralStatusList.add("Open");
            genralStatusList.add("Pause");
            genralStatusList.add("Completed");

        } else {
            genralStatusList.add("Select status");
            genralStatusList.add("Open");
            genralStatusList.add("Closed");
            genralStatusList.add("Pause");
            genralStatusList.add("Completed");
        }

        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        token = sharedPreferences.getString("token", "");
        role = sharedPreferences.getString("role", "");

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
/*
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
*/
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
    private void callUploadsFileMethods() {
        uploadFileIntent = new Intent(Intent.ACTION_GET_CONTENT);
        uploadFileIntent.setType("*/*");
        startActivityForResult(Intent.createChooser(uploadFileIntent, "Select file"), 10);


    }

    private void initviewsAndGetInitialDataOnEquip(String data) {

        Call<JsonObject> call = APIClient.getUserServices().getCallEquipment(data, token, role, workSpaceid);
        call.enqueue(new Callback<JsonObject>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.code() == 200) {
                    progressDialog.dismiss();
                    JsonObject jsonObject = response.body();
                    Log.d(TAG, "onResponse: json on qr search" + jsonObject);
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
                        int id = jsonObject.get("building").getAsJsonObject().get("id").getAsInt();
                        genralBuildingList.add(new list(buildingname, id));
                        buildingSpinner.setAdapter(buildingAdapter);
                      /*  OptionalInt index = IntStream.range(0, genralBuildingList.size())
                                .filter(i -> genralBuildingList.get(i).name.equals(buildingname))
                                .findFirst();
                        if (index.isPresent()) {
                            int realid = index.getAsInt();
                            buildingSpinner.setSelection(realid);
                        }*/
                    }


                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                        String buildingname = jsonObject.get("location").getAsJsonObject().get("name").getAsString();
                        int id = jsonObject.get("location").getAsJsonObject().get("id").getAsInt();
                        genralLoaction.add(new list(buildingname, id));
                        locationSpinner.setAdapter(locationAdapter);
                    }

                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                        if (!(jsonObject.get("division").isJsonNull())) {

                            String divisionname = jsonObject.get("division").getAsJsonObject().get("name").getAsString();
                            int id = jsonObject.get("division").getAsJsonObject().get("id").getAsInt();
                            genralDivisionList.add(new list(divisionname, id));
                            divisionSpinner.setAdapter(divisionAdapter);
                          /*  OptionalInt index = IntStream.range(0, genralDivisionList.size())
                                    .filter(i -> genralDivisionList.get(i).name.equals(divisionname))
                                    .findFirst();
                            if (index.isPresent()) {
                                int realid = index.getAsInt();
                                divisionSpinner.setSelection(realid);
                            }*/
                        }
                    }
                    if (!(jsonObject.get("department").isJsonNull())) {
                        String deptname = jsonObject.get("department").getAsJsonObject().get("name").getAsString();
                        int id = jsonObject.get("department").getAsJsonObject().get("id").getAsInt();
                        genralDepList.add(new list(deptname, id));
                        deptSpinner.setAdapter(deptListAdapter);

                    /*    list list = new list(deptname, id);
                        if (!(genralDepList.contains(list))) {
                            genralDepList.add(new list(deptname, id));
                            deptSpinner.setAdapter(deptListAdapter);
                        }
                        OptionalInt index = IntStream.range(0, genralDepList.size())
                                .filter(i -> genralDepList.get(i).name.equals(deptname))
                                .findFirst();
                        if (index.isPresent()) {
                            int realid = index.getAsInt();
                            Log.d(TAG, "onResponse: dep" + realid);
                            deptSpinner.setSelection(realid);
                        }*/
                    }
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                        String prirityname = jsonObject.get("priority").getAsJsonObject().get("name").getAsString();
                        int id = jsonObject.get("priority").getAsJsonObject().get("id").getAsInt();
                        genralPriorityList.add(new list(prirityname, id));
                        prioritySpinner.setAdapter(priAdapter);
                      /*  OptionalInt index = IntStream.range(0, genralPriorityList.size())
                                .filter(i -> genralPriorityList.get(i).name.equals(prirityname))
                                .findFirst();
                        if (index.isPresent()) {
                            int realid = index.getAsInt();
                            prioritySpinner.setSelection(realid);
                        }*/
                    }
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                        String maintname = jsonObject.get("maintGrp").getAsJsonObject().get("name").getAsString();
                        int id = jsonObject.get("maintGrp").getAsJsonObject().get("id").getAsInt();
                        genralMaintGrp.add(new list(maintname, id));
                        mainGrpSpinner.setAdapter(maintAdapter);
                       /* OptionalInt index = IntStream.range(0, genralMaintGrp.size())
                                .filter(i -> genralMaintGrp.get(i).name.equals(maintname))
                                .findFirst();
                        if (index.isPresent()) {
                            int realid = index.getAsInt();
                            mainGrpSpinner.setSelection(realid);
                        }*/
                    }

                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                        String faulname = jsonObject.get("faultCategory").getAsJsonObject().get("name").getAsString();
                        int id = jsonObject.get("faultCategory").getAsJsonObject().get("id").getAsInt();
                        genralFaultCatList.add(new list(faulname, id));
                        faultCategorySpinner.setAdapter(faultCatAdapter);
                        /*OptionalInt index = IntStream.range(0, genralFaultCatList.size())
                                .filter(i -> genralFaultCatList.get(i).name.equals(faulname))
                                .findFirst();
                        if (index.isPresent()) {
                            int realid = index.getAsInt();
                            Log.d(TAG, "onResponse: fff" + realid);
                            faultCategorySpinner.setSelection(realid);
                        }*/
                    }
                    if (!(jsonObject.get("attendedBy").isJsonNull())) {
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                            JsonArray ja = jsonObject.get("attendedBy").getAsJsonArray();
                            Log.d(TAG, "onResponse: jaray attendedby" + ja);
                            for (int j = 0; j < ja.size(); j++) {
                                JsonObject jsonObject1 = ja.get(j).getAsJsonObject();
                                if (!(jsonObject1.isJsonNull())) {
                                    String techname = ja.get(j).getAsJsonObject().get("name").getAsString();

                                    Log.d(TAG, "onResponse: hhhh" + techname);
                                    techTv.setText(techname);
                                    OptionalInt index = IntStream.range(0, genralTechnicalList.size())
                                            .filter(i -> genralTechnicalList.get(i).name.equals(techname))
                                            .findFirst();
                                    if (index.isPresent()) {
                                        int realid = index.getAsInt();
                                        //  technicianSpinner.setSelection(realid);
                                    }
                                }
                            }
                        }
                    }
/*
                    if (!(jsonObject.get("costCenter").isJsonNull())) {
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                            JsonObject jsonObject1 = jsonObject.get("costCenter").getAsJsonObject();
                            String costName = jsonObject1.get("costCenterName").getAsString();
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
*/

                    if (!(jsonObject.get("locationDesc").isJsonNull())) {
                        locDescEditText.setText(jsonObject.get("locationDesc").getAsString());
                    }
                    if (!(jsonObject.get("faultCategoryDesc").isJsonNull())) {
                        faultDetailsEditText.setText(jsonObject.get("locationDesc").getAsString());
                    }

                    if (!(jsonObject.get("observation").isJsonNull())) {
                        observationEditText.setText(jsonObject.get("observation").getAsString());
                    }
                    if (!(jsonObject.get("remarks").isJsonNull())) {
                        for (int i = 0; i < jsonObject.get("remarks").getAsJsonArray().size(); i++) {
                            if (!jsonObject.get("remarks").getAsJsonArray().get(i).isJsonNull()) {
                                String remString = jsonObject.get("remarks").getAsJsonArray()
                                        .get(i).getAsString();
                                // int remId = jsonObject.get("remarks").getAsJsonArray().get(i).getAsInt();
                                mlayout.addView(createNewEditText(remString, i));
                            }
                        }
                    }

                    if (!(jsonObject.get("actionTaken").isJsonNull())) {
                        actionTakenEditText.setText(jsonObject.get("actionTaken").getAsString());
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
                    if (!(jsonObject.get("activationTime").isJsonNull())) {
                        String hour = jsonObject.get("activationTime").getAsJsonObject().get("hour").getAsString();
                        String minute = jsonObject.get("activationTime").getAsJsonObject().get("minute").getAsString();
                        String year = jsonObject.get("activationTime").getAsJsonObject().get("year").getAsString();
                        String monthValue = jsonObject.get("activationTime").getAsJsonObject().get("monthValue").getAsString();
                        String dayOfMonth = jsonObject.get("activationTime").getAsJsonObject().get("dayOfMonth").getAsString();

                        String time = hour + ":" + minute;
                        String date = dayOfMonth + "-" + monthValue + "-" + year + "  " + time;
                        activationDate.setText(date);
                    }
                    if (!(jsonObject.get("arrivalTime").isJsonNull())) {
                        String hour = jsonObject.get("arrivalTime").getAsJsonObject().get("hour").getAsString();
                        String minute = jsonObject.get("arrivalTime").getAsJsonObject().get("minute").getAsString();
                        String year = jsonObject.get("arrivalTime").getAsJsonObject().get("year").getAsString();
                        String monthValue = jsonObject.get("arrivalTime").getAsJsonObject().get("monthValue").getAsString();
                        String dayOfMonth = jsonObject.get("arrivalTime").getAsJsonObject().get("dayOfMonth").getAsString();

                        String time = hour + ":" + minute;
                        String date = dayOfMonth + "-" + monthValue + "-" + year + "   " + time;
                        activationTime.setText(date);
                    }

                } else
                    Toast.makeText(EditFaultReportActivity.this, "Error : " + response.code(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(EditFaultReportActivity.this, "Failed : " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("TAG", "onFailure:equip call " + t.getMessage());
                Log.d(TAG, "onFailure: equip call" + t.getCause());

            }
        });
    }


    private void initviewsAndGetInitialData(String data) {

        Call<JsonObject> call = APIClient.getUserServices().getEditfaultDetails(data, workSpaceid, token, role);
        call.enqueue(new Callback<JsonObject>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.code() == 200) {
                    progressDialog.dismiss();
                    JsonObject jsonObject = response.body();
                    Log.d(TAG, "onResponse: json on frid search" + jsonObject);
                    assert jsonObject != null;

                    frIdEditText.setText(jsonObject.get("frId").getAsString());
                    reqNameEditText.setText(jsonObject.get("requestorName").getAsString());
                    requestorNumberEditText.setText(jsonObject.get("requestorContactNo").getAsString());
                    if (!(jsonObject.get("equipment").isJsonNull())) {
                        equipmentIdTv.setText(jsonObject.get("equipment").getAsJsonObject().get("id").getAsString());
                        equipmentTextView.setText(jsonObject.get("equipment").getAsJsonObject().get("name").getAsString());
                    }

                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                        String locationname = jsonObject.get("location").getAsJsonObject().get("name").getAsString();
                        int id = jsonObject.get("location").getAsJsonObject().get("id").getAsInt();
                        genralLoaction.add(new list(locationname, id));
                        locationSpinner.setAdapter(locationAdapter);

                    }


                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                        String buildingname = jsonObject.get("building").getAsJsonObject().get("name").getAsString();
                        int id = jsonObject.get("building").getAsJsonObject().get("id").getAsInt();
                        genralBuildingList.add(new list(buildingname, id));
                        buildingSpinner.setAdapter(buildingAdapter);

/*
                        OptionalInt index = IntStream.range(0, genralBuildingList.size())
                                .filter(i -> genralBuildingList.get(i).name.equals(buildingname))
                                .findFirst();
                        if (index.isPresent()) {
                            int realid = index.getAsInt();
                            buildingSpinner.setSelection(realid);
                        }*/
                    }

                    if (!(jsonObject.get("division").isJsonNull())) {

                        String divisionname = jsonObject.get("division").getAsJsonObject().get("name").getAsString();
                        int id = jsonObject.get("division").getAsJsonObject().get("id").getAsInt();
                        genralDivisionList.add(new list(divisionname, id));
                        divisionSpinner.setAdapter(divisionAdapter);

                         /*   OptionalInt index = IntStream.range(0, genralDivisionList.size())
                                    .filter(i -> genralDivisionList.get(i).name.equals(divisionname))
                                    .findFirst();
                            if (index.isPresent()) {
                                int realid = index.getAsInt();
                                divisionSpinner.setSelection(realid);
                            }*/

                    }
                    if (!(jsonObject.get("department").isJsonNull())) {
                        String deptname = jsonObject.get("department").getAsJsonObject().get("name").getAsString();
                        int id = jsonObject.get("department").getAsJsonObject().get("id").getAsInt();
                        genralDepList.add(new list(deptname, id));
                        deptSpinner.setAdapter(deptListAdapter);
                        /*OptionalInt index = IntStream.range(0, genralDepList.size())
                                .filter(i -> genralDepList.get(i).name.equals(deptname))
                                .findFirst();
                        if (index.isPresent()) {
                            int realid = index.getAsInt();
                            Log.d(TAG, "onResponse: dep" + realid);
                            deptSpinner.setSelection(realid);
                        }*/
                    }
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                        String prirityname = jsonObject.get("priority").getAsJsonObject().get("name").getAsString();
                        int id = jsonObject.get("priority").getAsJsonObject().get("id").getAsInt();
                        genralPriorityList.add(new list(prirityname, id));
                        prioritySpinner.setAdapter(priAdapter);

                        /*OptionalInt index = IntStream.range(0, genralPriorityList.size())
                                .filter(i -> genralPriorityList.get(i).name.equals(prirityname))
                                .findFirst();
                        if (index.isPresent()) {
                            int realid = index.getAsInt();
                            prioritySpinner.setSelection(realid);
                        }*/
                    }
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                        String maintname = jsonObject.get("maintGrp").getAsJsonObject().get("name").getAsString();
                        int id = jsonObject.get("maintGrp").getAsJsonObject().get("id").getAsInt();
                        genralMaintGrp.add(new list(maintname, id));
                        mainGrpSpinner.setAdapter(maintAdapter);
                       /* OptionalInt index = IntStream.range(0, genralMaintGrp.size())
                                .filter(i -> genralMaintGrp.get(i).name.equals(maintname))
                                .findFirst();
                        if (index.isPresent()) {
                            int realid = index.getAsInt();
                            mainGrpSpinner.setSelection(realid);
                        }*/
                    }

                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                        String faulname = jsonObject.get("faultCategory").getAsJsonObject().get("name").getAsString();
                        int id = jsonObject.get("faultCategory").getAsJsonObject().get("id").getAsInt();
                        genralFaultCatList.add(new list(faulname, id));
                        faultCategorySpinner.setAdapter(faultCatAdapter);
                      /*  OptionalInt index = IntStream.range(0, genralFaultCatList.size())
                                .filter(i -> genralFaultCatList.get(i).name.equals(faulname))
                                .findFirst();
                        if (index.isPresent()) {
                            int realid = index.getAsInt();
                            Log.d(TAG, "onResponse: fff" + realid);
                            faultCategorySpinner.setSelection(realid);
                        }*/
                    }
                    if (!(jsonObject.get("attendedBy").isJsonNull())) {
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                            JsonArray ja = jsonObject.get("attendedBy").getAsJsonArray();
                            Log.d(TAG, "onResponse: jaray attendedby" + ja);
                            for (int j = 0; j < ja.size(); j++) {
                                JsonObject jsonObject1 = ja.get(j).getAsJsonObject();
                                if (!(jsonObject1.isJsonNull())) {
                                    String techname = ja.get(j).getAsJsonObject().get("name").getAsString();
                                    Log.d(TAG, "onResponse: hhhh" + techname);
                                    techTv.setText(techname);
                                 /*   OptionalInt index = IntStream.range(0, genralTechnicalList.size())
                                            .filter(i -> genralTechnicalList.get(i).name.equals(techname))
                                            .findFirst();
                                    if (index.isPresent()) {
                                        int realid = index.getAsInt();
                                        //  technicianSpinner.setSelection(realid);
                                    }*/
                                }
                            }
                        }
                    }
/*
                    if (!(jsonObject.get("costCenter").isJsonNull())) {
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                            JsonObject jsonObject1 = jsonObject.get("costCenter").getAsJsonObject();
                            String costName = jsonObject1.get("costCenterName").getAsString();
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
*/

                    if ((jsonObject.get("locationDesc").getAsString()) != null) {
                        locDescEditText.setText(jsonObject.get("locationDesc").getAsString());
                    }
                    if ((jsonObject.get("faultCategoryDesc").getAsString()) != null) {
                        faultDetailsEditText.setText(jsonObject.get("faultCategoryDesc").getAsString());
                    }

                    if (!(jsonObject.get("observation").isJsonNull())) {
                        observationEditText.setText(jsonObject.get("observation").getAsString());
                    }
                    if (!(jsonObject.get("actionTaken").isJsonNull())) {
                        actionTakenEditText.setText(jsonObject.get("actionTaken").getAsString());
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



                    if (!(jsonObject.get("activationTime").isJsonNull())) {
                        String hour = jsonObject.get("activationTime").getAsJsonObject().get("hour").getAsString();
                        String minute = jsonObject.get("activationTime").getAsJsonObject().get("minute").getAsString();
                        String year = jsonObject.get("activationTime").getAsJsonObject().get("year").getAsString();
                        String monthValue = jsonObject.get("activationTime").getAsJsonObject().get("monthValue").getAsString();
                        String dayOfMonth = jsonObject.get("activationTime").getAsJsonObject().get("dayOfMonth").getAsString();

                        String time = hour + ":" + minute;
                        String date = dayOfMonth + "-" + monthValue + "-" + year + "  " + time;
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyy hh:mm aa ");
                        //    String sedate=sdf.format(date);
                        activationDate.setText(date);
                        //   activationTime.setText(time);
                    }
                    if (!(jsonObject.get("arrivalTime").isJsonNull())) {
                        String hour = jsonObject.get("arrivalTime").getAsJsonObject().get("hour").getAsString();
                        String minute = jsonObject.get("arrivalTime").getAsJsonObject().get("minute").getAsString();
                        String year = jsonObject.get("arrivalTime").getAsJsonObject().get("year").getAsString();
                        String monthValue = jsonObject.get("arrivalTime").getAsJsonObject().get("monthValue").getAsString();
                        String dayOfMonth = jsonObject.get("arrivalTime").getAsJsonObject().get("dayOfMonth").getAsString();

                        String time = hour + ":" + minute;
                        String date = dayOfMonth + "-" + monthValue + "-" + year + "   " + time;
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyy hh:mm aa ");
                        //   String sedate=sdf.format(date);
                        activationTime.setText(date);
                    }


                    if (jsonObject.get("remarks") != null) {
                        for (int i = 0; i < jsonObject.get("remarks").getAsJsonArray().size(); i++) {
                            if (!jsonObject.get("remarks").getAsJsonArray().get(i).isJsonNull()) {
                                String remString = jsonObject.get("remarks").getAsJsonArray()
                                        .get(i).getAsString();
                                // int remId = jsonObject.get("remarks").getAsJsonArray().get(i).getAsInt();
                                mlayout.addView(createNewEditText(remString, i));
                            }
                        }
                    }


                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(EditFaultReportActivity.this, "Failed :" + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("TAG", "onFailure: init frid " + t.getMessage());
                Log.d(TAG, "onFailure: init frid" + t.getCause());

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

      /*  technicalListAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, genralTechnicalList);
        technicalListAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        technicianSpinner.setAdapter(technicalListAdapter);*/

        statusListAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, genralStatusList);
        statusListAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statusSpinner.setAdapter(statusListAdapter);

      /*  costCenterListAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, genCostCebterList);
        costCenterListAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        costCenterSpinner.setAdapter(costCenterListAdapter);*/


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




    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                assert data != null;
                String eqip = data.getStringExtra("equipment_code");
                int equipId = data.getIntExtra("equipmentId", 0);
                equipmentTextView.setText(eqip);
            }
        } else if (requestCode == 10) {
            if (resultCode == RESULT_OK) {
                assert data != null;
                uploadFileTv.setText(data.getData().getPath());
                uploadFileBtn.setVisibility(View.VISIBLE);
                Uri uri = data.getData();
                //   callUpload(uri);

            }
        }
    }


    private void checkFieldsForEmptyValues() {
        if (statusSpinner.getSelectedItem().toString().equals("Pause")) {
            buttonEnableMethod();

        } else {
            buttonEnableMethod();


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

    private void updateFaultReport() throws ParseException {

        String contactNumber = requestorNumberEditText.getText().toString();

        if ( (TextUtils.isEmpty(requestorNumberEditText.getText())) || ((contactNumber.length() < 8))) {
            Toast.makeText(this, "Contact not valid", Toast.LENGTH_SHORT).show();

        } else {
            ProgressDialog mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setCancelable(false);
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setTitle("Updating");
            mProgressDialog.show();

            frId = frIdEditText.getText().toString();
            String reqName = reqNameEditText.getText().toString();
            String locationDesc = locDescEditText.getText().toString();
            faultDetailString = faultDetailsEditText.getText().toString();
            String observerString = observationEditText.getText().toString();
//            String diagnosesString = diagnosisEditText.getText().toString();
            String actionTakenString = actionTakenEditText.getText().toString();
            String statusString = statusSpinner.getSelectedItem().toString();
            String diagnosesString = null;
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

            attendedByIdsList = new ArrayList();

            String attendedbyString = techTv.getText().toString();
            List<String> attendedbylist = Arrays.asList(attendedbyString.split(", "));
            for (int j = 0; j < attendedbylist.size(); j++) {
                String techincian = attendedbylist.get(j);
                for (list list : genralTechnicalList) {
                    if (list.getName().equals(techincian)) {
                        Integer idd = list.getId();
                        attendedByIdsList.add(idd);
                    }
                }
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
            CostCenter costCenter = null;// = new CostCenter(costId);

            ArrayList<AttendedBy> attendedBy = new ArrayList<>();


            for (int j = 0; j < attendedByIdsList.size(); j++) {
                AttendedBy attendedbyObject = new AttendedBy();
                attendedbyObject.setId((Integer) attendedByIdsList.get(j));
                attendedBy.add(attendedbyObject);

            }

          /*  AttendedBy attendedBy = null;// = new AttendedBy(techId);
            ArrayList<AttendedBy> attendedByArrayList = new ArrayList<AttendedBy>(Collections.singleton(attendedBy));*/
            EditFaultReportRequest editFaultReportRequest = new EditFaultReportRequest(frId,
                    building,
                    location,
                    reqName,
                    department,
                    contactNumber,
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
                    attendedBy);

            Call<Void> callEditFaultReport = APIClient.getUserServices().updateReport(editFaultReportRequest, token, workSpaceid, role);
            callEditFaultReport.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.code() == 200) {
                        if (statusSpinner.getSelectedItem().toString().equals("Pause")) {
                            Intent intent = new Intent(EditFaultReportActivity.this, UploadFile.class);
                            intent.putExtra("frid", frId);
                            intent.putExtra("workspace", workSpaceid);
                            intent.putExtra("token", token);
                            intent.putExtra("user", role);
                            startActivity(intent);
                            finish();

                        } else {
                            Intent intent1 = new Intent(EditFaultReportActivity.this, Dashboard.class);
                            intent1.putExtra("workspaceId", workSpaceid);
                            startActivity(intent1);
                            finish();
                            Toast.makeText(EditFaultReportActivity.this, "Fault Report updated successfully", Toast.LENGTH_SHORT).show();

                        }


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

    private void initializeFab() {

        fab_before.setAlpha(0f);
        fab_after.setAlpha(0f);


        fab_before.setTranslationY(translationY);
        fab_after.setTranslationY(translationY);


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
                intent.putExtra("checkForFrid",frid);
                intent.putExtra("workspace", workSpaceid);
                intent.putExtra("frId", frIdEditText.getText().toString());
                startActivity(intent);

            }
        });
        fab_after.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditFaultReportActivity.this, BeforeImage.class);
                intent.putExtra("token", token);
                intent.putExtra("value", "After");
                intent.putExtra("checkForFrid",frid);
                intent.putExtra("workspace", workSpaceid);
                intent.putExtra("frId", frIdEditText.getText().toString());
                startActivity(intent);


            }
        });

    }

    private void openMenu() {
        isMenuOpen = !isMenuOpen;
        fab_main.animate().setInterpolator(interpolator).rotation(45f).setDuration(300).start();
        fab_before.animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start();
        fab_after.animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start();
    }

    private void closeMenu() {
        isMenuOpen = !isMenuOpen;
        fab_main.animate().setInterpolator(interpolator).rotation(0f).setDuration(300).start();
        fab_before.animate().translationY(translationY).alpha(0f).setInterpolator(interpolator).setDuration(300).start();
        fab_after.animate().translationY(translationY).alpha(0f).setInterpolator(interpolator).setDuration(300).start();
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
            LogoutClass logoutClass = new LogoutClass();
            logoutClass.logout(this);
        }
        return true;
    }
}