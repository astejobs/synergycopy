package com.synergy.Search;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SearchView;
<<<<<<< Updated upstream:app/src/main/java/com/synergy/Search/EquipmentSearchActivity.java

import androidx.appcompat.app.AppCompatActivity;

import com.synergy.EquipmentSearch.EquipmentSearchResponse;
import com.synergy.R;

import java.util.ArrayList;
=======
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.synergy.APIClient;
import com.synergy.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
>>>>>>> Stashed changes:app/src/main/java/com/synergy/Search/EquipmentSearchActivityforEdit.java

public class EquipmentSearchActivityforEdit extends AppCompatActivity {
    private static final String TAG = "";
    ListView listView;
    SearchView searchView;
    ArrayList<EquipmentSearchResponseforEdit> equipDetails = new ArrayList<EquipmentSearchResponseforEdit>();
    EquipmentSearchAdapterforEdit equipmentSearchAdapterforEdit;
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
                    equipmentSearchAdapterforEdit.notifyDataSetInvalidated();
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

        Call<EquipmentSearchResponseforEdit> call = APIClient.getUserServices().getSearchEquipment(queryParam, token);

        call.enqueue(new Callback<EquipmentSearchResponseforEdit>() {
            @Override
            public void onResponse(Call<EquipmentSearchResponseforEdit> call, Response<EquipmentSearchResponseforEdit> response) {
                if (response.code() == 200) {
                    Toast.makeText(EquipmentSearchActivityforEdit.this, "Here are Details of Equipment", Toast.LENGTH_SHORT).show();
                    EquipmentSearchResponseforEdit equipmentSearchResponse = response.body();
/*
                    for (EquipmentSearchResponseforEdit equipment : equipmentSearchResponse) {
                        // contacts.clear();
                        EquipmentSearchResponseforEdit n = new EquipmentSearchResponseforEdit();
                        int id = equipment.getId();
                        String name = equipment.getName();
                        String eq_code = equipment.getEquipmentCode();

                        n.setId(id);
                        n.setName(name);
                        n.setEquipmentCode(eq_code);

                        equipDetails.add(n);
                    }
*/
                    int id=equipmentSearchResponse.getId();
                    String name=equipmentSearchResponse.getName();
                    String equipcode=equipmentSearchResponse.getEquipmentCode();
                    EquipmentSearchResponseforEdit ob=new EquipmentSearchResponseforEdit(id,name,equipcode);
                    equipDetails.add(ob);
                    equipmentSearchAdapterforEdit = new
                            EquipmentSearchAdapterforEdit(EquipmentSearchActivityforEdit.this, equipDetails);
                    listView.setAdapter(equipmentSearchAdapterforEdit);
                    equipmentSearchAdapterforEdit.notifyDataSetChanged();

                }
            }

            @Override
            public void onFailure(Call<EquipmentSearchResponseforEdit> call, Throwable t) {
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