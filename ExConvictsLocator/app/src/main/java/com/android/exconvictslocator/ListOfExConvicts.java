package com.android.exconvictslocator;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.preference.PreferenceManager;
import androidx.viewpager.widget.ViewPager;

import com.android.exconvictslocator.synchronization.SyncReceiver;
import com.android.exconvictslocator.synchronization.SyncReportService;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

import java.util.HashMap;


public class ListOfExConvicts extends MainActivity {

    private DrawerLayout mDrawer;

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private TabItem tabMap, tabList;
    public PageAdapter pagerAdapter;

    // Session Management Class
    SessionManagement sessionManagement;

    private PendingIntent pendingIntent;
    private SyncReceiver sync;
    public static String SYNC_DATA = "SYNC_DATA";

    // Constants
    // The authority for the sync adapter's content provider
    public static final String AUTHORITY = "com.android.exconvictslocator.synchronization.provider";
    // An account type, in the form of a domain name
    public static final String ACCOUNT_TYPE = "exconvictslocator.com";
    // The account name
    public static final String ACCOUNT = "dummyaccount";
    // Instance fields
    Account mAccount;
    // A content resolver for accessing the provider
    ContentResolver mResolver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_list_of_ex_convicts, null, false);
        mDrawer = (DrawerLayout) findViewById(R.id.drawer);
        mDrawer.addView(contentView, 0);

        // Create the dummy account
        mAccount = CreateSyncAccount(this);
        // Get the content resolver for your app
        mResolver = getContentResolver();

        /*
         * Turn on periodic syncings
         */

        setAccountSyncable();

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        String synhonization_interval = sharedPref.getString("synhonization_interval", "15");
        Log.d("RESTTASK", "Interval (STRING): " + synhonization_interval);
        long sync_interval_in_minutes = Long.parseLong(synhonization_interval);
        long sync_interval = sync_interval_in_minutes * 60L; //minuti * sekunde
        Log.d("RESTTASK", "Interval (LONG): " + sync_interval);


        ContentResolver.addPeriodicSync(
                mAccount,
                AUTHORITY,
                Bundle.EMPTY, sync_interval*60L);

        ContentResolver.setSyncAutomatically(mAccount, AUTHORITY, true);



        // Session Management class instance
        sessionManagement = new SessionManagement(getApplicationContext());
        //Toast.makeText(getApplicationContext(), "User Login Status: " + sessionManagement.isLoggedIn(), Toast.LENGTH_SHORT).show();

        /**
         * Call this function whenever you want to check user login
         * This will redirect user to LoginActivity is he is not
         * logged in
         * */
        //sessionManagement.checkLogin();

        if (sessionManagement.isLoggedIn()) {
            // Get user data from session
            //HashMap<String, String> user = sessionManagement.getUserDetails();
            // Email
            //String email = user.get(SessionManagement.KEY_EMAIL);
            //Toast.makeText(getApplicationContext(), "Ulogovan korisnik: " + email, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplicationContext(), "Koristite aplikaciju u neprijavljenom režimu. Da biste koristili napredne funkcije aplikacije, ulogujete se na vaš korisnički nalog.", Toast.LENGTH_LONG).show();
        }


        sync = new SyncReceiver();

        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabMap = (TabItem) findViewById(R.id.tabMap);
        tabList = (TabItem) findViewById(R.id.tabList);
        viewPager = findViewById(R.id.viewPager);
        pagerAdapter = new PageAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                if (tab.getPosition() == 0) {
                    pagerAdapter.notifyDataSetChanged();
                } else if (tab.getPosition() == 1) {
                    pagerAdapter.notifyDataSetChanged();

                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        //SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        //Primer
        //String marketPref = sharedPref.getString("distance_radius", "-1");
        //Toast.makeText(this, marketPref, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter();
        filter.addAction(SYNC_DATA);

        filter.addAction("android.net.wifi.WIFI_STATE_CHANGED");
        filter.addAction("android.net.wifi.STATE_CHANGE");
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(sync, filter);

        Intent intent = new Intent(ListOfExConvicts.this, SyncReportService.class);
        intent.putExtra("activityName", "ListOfExConvicts");
        startService(intent);
    }

    @Override
    protected void onPause() {
        unregisterReceiver(sync);
        super.onPause();
    }


    /**
     * Create a new dummy account for the sync adapter
     *
     * @param context The application context
     */
    public static Account CreateSyncAccount(Context context) {
        // Create the account type and default account
        Account newAccount = new Account(
                ACCOUNT, ACCOUNT_TYPE);
        // Get an instance of the Android account manager
        AccountManager accountManager =
                (AccountManager) context.getSystemService(
                        ACCOUNT_SERVICE);
        /*
         * Add the account and account type, no password or user data
         * If successful, return the Account object, otherwise report an error.
         */
        if (accountManager.addAccountExplicitly(newAccount, null, null)) {
            /*
             * If you don't set android:syncable="true" in
             * in your <provider> element in the manifest,
             * then call context.setIsSyncable(account, AUTHORITY, 1)
             * here.
             */
        } else {
            /*
             * The account exists or some other error occurred. Log this, report it,
             * or handle it internally.
             */
        }
        return newAccount;
    }


    //Samo privremeno, dok se ne odradi neka pocetna verzija sinhronizacije
    //Sinhronizacija na zahtev pritiskom na button "Sync"
    /*
    public void onRefreshButtonClick(View v) {
        // Pass the settings flags by inserting them in a bundle
        Bundle settingsBundle = new Bundle();
        settingsBundle.putBoolean(
                ContentResolver.SYNC_EXTRAS_MANUAL, true);
        settingsBundle.putBoolean(
                ContentResolver.SYNC_EXTRAS_EXPEDITED, true);

         //Request the sync for the default account, authority, and
         //manual sync settings

        ContentResolver.requestSync(mAccount, AUTHORITY, settingsBundle);
    }*/

    private void setAccountSyncable() {
        if (ContentResolver.getIsSyncable(mAccount, AUTHORITY) == 0) {
            ContentResolver.setIsSyncable(mAccount, AUTHORITY, 1);
        }
    }

}