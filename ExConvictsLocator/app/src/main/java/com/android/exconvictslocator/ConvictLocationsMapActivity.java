package com.android.exconvictslocator;


import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;


public class ConvictLocationsMapActivity extends MainActivity  {

    private DrawerLayout mDrawer;

    MyDatabase myDatabase =  MyDatabase.getDatabase(this.getApplication());
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View contentView = inflater.inflate(R.layout.activity_convict_locations_map, null, false);ConvictLocationsMapsFragment mapsFragment = new ConvictLocationsMapsFragment();
                mDrawer = (DrawerLayout) findViewById(R.id.drawer);Bundle b = getIntent().getExtras();
                mDrawer.addView(contentView, 0);String name = null; // or other values
        String nickname = null; // or other values
        int img = -1; // or other values

        if(b != null) {
            name = b.getString("name");
            nickname = b.getString("nickname");
            img = b.getInt("image");
        }

        Bundle arguments = new Bundle();
        arguments.putString("name", name);
        arguments.putString("nickname", nickname);
        arguments.putInt("img", img);
        mapsFragment.setArguments(arguments);
        getSupportFragmentManager().beginTransaction().add(R.id.fragmentmaps, mapsFragment, "tag").commit();

    }
    public MyDatabase getMyDatabase() {
        return myDatabase;
    }

}
