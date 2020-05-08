package com.android.exconvictslocator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements  NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    Toolbar toolbar;
    NavigationView navigationView;
    ActionBarDrawerToggle toggle;
    Button btn_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        navigationView = (NavigationView) findViewById(R.id.navigationView);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawerOpen, R.string.drawerClose);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.profile:
                Intent i = new Intent(MainActivity.this, UserProfileActivity.class);
                startActivity(i);
                break;
            case R.id.settings:
                Intent i2 = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(i2);
                break;
            case R.id.convicts_list:
                // ovde sam stavila prijavu nove lokacije za isprobavanje
                Intent i3 = new Intent(MainActivity.this, UpdateLocationActivity.class);
                startActivity(i3);
                break;
            case R.id.logout:
                // ovde sam stavila detalje o osudjeniku za isprobavanje
                Intent i4 = new Intent(MainActivity.this, ExConvictDetailsActivity.class);
                startActivity(i4);
                break;
            case R.id.all_convicts_list:
                Intent i5 = new Intent(MainActivity.this, ListOfExConvictes.class);
                startActivity(i5);
                break;
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.login:
                Intent i = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(i);
                break;
            case R.id.settings:
                Intent i2 = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(i2);
                break;
        }
        return true;
    }
}
