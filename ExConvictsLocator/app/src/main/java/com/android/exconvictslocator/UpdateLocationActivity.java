package com.android.exconvictslocator;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.drawerlayout.widget.DrawerLayout;

public class UpdateLocationActivity extends MainActivity {

    private DrawerLayout mDrawer;

    // polja sa dobijenim podacima
    String nameSurname = null;
    String nickname = null;
    String updatedAt = null;
    int img = -1;

    // prazna polja
    String lastLocation = null;
    String comment = null ;



    // polja sa forme
    ImageView ivUser ;
    EditText etImePrezime, etNadimak ;
    EditText etPrijaviNovuLokaciju, etKomentar ;
    Button btnPrijavi ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_update_location, null, false);
        mDrawer = (DrawerLayout) findViewById(R.id.drawer);
        mDrawer.addView(contentView, 0);

        setView();

        btnPrijavi = findViewById(R.id.btn_prijavi);
        btnPrijavi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Bundle b = getIntent().getExtras();

                if (b != null) {


                }

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
            updatedAt = b.getString("updatedAt");
            img = b.getInt("image");

        }

        etImePrezime.setText(nameSurname);
        etNadimak.setText(nickname);
        ivUser.setImageResource(img);

    }
}
