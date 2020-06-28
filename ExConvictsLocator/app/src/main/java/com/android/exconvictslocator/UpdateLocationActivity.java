package com.android.exconvictslocator;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.drawerlayout.widget.DrawerLayout;

import com.android.exconvictslocator.entities.ExConvictReport;
import com.android.exconvictslocator.entities.Report;
import com.android.exconvictslocator.repositories.impl.ExConvictRepository;
import com.android.exconvictslocator.repositories.impl.ReportRepository;
import com.android.exconvictslocator.repositories.impl.UserRepository;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class UpdateLocationActivity extends MainActivity {

    private DrawerLayout mDrawer;

    // polja sa dobijenim podacima
    String nameSurname = null;
    String nickname = null;
    int img = -1;

    // prazna polja
    String newLocation = null;
    String comment = null ;
    String updatedAt ;

    // Dodatna polja za izvestaj
    String userId;
    int idExConvict, exConvictId ;
    String city ;
    double lat, lang ;

    // polja sa forme
    ImageView ivUser ;
    EditText etImePrezime, etNadimak ;
    EditText etPrijaviNovuLokaciju, etKomentar ;
    Button btnPrijavi ;

    private ExConvictRepository exConvictRepo;
    private ReportRepository reportRepo;
    private UserRepository userRepository;

    private List<ExConvictReport> exConvicts;
    private MyDatabase myDatabase;
    private String emailUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_update_location, null, false);
        mDrawer = (DrawerLayout) findViewById(R.id.drawer);
        mDrawer.addView(contentView, 0);

        myDatabase = MyDatabase.getDatabase(this.getApplication());
        reportRepo = ReportRepository.getInstance(myDatabase.reportDao());

        setView();

        btnPrijavi = findViewById(R.id.btn_prijavi);
        btnPrijavi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAllLocationsActivity();
            }
        });
    }

    private void setView(){
        etImePrezime = findViewById(R.id.et_ImePrezime);
        etNadimak = findViewById(R.id.et_Nadimak);
        etPrijaviNovuLokaciju = findViewById(R.id.et_PrijaviNovuLokaciju);
        etKomentar = findViewById(R.id.MLKomentar);
        ivUser = findViewById(R.id.iv_user);

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

    }

    public void openAllLocationsActivity() {
        Intent intent = new Intent(this, ConvictLocationsMapActivity.class);
        Bundle b = new Bundle();

        // --- PODACI ZA IZVESTAJ ---
        // Lokacija
        newLocation = etPrijaviNovuLokaciju.getText().toString();

        // Datum prijave
        updatedAt = new Date().toString();

        // Grad ??
        city = "";

        // Komentar
        comment = etKomentar.getText().toString();

        // UserId = email korisnika koji vrsi prijavu
        // Ulogovani korisnik
        sessionManagement = new SessionManagement(getApplicationContext());
        sessionManagement.checkLogin();
        HashMap<String, String> user = sessionManagement.getUserDetails();
        emailUser = user.get(SessionManagement.KEY_EMAIL);
        userId = emailUser;

        // ExConvictId
        exConvictId = idExConvict;

        // Lat ?? -> latituda
        lat = 0.0;

        // Lang ?? -> longituda
        lang = 0.0;

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

        // ExConvictReport exConvictReport = new ExConvictReport();

        // exConvictReport.getReports().add(report)

        b.putString("name", nameSurname);
        b.putString("nickname", nickname);
        b.putInt("image", img);
        b.putInt("idExConvict", idExConvict);

        intent.putExtras(b);
        startActivity(intent);

    }
}
