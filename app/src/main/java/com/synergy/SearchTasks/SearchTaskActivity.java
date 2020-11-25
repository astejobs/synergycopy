package com.synergy.SearchTasks;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.SearchView;
import android.widget.Toast;

import com.synergy.APIClient;
import com.synergy.EquipmentSearch.EquipmentSearchActivity;
import com.synergy.EquipmentSearch.EquipmentSearchAdapter;
import com.synergy.EquipmentSearch.EquipmentSearchCard;
import com.synergy.LogoutClass;
import com.synergy.R;
import com.synergy.Search.SearchResponse;
import com.synergy.Search.SearchResponseAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.synergy.MainActivityLogin.SHARED_PREFS;

public class SearchTaskActivity extends AppCompatActivity {

    private List<String> taskIdList = new ArrayList<>();
    private ListView listView;
    private ArrayList<TaskSearchResponse> contacts = new ArrayList<>();
    private SearchResponseAdapter searchResponseAdapter;
    private String role;
    private ArrayList<EquipmentSearchCard> equipmentSearchCardArrayList = new ArrayList<>();
    private EquipmentSearchAdapter mAdapter = new EquipmentSearchAdapter(equipmentSearchCardArrayList);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_task);

        Intent intent = getIntent();
        String workspace = intent.getStringExtra("workspaceId");

        SharedPreferences preferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        role = preferences.getString("role", "");
        String token = preferences.getString("token", "");

        Toolbar toolbar = findViewById(R.id.tool_searchTasks);
        setSupportActionBar(toolbar);

        SearchView searchView = findViewById(R.id.search_viewTask);
        searchView.setIconifiedByDefault(false);

        ScrollView view = (ScrollView) findViewById(R.id.scrollViewTaskSearch);
        view.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.requestFocusFromTouch();
                return false;
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()) {
                } else {
                    equipmentSearchCardArrayList.clear();
                    loadSearch(newText, token, role, workspace);
                }

                return false;
            }
        });

    }

    private void loadSearch(String newText, String token, String role, String workspace) {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);
        progressDialog.show();

        Call<List<TaskSearchResponse>> callTaskSearch = APIClient.getUserServices().taskSearch(newText, token, role, workspace);
        callTaskSearch.enqueue(new Callback<List<TaskSearchResponse>>() {
            @Override
            public void onResponse(Call<List<TaskSearchResponse>> call, Response<List<TaskSearchResponse>> response) {
                if (response.code() == 200) {

                    List<TaskSearchResponse> equipmentSearchResponse = response.body();
                    RecyclerView recyclerView = findViewById(R.id.recycler_view_Task);
                    for (int i = 0; i < equipmentSearchResponse.size(); i++) {
                        String taskNumber = equipmentSearchResponse.get(i).getTaskNumber();
                        int taskId = equipmentSearchResponse.get(i).getTaskId();
                        String buildingName = equipmentSearchResponse.get(i).getBuildingName();
                        String locationName = equipmentSearchResponse.get(i).getLocationName();
                        long scheduleDate = Long.parseLong(equipmentSearchResponse.get(i).getScheduleDate());
                        String status = equipmentSearchResponse.get(i).getStatus();
                        String afterImage = equipmentSearchResponse.get(i).getAfterImage();
                        String beforeImage = equipmentSearchResponse.get(i).getBeforeImage();
                      
                        equipmentSearchCardArrayList.add(new EquipmentSearchCard(taskId, taskNumber, workspace, status, buildingName, locationName, scheduleDate, afterImage, beforeImage));
                    }

                    recyclerView.setHasFixedSize(true);

                    recyclerView.setLayoutManager(new LinearLayoutManager(SearchTaskActivity.this));
                    recyclerView.setAdapter(mAdapter);
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<List<TaskSearchResponse>> call, Throwable t) {
                progressDialog.dismiss();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        MenuItem item = (MenuItem) menu.findItem(R.id.admin).setTitle(role);
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