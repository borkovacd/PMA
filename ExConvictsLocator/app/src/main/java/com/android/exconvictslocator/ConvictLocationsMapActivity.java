package com.android.exconvictslocator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class ConvictLocationsMapActivity extends MainActivity  {

    private DrawerLayout mDrawer;

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

}
