package com.synergy.FaultReport;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.synergy.APIClient;
import com.synergy.R;
import com.synergy.Search.EquipmentSearchAdapterforEdit;
import com.synergy.Search.EquipmentSearchResponseforEdit;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SelectTechnicianActivity extends AppCompatActivity {
    private static final String TAG = "";
    ListView listView;
    SearchView searchView;
    ArrayList<SelectTechnicianResponse> equipDetails = new ArrayList<SelectTechnicianResponse>();
    SelectTechnicianAdapter selectTechnicianAdapter;
    String token,role;
    String workspace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seach_layout_list);

        listView = findViewById(R.id.listview_equip_search);
        searchView = findViewById(R.id.search_equip_bar);
        Intent intent = getIntent();
        token = intent.getStringExtra("token");
        role=intent.getStringExtra("role");
        workspace = intent.getStringExtra("workspace");
        searchView.setIconifiedByDefault(false);


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String queryParam) {
                if (queryParam.isEmpty()) {
                    equipDetails.clear();
                  //  equipmentSearchAdapterforEdit.notifyDataSetInvalidated();
                } else {
                    equipDetails.clear();
                    getEquipment(queryParam, token);
                }
                return false;
            }
        });
    }


    private void getEquipment(String queryParam, String token) {
        equipDetails.clear();

        Call<SelectTechnicianResponse> call = APIClient.getUserServices().getTechnicianList(
                queryParam, token,workspace,role);

        call.enqueue(new Callback<SelectTechnicianResponse>() {
            @Override
            public void onResponse(Call<SelectTechnicianResponse> call, Response<SelectTechnicianResponse> response) {
                if (response.code() == 200) {
                    Toast.makeText(SelectTechnicianActivity.this, "Here are Details of Equipment", Toast.LENGTH_SHORT).show();
                    SelectTechnicianResponse equipmentSearchResponse = response.body();

                    int id=equipmentSearchResponse.getId();
                    String name=equipmentSearchResponse.getName();
                    String equipcode=equipmentSearchResponse.getUsername();
                    SelectTechnicianResponse ob=new SelectTechnicianResponse(id,name,equipcode);
                    equipDetails.add(ob);
                    selectTechnicianAdapter = new SelectTechnicianAdapter(SelectTechnicianActivity.this, equipDetails);
                    listView.setAdapter(selectTechnicianAdapter);
                    selectTechnicianAdapter.notifyDataSetChanged();

                }else
                    Toast.makeText(SelectTechnicianActivity.this, "Error : "+response.message(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<SelectTechnicianResponse> call, Throwable t) {
                Toast.makeText(SelectTechnicianActivity.this, "Failed to SearchActivity Equipment", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onFailure get message of fail:  " + t.getMessage());
                Log.d(TAG, "onFailure: cause" + t.getCause());
            }
        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);

    }
}