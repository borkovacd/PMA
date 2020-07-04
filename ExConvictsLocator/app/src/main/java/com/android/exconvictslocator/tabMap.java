package com.android.exconvictslocator;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import com.google.maps.android.SphericalUtil;

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
    Button expandAdvancedSearchBtn;
    LinearLayout advancedSearchLayout;
    Button cancelAdvanceSearchBtn;

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
        setView();
        advancedSearchLayout.setVisibility(View.GONE);
        expandAdvancedSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (advancedSearchLayout.getVisibility()==View.GONE){
                    expand();
                }else{
                    collapse();
                }
            }
        });



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

                List<Report> filtered = new ArrayList<Report>();
                for (ExConvictReport e : exConvicts) {
                    for(Report r : e.getReports()){
                        Location startPoint=new Location("locationA");
                        startPoint.setLatitude(45.2523492);
                        startPoint.setLongitude(19.7960865);

                        Location endPoint=new Location("locationB");
                        endPoint.setLatitude(r.getLat());
                        endPoint.setLongitude(r.getLang());

                        double distance =startPoint.distanceTo(endPoint)/1000;
                        String searchCity = citySearch.getText().toString().toLowerCase();
                        boolean checkDistance = true;
                        try {
                            double radiusValue = Double.parseDouble(String.valueOf(radius.getSelectedItem()));
                            checkDistance =  (distance <= radiusValue);
                        }catch (Exception exc){ }
                            if (r.getCity() != null && r.getCity().toLowerCase().contains(searchCity) && checkDistance ) {
                                filtered.add(r);
                            }
                        }
                }
                refreshMapMarkersReports(filtered);
            }

        });
        cancelAdvanceSearchBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

              citySearch.setText("");
              radius.setSelection(0);
                refreshMapMarkers(exConvicts);
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

    public void refreshMapMarkersReports(List<Report> filtered){
        if(mGoogleMap != null){ //prevent crashing if the map doesn't exist yet (eg. on starting activity)
            mGoogleMap.clear();

            for(Report r : filtered) {
                        mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(r.getLat(),
                                r.getLang())));
                 CameraPosition.builder().target(new LatLng(r.getLat(), r.getLang())).zoom(16).bearing(0).build();


            }
        }
    }

    private void expand() {
        //set Visible
        advancedSearchLayout.setVisibility(View.VISIBLE);
        final int widthSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        final int heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        advancedSearchLayout.measure(widthSpec, heightSpec);

        ValueAnimator mAnimator = slideAnimator(0, advancedSearchLayout.getMeasuredHeight());
        mAnimator.start();
    }

    private void collapse() {
        int finalHeight = advancedSearchLayout.getHeight();

        ValueAnimator mAnimator = slideAnimator(finalHeight, 0);
        mAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                advancedSearchLayout.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        mAnimator.start();
    }

    private ValueAnimator slideAnimator(int start, int end) {

        ValueAnimator animator = ValueAnimator.ofInt(start, end);

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                //Update Height
                int value = (Integer) valueAnimator.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = advancedSearchLayout.getLayoutParams();
                layoutParams.height = value;
                advancedSearchLayout.setLayoutParams(layoutParams);
            }
        });
        return animator;
    }

private void setView(){
    mapView = (MapView) mview.findViewById(R.id.list_convicts_map);
    sv = mview.findViewById(R.id.searchViewMap);
    citySearch = mview.findViewById(R.id.et_citySearch);
    radius = (Spinner)  mview.findViewById(R.id.spinnerRadius);
    searchButton = (Button) mview.findViewById(R.id.advanceSearchBtn);
    expandAdvancedSearchBtn = (Button) mview.findViewById(R.id.expandAdvancedSearch);
    advancedSearchLayout = (LinearLayout) mview.findViewById(R.id.advancedSearchLayout);
    cancelAdvanceSearchBtn = (Button) mview.findViewById(R.id.cancelAdvanceSearchBtn);
}
}
