package com.android.exconvictslocator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ExConvictDetailsActivity extends MainActivity {

    private DrawerLayout mDrawer;

    Button btnPrijavi;
    Button btnAllLocations;

    private String mname;
    private String mnickName;
    private int mimg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_ex_convict_details, null, false);
        mDrawer = (DrawerLayout) findViewById(R.id.drawer);
        mDrawer.addView(contentView, 0);

        btnPrijavi = findViewById(R.id.btn_prijavi);

        btnPrijavi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ExConvictDetailsActivity.this, UpdateLocationActivity.class);
                startActivity(i);
            }
        });
        setView();

        btnAllLocations = findViewById(R.id.btn_sveLokacije);
        btnAllLocations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAllLocationsActivity();
            }
        });
    }


    private void setView(){

        TextView addressV = findViewById(R.id.convict_details_address);
        TextView genderV = findViewById(R.id.convict_details_gender);
        TextView birthV = findViewById(R.id.convict_details_birth);
        TextView crimeV = findViewById(R.id.convict_details_crime);
        TextView descriptionV = findViewById(R.id.convict_details_description);
        TextView lastLocationV = findViewById(R.id.convict_details_last_location);
        TextView updateTimeV = findViewById(R.id.convict_details_update_time);
        TextView nameDetailV = findViewById(R.id.convict_details_name);
        TextView nicknameDetailV = findViewById(R.id.convict_details_nickname);
        ImageView imageDetailV = findViewById(R.id.convict_details_image);



        Bundle b = getIntent().getExtras();
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


        if(b != null) {
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
        updateTimeV.setText(updatedAt);
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
        intent.putExtras(b);
        startActivity(intent);
    }
}
