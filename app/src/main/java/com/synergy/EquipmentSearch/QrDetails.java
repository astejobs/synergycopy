package com.synergy.EquipmentSearch;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.synergy.APIClient;
import com.synergy.R;
import com.synergy.TaskResponse;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.synergy.MainActivityLogin.SHARED_PREFS;

public class QrDetails extends AppCompatActivity {

    private static final String TAG = "";
    private TextView codeTV, typeTV, nameTV, buildingTV, locationTV, assetTV;
    private Button searchFaultButton, searchTaskButton;
    private ProgressDialog mProgress;
    private ArrayList<String> frIdList = new ArrayList();
    private ListView listView;
    private String equipmentCode,token;
    private ArrayList<String> taskNumberList = new ArrayList<>();
    private ArrayAdapter<String> listAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_details);
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        token=sharedPreferences.getString("token", "");


        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        equipmentCode = intent.getStringExtra("code");
        String type = intent.getStringExtra("type");
        String locationName = intent.getStringExtra("locationName");
        String buildingName = intent.getStringExtra("building");
        String asset = intent.getStringExtra("asset");
        mProgress = new ProgressDialog(QrDetails.this);
        mProgress.setIndeterminate(true);
        mProgress.setCancelable(false);
        codeTV = findViewById(R.id.eq_code);
        typeTV = findViewById(R.id.eq_type);
        nameTV = findViewById(R.id.eq_name);
        buildingTV = findViewById(R.id.eq_building);
        locationTV = findViewById(R.id.eq_location);
        assetTV = findViewById(R.id.eq_assetn0);

        codeTV.setText("Equipment code: " + equipmentCode);
        typeTV.setText("Equipment type: " + type);
        nameTV.setText("Equipment name: " + name);
        buildingTV.setText("Equipment building: " + buildingName);
        locationTV.setText("Equipment location: " + locationName);
        assetTV.setText("Asset No: " + asset);

        listView = findViewById(R.id.list_view_equip);
        searchFaultButton = findViewById(R.id.first_btn);
        searchTaskButton = findViewById(R.id.second_btn);

        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "");

        searchFaultButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadFaultReportEquip();
            }
        });

        searchTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog(token);
            }
        });
    }

    private void loadFaultReportEquip() {
        mProgress.setTitle("Loading FaultReports...");
        mProgress.dismiss();
    }

    private void dialog(String token) {
        AlertDialog alertDialog = new AlertDialog.Builder(QrDetails.this).create(); //Read Update
        LayoutInflater layoutInflater = this.getLayoutInflater();
        View radioLayoutView = layoutInflater.inflate(R.layout.custom_dialog_radio_layout_qr, null);
        RadioGroup radioGroup = radioLayoutView.findViewById(R.id.radio_grp_id);

        alertDialog.setView(radioLayoutView);
        alertDialog.setTitle("Select Type of Tasks");

        alertDialog.setButton(Dialog.BUTTON_POSITIVE, "ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int selectedId = radioGroup.getCheckedRadioButtonId();
                RadioButton radioSelectedButton = radioGroup.findViewById(selectedId);
                String status = radioSelectedButton.getText().toString();
                status = status.substring(0, status.length() - 6);
                loadTaskOnEq(status, token);
            }
        });
        alertDialog.setButton(Dialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }

    private void loadTaskOnEq(String status, String token) {
        taskNumberList.clear();
        listView.setAdapter(listAdapter);
        mProgress.setTitle("Loading tasks...");
        mProgress.show();

        Call<List<TaskResponse>> call = APIClient.getUserServices().getTaskOnQrList(equipmentCode, status);        call.enqueue(new Callback<List<TaskResponse>>() {
            @Override
            public void onResponse(Call<List<TaskResponse>> call, Response<List<TaskResponse>> response) {
                mProgress.dismiss();
                if (response.code() == 200) {
                    /*mProgress.dismiss();
                    JsonArray jsonArray = response.body();
                    if (jsonArray.contains(null)) {
                        Toast.makeText(QrDetails.this, "No Tasks Available", Toast.LENGTH_SHORT).show();
                    } else {

                        for (int i = 0; i < jsonArray.size(); i++) {
                            JsonObject jo = jsonArray.get(i).getAsJsonObject();
                            String tno = String.valueOf(jo.get("task_number"));
                            tno = tno.replace("\"", "");
                            tno_list.add(tno);
                            listView.setVisibility(View.VISIBLE);
                            taskListAdapter = new ArrayAdapter<String>(QrDetails.this, android.R.layout.simple_list_item_1, tno_list);
                            listView.setAdapter(taskListAdapter);
                        }
                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


                                String taskNumber = tno_list.get(i);

                                Intent intent = new Intent(QrDetails.this, PmTaskActivity.class);
                                intent.putExtra("taskNumber", taskNumber);
                                intent.putExtra("token", token);
                                intent.putExtra("workspace", workspace);
                                intent.putExtra("username", user);
                                startActivity(intent);
                            }
                        });
*/

                    Integer taskId = null;
                    List<TaskResponse> responseList = response.body();
                    if (!responseList.isEmpty()) {
                        for (int i = 0; i < responseList.size(); i++) {
                            String taskNumber = responseList.get(i).getTask_number();
                            taskId = responseList.get(i).getTaskId();
                            taskNumberList.add(taskNumber);
                            listView.setVisibility(View.VISIBLE);
                            listAdapter = new ArrayAdapter<String>(QrDetails.this, android.R.layout.simple_list_item_1, taskNumberList);
                            listView.setAdapter(listAdapter);
                        }
                        int finalTaskId = taskId;
                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                                String taskNumber = taskNumberList.get(i);
                                Intent intent = new Intent(QrDetails.this, PmTaskActivity.class);
                                intent.putExtra("taskNumber", taskNumber);
                                intent.putExtra("taskId", finalTaskId);
                                startActivity(intent);
                            }
                        });
                    } else
                        Toast.makeText(QrDetails.this, "No Tasks Available!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<TaskResponse>> call, Throwable t) {
                Toast.makeText(QrDetails.this, "Failed: " + t.getMessage(), Toast.LENGTH_LONG).show();
                mProgress.dismiss();
            }
        });


    }

}
