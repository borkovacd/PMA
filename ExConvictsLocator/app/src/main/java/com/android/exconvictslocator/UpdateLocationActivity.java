package com.android.exconvictslocator;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.android.exconvictslocator.entities.Address;
import com.android.exconvictslocator.entities.ExConvictReport;
import com.android.exconvictslocator.entities.Report;
import com.android.exconvictslocator.entities.User;
import com.android.exconvictslocator.repositories.impl.ExConvictRepository;
import com.android.exconvictslocator.repositories.impl.ReportRepository;
import com.android.exconvictslocator.repositories.impl.UserRepository;
import com.android.exconvictslocator.synchronization.SyncReportService;
import com.android.exconvictslocator.synchronization.resttemplate.AddressesRestClient;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;

public class UpdateLocationActivity extends MainActivity implements LocationListener {

    private DrawerLayout mDrawer;

    private List<Address> addresses =new ArrayList<Address>();

    // polja sa dobijenim podacima
    String nameSurname = null;
    String nickname = null;
    int img = -1;

    // prazna polja
    String newLocation = null;
    String comment = null ;
    String updatedAt ;

    // Dodatna polja za izvestaj
    int userId;
    int idExConvict, exConvictId ;
    String city = "Novi Sad" ;
    double lat, lang ;

    // polja sa forme
    ImageView ivUser ;
    ImageButton locateMe;
    EditText etImePrezime, etNadimak ;
    EditText  etKomentar ;
    Button btnPrijavi ;
    AutoCompleteTextView etPrijaviNovuLokaciju;

    private ExConvictRepository exConvictRepo;
    private ReportRepository reportRepo;
    private UserRepository userRepository;

    private List<ExConvictReport> exConvicts;
    private MyDatabase myDatabase;
    private String emailUser;

    LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_update_location, null, false);
        mDrawer = (DrawerLayout) findViewById(R.id.drawer);
        mDrawer.addView(contentView, 0);
        try {
            getAddresses();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        myDatabase = MyDatabase.getDatabase(this.getApplication());
        reportRepo = ReportRepository.getInstance(myDatabase.reportDao());
        userRepository = UserRepository.getInstance(myDatabase.userDao());

        setView();
        btnPrijavi = findViewById(R.id.btn_prijavi);

        locateMe.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                locate();

            }

        });
        btnPrijavi.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(newLocation ==null  || newLocation.equals("") || lat == 0 || lang == 0){
                    Toast.makeText(getApplicationContext(), "Uneta lokacija nije ispravna.", Toast.LENGTH_LONG).show();
                }else {

                    openAllLocationsActivity();
                }
            }
        });

        if(ContextCompat.checkSelfPermission(UpdateLocationActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(UpdateLocationActivity.this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION
            }, 100);

        }
    }

    private void setView(){
        etImePrezime = findViewById(R.id.et_ImePrezime);
        etNadimak = findViewById(R.id.et_Nadimak);
        etPrijaviNovuLokaciju = findViewById(R.id.et_PrijaviNovuLokaciju);
        etKomentar = findViewById(R.id.MLKomentar);
        ivUser = findViewById(R.id.iv_user);
        locateMe = findViewById(R.id.locate_me);

        Bundle b = getIntent().getExtras();
        if (b != null) {
            nameSurname = b.getString("name");
            nickname = b.getString("nickname");
            img = b.getInt("image");
            idExConvict = b.getInt("idExConvict");

        }

        etImePrezime.setText(nameSurname);
        etNadimak.setText(nickname);
        ivUser.setImageResource(img);
        locateMe.setImageResource(R.drawable.ic_action_name);

    }

    public void openAllLocationsActivity() {
        Intent intent = new Intent(this, ConvictLocationsMapActivity.class);
        Bundle b = new Bundle();

        // --- PODACI ZA IZVESTAJ ---

        // Datum prijave
        updatedAt = new Date().toString();
        // Komentar
        comment = etKomentar.getText().toString();

        // UserId = email korisnika koji vrsi prijavu
        // Ulogovani korisnik
        sessionManagement = new SessionManagement(getApplicationContext());
        sessionManagement.checkLogin();
        HashMap<String, String> user = sessionManagement.getUserDetails();
        emailUser = user.get(SessionManagement.KEY_EMAIL);
        User loggedInUser = userRepository.findUserByEmail(emailUser);
        userId = loggedInUser.getId();

        // ExConvictId
        exConvictId = idExConvict;
        Report report = new Report();
        report.setCity(city);
        report.setComment(comment);
        report.setDate(updatedAt);
        report.setExConvictId(exConvictId);
        report.setLang(lang);
        report.setLat(lat);
        report.setLocation(newLocation);
        report.setUserId(userId);
        report.setSync(false);
       myDatabase.reportDao().insertReport(report);

        b.putString("name", nameSurname);
        b.putString("nickname", nickname);
        b.putInt("image", img);
        b.putInt("idExConvict", idExConvict);

        intent.putExtras(b);
        startActivity(intent);

    }

    public void onNewSyncButtonClick(View v) {
        Intent intent3 = new Intent(this, SyncReportService.class);
        startService(intent3);
    }

    @Override
    public void onLocationChanged(Location location) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Locale.setDefault(new Locale.Builder().setLanguage("sr").setScript("Latn").build());
            };
            Geocoder geocoder = new Geocoder(UpdateLocationActivity.this, Locale.getDefault());

            List<android.location.Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            double longitude = addresses.get(0).getLongitude();
            double latitude = addresses.get(0).getLatitude();
            String address = addresses.get(0).getAddressLine(0);
            etPrijaviNovuLokaciju.setText(address);
            newLocation = address;
            lang = longitude;
            lat = latitude;
          }catch (Exception e) {
            System.out.println(e.getStackTrace());
        }

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    @SuppressLint("MissingPermission")
    private void locate(){
        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            Location current = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
           if(current != null){
               Geocoder geocoder = new Geocoder(UpdateLocationActivity.this, Locale.getDefault());
               List<android.location.Address> addresses = geocoder.getFromLocation(current.getLatitude(), current.getLongitude(),1);
               double longitude = addresses.get(0).getLongitude();
               double latitude = addresses.get(0).getLatitude();
               String address = addresses.get(0).getAddressLine(0);
               etPrijaviNovuLokaciju.setText(address);
               newLocation = address;
               lang = longitude;
               lat = latitude;
               city = addresses.get(0).getLocality();

           }
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5, UpdateLocationActivity.this);

        }catch (Exception e) {
            city = "Novi Sad";
        }
        }
    public void getAddresses() throws JSONException {
        AddressesRestClient.get("addresses", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {


                if (response != null) {
                    Gson gson = new Gson();
                    Type type = new TypeToken<List<Address>>(){}.getType();
                     addresses= gson.fromJson(String.valueOf(response), type);
                    setAutocompleteAdapter();
                }
            }
        });
    }

    private void setAutocompleteAdapter(){
        ArrayAdapter<Address> adapter = new ArrayAdapter<Address>(this,
                android.R.layout.simple_dropdown_item_1line, addresses);
        AutoCompleteTextView actv = (AutoCompleteTextView)findViewById(R.id.et_PrijaviNovuLokaciju);
        actv.setThreshold(1);
        actv.setAdapter(adapter);
        actv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                Address selected = (Address) arg0.getAdapter().getItem(arg2);
                newLocation = selected.getName();

                lang = selected.getLang();
                lat = selected.getLat();

                Geocoder geocoder = new Geocoder(UpdateLocationActivity.this, Locale.getDefault());
                try {
                    List<android.location.Address> addresses = geocoder.getFromLocation(selected.getLat(), selected.getLang(),1);
                    city = addresses.get(0).getLocality();
                } catch (IOException e) {
                   city = "Novi Sad";
                }

            }
        });
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }
}
