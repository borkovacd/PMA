package com.android.exconvictslocator;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_list_of_ex_convicts, null, false);
        mDrawer = (DrawerLayout) findViewById(R.id.drawer);
        mDrawer.addView(contentView, 0);

        // Session Management class instance
        sessionManagement = new SessionManagement(getApplicationContext());
        Toast.makeText(getApplicationContext(), "User Login Status: " + sessionManagement.isLoggedIn(), Toast.LENGTH_SHORT).show();

        /**
         * Call this function whenever you want to check user login
         * This will redirect user to LoginActivity is he is not
         * logged in
         * */
        sessionManagement.checkLogin();
        // get user data from session
        HashMap<String, String> user = sessionManagement.getUserDetails();
        // email
        String email = user.get(SessionManagement.KEY_EMAIL);
        Toast.makeText(getApplicationContext(), "Logged In User: " + email, Toast.LENGTH_LONG).show();

        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabMap = (TabItem) findViewById(R.id.tabMap);
        tabList = (TabItem) findViewById(R.id.tabList);
        viewPager= findViewById(R.id.viewPager);
        pagerAdapter = new PageAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
              if (tab.getPosition() == 0){
                  pagerAdapter.notifyDataSetChanged();
              }else if (tab.getPosition() == 1){
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
    }
}