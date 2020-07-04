package com.android.exconvictslocator;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
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
import androidx.core.app.NotificationCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.preference.PreferenceManager;

import com.android.exconvictslocator.notifications.NotificationService;
import com.google.android.material.navigation.NavigationView;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    // *** NOTIFICATIONS ***
    // Every notification channel must be associated with an ID that is unique within your package.
    private static final String PRIMARY_CHANNEL_ID = "primary_notification_channel";
    private NotificationManager mNotifyManager;
    private static final int NOTIFICATION_ID = 0;

    // Session Management Class
    SessionManagement sessionManagement;

    DrawerLayout drawerLayout;
    Toolbar toolbar;
    NavigationView navigationView;
    ActionBarDrawerToggle toggle;
    Button btn_login;

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

        createNotificationChannel(); //!!!

        //startService( new Intent( this, NotificationService. class )) ;
    }

    @Override
    protected void onStop () {
        super.onStop() ;
        //getApplicationContext().bindService(new Intent(getApplicationContext(), NotificationService.class), ServiceConnection , BIND_AUTO_CREATE);
        //startService( new Intent( this, NotificationService.class));
    }



    public void sendNotification() {
        NotificationCompat.Builder notifyBuilder = getNotificationBuilder();
        mNotifyManager.notify(NOTIFICATION_ID, notifyBuilder.build());
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(sessionManagement.isNotificationServiceStarted()) {
            Log.d("OLGA", "VREDNOST" + sessionManagement.isNotificationServiceStarted());
            Log.d("OLGA", "NotificationService je vec pokrenut!");
            Log.d("OLGA", "*********");
        } else {
            Log.d("OLGA", "NotificationService nije pokrenut!");
            Log.d("OLGA", "VREDNOST" + sessionManagement.isNotificationServiceStarted());
            Log.d("OLGA", "--------------------");
            sessionManagement.updateNotificationService(true);
            startService( new Intent( this, NotificationService.class));
        }


    }

    public void createNotificationChannel() {
        mNotifyManager = (NotificationManager)
                getSystemService(NOTIFICATION_SERVICE);
        // Because notification channels are only available in API 26 and higher,
        // adding a condition to check for the device's API version.
        if (android.os.Build.VERSION.SDK_INT >=
                android.os.Build.VERSION_CODES.O) {
            // The name is displayed under notification Categories in the device's user-visible Settings app.
            NotificationChannel notificationChannel = new NotificationChannel(PRIMARY_CHANNEL_ID,
                    "Ex-Convicts Notifications", NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setDescription("Notification from Ex-Convicts Locator");
            mNotifyManager.createNotificationChannel(notificationChannel);
        }
    }

    private NotificationCompat.Builder getNotificationBuilder() {
        Intent notificationIntent = new Intent(this, ListOfExConvicts.class);
        PendingIntent notificationPendingIntent =
                PendingIntent.getActivity(this, NOTIFICATION_ID, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder notifyBuilder = new NotificationCompat.Builder(this, PRIMARY_CHANNEL_ID)
                .setContentTitle("Obavešteni ste!")
                .setContentText("U vašoj blizini nalazi se bivši osuđenik.")
                .setSmallIcon(R.drawable.ic_warning)
                .setContentIntent(notificationPendingIntent)
                .setAutoCancel(true) //Setting auto-cancel to true closes the notification when user taps on it.
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setDefaults(NotificationCompat.DEFAULT_ALL);
        return notifyBuilder;
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
            case R.id.notify:
                sendNotification();
                break;
            case R.id.settings:
                Intent i2 = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(i2);
                break;
        }
        return true;
    }

}
