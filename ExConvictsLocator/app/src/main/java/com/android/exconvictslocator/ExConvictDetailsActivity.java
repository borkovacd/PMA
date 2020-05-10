package com.android.exconvictslocator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ExConvictDetailsActivity extends AppCompatActivity {

    Button btnPrijavi;

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
    }
}
