package com.android.exconvictslocator;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.drawerlayout.widget.DrawerLayout;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ExConvictDetailsActivity extends MainActivity {

    private DrawerLayout mDrawer;

    Button btnPrijavi;
    Button btnAllLocations;

    private String mname;
    private String mnickName;
    private int mimg;



    TextView addressV, genderV, birthV, crimeV, descriptionV ;
    TextView lastLocationV, updateTimeV, nameDetailV, nicknameDetailV ;
    ImageView imageDetailV ;

    int idExConvict = 0;
    String name = null;
    String nickname = null;
    String address = null;
    String birth = null;
    String gender = null;
    String crime = null;
    String desc = null;
    String lastLocation = null;
    String updatedAt = null;
    int img = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_ex_convict_details, null, false);
        mDrawer = (DrawerLayout) findViewById(R.id.drawer);
        mDrawer.addView(contentView, 0);

        setView();


        btnPrijavi = findViewById(R.id.btn_prijavi);

        if (!sessionManagement.isLoggedIn()) {
            btnPrijavi.setVisibility(View.INVISIBLE);
        }

        btnPrijavi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent i = new Intent(ExConvictDetailsActivity.this, UpdateLocationActivity.class);
//                startActivity(i);
                openUpdateLocationActivity();
            }
        });

        btnAllLocations = findViewById(R.id.btn_sveLokacije);
        btnAllLocations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAllLocationsActivity();
            }
        });
    }


    private void setView(){

        addressV = findViewById(R.id.convict_details_address);
        genderV = findViewById(R.id.convict_details_gender);
        birthV = findViewById(R.id.convict_details_birth);
        crimeV = findViewById(R.id.convict_details_crime);
        descriptionV = findViewById(R.id.convict_details_description);
        lastLocationV = findViewById(R.id.convict_details_last_location);
        updateTimeV = findViewById(R.id.convict_details_update_time);
        nameDetailV = findViewById(R.id.convict_details_name);
        nicknameDetailV = findViewById(R.id.convict_details_nickname);
        imageDetailV = findViewById(R.id.convict_details_image);



        Bundle b = getIntent().getExtras();



        if(b != null) {
            idExConvict = b.getInt("idExConvict");
            name = b.getString("name");
            nickname = b.getString("nickname");
            img = b.getInt("image");
            address = b.getString("address");
            birth = b.getString("birth");
            gender =b.getString("gender");
            crime =b.getString("crime");
            updatedAt = b.getString("updatedAt");
            desc = b.getString("desc");
            lastLocation =b.getString("lastLocation");
        }
        this.mname= name;
        this.mnickName = nickname;
        this.mimg = img;
        addressV.setText(address);
        genderV.setText(gender);
        birthV.setText(birth);
        crimeV.setText(crime);
        descriptionV.setText(desc);
        lastLocationV.setText(lastLocation);
        SimpleDateFormat formatter = new SimpleDateFormat("dd/M/yyyy hh:mm:ss");
        //updateTimeV.setText(formatter.format(new Date(updatedAt)));
        nameDetailV.setText(name);
        nicknameDetailV.setText(nickname);
        imageDetailV.setImageResource(img);
    }

    public void openAllLocationsActivity(){
        Intent intent = new Intent(this, ConvictLocationsMapActivity.class);
        Bundle b = new Bundle();
        b.putString("name", mname);
        b.putString("nickname", mnickName);
        b.putInt("image", mimg);
        b.putInt("idExConvict", idExConvict);


        intent.putExtras(b);
        startActivity(intent);
    }

    public void openUpdateLocationActivity(){
        Intent intent = new Intent(this , UpdateLocationActivity.class);
        Bundle b = new Bundle();

        b.putInt("idExConvict", idExConvict);
        b.putString("name", name);
        b.putString("nickname", nickname);
        b.putInt("image", img);
        b.putString("address", address);
        b.putString("birth", birth);
        b.putString("gender", gender);
        b.putString("crime", crime);
        b.putString("updatedAt",  updatedAt);
        b.putString("desc", desc);
        b.putString("lastLocation", lastLocation);

        intent.putExtras(b);
        startActivity(intent);
    }
}
