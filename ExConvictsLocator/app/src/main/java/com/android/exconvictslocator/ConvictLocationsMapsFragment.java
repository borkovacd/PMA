package com.android.exconvictslocator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class ConvictLocationsMapsFragment extends Fragment implements OnMapReadyCallback {

    GoogleMap googleMap;
    MapView mapView;
    View mview;
    public ConvictLocationsMapsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mview = inflater.inflate(R.layout.activity_convict_locations_maps, container, false);
        Bundle bundle = this.getArguments();
        String name = "";
        String nickname ="";
        int img = -1;

        if(bundle != null) {
            name = bundle.getString("name");
            nickname = bundle.getString("nickname");
            img = bundle.getInt("img");
        }
        TextView nameDetail = mview.findViewById(R.id.convict_details_name);
        TextView nicknameDetail = mview.findViewById(R.id.convict_details_nickname);
        ImageView imageDetail = mview.findViewById(R.id.convict_details_image);
        nameDetail.setText(name);
        nicknameDetail.setText(nickname);
        imageDetail.setImageResource(img);
        return mview;

    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mapView = (MapView) mview.findViewById(R.id.convict_locations_map);
        if(mapView != null){
            mapView.onCreate(null);
            mapView.onResume();
            mapView.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(getContext());
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        //lat 45.267136 , long 19.833549
        googleMap.addMarker(new MarkerOptions().position(new LatLng(45.267136, 19.833549)).title("Pera Perić"));
        googleMap.addMarker(new MarkerOptions().position(new LatLng(45.267136, 19.835546)).title("Žika Žikić"));
        googleMap.addMarker(new MarkerOptions().position(new LatLng(45.267136, 19.835550)).title("Mika Mikić"));

        CameraPosition camera = CameraPosition.builder().target(new LatLng(45.267136, 19.833549)).zoom(16).bearing(0).build();
        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(camera));
    }
}
