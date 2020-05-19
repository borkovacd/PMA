package com.android.exconvictslocator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
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

public class ConvictLocationsMapActivity extends FragmentActivity  {

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

}
