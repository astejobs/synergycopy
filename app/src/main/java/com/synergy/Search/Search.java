package com.synergy.Search;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.synergy.APIClient;
import com.synergy.CheckInternet;
import com.synergy.Constants;
import com.synergy.LogoutClass;
import com.synergy.MainActivityLogin;
import com.synergy.MyBaseActivity;
import com.synergy.R;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.synergy.MainActivityLogin.SHARED_PREFS;

public class Search extends MyBaseActivity {
    private String TAG;
    private String frId = "";
    private String workspaceId, token;
    private List<String> frIdList = new ArrayList<>();
    private ListView listView;
    private ArrayList<SearchResponse> contacts = new ArrayList<>();
    private SearchResponseAdapter searchResponseAdapter;
    private String role,username;
    private FusedLocationProviderClient client;
    private double latitude, longitude;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    ArrayList<String> list;


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View viewLayout = layoutInflater.inflate(R.layout.activity_search, null, false);
        drawer.addView(viewLayout, 0);

        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        token = sharedPreferences.getString("token", "");
        role = sharedPreferences.getString("role", "");
        username=sharedPreferences.getString("username","");
        String wk = sharedPreferences.getString("workspaceId", "");

        toolbar.setTitle("Search Fault Report");
        setSupportActionBar(toolbar);
        username = sharedPreferences.getString("username", "");
        Intent intent = getIntent();
        workspaceId = intent.getStringExtra("workspaceId");

        viewPager = findViewById(R.id.viewpager);
        tabLayout = findViewById(R.id.tab_layout);
        list = new ArrayList<String>();
        list.add("Active");
        list.add("Inactive");

        prepareViewPager(viewPager, list);
        tabLayout.setupWithViewPager(viewPager);
        client = LocationServices.getFusedLocationProviderClient(Search.this);
        client.flushLocations();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this
                , Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Please on GPS", Toast.LENGTH_SHORT).show();
            return;
        }
/*
        client.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {


                Location location = task.getResult();
                if (location != null) {
                    try {
                        Geocoder geocoder = new Geocoder(Search.this, Locale.getDefault());
                        List<Address> addresses = geocoder.getFromLocation(location.getLatitude()
                                , location.getLongitude(), 1);
                        latitude = addresses.get(0).getLatitude();
                        longitude = addresses.get(0).getLongitude();
                        Log.d(TAG, "onComplete: lat" + addresses.get(0).getLatitude());
                        Log.d(TAG, "onComplete: long" + addresses.get(0).getLongitude());

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
*/

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
                    loadSearch(onTextChangeQueryCall);
                    listView.setFilterText(onTextChangeQueryCall);
                }

                return false;
            }
        });

    }


    private void loadSearch(String callQueryDependent) {

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);
        progressDialog.show();

        Log.d(TAG, "loadSearch: nmk" + workspaceId);
        Call<List<SearchResponse>> call = APIClient.getUserServices().getSearchResult(workspaceId, callQueryDependent, token, role,"Active");
        call.enqueue(new Callback<List<SearchResponse>>() {
            @Override
            public void onResponse(Call<List<SearchResponse>> call, Response<List<SearchResponse>> response) {

                progressDialog.dismiss();
                if (response.code() == 200) {
                    List<SearchResponse> list = response.body();
                    if (list.isEmpty()) {
                        Toast.makeText(Search.this, "No Faults Available", Toast.LENGTH_SHORT).show();
                    } else {

                        for (SearchResponse searchResponse : list) {

                            SearchResponse searchResp = new SearchResponse();
                            frId = searchResponse.getFrId();
                            long rtdate = searchResponse.getReportedDate();
                            String status = searchResponse.getStatus();
                            String buildingg = searchResponse.getBuildingName();
                            String locationn = searchResponse.getLocationName();

                            LocalDateTime localDateTime = searchResponse.getActivationTime();

                            searchResp.setFrId(frId);
                            searchResp.setReportedDate(rtdate);
                            searchResp.setStatus(status);
                            searchResp.setBuilding(buildingg);
                            searchResp.setLocation(locationn);
                            searchResp.setActivationTime(localDateTime);
                            searchResp.setWorkspaceId(workspaceId);
                            searchResp.setLatitude(latitude);
                            searchResp.setLongitude(longitude);
                            //  frIdList.add(frId);
                            contacts.add(searchResp);
                        }
                    }
                    Collections.sort(frIdList);
                    searchResponseAdapter = new SearchResponseAdapter(Search.this,
                            contacts, workspaceId, latitude, longitude);
                    listView.setAdapter(searchResponseAdapter);
                    searchResponseAdapter.notifyDataSetChanged();


                } else if (response.code() == 401) {
                    Toast.makeText(Search.this, Constants.ERROR_CODE_401_MESSAGE, Toast.LENGTH_SHORT).show();
                    LogoutClass logoutClass = new LogoutClass();
                    logoutClass.logout(Search.this);
                } else if (response.code() == 500) {
                    Toast.makeText(Search.this, Constants.ERROR_CODE_500_MESSAGE, Toast.LENGTH_SHORT).show();
                } else if (response.code() == 404) {
                    Toast.makeText(Search.this, Constants.ERROR_CODE_404_MESSAGE, Toast.LENGTH_SHORT).show();
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


    private void prepareViewPager(ViewPager viewPager, ArrayList<String> list) {
        MainAdapter adapter = new MainAdapter(getSupportFragmentManager());
        MainFragment fragment = new MainFragment();
        for (int i = 0; i < list.size(); i++) {
            Bundle bundle = new Bundle();
            bundle.putString("tittle", list.get(i));
            bundle.putString("workspace",workspaceId);
            fragment.setArguments(bundle);
            adapter.addFragment(fragment, list.get(i));
            fragment = new MainFragment();
        }
        viewPager.setAdapter(adapter);
    }


    private class MainAdapter extends FragmentPagerAdapter {
        ArrayList<String> arrayList = new ArrayList<>();
        List<Fragment> fragmentList = new ArrayList<>();

        public void addFragment(Fragment fragment, String tittle) {
            arrayList.add(tittle);
            fragmentList.add(fragment);
        }

        ;

        public MainAdapter(@NonNull FragmentManager fm) {
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return arrayList.get(position);
        }
    }
}