package com.android.exconvictslocator;


import androidx.fragment.app.FragmentActivity;
import androidx.room.Room;

import android.os.Bundle;


public class ConvictLocationsMapActivity extends FragmentActivity  {

    MyDatabase myDatabase =  MyDatabase.getDatabase(this.getApplication());
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_convict_locations_map);
        ConvictLocationsMapsFragment mapsFragment = new ConvictLocationsMapsFragment();
        Bundle b = getIntent().getExtras();
        String name = null; // or other values
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
