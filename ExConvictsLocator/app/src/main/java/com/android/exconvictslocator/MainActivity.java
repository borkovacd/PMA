package com.android.exconvictslocator;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.preference.PreferenceManager;

import com.android.exconvictslocator.notifications.NotificationService;
import com.google.android.material.navigation.NavigationView;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    // Session Management Class
    SessionManagement sessionManagement;

    DrawerLayout drawerLayout;
    Toolbar toolbar;
    NavigationView navigationView;
    ActionBarDrawerToggle toggle;
    Button btn_login;

    String NOTIFICATION_TAG = "NOTIFICATION_TAG";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Session Management class instance
        sessionManagement = new SessionManagement(getApplicationContext());

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        navigationView = (NavigationView) findViewById(R.id.navigationView);

        //Podešavanje NavigationDrawer-a
        //u zavisnosti da li postoji ulogovan korisnik
        Menu nav_menu = navigationView.getMenu();
        View nav_view = navigationView.getHeaderView(0);
        TextView name = nav_view.findViewById(R.id.nav_header_name);
        TextView email = nav_view.findViewById(R.id.nav_header_email);
        if (sessionManagement.isLoggedIn()) {
            nav_menu.findItem(R.id.login).setVisible(false);
            HashMap<String, String> user = sessionManagement.getUserDetails();
            email.setText(user.get(SessionManagement.KEY_EMAIL));
            name.setText(user.get(SessionManagement.KEY_NAME));
        } else {
            nav_menu.findItem(R.id.logout).setVisible(false);
            nav_menu.findItem(R.id.profile).setVisible(false);
        }

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawerOpen, R.string.drawerClose);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        //Postavljanje podrazumevanih vrednosti za podešavanja
        PreferenceManager.setDefaultValues(this,
                R.xml.root_preferences, false);

    }


    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        boolean enable_notifications = sharedPref.getBoolean("enable_notifications", false);
        Log.d(NOTIFICATION_TAG, "Enable Notification: " + enable_notifications);
        Intent i = new Intent( this, NotificationService.class);
        if(enable_notifications) { //Korisnik je omugućio slanje notifikacija
            if(sessionManagement.isNotificationServiceStarted()) {
                Log.d(NOTIFICATION_TAG, "NotificationService je vec pokrenut!");
            } else {

                if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION
                    }, 100);

                }

                Log.d(NOTIFICATION_TAG, "NotificationService nije pokrenut!");
                sessionManagement.updateNotificationService(true);
                startService(i);
            }
        } else { //Korisnik je zabranio slanje notifikacija
            if(sessionManagement.isNotificationServiceStarted()) {
                stopService(i);
                Log.d(NOTIFICATION_TAG, "NotificationService je zaustavljen!");
                sessionManagement.updateNotificationService(false);
                //NotificationService je vec pokrenut
                //Sada bi ga trebalo zaustaviti kako ne bi dalje stizale notifikacije
            } //Za else granu nema potrebe
        }


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
                Intent i3 = new Intent(MainActivity.this, ListOfExConvicts.class);
                startActivity(i3);
                break;
            case R.id.login:
                Intent i4 = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(i4);
                break;
            case R.id.logout:
                sessionManagement.logoutUser();
                break;
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        //Podešavanja Toolbar-a
        //u zavisnosti da li postoji ulogovan korisnik
        Menu toolbarMenu = toolbar.getMenu();
        if (sessionManagement.isLoggedIn()) {
            toolbarMenu.findItem(R.id.login).setVisible(false);
        } else {
            toolbarMenu.findItem(R.id.logout).setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.login:
                Intent i = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(i);
                break;
            case R.id.logout:
                sessionManagement.logoutUser();
                break;
            case R.id.settings:
                Intent i2 = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(i2);
                break;
        }
        return true;
    }

}
