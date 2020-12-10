package com.synergy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DividerItemDecoration;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.internal.NavigationMenuView;
import com.google.android.material.navigation.NavigationView;
import com.synergy.EquipmentSearch.EquipmentSearchActivity;
import com.synergy.FaultReport.FaultReportActivity;
import com.synergy.Search.Search;
import com.synergy.SearchTasks.SearchTaskActivity;
import com.synergy.Setting.SettingActivity;

import static com.synergy.MainActivityLogin.SHARED_PREFS;

public class MyBaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public DrawerLayout drawer;
    public Toolbar toolbar;
    NavigationView navigationView, navigationViewTech, navigationViewAgent;
    public ActionBarDrawerToggle toggle;
    String role, username, workspaceId;
    private final CheckInternet checkInternet = new CheckInternet();

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(checkInternet, intentFilter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(checkInternet);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_base);

        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "");
        role = sharedPreferences.getString("role", "");
        username = sharedPreferences.getString("username", "").toUpperCase();
        workspaceId = sharedPreferences.getString("workspaceId", "");

        drawer = findViewById(R.id.drawer);
        toolbar = findViewById(R.id.toolbar_mybase);
        navigationView = findViewById(R.id.navView);

        View headerView = navigationView.getHeaderView(0);
        TextView usernameTv = (TextView) headerView.findViewById(R.id.headerName);
        usernameTv.setText(username);
        setSupportActionBar(toolbar);

        navigationView.setNavigationItemSelectedListener(this);
        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, 0, 0);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        Context context = drawer.getContext();
        navigationView.setItemIconTintList(null);
        Menu nav_Menu = navigationView.getMenu();

        if (role.equals(Constants.ROLE_TECHNICIAN) || role.equals(Constants.ROLE_MANAGINGAGENT)) {
            nav_Menu.findItem(R.id.createFNav).setVisible(false);
            nav_Menu.findItem(R.id.setNav).setVisible(false);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
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

    public void intentMethod(Class cla, String value) {
        Intent intent = new Intent(getApplicationContext(), cla);
        intent.putExtra("workspaceId", workspaceId);
        intent.putExtra("value", value);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else super.onBackPressed();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.messageNav:
                break;
            case R.id.createFNav:
                toolbar.setTitle("Fault Report");
                intentMethod(FaultReportActivity.class, "");
                break;
            case R.id.searchFNav:
                intentMethod(Search.class, "");
                break;
            case R.id.EqFNav:
                intentMethod(EquipmentSearchActivity.class, "Fault");
                break;
            case R.id.eqTNav:
                intentMethod(EquipmentSearchActivity.class, "Task");
                break;
            case R.id.tSearchNav:
                intentMethod(SearchTaskActivity.class, "");
                break;
            case R.id.setNav:
                intentMethod(SettingActivity.class, "");
                break;
            case R.id.upload_qoutation_nav:
                break;
            case R.id.upload_po_nav:
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return false;
    }
}