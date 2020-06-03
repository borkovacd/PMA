package com.android.exconvictslocator;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.exconvictslocator.entities.ExConvictReport;
import com.android.exconvictslocator.repositories.impl.ExConvictRepository;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class tabMap extends Fragment implements OnMapReadyCallback {

    GoogleMap googleMap;
    MapView mapView;
    View mview;
    private List<ExConvictReport> exConvicts;
    private ExConvictRepository exConvictRepo;


    public tabMap() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mview = inflater.inflate(R.layout.fragment_tab_map, container, false);
        return mview;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mapView = (MapView) mview.findViewById(R.id.list_convicts_map);
        if(mapView != null){
            mapView.onCreate(null);
            mapView.onResume();
            mapView.getMapAsync(this);
        }
        MyDatabase db =  MyDatabase.getDatabase(getActivity().getApplication());
        exConvictRepo = ExConvictRepository.getInstance(db.exConvictDao());
        exConvicts = exConvictRepo.getExConvictReports();

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(getContext());
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        for(ExConvictReport exr : exConvicts){
            if (exr.getReports() != null &&  exr.getReports().size()> 0 ) {
                googleMap.addMarker(new MarkerOptions().position(new LatLng(exr.getReports().get(0).getLat(),
                        exr.getReports().get(0).getLang())).title(exr.getExConvict().getFirstName() + " " + exr.getExConvict().getLastName()));
            }
        }

        CameraPosition camera = CameraPosition.builder().target(new LatLng(45.267136, 19.833549)).zoom(16).bearing(0).build();
         googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(camera));
    }


}
