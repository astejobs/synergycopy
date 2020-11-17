package com.synergy.Search;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
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
import com.synergy.MainActivityLogin;
import com.synergy.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.synergy.MainActivityLogin.SHARED_PREFS;

public class Search extends AppCompatActivity {
    private String TAG;
    private String frId = "";
    private String workspaceId, token;
    private List<String> frIdList = new ArrayList<>();


    private ListView listView;
    private ArrayList<SearchResponse> contacts = new ArrayList<>();
    private SearchResponseAdapter searchResponseAdapter;
    Toolbar toolbar;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        toolbar = findViewById(R.id.tool_search);
        setSupportActionBar(toolbar);
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        token = sharedPreferences.getString("token", "");

        ScrollView view = (ScrollView) findViewById(R.id.scrollViewSearch);
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

        Intent intent = getIntent();
        workspaceId = intent.getStringExtra("workspaceId");
        SearchView searchView = findViewById(R.id.search_view);
        searchView.setIconifiedByDefault(false);
        listView = findViewById(R.id.list_view);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String onTextChangeQueryCall) {
                if (onTextChangeQueryCall.isEmpty()) {
                    contacts.clear();
                    //   searchResponseAdapter.notifyDataSetChanged();
                } else {
                    contacts.clear();
                    loadSearch(workspaceId, onTextChangeQueryCall);
                    listView.setFilterText(onTextChangeQueryCall);
                }

                return false;
            }
        });

    }

    private void loadSearch(String worklistString, String callQueryDependent) {

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);
        progressDialog.show();

        int workspace = 1;
        Call<List<SearchResponse>> call = APIClient.getUserServices().getSearchResult(workspace, callQueryDependent, token);
        call.enqueue(new Callback<List<SearchResponse>>() {
            @Override
            public void onResponse(Call<List<SearchResponse>> call, Response<List<SearchResponse>> response) {

                progressDialog.dismiss();
                if (response.code() == 200) {
                    List<SearchResponse> list = response.body();
                    if (list.isEmpty()) {
                        Toast.makeText(Search.this, "Nothing Here", Toast.LENGTH_SHORT).show();
                    } else {

                        Toast.makeText(Search.this, "sucessfullsearch", Toast.LENGTH_SHORT).show();

                        for (SearchResponse searchResponse : list) {

                            SearchResponse searchResp = new SearchResponse();
                            frId = searchResponse.getFrId();
                            long rtdate = searchResponse.getReportedDate();
                            String status = searchResponse.getStatus();
                            String buildingg = searchResponse.getBuilding();
                            String locationn = searchResponse.getLocation();
                            Log.d(TAG, "onResponse: lov" + locationn + buildingg);

                            searchResp.setFrId(frId);
                            searchResp.setReportedDate(rtdate);
                            searchResp.setStatus(status);
                            searchResp.setBuilding(buildingg);
                            searchResp.setLocation(locationn);
                            searchResp.setWorkspaceId(workspaceId);
                            //  frIdList.add(frId);
                            contacts.add(searchResp);
                        }
                    }
                    Collections.sort(frIdList);
                    searchResponseAdapter = new SearchResponseAdapter(Search.this, contacts, workspaceId);
                    listView.setAdapter(searchResponseAdapter);
                    searchResponseAdapter.notifyDataSetChanged();


                }
            }


            @Override
            public void onFailure(Call<List<SearchResponse>> call, Throwable t) {
                Toast.makeText(Search.this, "fail", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
                Log.d(TAG, "onFailure: " + t.getCause());
                Log.d(TAG, "onFailure: " + t.getMessage());

            }
        });
     /*   call.enqueue(new Callback<List<SearchResponse>>() {
            @Override
            public void onResponse(Call<List<SearchResponse>> call, Response<List<SearchResponse>> response) {
                progressDialog.dismiss();
                if (response.code() == 200) {
                    List<SearchResponse> list = response.body();
                    for (SearchResponse searchResponse : list) {

                        SearchResponse searchResp = new SearchResponse();
                        frId = searchResponse.getFrId();
                        long rtdate = searchResponse.getReportedDate();
                        String status = searchResponse.getStatus();
                        String buildingg = searchResponse.getBuilding();
                        String locationn = searchResponse.getLocation();

                        searchResp.setFrId(frId);
                        searchResp.setReportedDate(rtdate);
                        searchResp.setStatus(status);
                        searchResp.setBuilding(buildingg);
                        searchResp.setLocation(locationn);

                    frIdList.add(frId);
                }
                Collections.sort(frIdList);
                searchResponseAdapter = new SearchResponseAdapter(Search.this, contacts);
                listView.setAdapter(searchResponseAdapter);
                searchResponseAdapter.notifyDataSetChanged();
            //}
            }

            @Override
            public void onFailure(Call<List<SearchResponse>> call, Throwable t) {
                Toast.makeText(Search.this, "Failed to load Searched Data" + t.getMessage() + "Cause is here" + t.getCause(), Toast.LENGTH_SHORT).show();
                Log.d(TAG, "Failed to load Searched Data" + t.getMessage());
                progressDialog.dismiss();
            }
        });*/

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        SharedPreferences preferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        String role = preferences.getString("role", "Role");
        MenuItem item = menu.findItem(R.id.admin).setTitle(role);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.logoutmenu) {
            SharedPreferences preferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.clear();
            editor.apply();
            Intent intent = new Intent(this, MainActivityLogin.class);
            startActivity(intent);
            finishAffinity();
        }
        return true;
    }
}