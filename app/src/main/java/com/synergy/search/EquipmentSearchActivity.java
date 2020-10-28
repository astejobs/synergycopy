package com.synergy.search;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.synergy.APIClient;
import com.synergy.EquipmentSearch.EquipmentSearchResponse;
import com.synergy.R;

import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EquipmentSearchActivity extends AppCompatActivity {
    private static final String TAG = "";
    ListView listView;
    SearchView searchView;
    ArrayList<EquipmentSearchResponse> equipDetails = new ArrayList<>();
    EquipmentSearchAdapter equipmentSearchAdapter;
    String token;
    String workspace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seach_layout_list);

        listView = findViewById(R.id.listview_equip_search);
        searchView = findViewById(R.id.search_equip_bar);
        Intent intent = getIntent();
        token = intent.getStringExtra("token");
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
                    equipmentSearchAdapter.notifyDataSetInvalidated();
                } else {
                    equipDetails.clear();
                  //  getEquipment(queryParam, token, workspace);
                }
                return false;
            }
        });
    }

/*
    private void getEquipment(String queryParam, String token, String workspace) {
        equipDetails.clear();

        Call<List<EquipmentSearchResponse>> call = APIClient.getUserServices().getEquipment(queryParam, token, workspace);

        call.enqueue(new Callback<List<EquipmentSearchResponse>>() {
            @Override
            public void onResponse(Call<List<EquipmentSearchResponse>> call, Response<List<EquipmentSearchResponse>> response) {
                if (response.code() == 200) {
                    List<EquipmentSearchResponse> equipmentSearchResponse = response.body();
                    for (EquipmentSearchResponse equipment : equipmentSearchResponse) {
                        // contacts.clear();
                        EquipmentSearchResponse s = new EquipmentSearchResponse();
                    //    int id = equipment.getId();
                        String name = equipment.getName();
                        String eq_code = equipment.getEquipmentCode();

                   //     s.setId(id);
                        s.setName(name);
                        s.setEquipmentCode(eq_code);
                        equipDetails.add(s);
                    }
                    equipmentSearchAdapter = new EquipmentSearchAdapter(EquipmentSearchActivity.this, equipDetails);
                    listView.setAdapter(equipmentSearchAdapter);
                    equipmentSearchAdapter.notifyDataSetChanged();

                }
            }

            @Override
            public void onFailure(Call<List<EquipmentSearchResponse>> call, Throwable t) {
                Toast.makeText(EquipmentSearchActivity.this, "Failed to SearchActivity Equipment", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onFailure get message of fail:  " + t.getMessage());
                Log.d(TAG, "onFailure: cause" + t.getCause());
            }
        });
    }
*/


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);

    }
}