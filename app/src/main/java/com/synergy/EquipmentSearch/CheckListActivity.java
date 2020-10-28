package com.synergy.EquipmentSearch;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.synergy.APIClient;
import com.synergy.GetCheckListResponse;
import com.synergy.R;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.synergy.MainActivityLogin.SHARED_PREFS;

public class CheckListActivity extends AppCompatActivity {

    private String descType;
    private LinearLayout linearLayout;
    private Button saveButton;
    private final List<EditText> remarksEditTextList = new ArrayList<>();
    private final List<TextView> textViewList = new ArrayList<>();
    private final List<Object> objectList = new ArrayList<>();
    private final List<String> descTypeList = new ArrayList<>();
    private final List<Integer> idList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_list);

        linearLayout = findViewById(R.id.linearLayoutCheck);
        saveButton = findViewById(R.id.saveButtonCheckList);
        saveButton.setEnabled(false);

        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "");

        Intent intent = getIntent();
        int taskId = intent.getIntExtra("taskId", 0);

        getCheckList(taskId, token);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveChecklist(taskId, token);
            }
        });


    }

    private void saveChecklist(int taskId, String token) {

        ProgressDialog mProgress = new ProgressDialog(this);
        mProgress.setIndeterminate(true);
        mProgress.setCancelable(false);
        mProgress.setMessage("Saving....");
        mProgress.show();

        List<CheckListAddRequest> checkListAddRequestList = new ArrayList<>();
        for (int i = 0; i < textViewList.size(); i++) {
            String objectName;
            if (descTypeList.get(i).equals("QUESTIONANSWER")) {
                Spinner objectSpinner = (Spinner) objectList.get(i);
                objectName = objectSpinner.getSelectedItem().toString();
            } else {
                EditText editText = (EditText) objectList.get(i);
                objectName = editText.getText().toString();
            }
            EditText remarksEditText = remarksEditTextList.get(i);
            String remarks = remarksEditText.getText().toString();

            int id = idList.get(i);

            CheckListAddRequest checkListAddRequest = new CheckListAddRequest(id, taskId, objectName, remarks);
            checkListAddRequestList.add(checkListAddRequest);
        }
        Call<List<CheckListAddRequest>> saveCall = APIClient.getUserServices().postCheckList(checkListAddRequestList, token);

        saveCall.enqueue(new Callback<List<CheckListAddRequest>>() {
            @Override
            public void onResponse(Call<List<CheckListAddRequest>> call, Response<List<CheckListAddRequest>> response) {
                if (response.code() == 200) {
                    Toast.makeText(CheckListActivity.this, "Success", Toast.LENGTH_SHORT).show();
                    finish();
                } else
                    Toast.makeText(CheckListActivity.this, "Error: " + response.code(), Toast.LENGTH_SHORT).show();

                mProgress.dismiss();
            }

            @Override
            public void onFailure(Call<List<CheckListAddRequest>> call, Throwable t) {
                Toast.makeText(CheckListActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                mProgress.dismiss();
            }
        });
    }

    private void getCheckList(int taskId, String token) {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Getting Checklist");
        progressDialog.show();
        Call<List<GetCheckListResponse>> checkListResponseCall = APIClient.getUserServices().getChecklist(String.valueOf(taskId), token);
        checkListResponseCall.enqueue(new Callback<List<GetCheckListResponse>>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(Call<List<GetCheckListResponse>> call, Response<List<GetCheckListResponse>> response) {
                if (response.code() == 200) {

                    List<GetCheckListResponse> listResponse = response.body();
                    for (int i = 0; i < listResponse.size(); i++) {
                        descType = listResponse.get(i).getChecklistProperty().getDescriptionType();
                        descTypeList.add(descType);
                        int id = listResponse.get(i).getId();
                        idList.add(id);
                        String desc = listResponse.get(i).getChecklistProperty().getDescription();
                        String status = listResponse.get(i).getStatus();
                        String remarks = listResponse.get(i).getRemarks();

                        createDynamicLayout(desc, remarks, status);
                        saveButton.setEnabled(true);
                    }

                } else
                    Toast.makeText(CheckListActivity.this, "Error: " + response.code(), Toast.LENGTH_LONG).show();

                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<List<GetCheckListResponse>> call, Throwable t) {
                Toast.makeText(CheckListActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void createDynamicLayout(String desc, String remarks, String status) {
        LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        TextView textView = new TextView(this);
        textView.setId(View.generateViewId());
        lparams.setMargins(20, 20, 20, 20);
        textView.setLayoutParams(lparams);
        textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        textView.setText(desc);
        linearLayout.addView(textView);
        textViewList.add(textView);

        if (descType.equals("QUESTIONANSWER")) {
            Spinner statusSpinner = new Spinner(this);
            statusSpinner.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            List<String> statusList = new ArrayList<>();
            statusList.add(status);
            statusList.add("no");
            statusList.add("yes");
            statusList.add("na");
            List<String> removedDupList = statusList.stream().distinct().collect(Collectors.toList());
            statusSpinner.setSelection(removedDupList.indexOf(status));
            ArrayAdapter<String> statusAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, removedDupList);
            statusSpinner.setAdapter(statusAdapter);
            statusSpinner.setId(View.generateViewId());
            lparams.setMargins(20, 20, 20, 20);
            statusSpinner.setLayoutParams(lparams);
            objectList.add(statusSpinner);
            linearLayout.addView(statusSpinner);
        } else {
            EditText editText = new EditText(this);
            editText.setHeight(60);
            editText.setWidth(300);
            editText.setInputType(InputType.TYPE_CLASS_TEXT);
            editText.setId(View.generateViewId());
            lparams.setMargins(20, 20, 20, 40);
            editText.setLayoutParams(lparams);
            objectList.add(editText);
            linearLayout.addView(editText, lparams);
        }

        EditText remarksEditText = new EditText(this);
        remarksEditText.setHeight(60);
        remarksEditText.setWidth(300);
        remarksEditText.setText(remarks);
        remarksEditText.setHint("Remarks");
        remarksEditText.setInputType(InputType.TYPE_CLASS_TEXT);
        remarksEditText.setId(View.generateViewId());
        lparams.setMargins(20, 20, 20, 40);
        remarksEditText.setLayoutParams(lparams);
        remarksEditTextList.add(remarksEditText);
        linearLayout.addView(remarksEditText, lparams);
    }
}