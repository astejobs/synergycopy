package com.synergy.Search;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.AdapterView;
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
import com.synergy.LogoutClass;
import com.synergy.MyBaseActivity;
import com.synergy.R;
import com.synergy.FaultReport.list;

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

public class EditFaultReportActivity extends MyBaseActivity {
    //test

    private FloatingActionButton fab_main, fab_before, fab_after;
    private Boolean isMenuOpen = false;
    private final OvershootInterpolator interpolator = new OvershootInterpolator();
    private Float translationY = 600f;
    private static final String TAG = "EditFault";
    private String timeS, remarksString, techName;
    private String frId;
    TextView uploadFileTv;
    private int idStatus;
    String statuscomming;
    private String statusComing = "";
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

    private double latitude, longitude, latOfSearch, longOfSearch;
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private double latitudeEquipment, longitudeEquipment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View viewLayout = layoutInflater.inflate(R.layout.activity_edit_fault_report, null, false);
        drawer.addView(viewLayout, 0);

        toolbar.setTitle("Edit Fault Report");
        setSupportActionBar(toolbar);

        progressDialog = new ProgressDialog(EditFaultReportActivity.this);
        progressDialog.setTitle("Loading...");

        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        role = sharedPreferences.getString("role", "");
        token = sharedPreferences.getString("token", "");
        username = sharedPreferences.getString("username", "");

        Intent i = getIntent();
        frid = i.getStringExtra("frId");
        Log.d(TAG, "onCreate: frid" + frId);
        longitude = i.getDoubleExtra("longitude", 0);
        latitude = i.getDoubleExtra("latitude", 0);
        latitudeEquipment = i.getDoubleExtra("lat", 0);
        longitudeEquipment = i.getDoubleExtra("long", 0);
        latOfSearch = i.getDoubleExtra("latOfSearch", 0);
        longOfSearch = i.getDoubleExtra("longOfSearch", 0);
        //        onClickNotification = "test";
//        onClickNotification = i.getStringExtra("onclick");
        workSpaceid = i.getStringExtra("workspaceId");
        equipCode = i.getStringExtra("equipcode");


        autoCompleteSpinner = findViewById(R.id.statusSpinner);
        AutoCompleteTextAdaptar adapter = new AutoCompleteTextAdaptar(EditFaultReportActivity.this, items, role);
        //ArrayAdapter<String> arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, items);
        autoCompleteSpinner.setAdapter(adapter);


        linearLayoutdisable = findViewById(R.id.layout_disable);
        if (role.equals("ManagingAgent")) {

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
            if (frid != null) {
                acceptButton.setVisibility(View.GONE);
                rejectButton.setVisibility(View.GONE);
                updateFaultReportButton.setVisibility(View.GONE);
                requestPauseButton.setVisibility(View.GONE);
                initviewsAndGetInitialData(frid);
            } else {
                requestPauseButton.setVisibility(View.GONE);
                initviewsAndGetInitialDataOnEquip(equipCode);
            }

        } else {
            items.add(new StatusItem("Select status"));
            items.add(new StatusItem("Open"));
            items.add(new StatusItem("Pause"));
            //      items.add(new StatusItem("Closed"));
            items.add(new StatusItem("Completed"));
            //     items.add(new StatusItem("Pause Requested"));
            initViews();
            acceptButton.setVisibility(View.GONE);
            rejectButton.setVisibility(View.GONE);
            initializeFab();
            calltech();
            if (frid != null) {
                //  updateFaultReportButton.setVisibility(View.GONE);
                // requestPauseButton.setVisibility(View.GONE);
                initviewsAndGetInitialData(frid);
            } else {
                initviewsAndGetInitialDataOnEquip(equipCode);

            }
        }
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
                    Toast.makeText(EditFaultReportActivity.this, Constants.ERROR_CODE_401_MESSAGE, Toast.LENGTH_SHORT).show();
                    LogoutClass logoutClass = new LogoutClass();
                    logoutClass.logout(EditFaultReportActivity.this);
                } else if (response.code() == 500) {
                    Toast.makeText(EditFaultReportActivity.this, Constants.ERROR_CODE_500_MESSAGE, Toast.LENGTH_SHORT).show();
                } else if (response.code() == 404) {
                    Toast.makeText(EditFaultReportActivity.this, Constants.ERROR_CODE_404_MESSAGE, Toast.LENGTH_SHORT).show();
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


    private void initViews() {
        acceptButton = findViewById(R.id.acceptbttn);
        rejectButton = findViewById(R.id.rejectbtn);
        scanEquipmentBtn = findViewById(R.id.equipmentScanButton);
        requestPauseButton = findViewById(R.id.requestPauseButton);
        techTv = findViewById(R.id.techtv);
        equipmentIdTv = findViewById(R.id.eq_id_send);
        updateFaultReportButton = findViewById(R.id.updateFaultReportButton);
        actionTakenEditText = findViewById(R.id.actionTaken);
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

        locationSpinner.setEnabled(false);
        buildingSpinner.setEnabled(false);
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

                checkFieldsForEmptyValues();
            }
        };
        observationEditText.addTextChangedListener(textWatcher);
        actionTakenEditText.addTextChangedListener(textWatcher);
        //autoCompleteSpinner.addTextChangedListener(textWatcher);


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

        plusbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mlayout.getChildCount() < 6) {
                    remarksId++;
                    mlayout.addView(createNewEditText(remarksString, remarksId));
                }
            }
        });
        requestPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final AlertDialog.Builder builder = new AlertDialog.Builder(EditFaultReportActivity.this);
                final View dialogView = getLayoutInflater().inflate(R.layout.custom_dialog_box, null);
                final RadioGroup group = dialogView.findViewById(R.id.radioPersonGroup);
                builder.setTitle("Fault Cost")
                        .setView(dialogView)
                        .setCancelable(false)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                if (group.getCheckedRadioButtonId() == -1) {
                                    Toast.makeText(EditFaultReportActivity.this, "Please Select Cost", Toast.LENGTH_LONG).show();
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

    private void rejectMethod() {
        progressDialog.show();
        AcceptRejectBody acceptRejectBody = new AcceptRejectBody(frIdEditText.getText().toString());
        Call<Void> call = APIClient.getUserServices().getReject(token, workSpaceid, acceptRejectBody);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

                if (response.code() == 200) {
                    progressDialog.dismiss();
                    Toast.makeText(EditFaultReportActivity.this, "Rejected ", Toast.LENGTH_SHORT).show();
                    finish();
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(EditFaultReportActivity.this, "Failed to Reject", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(EditFaultReportActivity.this, "Accepted", Toast.LENGTH_SHORT).show();
                    finish();
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(EditFaultReportActivity.this, "Failed to Accepted", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(EditFaultReportActivity.this, "Paused", Toast.LENGTH_SHORT).show();
                    finish();
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(EditFaultReportActivity.this, "Failed to Pause", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onFailure: " + t.getMessage() + t.getCause());
                progressDialog.dismiss();
            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
    private void callUploadsFileMethods() {
        uploadFileIntent = new Intent(Intent.ACTION_GET_CONTENT);
        uploadFileIntent.setType("*/*");
        startActivityForResult(Intent.createChooser(uploadFileIntent, "Select file"), 10);


    }

    private void initviewsAndGetInitialDataOnEquip(String data) {

        progressDialog.show();
        GeoLocation geoLocation = new GeoLocation(latitudeEquipment, longitudeEquipment);
        EquipmentGeoLocationClass geoLocationClass = new EquipmentGeoLocationClass(geoLocation, data);
        Call<JsonObject> call = APIClient.getUserServices().getEquipmentDetailsOnGeolocation
                (workSpaceid, token, role, geoLocationClass);
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
                        buildId = jsonObject.get("building").getAsJsonObject().get("id").getAsInt();
                        // genralBuildingList.add(new list(buildingname, id));
                        buildingSpinner.setText(buildingname);

                    }


                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                        String buildingname = jsonObject.get("location").getAsJsonObject().get("name").getAsString();
                        locId = jsonObject.get("location").getAsJsonObject().get("id").getAsInt();
                        // genralLoaction.add(new list(buildingname, id));
                        locationSpinner.setText(buildingname);
                        //locationSpinner.setAdapter(locationAdapter);
                    }

                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                        if (!(jsonObject.get("division").isJsonNull())) {

                            String divisionname = jsonObject.get("division").getAsJsonObject().get("name").getAsString();
                            divId = jsonObject.get("division").getAsJsonObject().get("id").getAsInt();
                            //   genralDivisionList.add(new list(divisionname, id));
                            divisionSpinner.setText(divisionname);
                            // divisionSpinner.setAdapter(divisionAdapter);
                        }
                    }
                    if (!(jsonObject.get("department").isJsonNull())) {
                        String deptname = jsonObject.get("department").getAsJsonObject().get("name").getAsString();
                        depId = jsonObject.get("department").getAsJsonObject().get("id").getAsInt();
                        // genralDepList.add(new list(deptname, id));
                        // deptSpinner.setAdapter(deptListAdapter);
                        deptSpinner.setText(deptname);

                    }
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                        String prirityname = jsonObject.get("priority").getAsJsonObject().get("name").getAsString();
                        priId = jsonObject.get("priority").getAsJsonObject().get("id").getAsInt();
                        //    genralPriorityList.add(new list(prirityname, id));
                        prioritySpinner.setText(prirityname);
                        //   prioritySpinner.setAdapter(priAdapter);
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
                        statusComing = jsonObject.get("status").getAsString();
                        if (genralStatusList.contains(statusComing)) {
                            autoCompleteSpinner.setText(statusComing, false);
                            //int index = genralStatusList.indexOf(statuscomming);
                            //statusSpinner.setText(statuscomming);
                            //  statusSpinner.setSelection(index);

                        }
                        //  statusSpinner.setText(statuscomming);
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
                    String editableVariable = jsonObject.get("editable").getAsString();

                    if (role.equals(Constants.ROLE_MANAGINGAGENT) && editableVariable.equals("false")) {
                        if (role.equals(Constants.ROLE_MANAGINGAGENT) && statusComing.equals("Closed")) {
                            updateFaultReportButton.setVisibility(View.GONE);
                            acceptButton.setVisibility(View.GONE);
                            rejectButton.setVisibility(View.GONE);
                            autoCompleteSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    if (autoCompleteSpinner.getText().toString().equals("Select status")) {
                                        autoCompleteSpinner.setText(statusComing, false);
                                    }
                                    if (autoCompleteSpinner.getText().toString().equals("Completed")) {
                                        autoCompleteSpinner.setText(statusComing, false);
                                    }
                                    if (autoCompleteSpinner.getText().toString().equals("Pause")) {
                                        autoCompleteSpinner.setText(statusComing, false);
                                    }

                                }
                            });

                        }


                    } else if ((role.equals(Constants.ROLE_MANAGINGAGENT) && editableVariable.equals("true"))) {


                        if (role.equals(Constants.ROLE_MANAGINGAGENT) && statusComing.equals("Pause Requested")) {
                            acceptButton.setVisibility(View.VISIBLE);
                            rejectButton.setVisibility(View.VISIBLE);
                            updateFaultReportButton.setVisibility(View.GONE);
                        } else if (role.equals(Constants.ROLE_MANAGINGAGENT) && statusComing.equals("Completed")) {
                            updateFaultReportButton.setVisibility(View.VISIBLE);
                            acceptButton.setVisibility(View.GONE);
                            rejectButton.setVisibility(View.GONE);
                            autoCompleteSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    if (autoCompleteSpinner.getText().toString().equals("Select status")) {
                                        autoCompleteSpinner.setText(statusComing, false);
                                    }
                                    if (autoCompleteSpinner.getText().toString().equals("Pause")) {
                                        autoCompleteSpinner.setText(statusComing, false);
                                    }

                                    if (autoCompleteSpinner.getText().toString().equals("Completed")) {
                                        autoCompleteSpinner.setText(statusComing, false);
                                    }
                                    if (autoCompleteSpinner.getText().toString().equals("Pause Requested")) {
                                        autoCompleteSpinner.setText(statusComing, false);
                                    }

                                }
                            });

                        } else if (role.equals(Constants.ROLE_MANAGINGAGENT) && statusComing.equals("Pause")) {
                            updateFaultReportButton.setVisibility(View.GONE);
                            acceptButton.setVisibility(View.GONE);
                            rejectButton.setVisibility(View.GONE);
                            autoCompleteSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    if (autoCompleteSpinner.getText().toString().equals("Select status")) {
                                        autoCompleteSpinner.setText(statusComing, false);
                                    }
                                    if (autoCompleteSpinner.getText().toString().equals("Completed")) {
                                        autoCompleteSpinner.setText(statusComing, false);
                                    }
                                    if (autoCompleteSpinner.getText().toString().equals("Closed")) {
                                        autoCompleteSpinner.setText(statusComing, false);
                                    }
                                    if (autoCompleteSpinner.getText().toString().equals("Pause")) {
                                        autoCompleteSpinner.setText(statusComing, false);
                                    }

                                }
                            });

                        } else if (role.equals(Constants.ROLE_MANAGINGAGENT) && statusComing.equals("Closed")) {
                            updateFaultReportButton.setVisibility(View.GONE);
                            acceptButton.setVisibility(View.GONE);
                            rejectButton.setVisibility(View.GONE);
                            autoCompleteSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    if (autoCompleteSpinner.getText().toString().equals("Select status")) {
                                        autoCompleteSpinner.setText(statusComing, false);
                                    }
                                    if (autoCompleteSpinner.getText().toString().equals("Completed")) {
                                        autoCompleteSpinner.setText(statusComing, false);
                                    }
                                    if (autoCompleteSpinner.getText().toString().equals("Pause")) {
                                        autoCompleteSpinner.setText(statusComing, false);
                                    }

                                }
                            });

                        } else if (role.equals(Constants.ROLE_MANAGINGAGENT) && statusComing.equals("Closeed")) {
                            updateFaultReportButton.setVisibility(View.VISIBLE);
                            updateFaultReportButton.setEnabled(true);
                            autoCompleteSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    if (autoCompleteSpinner.getText().toString().equals("Select status")) {
                                        autoCompleteSpinner.setText(statusComing, false);
                                    }
                                    if (autoCompleteSpinner.getText().toString().equals("Completed")) {
                                        autoCompleteSpinner.setText(statusComing, false);
                                    }
                                    if (autoCompleteSpinner.getText().toString().equals("Pause")) {
                                        autoCompleteSpinner.setText(statusComing, false);
                                    }

                                }
                            });

                        }
                    }

                    if (editableVariable.equals("false")) {
                        Toast.makeText(EditFaultReportActivity.this,
                                "You are not currently at the location of the equipment", Toast.LENGTH_LONG).show();
                        scanEquipmentBtn.setVisibility(View.GONE);
                        updateFaultReportButton.setVisibility(View.GONE);
                        requestPauseButton.setVisibility(View.GONE);
                        acceptButton.setVisibility(View.GONE);
                        rejectButton.setVisibility(View.GONE);

                    } else if (editableVariable.equals("true")) {

                        if (role.equals(Constants.ROLE_TECHNICIAN) && statusComing.equals("Open")) {
                            requestPauseButton.setVisibility(View.VISIBLE);
                            updateFaultReportButton.setVisibility(View.VISIBLE);
                            autoCompleteSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    //performAction(currentStatus);
                                    if (autoCompleteSpinner.getText().toString().equals("Select status")) {
                                        autoCompleteSpinner.setText(statusComing, false);
                                    }
                                    if (autoCompleteSpinner.getText().toString().equals("Pause")) {
                                        autoCompleteSpinner.setText(statusComing, false);
                                    }
                                    if (autoCompleteSpinner.getText().toString().equals("Closed")) {
                                        autoCompleteSpinner.setText(statusComing, false);
                                    }
                                    if (autoCompleteSpinner.getText().toString().equals("Completed")) {
                                        requestPauseButton.setEnabled(false);
                                    }
                                    if (autoCompleteSpinner.getText().toString().equals("Open")) {
                                        requestPauseButton.setEnabled(true);
                                    }
                                }
                            });
                        } else if (role.equals(Constants.ROLE_TECHNICIAN) && statusComing.equals("Pause")) {
                            requestPauseButton.setVisibility(View.INVISIBLE);
                            autoCompleteSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    if (autoCompleteSpinner.getText().toString().equals("Select status")) {
                                        autoCompleteSpinner.setText(statusComing, false);
                                    }
                                    if (autoCompleteSpinner.getText().toString().equals("Open")) {
                                        autoCompleteSpinner.setText(statusComing, false);
                                    }
                                    if (autoCompleteSpinner.getText().toString().equals("Closed")) {
                                        autoCompleteSpinner.setText(statusComing, false);
                                    }
                                }
                            });
                        } else if (role.equals(Constants.ROLE_TECHNICIAN) && statusComing.equals("Closed")) {
                            requestPauseButton.setVisibility(View.VISIBLE);
                            updateFaultReportButton.setVisibility(View.VISIBLE);
                            autoCompleteSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    if (autoCompleteSpinner.getText().toString().equals("Select status")) {
                                        autoCompleteSpinner.setText(statusComing, false);
                                    }
                                    if (autoCompleteSpinner.getText().toString().equals("Open")) {
                                        autoCompleteSpinner.setText(statusComing, false);
                                    }
                                    if (autoCompleteSpinner.getText().toString().equals("Completed")) {
                                        autoCompleteSpinner.setText(statusComing, false);
                                    }
                                    if (autoCompleteSpinner.getText().toString().equals("Pause")) {
                                        autoCompleteSpinner.setText(statusComing, false);
                                    }


                                }
                            });

                        } else if (role.equals(Constants.ROLE_TECHNICIAN) && statusComing.equals("Completed")) {
                            updateFaultReportButton.setVisibility(View.VISIBLE);
                            updateFaultReportButton.setEnabled(true);
                            autoCompleteSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    if (autoCompleteSpinner.getText().toString().equals("Select status")) {
                                        autoCompleteSpinner.setText(statusComing, false);
                                    }
                                    if (autoCompleteSpinner.getText().toString().equals("Open")) {
                                        autoCompleteSpinner.setText(statusComing, false);
                                    }
                                    if (autoCompleteSpinner.getText().toString().equals("Closed")) {
                                        autoCompleteSpinner.setText(statusComing, false);
                                    }
                                    if (autoCompleteSpinner.getText().toString().equals("Pause")) {
                                        autoCompleteSpinner.setText(statusComing, false);
                                    }

                                }
                            });
                        }
                    }
                } else if (response.code() == 401) {
                    Toast.makeText(EditFaultReportActivity.this, Constants.ERROR_CODE_401_MESSAGE, Toast.LENGTH_SHORT).show();
                    LogoutClass logoutClass = new LogoutClass();
                    logoutClass.logout(EditFaultReportActivity.this);
                } else if (response.code() == 500) {
                    Toast.makeText(EditFaultReportActivity.this, Constants.ERROR_CODE_500_MESSAGE, Toast.LENGTH_SHORT).show();
                } else if (response.code() == 404) {
                    Toast.makeText(EditFaultReportActivity.this, Constants.ERROR_CODE_404_MESSAGE, Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                progressDialog.dismiss();
                requestPauseButton.setEnabled(false);
                Toast.makeText(EditFaultReportActivity.this, "No Data Available", Toast.LENGTH_LONG).show();
                Log.d("TAG", "onFailure:equip call " + t.getMessage());
                Log.d(TAG, "onFailure: equip call" + t.getCause());

            }
        });
    }


    private void initviewsAndGetInitialData(String data) {
        // data from frid
        progressDialog.show();
        GeoLocation geolocation = new GeoLocation(latOfSearch, longOfSearch);
        SearchResposeWithLatLon respose = new SearchResposeWithLatLon(geolocation, data);
        Call<JsonObject> call = APIClient.getUserServices().getFindOne(workSpaceid, token, role, respose);
        call.enqueue(new Callback<JsonObject>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.code() == 200) {
                    progressDialog.dismiss();
                    JsonObject jsonObject = response.body();
                    Log.d(TAG, "onResponse: json on frid search" + jsonObject);
                    String editablevariable = jsonObject.get("editable").getAsString();
                    statuscomming = jsonObject.get("status").getAsString();

                    if (role.equals(Constants.ROLE_TECHNICIAN) && statuscomming.equals("Open")) {
                        acceptButton.setVisibility(View.INVISIBLE);
                        rejectButton.setVisibility(View.INVISIBLE);
                        requestPauseButton.setVisibility(View.VISIBLE);
                        autoCompleteSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                if (autoCompleteSpinner.getText().toString().equals("Select status")) {
                                    autoCompleteSpinner.setText(statuscomming, false);
                                }

                                if (autoCompleteSpinner.getText().toString().equals("Closed")) {
                                    autoCompleteSpinner.setText(statuscomming, false);

                                }
                                if (autoCompleteSpinner.getText().toString().equals("Pause")) {
                                    autoCompleteSpinner.setText(statuscomming,false);

                                }
                                if (autoCompleteSpinner.getText().toString().equals("Pause Requested")) {
                                    autoCompleteSpinner.setText(statuscomming, false);

                                }
                            }
                        });
                    }
                    if (role.equals(Constants.ROLE_TECHNICIAN) && statuscomming.equals("Pause")) {
                        acceptButton.setVisibility(View.INVISIBLE);
                        rejectButton.setVisibility(View.INVISIBLE);
                        requestPauseButton.setVisibility(View.GONE);
                        autoCompleteSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                if (autoCompleteSpinner.getText().toString().equals("Select status")) {
                                    autoCompleteSpinner.setText(statuscomming, false);
                                }
                                if (autoCompleteSpinner.getText().toString().equals("Open")) {
                                    autoCompleteSpinner.setText(statuscomming, false);
                                }
                                if (autoCompleteSpinner.getText().toString().equals("Closed")) {
                                    autoCompleteSpinner.setText(statuscomming, false);

                                }
                                if (autoCompleteSpinner.getText().toString().equals("Pause Requested")) {
                                    autoCompleteSpinner.setText(statuscomming, false);

                                }

                            }
                        });


                    }
                    if (editablevariable.equals("true")) {


                        updateFaultReportButton.setVisibility(View.VISIBLE);
                        //let edit

                    } else if (editablevariable.equals("false")) {
                        updateFaultReportButton.setEnabled(false);
                        if (!(jsonObject.get("equipment").isJsonNull())) {
                            scanEquipmentBtn.setVisibility(View.VISIBLE);
                            scanEquipmentBtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(EditFaultReportActivity.this, EquipmentSearchActivity.class);
                                    intent.putExtra("workspaceId", workSpaceid);
                                    intent.putExtra("value", "Fault");
                                    startActivity(intent);
                                }
                            });
                        } else if (jsonObject.get("equipment").isJsonNull()) {
                            Toast.makeText(EditFaultReportActivity.this, "you dont have equipment", Toast.LENGTH_SHORT).show();

                        }


                    }
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
                        locId = jsonObject.get("location").getAsJsonObject().get("id").getAsInt();
                        //  genralLoaction.add(new list(locationname, id));
                        locationSpinner.setText(locationname);
                        // locationSpinner.setAdapter(locationAdapter);

                    }


                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                        String buildingname = jsonObject.get("building").getAsJsonObject().get("name").getAsString();
                        buildId = jsonObject.get("building").getAsJsonObject().get("id").getAsInt();
                        // genralBuildingList.add(new list(buildingname, id));
                        //  buildingSpinner.setAdapter(buildingAdapter);
                        buildingSpinner.setText(buildingname);
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
                        divId = jsonObject.get("division").getAsJsonObject().get("id").getAsInt();
                        //   genralDivisionList.add(new list(divisionname, id));
                        divisionSpinner.setText(divisionname);
                        //  divisionSpinner.setAdapter(divisionAdapter);

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
                        depId = jsonObject.get("department").getAsJsonObject().get("id").getAsInt();
                        //  genralDepList.add(new list(deptname, id));
                        deptSpinner.setText(deptname);
                        //  deptSpinner.setAdapter(deptListAdapter);
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
                        priId = jsonObject.get("priority").getAsJsonObject().get("id").getAsInt();
                        //  genralPriorityList.add(new list(prirityname, id));
                        //  prioritySpinner.setAdapter(priAdapter);
                        prioritySpinner.setText(prirityname);
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
                        maintId = jsonObject.get("maintGrp").getAsJsonObject().get("id").getAsInt();
                        //  genralMaintGrp.add(new list(maintname, id));
                        mainGrpSpinner.setText(maintname);
                        //  mainGrpSpinner.setAdapter(maintAdapter);
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
                        faultId = jsonObject.get("faultCategory").getAsJsonObject().get("id").getAsInt();
                        //  genralFaultCatList.add(new list(faulname, id));
                        faultCategorySpinner.setText(faulname);
                        // faultCategorySpinner.setAdapter(faultCatAdapter);
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
                            for (int j = 0; j < ja.size(); j++) {
                                JsonObject jsonObject1 = ja.get(j).getAsJsonObject();
                                if (!(jsonObject1.isJsonNull())) {
                                    String techname = ja.get(j).getAsJsonObject().get("name").getAsString();
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
                        statuscomming = jsonObject.get("status").getAsString();
                        if (genralStatusList.contains(statuscomming)) {
                            autoCompleteSpinner.setText(statuscomming, false);
                        }
                        //   statusSpinner.setText(statuscomming);
                      /*  int indeex = 0;
                        if (genralStatusList.contains(statuscomming)) {
                            indeex = statusListAdapter.getPosition(statuscomming);
                            statusSpinner.setSelection(indeex);
                            statusListAdapter.notifyDataSetChanged();
                        }*/

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
                                // int remId = jsonObject.get("remarks").getAsJsonArray().get(i).getAsInt();
                                mlayout.addView(createNewEditText(remString, i));
                            }
                        }
                    }


                } else if (response.code() == 401) {
                    Toast.makeText(EditFaultReportActivity.this, Constants.ERROR_CODE_401_MESSAGE, Toast.LENGTH_SHORT).show();
                    LogoutClass logoutClass = new LogoutClass();
                    logoutClass.logout(EditFaultReportActivity.this);
                } else if (response.code() == 500) {
                    Toast.makeText(EditFaultReportActivity.this, Constants.ERROR_CODE_500_MESSAGE, Toast.LENGTH_SHORT).show();
                } else if (response.code() == 404) {
                    Toast.makeText(EditFaultReportActivity.this, Constants.ERROR_CODE_404_MESSAGE, Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(EditFaultReportActivity.this, "No Data Available", Toast.LENGTH_SHORT).show();
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
                //   callUpload(uri);

            }
        }
    }


    private void checkFieldsForEmptyValues() {
        if (role.equals(managingAgent) && (!(frid == null))) {
            Log.d(TAG, "checkFieldsForEmptyValues: yes manage work");
            requestPauseButton.setVisibility(View.GONE);
            updateFaultReportButton.setVisibility(View.GONE);
            acceptButton.setVisibility(View.GONE);
            autoCompleteSpinner.setEnabled(false);
            rejectButton.setVisibility(View.GONE);
            autoCompleteSpinner.setDropDownHeight(0);
        }
        if (role.equals(tech) && ((frid != null))) {
            Log.d(TAG, "checkFieldsForEmptyValues: yes tech serach");
            //  requestPauseButton.setVisibility(View.GONE);
            //   updateFaultReportButton.setVisibility(View.GONE);
            acceptButton.setVisibility(View.GONE);
            //   autoCompleteSpinner.setEnabled(false);
            rejectButton.setVisibility(View.GONE);
            //  autoCompleteSpinner.dismissDropDown();
            // autoCompleteSpinner.setDropDownHeight(0);
        }


        if (role.equals(tech) && ((frid == null))) {
            Iterator<StatusItem> iterator = items.iterator();
            while (iterator.hasNext()) {
                StatusItem next = iterator.next();
                if (next.getStatus().equals("Pause Requested")) {
                    iterator.remove();
                }
            }
            if (autoCompleteSpinner.getText().toString().equals("Completed")) {
                requestPauseButton.setEnabled(false);

                buttonEnableMethod();
            }
            if (autoCompleteSpinner.getText().toString().equals("Open")) {
                requestPauseButton.setEnabled(true);

                buttonEnableMethod();
            }
            if (autoCompleteSpinner.getText().toString().equals("Closed")) {

                requestPauseButton.setEnabled(false);
                updateFaultReportButton.setEnabled(false);

            }
            if (autoCompleteSpinner.getText().toString().equals("Pause Requested")) {
                updateFaultReportButton.setEnabled(false);
                requestPauseButton.setEnabled(false);
            }
            if (autoCompleteSpinner.getText().toString().equals("Pause") ||
                    autoCompleteSpinner.getText().toString().equals("Select status")) {
                updateFaultReportButton.setEnabled(false);
                requestPauseButton.setEnabled(false);

                genralStatusList.remove("Open");
                //   statusListAdapter.notifyDataSetChanged();
/*
                autoCompleteSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        autoCompleteSpinner.setDropDownHeight(0);
                    }
                });
*/
            }
        }
        if (role.equals(managingAgent) && (frid == null)) {

            if ((autoCompleteSpinner.getText().toString().equals("Pause Requested")) && (frid == null)) {
                acceptButton.setVisibility(View.VISIBLE);
                rejectButton.setVisibility(View.VISIBLE);
                autoCompleteSpinner.setEnabled(false);
                genralStatusList.clear();
                autoCompleteSpinner.setDropDownHeight(0);
                updateFaultReportButton.setVisibility(View.GONE);
                // buttonEnableMethod();
            }
            if (autoCompleteSpinner.getText().toString().equals("Open")) {
                acceptButton.setVisibility(View.GONE);
                rejectButton.setVisibility(View.GONE);
                // statusListAdapter.notifyDataSetChanged();
                Iterator<StatusItem> iterator = items.iterator();
                while (iterator.hasNext()) {
                    StatusItem next = iterator.next();
                    if (next.getStatus().equals("Pause Requested")) {
                        iterator.remove();
                    }
                }
                updateFaultReportButton.setVisibility(View.VISIBLE);
                buttonEnableMethod();
            }
            if (autoCompleteSpinner.getText().toString().equals("Closed")) {
                acceptButton.setVisibility(View.GONE);
                Iterator<StatusItem> iterator = items.iterator();
                while (iterator.hasNext()) {
                    StatusItem next = iterator.next();
                    if (next.getStatus().equals("Pause Requested")) {
                        iterator.remove();
                    }
                }
                //   statusListAdapter.notifyDataSetChanged();
                rejectButton.setVisibility(View.GONE);
                updateFaultReportButton.setVisibility(View.VISIBLE);
                buttonEnableMethod();
            }
            if (autoCompleteSpinner.getText().toString().equals("Pause")
                    || autoCompleteSpinner.getText().toString().equals("Completed")) {
                updateFaultReportButton.setEnabled(false);
                Iterator<StatusItem> iterator = items.iterator();
                while (iterator.hasNext()) {
                    StatusItem next = iterator.next();
                    if (next.getStatus().equals("Pause Requested")) {
                        iterator.remove();
                    }
                }

                //  statusListAdapter.notifyDataSetChanged();
                acceptButton.setVisibility(View.GONE);
                rejectButton.setVisibility(View.GONE);

            }
        }


       /* if (statusSpinner.getText().equals("Pause")) {
            buttonEnableMethod();

        } else {
            buttonEnableMethod();


        }
*/
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
        if ((TextUtils.isEmpty(requestorNumberEditText.getText())) || ((contactNumber.length() < 8))) {
            Toast.makeText(this, "Contact not valid", Toast.LENGTH_SHORT).show();

        } else if (techTv.getText().toString().isEmpty()) {
            Toast.makeText(this, "Technician not Selected", Toast.LENGTH_SHORT).show();
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
            String actionTakenString = actionTakenEditText.getText().toString();
            String statusString = autoCompleteSpinner.getText().toString();
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
                Log.d(TAG, "updateFaultReport: technician" + genralTechnicalList);
                for (list list : genralTechnicalList) {
                    if (list.getName().equals(techincian)) {
                        Integer idd = list.getId();
                        attendedByIdsList.add(idd);
                        Log.d(TAG, "updateFaultReport: gentech " + genralTechnicalList);
                        Log.d(TAG, "updateFaultReport: attendedlist" + attendedbylist);
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

            Call<Void> callEditFaultReport = APIClient.getUserServices().updateReport(editFaultReportRequest,
                    token, workSpaceid, role);
            callEditFaultReport.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.code() == 200) {


                        if (autoCompleteSpinner.getText().toString().equals("Pause")) {
                            Intent intent = new Intent(EditFaultReportActivity.this, UploadFile.class);
                            intent.putExtra("frid", frId);
                            intent.putExtra("workspace", workSpaceid);
                            intent.putExtra("token", token);
                            intent.putExtra("user", role);
                            startActivity(intent);
                            finish();

                        }
                        Toast.makeText(EditFaultReportActivity.this, "Fault Report Updated", Toast.LENGTH_LONG).show();
                        updateFaultReportButton.setVisibility(View.INVISIBLE);
                        /*else {
                            Intent intent1 = new Intent(EditFaultReportActivity.this, Dashboard.class);
                            intent1.putExtra("workspaceId", workSpaceid);
                            startActivity(intent1);
                            finish();
                            Toast.makeText(EditFaultReportActivity.this, "Fault Report updated successfully", Toast.LENGTH_SHORT).show();

                        }*/


                    } else if (response.code() == 401) {
                        Toast.makeText(EditFaultReportActivity.this, Constants.ERROR_CODE_401_MESSAGE, Toast.LENGTH_SHORT).show();
                        LogoutClass logoutClass = new LogoutClass();
                        logoutClass.logout(EditFaultReportActivity.this);
                    } else if (response.code() == 500) {
                        Toast.makeText(EditFaultReportActivity.this, Constants.ERROR_CODE_500_MESSAGE, Toast.LENGTH_SHORT).show();
                    } else if (response.code() == 404) {
                        Toast.makeText(EditFaultReportActivity.this, Constants.ERROR_CODE_404_MESSAGE, Toast.LENGTH_SHORT).show();
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
                Intent intent = new Intent(EditFaultReportActivity.this, BeforeImage.class);
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


    private String prependZero(String text) {
        if (text.length() == 1) {
            return '0' + text;
        }
        return text;
    }

}