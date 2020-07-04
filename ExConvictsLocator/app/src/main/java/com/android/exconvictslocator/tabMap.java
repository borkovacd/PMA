package com.android.exconvictslocator;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Spinner;

import com.android.exconvictslocator.entities.ExConvict;
import com.android.exconvictslocator.entities.ExConvictReport;
import com.android.exconvictslocator.entities.Report;
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
    Spinner radius;
    Button searchButton;
    EditText citySearch;

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
        citySearch = mview.findViewById(R.id.et_citySearch);
        radius = (Spinner)  mview.findViewById(R.id.spinnerRadius);
        searchButton = (Button) mview.findViewById(R.id.advanceSearchBtn);
        if(mapView != null){
            mapView.onCreate(null);
            mapView.onResume();
            mapView.getMapAsync(this);
        }
        MyDatabase db =  MyDatabase.getDatabase(getActivity().getApplication());
        exConvictRepo = ExConvictRepository.getInstance(db.exConvictDao());
        exConvicts = exConvictRepo.getExConvictReports();
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


        searchButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                System.out.println( "OnClickListener : " +
                        "radijus 1 : "+ String.valueOf(radius.getSelectedItem())  );

                List<ExConvictReport> filtered = new ArrayList<ExConvictReport>();
                for (ExConvictReport e : exConvicts) {
                    for(Report r : e.getReports()){
                        String searchCity = citySearch.getText().toString().toLowerCase();

                            if (r.getCity() != null && r.getCity().toLowerCase().contains(searchCity)) {
                                filtered.add(e);
                            }
                    }
                }
                refreshMapMarkers(filtered);
            }

        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(getContext());
        mGoogleMap= googleMap;
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        CameraPosition camera = CameraPosition.builder().target(new LatLng(45.267136, 19.833549)).zoom(16).bearing(0).build();

        for(ExConvictReport exr : exConvicts){

            if (exr.getReports() != null &&  exr.getReports().size()> 0 ) {
                for(Report r : exr.getReports()) {
                    googleMap.addMarker(new MarkerOptions().position(new LatLng(r.getLat(),
                            r.getLang())).title(exr.getExConvict().getFirstName() + " " + exr.getExConvict().getLastName()));
                    camera = CameraPosition.builder().target(new LatLng(r.getLat(), r.getLang())).zoom(10).bearing(0).build();
                }
            }
        }

         googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(camera));
    }
    public void refreshMapMarkers(List<ExConvictReport> filtered){
        if(mGoogleMap != null){ //prevent crashing if the map doesn't exist yet (eg. on starting activity)
            mGoogleMap.clear();
            for(ExConvictReport exr : filtered){
                if (exr.getReports() != null &&  exr.getReports().size()> 0 ) {
                    for(Report r : exr.getReports()) {
                        mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(r.getLat(),
                                r.getLang())).title(exr.getExConvict().getFirstName() + " " + exr.getExConvict().getLastName()));
                    }      }
            }
        }
    }

}
