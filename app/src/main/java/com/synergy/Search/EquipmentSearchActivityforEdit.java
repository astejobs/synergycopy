package com.synergy.Search;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.synergy.LogoutClass;
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
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seach_layout_list);
        toolbar = findViewById(R.id.toolbar_eq_search);
        setSupportActionBar(toolbar);
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

        Call<List<EquipmentSearchResponseforEdit>> call = APIClient.getUserServices().
                getSearchEquipment(queryParam, token, workspace, role);

        call.enqueue(new Callback<List<EquipmentSearchResponseforEdit>>() {
            @Override
            public void onResponse(Call<List<EquipmentSearchResponseforEdit>> call,
                                   Response<List<EquipmentSearchResponseforEdit>> response) {
                if (response.code() == 200) {
                    List<EquipmentSearchResponseforEdit> equipmentSearchResponse = response.body();
                    for (int i = 0; i < equipmentSearchResponse.size(); i++) {
                        int id = equipmentSearchResponse.get(i).getId();
                        String name = equipmentSearchResponse.get(i).getName();
                        String equipcode = equipmentSearchResponse.get(i).getEquipmentCode();
                        String equipmentType = equipmentSearchResponse.get(i).getAssetType();
                        String equipSubType = equipmentSearchResponse.get(i).getAssetSubType();
                        String building = equipmentSearchResponse.get(i).getBuildingName();
                        String location = equipmentSearchResponse.get(i).getLocationName();
                        EquipmentSearchResponseforEdit ob = new EquipmentSearchResponseforEdit
                                (equipcode, name, id, equipmentType, equipSubType, location, building);

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
            /*SharedPreferences preferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.clear();
            editor.apply();
            Intent intent = new Intent(this, MainActivityLogin.class);
            startActivity(intent);
            finishAffinity();*/

            LogoutClass logoutClass = new LogoutClass();
            logoutClass.logout(this);
        }
        return true;
    }
}