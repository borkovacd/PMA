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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class UpdateLocationActivity extends MainActivity implements LocationListener {

    private DrawerLayout mDrawer;

    private Address[] addresses = new Address[] {
            new Address(1, "Danila Kisa", 45.12, 19.15),
            new Address(2, "Bulevar oslobodjenja", 45.13, 19.45),
            new Address(3, "Sutjeska", 45.172, 19.155),
            new Address(4, "Djurdja Brankovica", 45.5512, 19.15),
            new Address(5, "Alekse Santica", 45.1562, 19.15),
            };

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
    String city ;
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

        myDatabase = MyDatabase.getDatabase(this.getApplication());
        reportRepo = ReportRepository.getInstance(myDatabase.reportDao());
        userRepository = UserRepository.getInstance(myDatabase.userDao());
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
                System.out.println("Clicked " + arg2 + " name: " + selected.getName() + ", id = " + selected.getId());

            }
        });
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
                openAllLocationsActivity();
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
        SimpleDateFormat formatter = new SimpleDateFormat("dd/M/yyyy hh:mm:ss");
        updatedAt = formatter.format(new Date());
        // Grad
        city = "Novi Sad";

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
            Geocoder geocoder = new Geocoder(UpdateLocationActivity.this, Locale.getDefault());
            List<android.location.Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLatitude(), 1);
            String address = addresses.get(0).getAddressLine(0);
            System.out.println(address);


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
            locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5, UpdateLocationActivity.this);
        }catch (Exception e) {
            System.out.println(e.getStackTrace());
        }
        }
}
