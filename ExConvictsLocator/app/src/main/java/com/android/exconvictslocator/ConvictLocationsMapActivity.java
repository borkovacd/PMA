package com.android.exconvictslocator;


import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.android.exconvictslocator.repositories.impl.ExConvictRepository;


public class ConvictLocationsMapActivity extends MainActivity  {

    private DrawerLayout mDrawer;
    String name = null; // or other values
    String nickname = null; // or other values
    int img = -1; // or other values
    int exConvictId;
    MyDatabase db =  MyDatabase.getDatabase(this.getApplication());
    private ExConvictRepository exConvictRepo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_convict_locations_map, null, false);
        exConvictRepo = ExConvictRepository.getInstance(db.exConvictDao());
                ConvictLocationsMapsFragment mapsFragment = new ConvictLocationsMapsFragment();
                mDrawer = (DrawerLayout) findViewById(R.id.drawer);
                Bundle b = getIntent().getExtras();
                mDrawer.addView(contentView, 0);

        if(b != null) {
            name = b.getString("name");
            nickname = b.getString("nickname");
            img = b.getInt("image");
            exConvictId = b.getInt("idExConvict");
        }

        Bundle arguments = new Bundle();
        arguments.putString("name", name);
        arguments.putString("nickname", nickname);
        arguments.putInt("img", img);
        arguments.putInt("idExConvict",exConvictId);
        mapsFragment.setArguments(arguments);
        getSupportFragmentManager().beginTransaction().add(R.id.fragmentmaps, mapsFragment, "tag").commit();

    }
    public MyDatabase getMyDatabase() {
        return db;
    }

}
