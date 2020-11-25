package com.synergy.Search;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;

import com.synergy.R;

import java.util.ArrayList;

import android.widget.Toast;

import com.synergy.APIClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EquipmentSearchActivityforEdit extends AppCompatActivity {
    private static final String TAG = "";
    ListView listView;
    SearchView searchView;
    ArrayList<EquipmentSearchResponseforEdit> equipDetails = new ArrayList<EquipmentSearchResponseforEdit>();
    EquipmentSearchAdapterforEdit equipmentSearchAdapterforEdit;
    String token, role;
    String workspace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seach_layout_list);

        listView = findViewById(R.id.listview_equip_search);
        searchView = findViewById(R.id.search_equip_bar);
        Intent intent = getIntent();
        token = intent.getStringExtra("token");
        role = intent.getStringExtra("role");
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

        Call<List<EquipmentSearchResponseforEdit>> call = APIClient.getUserServices().
                getSearchEquipment(queryParam, token, workspace, role);

        call.enqueue(new Callback<List<EquipmentSearchResponseforEdit>>() {
            @Override
            public void onResponse(Call<List<EquipmentSearchResponseforEdit>> call,
                                   Response<List<EquipmentSearchResponseforEdit>> response) {
                if (response.code() == 200) {
                    Toast.makeText(EquipmentSearchActivityforEdit.this, "Here are Details of Equipment", Toast.LENGTH_SHORT).show();
                    List<EquipmentSearchResponseforEdit> equipmentSearchResponse = response.body();
                    for (int i = 0; i < equipmentSearchResponse.size(); i++) {
                        int id = equipmentSearchResponse.get(i).getId();
                        String name = equipmentSearchResponse.get(i).getName();
                        String equipcode = equipmentSearchResponse.get(i).getEquipmentCode();
                        String equipmentType = equipmentSearchResponse.get(i).getEquipmentType();
                        EquipmentSearchResponseforEdit ob = new EquipmentSearchResponseforEdit(equipcode,name,id,equipmentType);

                        equipDetails.add(ob);
                    }
                    equipmentSearchAdapterforEdit = new
                            EquipmentSearchAdapterforEdit(EquipmentSearchActivityforEdit.this, equipDetails);
                    listView.setAdapter(equipmentSearchAdapterforEdit);
                    equipmentSearchAdapterforEdit.notifyDataSetChanged();

                } else
                    Toast.makeText(EquipmentSearchActivityforEdit.this, "Error : " + response.message(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<List<EquipmentSearchResponseforEdit>> call, Throwable t) {
                Toast.makeText(EquipmentSearchActivityforEdit.this, "Failed to SearchActivity Equipment", Toast.LENGTH_SHORT).show();
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