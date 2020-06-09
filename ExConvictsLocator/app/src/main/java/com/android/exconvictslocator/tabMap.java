package com.android.exconvictslocator;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;

import com.android.exconvictslocator.entities.ExConvict;
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

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class tabMap extends Fragment implements OnMapReadyCallback {

    GoogleMap mGoogleMap;
    MapView mapView;
    SearchView sv;
    View mview;
    private List<ExConvictReport> exConvicts;
    private ExConvictRepository exConvictRepo;


    public tabMap() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mview = inflater.inflate(R.layout.fragment_tab_map, container, false);
        return mview;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mapView = (MapView) mview.findViewById(R.id.list_convicts_map);
        sv = mview.findViewById(R.id.searchViewMap);
        if(mapView != null){
            mapView.onCreate(null);
            mapView.onResume();
            mapView.getMapAsync(this);
        }
        MyDatabase db =  MyDatabase.getDatabase(getActivity().getApplication());
        exConvictRepo = ExConvictRepository.getInstance(db.exConvictDao());
        exConvicts = exConvictRepo.getExConvictReports();
        int id = sv.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
        TextView textView = (TextView) sv.findViewById(id);
        textView.setTextColor(Color.WHITE);
        textView.setHintTextColor(Color.WHITE);
        sv.setQueryHint("Pretraga...");
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String txt) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String txt) {
                List<ExConvictReport> filtered = new ArrayList<ExConvictReport>();
                for (ExConvictReport e : exConvicts) {
                    if ( e.getExConvict().getFirstName().toLowerCase().contains(txt.toLowerCase()) || (e.getExConvict().getLastName().toLowerCase().contains(txt.toLowerCase()))) {
                        filtered.add(e);
                    }
                }
                refreshMapMarkers(filtered);
                return false;
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(getContext());
        mGoogleMap= googleMap;
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
    public void refreshMapMarkers(List<ExConvictReport> filtered){
        if(mGoogleMap != null){ //prevent crashing if the map doesn't exist yet (eg. on starting activity)
            mGoogleMap.clear();
            for(ExConvictReport exr : filtered){
                if (exr.getReports() != null &&  exr.getReports().size()> 0 ) {
                    mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(exr.getReports().get(0).getLat(),
                            exr.getReports().get(0).getLang())).title(exr.getExConvict().getFirstName() + " " + exr.getExConvict().getLastName()));
                }
            }
        }
    }

}
