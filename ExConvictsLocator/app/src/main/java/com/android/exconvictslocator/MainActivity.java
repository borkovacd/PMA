package com.android.exconvictslocator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.HeaderViewListAdapter;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements  NavigationView.OnNavigationItemSelectedListener {

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

    TextView nav_header_name, nav_header_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Session Management class instance
        sessionManagement = new SessionManagement(getApplicationContext());

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        navigationView = (NavigationView) findViewById(R.id.navigationView);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawerOpen, R.string.drawerClose);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        /*if(sessionManagement.isLoggedIn()) {
            toolbar.getMenu();
            //.findItem(R.id.login).setVisible(false);
        }*/

        /*View nav_header = navigationView.getHeaderView(R.id.nav_header);

        nav_header_name = (TextView) findViewById(R.id.nav_header_name);
        nav_header_email = (TextView) findViewById(R.id.nav_header_email);
        if(sessionManagement.isLoggedIn()) {
            // get user data from session
            HashMap<String, String> user = sessionManagement.getUserDetails();
            // email
            String email = user.get(SessionManagement.KEY_EMAIL);
            nav_header_email.setText(email);
        }*/

        createNotificationChannel(); //!!!

    }

    public void sendNotification() {
        NotificationCompat.Builder notifyBuilder = getNotificationBuilder();
        mNotifyManager.notify(NOTIFICATION_ID, notifyBuilder.build());
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
            case R.id.logout:
                sessionManagement.logoutUser();
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
            case R.id.notify:
                sendNotification();
                break;
        }
        return true;
    }
}
