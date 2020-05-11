package com.android.exconvictslocator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ExConvictDetailsActivity extends AppCompatActivity {

    Button btnPrijavi;
    Button btnAllLocations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ex_convict_details);

        btnPrijavi = findViewById(R.id.btn_prijavi);

        btnPrijavi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ExConvictDetailsActivity.this, UpdateLocationActivity.class);
                startActivity(i);
            }
        });

        btnAllLocations = findViewById(R.id.btn_sveLokacije);
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
        String updetedAt = null;
        int img = -1;


        if(b != null) {
            name = b.getString("name");
            nickname = b.getString("nickname");
            img = b.getInt("image");
             address = b.getString("address");
             birth = b.getString("birth");
            gender =b.getString("gender");
             crime =b.getString("crime");
             updetedAt = b.getString("updetedAt");
             desc = b.getString("desc");
             lastLocation =b.getString("lastLocation");
        }
     addressV.setText(address);
    genderV.setText(gender);
         birthV.setText(birth);
      crimeV.setText(crime);
       descriptionV.setText(desc);
        lastLocationV.setText(lastLocation);
        updateTimeV.setText(updetedAt);
       nameDetailV.setText(name);
        nicknameDetailV.setText(nickname);
       imageDetailV.setImageResource(img);

    }
}
