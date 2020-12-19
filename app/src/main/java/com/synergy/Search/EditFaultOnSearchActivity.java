package com.synergy.Search;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.synergy.APIClient;
import com.synergy.CheckInternet;
import com.synergy.Constants;
import com.synergy.EquipmentSearch.EquipmentSearchActivity;
import com.synergy.FaultReport.BeforeImage;
import com.synergy.FaultReport.list;
import com.synergy.LogoutClass;
import com.synergy.MainActivityLogin;
import com.synergy.MyBaseActivity;
import com.synergy.R;

import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.OptionalInt;
import java.util.stream.IntStream;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.synergy.MainActivityLogin.SHARED_PREFS;

public class EditFaultOnSearchActivity extends MyBaseActivity {


    private FloatingActionButton fab_main, fab_before, fab_after;
    private Boolean isMenuOpen = false;
    private final OvershootInterpolator interpolator = new OvershootInterpolator();
    private Float translationY = 600f;
    private static final String TAG = "EditFault";
    private String timeS, remarksString, techName;
    private String frId;
    TextView uploadFileTv;
    private int idStatus;
    private String statusNameCurrent;
    Button uploadFileBtn;
    Intent uploadFileIntent;
    private String token, role;
    private String faultDetailString, username;
    private TextView equipmentIdTv;
    private Button plusbtn, deletebtn;
    private MaterialButton updateFaultReportButton, requestPauseButton, acceptButton, rejectButton, scanEquipmentBtn;
    private LinearLayout mlayout;
    String frid, workSpaceid, equipCode;
    private int remarksId;
    //  private EditText editText;
    private TextWatcher textWatcher;
    private List<String> remarksList = new ArrayList<>();
    private List<TextInputEditText> editTextList = new ArrayList<TextInputEditText>();

    List<String> genralStatusList = new ArrayList<String>();
    List<list> genralTechnicalList = new ArrayList<list>();


    int depId, divId, priId, buildId, locId, faultId, maintId;
    private Integer costId = null;
    private Integer techId = null;
    private ArrayAdapter<String> statusListAdapter;
    ProgressDialog progressDialog;
    LinearLayout linearLayoutdisable;
    boolean[] checkedItems;
    List<String> stockList = new ArrayList<>();
    String[] listItems;
    List attendedByIdsList;

    private double latitude, longitude;
    private AutoCompleteTextView autoCompleteSpinner;
    private TextInputEditText frIdEditText, deptSpinner, reqNameEditText, activationDate,
            activationTime, faultDetailsEditText, locDescEditText, faultCategorySpinner,
            divisionSpinner, locationSpinner, buildingSpinner, prioritySpinner,
            mainGrpSpinner, observationEditText,
            requestorNumberEditText, actionTakenEditText, equipmentTextView, editText, techTv;

    private RadioButton radioSelectedButton;
    private List<StatusItem> items = new ArrayList<StatusItem>();
    //private String onClickNotification;

    String tech = "Technician";
    String managingAgent = "ManagingAgent";
    private final CheckInternet checkInternet = new CheckInternet();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_edit_fault_on_search);

        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View viewLayout = layoutInflater.inflate(R.layout.activity_edit_fault_on_search, null, false);
        drawer.addView(viewLayout, 0);

        progressDialog = new ProgressDialog(com.synergy.Search.EditFaultOnSearchActivity.this);
        progressDialog.setTitle("Loading...");

        linearLayoutdisable = findViewById(R.id.layout_disable);
        toolbar.setTitle("FR Detail");
        setSupportActionBar(toolbar);
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        role = sharedPreferences.getString("role", "");
        token = sharedPreferences.getString("token", "");
        username = sharedPreferences.getString("username", "");

        Intent i = getIntent();
        frid = i.getStringExtra("frId");
        longitude = i.getDoubleExtra("longitude", 0);
        latitude = i.getDoubleExtra("latitude", 0);
        workSpaceid = i.getStringExtra("workspaceId");

        Log.d(TAG, "initviewsAndGetInitialData1: This is working here tharm1");
        Log.d(TAG, "onCreate: tharm1 " + role);

        initViews();
        callDisable();
        initializeFab();
        calltech();
        items.add(new StatusItem("Select status"));
        items.add(new StatusItem("Open"));
        items.add(new StatusItem("Pause"));
        items.add(new StatusItem("Closed"));
        items.add(new StatusItem("Completed"));
        items.add(new StatusItem("Pause Requested"));
        initviewsAndGetInitialData(frid);

        AutoCompleteTextAdaptar adapter = new AutoCompleteTextAdaptar(
                EditFaultOnSearchActivity.this, items, role);
        autoCompleteSpinner.setAdapter(adapter);

    }


    private void callDisable() {
        techTv.setEnabled(false);
        frIdEditText.setEnabled(false);
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
        actionTakenEditText.setEnabled(false);
        plusbtn.setEnabled(false);
        deletebtn.setEnabled(false);
    }


    private void calltech() {

        Call<JsonArray> callTechAllGet = APIClient.getUserServices().getTechnicianCall(token, workSpaceid);
        callTechAllGet.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                if (response.code() == 200) {

                    JsonArray jsonarraytech = response.body();
                    Log.d(TAG, "onResponse: hi my texh" + jsonarraytech);
                    for (int i = 0; i < jsonarraytech.size(); i++) {
                        String technicianName = jsonarraytech.get(i).getAsJsonObject().get("name").getAsString();
                        int id = jsonarraytech.get(i).getAsJsonObject().get("id").getAsInt();
                        genralTechnicalList.add(new list(technicianName, id));
                        stockList.add(technicianName);

                    }
                    listItems = new String[stockList.size()];
                    listItems = stockList.toArray(listItems);
                    checkedItems = new boolean[listItems.length];
                } else if (response.code() == 401) {
                    Toast.makeText(EditFaultOnSearchActivity.this, Constants.ERROR_CODE_401_MESSAGE, Toast.LENGTH_SHORT).show();
                    LogoutClass logoutClass = new LogoutClass();
                    logoutClass.logout(EditFaultOnSearchActivity.this);
                } else if (response.code() == 500) {
                    Toast.makeText(EditFaultOnSearchActivity.this, Constants.ERROR_CODE_500_MESSAGE, Toast.LENGTH_SHORT).show();
                } else if (response.code() == 404) {
                    Toast.makeText(EditFaultOnSearchActivity.this, Constants.ERROR_CODE_404_MESSAGE, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                progressDialog.dismiss();
                Log.d(TAG, "onFailure: tech" + t.getCause());
                Log.d(TAG, "onFailure: tech" + t.getMessage());
                Toast.makeText(EditFaultOnSearchActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }


    private void initViews() {
        acceptButton = findViewById(R.id.acceptbttn);
        rejectButton = findViewById(R.id.rejectbtn);
        autoCompleteSpinner = findViewById(R.id.statusSpinner);
        scanEquipmentBtn = findViewById(R.id.equipmentScanButton);
        techTv = findViewById(R.id.techtv);
        equipmentIdTv = findViewById(R.id.eq_id_send);
        actionTakenEditText = findViewById(R.id.actionTaken);
        mainGrpSpinner = findViewById(R.id.mainGrp);
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

        locationSpinner.setEnabled(false);
        buildingSpinner.setEnabled(false);
        requestorNumberEditText.setEnabled(false);
        prioritySpinner.setEnabled(false);
        divisionSpinner.setEnabled(false);
        locDescEditText.setEnabled(false);
        faultCategorySpinner.setEnabled(false);
        faultDetailsEditText.setEnabled(false);
        mainGrpSpinner.setEnabled(false);

        textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                //  checkFieldsForEmptyValues();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                //    checkFieldsForEmptyValues();
            }
        };


        if (role.equals(tech)) {
            genralStatusList.add("Select status");
            genralStatusList.add("Open");
            genralStatusList.add("Pause");
            genralStatusList.add("Completed");

        } else if (role.equals(managingAgent)) {
            genralStatusList.add("Select status");
            genralStatusList.add("Open");
            genralStatusList.add("Pause");
            genralStatusList.add("Closed");
            genralStatusList.add("Completed");
            genralStatusList.add("Pause Requested");

        } else {
            genralStatusList.add("Select status");
            genralStatusList.add("Open");
            genralStatusList.add("Pause");
            genralStatusList.add("Closed");
            genralStatusList.add("Completed");
        }
       /* AutoCompleteAdapter adapter = new AutoCompleteAdapter(
                this, android.R.layout.simple_dropdown_item_1line, android.R.id.text1, genralStatusList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statusSpinner.setAdapter(adapter);*/


        /*statusListAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, genralStatusList);
        statusListAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statusSpinner.setAdapter(statusListAdapter);*/
        scanEquipmentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditFaultOnSearchActivity.this, EquipmentSearchActivity.class);
                intent.putExtra("workspaceId", workSpaceid);
                intent.putExtra("value", "Fault");
                startActivity(intent);
                finish();
            }
        });

        plusbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mlayout.getChildCount() < 6) {
                    remarksId++;
                    mlayout.addView(createNewEditText(remarksString, remarksId));
                }
            }
        });
/*
        requestPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final AlertDialog.Builder builder = new AlertDialog.Builder(EditFaultOnSearchActivity.this);
                final View dialogView = getLayoutInflater().inflate(R.layout.custom_dialog_box, null);
                final RadioGroup group = dialogView.findViewById(R.id.radioPersonGroup);
                builder.setTitle("Fault Cost")
                        .setView(dialogView)
                        .setCancelable(false)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                if (group.getCheckedRadioButtonId() == -1) {
                                    Toast.makeText(EditFaultOnSearchActivity.this, "Please Select Cost", Toast.LENGTH_LONG).show();
                                    // no radio buttons are checked
                                } else {
                                    // one of the radio buttons is checked

                                    int selectedId = group.getCheckedRadioButtonId();
                                    radioSelectedButton = (RadioButton) dialogView.findViewById(selectedId);

                                    if (radioSelectedButton.getText().toString().equals("Greater than 1000")) {
                                        pauseMethodCall("greater");
                                    }
                                    if (radioSelectedButton.getText().toString().equals("Less than 1000")) {
                                        pauseMethodCall("less");
                                    }

                                }
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });

                AlertDialog alert = builder.create();
                alert.show();


            }
        });
*/

        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                acceptMethod();
            }
        });
        rejectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rejectMethod();
            }
        });

        deletebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteRemarks(view);
            }
        });
    }

    private void rejectMethod() {
        progressDialog.show();
        AcceptRejectBody acceptRejectBody = new AcceptRejectBody(frIdEditText.getText().toString());
        Call<Void> call = APIClient.getUserServices().getReject(token, workSpaceid, acceptRejectBody);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

                if (response.code() == 200) {
                    progressDialog.dismiss();
                    Toast.makeText(EditFaultOnSearchActivity.this, "Rejected ", Toast.LENGTH_SHORT).show();
                    finish();
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(EditFaultOnSearchActivity.this, "Failed to Reject", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onFailure: " + t.getMessage() + t.getCause());
                progressDialog.dismiss();
            }
        });
    }

    private void acceptMethod() {
        progressDialog.show();
        Log.d(TAG, "acceptMethod: work" + workSpaceid);
        AcceptRejectBody acceptRejectBody = new AcceptRejectBody(frIdEditText.getText().toString());
        Call<Void> call = APIClient.getUserServices().getAccept(token, workSpaceid, acceptRejectBody);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

                if (response.code() == 200) {
                    progressDialog.dismiss();
                    Toast.makeText(EditFaultOnSearchActivity.this, "Accepted", Toast.LENGTH_SHORT).show();
                    finish();
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(EditFaultOnSearchActivity.this, "Failed to Accepted", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onFailure: " + t.getMessage() + t.getCause());
                progressDialog.dismiss();
            }
        });


    }

    private void pauseMethodCall(String value) {
        progressDialog.show();
        PauseRequestBody pauseRequestBody = new PauseRequestBody(frIdEditText.getText().toString(), value);
        Call<Void> call = APIClient.getUserServices().getRequestPause(token, workSpaceid, pauseRequestBody);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

                if (response.code() == 200) {
                    progressDialog.dismiss();
                    Toast.makeText(EditFaultOnSearchActivity.this, "Paused", Toast.LENGTH_SHORT).show();
                    finish();
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(EditFaultOnSearchActivity.this, "Failed to Pause", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onFailure: " + t.getMessage() + t.getCause());
                progressDialog.dismiss();
            }
        });

    }


    private void initviewsAndGetInitialData(String data) {
        progressDialog.show();
        GeoLocation geolocation = new GeoLocation(latitude, longitude);
        SearchResposeWithLatLon respose = new SearchResposeWithLatLon(geolocation, data);
        Call<JsonObject> call = APIClient.getUserServices().getFindOne(workSpaceid, token, role, respose);
        call.enqueue(new Callback<JsonObject>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.code() == 200) {
                    progressDialog.dismiss();
                    JsonObject jsonObject = response.body();
                    assert jsonObject != null;
                    String statuscomming = null;
                    Log.d(TAG, "onResponse: json on frid search" + jsonObject);

                    if (!(jsonObject.get("status").isJsonNull())) {
                        statuscomming = jsonObject.get("status").getAsString();
                        autoCompleteSpinner.setText(statuscomming, false);
                    }
                    String editablevariable = jsonObject.get("editable").getAsString();

                    if (role.equals(Constants.ROLE_MANAGINGAGENT) && statuscomming.equals("Pause Requested")) {
                        acceptButton.setVisibility(View.VISIBLE);
                        rejectButton.setVisibility(View.VISIBLE);
                        autoCompleteSpinner.setDropDownHeight(0);

                    } else if (role.equals(Constants.ROLE_MANAGINGAGENT) && statuscomming.equals("Pause")
                            || statuscomming.equals("Closed")) {
                        acceptButton.setVisibility(View.GONE);
                        autoCompleteSpinner.setDropDownHeight(0);
                        rejectButton.setVisibility(View.GONE);
                    } else if (role.equals(Constants.ROLE_MANAGINGAGENT) && statuscomming.equals("Completed")) {
                        autoCompleteSpinner.setDropDownHeight(0);

                        if (!(jsonObject.get("equipment").isJsonNull())) {
                            if (editablevariable.equals("true")) {
                                Intent intent = new Intent(EditFaultOnSearchActivity.this,
                                        EditFaultReportActivity.class);
                                intent.putExtra("workspaceId", workSpaceid);
                                intent.putExtra("value", "Fault");
                                intent.putExtra("frId", jsonObject.get("frId").getAsString());
                                intent.putExtra("latOfSearch", latitude);
                                intent.putExtra("longOfSearch", longitude);
                                startActivity(intent);
                                finish();
                            } else if (editablevariable.equals("false")) {
                                Log.d(TAG, "onResponse: hi false" + editablevariable);
                                scanEquipmentBtn.setVisibility(View.VISIBLE);
                                autoCompleteSpinner.setDropDownHeight(0);

                            }

                        }else {if (jsonObject.get("equipment").isJsonNull()){

                        //added this now
                            Intent intent=new Intent(EditFaultOnSearchActivity.this,
                                    EditFaultReportActivity.class);
                            intent.putExtra("workspaceId", workSpaceid);
                            intent.putExtra("value", "Fault");
                            intent.putExtra("frId", jsonObject.get("frId").getAsString());
                            intent.putExtra("latOfSearch", latitude);
                            intent.putExtra("longOfSearch", longitude);
                            startActivity(intent);
                            finish();
                        }}


                    }


                    if (role.equals(Constants.ROLE_TECHNICIAN) && editablevariable.equals("true")) {
                        linearLayoutdisable.setVisibility(View.GONE);
                        Intent intent = new Intent(EditFaultOnSearchActivity.this, EditFaultReportActivity.class);
                        intent.putExtra("workspaceId", workSpaceid);
                        intent.putExtra("value", "Fault");
                        intent.putExtra("frId", jsonObject.get("frId").getAsString());
                        intent.putExtra("latOfSearch", latitude);
                        intent.putExtra("longOfSearch", longitude);
                        startActivity(intent);
                        finish();
                        //let edit

                    } else if (role.equals(Constants.ROLE_TECHNICIAN) && editablevariable.equals("false")) {
                        if (statuscomming.equals("Pause Requested") || statuscomming.equals("Completed")
                                || statuscomming.equals("Closed")) {
                            scanEquipmentBtn.setVisibility(View.GONE);
                            //added this line 1pm 14 dec before app submission
                            autoCompleteSpinner.setDropDownHeight(0);
                        } else if (statuscomming.equals("Open") || statuscomming.equals("Pause")) {

                            if (!(jsonObject.get("equipment").isJsonNull())) {
                                scanEquipmentBtn.setVisibility(View.VISIBLE);
                                autoCompleteSpinner.setDropDownHeight(0);
                                scanEquipmentBtn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(EditFaultOnSearchActivity.this, EquipmentSearchActivity.class);
                                        intent.putExtra("workspaceId", workSpaceid);
                                        intent.putExtra("value", "Fault");
                                        startActivity(intent);
                                        finish();
                                    }
                                });
                            } else if (jsonObject.get("equipment").isJsonNull()) {
                                Toast.makeText(EditFaultOnSearchActivity.this,
                                        "You are not currently at Fault Location", Toast.LENGTH_SHORT).show();

                            }


                        }
                    }


                    frIdEditText.setText(jsonObject.get("frId").getAsString());
                    reqNameEditText.setText(jsonObject.get("requestorName").getAsString());
                    requestorNumberEditText.setText(jsonObject.get("requestorContactNo").getAsString());
                    if (!(jsonObject.get("equipment").isJsonNull())) {
                        equipmentIdTv.setText(jsonObject.get("equipment").getAsJsonObject().get("id").getAsString());
                        equipmentTextView.setText(jsonObject.get("equipment").getAsJsonObject().get("name").getAsString());
                    }

                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                        String locationname = jsonObject.get("location").getAsJsonObject().get("name").getAsString();
                        locId = jsonObject.get("location").getAsJsonObject().get("id").getAsInt();
                        locationSpinner.setText(locationname);

                    }


                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                        String buildingname = jsonObject.get("building").getAsJsonObject().get("name").getAsString();
                        buildId = jsonObject.get("building").getAsJsonObject().get("id").getAsInt();
                        buildingSpinner.setText(buildingname);
                    }

                    if (!(jsonObject.get("division").isJsonNull())) {

                        String divisionname = jsonObject.get("division").getAsJsonObject().get("name").getAsString();
                        divId = jsonObject.get("division").getAsJsonObject().get("id").getAsInt();
                        divisionSpinner.setText(divisionname);
                    }
                    if (!(jsonObject.get("department").isJsonNull())) {
                        String deptname = jsonObject.get("department").getAsJsonObject().get("name").getAsString();
                        depId = jsonObject.get("department").getAsJsonObject().get("id").getAsInt();
                        deptSpinner.setText(deptname);

                    }
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                        String prirityname = jsonObject.get("priority").getAsJsonObject().get("name").getAsString();
                        priId = jsonObject.get("priority").getAsJsonObject().get("id").getAsInt();
                        prioritySpinner.setText(prirityname);
                    }
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                        String maintname = jsonObject.get("maintGrp").getAsJsonObject().get("name").getAsString();
                        maintId = jsonObject.get("maintGrp").getAsJsonObject().get("id").getAsInt();
                        mainGrpSpinner.setText(maintname);

                    }

                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                        String faulname = jsonObject.get("faultCategory").getAsJsonObject().get("name").getAsString();
                        faultId = jsonObject.get("faultCategory").getAsJsonObject().get("id").getAsInt();
                        faultCategorySpinner.setText(faulname);

                    }
                    if (!(jsonObject.get("attendedBy").isJsonNull())) {
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                            JsonArray ja = jsonObject.get("attendedBy").getAsJsonArray();
                            for (int j = 0; j < ja.size(); j++) {
                                JsonObject jsonObject1 = ja.get(j).getAsJsonObject();
                                if (!(jsonObject1.isJsonNull())) {
                                    String techname = ja.get(j).getAsJsonObject().get("name").getAsString();
                                    techTv.setText(techname);

                                }
                            }
                        }
                    }

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


                    if (!(jsonObject.get("activationTime").isJsonNull())) {
                        String hour = jsonObject.get("activationTime").getAsJsonObject().get("hour").getAsString();
                        String minute = jsonObject.get("activationTime").getAsJsonObject().get("minute").getAsString();
                        String year = jsonObject.get("activationTime").getAsJsonObject().get("year").getAsString();
                        String monthValue = jsonObject.get("activationTime").getAsJsonObject().get("monthValue").getAsString();
                        String dayOfMonth = jsonObject.get("activationTime").getAsJsonObject().get("dayOfMonth").getAsString();

                        String time = prependZero(hour) + ":" + prependZero(minute);
                        String date = prependZero(dayOfMonth) + "-" + prependZero(monthValue) + "-" + year + "  " + time;
                        activationDate.setText(date);
                    }
                    if (!(jsonObject.get("arrivalTime").isJsonNull())) {
                        String hour = jsonObject.get("arrivalTime").getAsJsonObject().get("hour").getAsString();
                        String minute = jsonObject.get("arrivalTime").getAsJsonObject().get("minute").getAsString();
                        String year = jsonObject.get("arrivalTime").getAsJsonObject().get("year").getAsString();
                        String monthValue = jsonObject.get("arrivalTime").getAsJsonObject().get("monthValue").getAsString();
                        String dayOfMonth = jsonObject.get("arrivalTime").getAsJsonObject().get("dayOfMonth").getAsString();

                        String time = prependZero(hour) + ":" + prependZero(minute);
                        String date = prependZero(dayOfMonth) + "-" + prependZero(monthValue) + "-" + year + "   " + time;
                        activationTime.setText(date);
                    }


                    if (jsonObject.get("remarks") != null) {
                        for (int i = 0; i < jsonObject.get("remarks").getAsJsonArray().size(); i++) {
                            if (!jsonObject.get("remarks").getAsJsonArray().get(i).isJsonNull()) {
                                String remString = jsonObject.get("remarks").getAsJsonArray()
                                        .get(i).getAsString();
                                mlayout.addView(createNewEditText(remString, i));
                            }
                        }
                    }


                } else if (response.code() == 401) {
                    Toast.makeText(EditFaultOnSearchActivity.this, Constants.ERROR_CODE_401_MESSAGE, Toast.LENGTH_SHORT).show();
                    LogoutClass logoutClass = new LogoutClass();
                    logoutClass.logout(EditFaultOnSearchActivity.this);
                } else if (response.code() == 500) {
                    Toast.makeText(EditFaultOnSearchActivity.this, Constants.ERROR_CODE_500_MESSAGE, Toast.LENGTH_SHORT).show();
                } else if (response.code() == 404) {
                    Toast.makeText(EditFaultOnSearchActivity.this, Constants.ERROR_CODE_404_MESSAGE, Toast.LENGTH_SHORT).show();
                }   else
                    Toast.makeText(EditFaultOnSearchActivity.this, "Error: " + response.code(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(EditFaultOnSearchActivity.this, "No Data Available", Toast.LENGTH_SHORT).show();
                Log.d("TAG", "onFailure: init frid " + t.getMessage());
                Log.d(TAG, "onFailure: init frid" + t.getCause());

            }
        });
    }

    private void deleteRemarks(View view) {
        if (!editTextList.isEmpty()) {
            if (mlayout.getChildCount() > 1) {
                if (autoCompleteSpinner.getText().equals("Closed")) {
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


    @NotNull
    private TextView createNewEditText(String remarksString, int remarksId) {
        LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(LinearLayout
                .LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lparams.setMargins(12, 8, 12, 8);

        editText = new TextInputEditText(this);
        editText.setId(remarksId);
        //    editText.setGravity(View.TEXT_ALIGNMENT_TEXT_START);

        editText.setBackgroundResource(R.drawable.mybutton);
        editText.setText(remarksString);
        editText.setLayoutParams(lparams);
        editText.setHint("   add remarks");
        editText.setMinHeight(60);
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


            }
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
                Intent intent = new Intent(EditFaultOnSearchActivity.this, BeforeImage.class);
                intent.putExtra("token", token);
                intent.putExtra("value", "Before");
                intent.putExtra("checkForFrid", frid);
                intent.putExtra("role", role);
                intent.putExtra("workspace", workSpaceid);
                intent.putExtra("frId", frIdEditText.getText().toString());
                intent.putExtra("status", autoCompleteSpinner.getText().toString());
                startActivity(intent);

            }
        });
        fab_after.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditFaultOnSearchActivity.this, BeforeImage.class);
                intent.putExtra("token", token);
                intent.putExtra("value", "After");
                intent.putExtra("checkForFrid", frid);
                intent.putExtra("workspace", workSpaceid);
                intent.putExtra("role", role);
                intent.putExtra("status", autoCompleteSpinner.getText().toString());
                intent.putExtra("frId", frIdEditText.getText().toString());
                startActivity(intent);


            }
        });

    }

    private void openMenu() {
        isMenuOpen = !isMenuOpen;
        fab_main.animate().setInterpolator(interpolator).rotation(0f).setDuration(300).start();
        fab_before.animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start();
        fab_after.animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start();
    }

    private void closeMenu() {
        isMenuOpen = !isMenuOpen;
        fab_main.animate().setInterpolator(interpolator).rotation(0f).setDuration(300).start();
        fab_before.animate().translationY(translationY).setInterpolator(interpolator).setDuration(300).start();
        fab_after.animate().translationY(translationY).setInterpolator(interpolator).setDuration(300).start();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(checkInternet, intentFilter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(checkInternet);
    }

    private String prependZero(String text) {
        if (text.length() == 1) {
            return '0' + text;
        }
        return text;
    }

}




