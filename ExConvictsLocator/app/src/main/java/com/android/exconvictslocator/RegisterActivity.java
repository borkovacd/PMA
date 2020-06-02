package com.android.exconvictslocator;

import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.exconvictslocator.entities.User;

public class RegisterActivity extends MainActivity {

    private DrawerLayout mDrawer;

    EditText etFirstName, etLastName, etEmail, etPassword, etConfirmPassword;
    Button btnRegister;

    //LoginDataBaseAdapter loginDataBaseAdapter;
    MyDatabase myDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_register, null, false);
        mDrawer = (DrawerLayout) findViewById(R.id.drawer);
        mDrawer.addView(contentView, 0);

        // get Instance of Database Adapter
        //loginDataBaseAdapter=new LoginDataBaseAdapter(this);
        //loginDataBaseAdapter=loginDataBaseAdapter.open();
        myDatabase =  MyDatabase.getDatabase(this.getApplication());

        etFirstName = findViewById(R.id.et_firstName);
        etLastName = findViewById(R.id.et_lastName);
        etEmail = findViewById(R.id.et_reg_email);
        etPassword = findViewById(R.id.et_reg_password);
        etConfirmPassword = findViewById(R.id.et_reg_password_2);

        btnRegister = findViewById(R.id.btn_register);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstName = etFirstName.getText().toString();
                String lastName = etLastName.getText().toString();
                String email = etEmail.getText().toString();
                String password = etPassword.getText().toString();
                String confirmPassword = etConfirmPassword.getText().toString();

                boolean error = false;

                // Check if any of the required fields is empty
                if (email.equals("") || password.equals("") || confirmPassword.equals("")) {
                    error = true;
                    Toast.makeText(getApplicationContext(), "Polje ne može biti prazno", Toast.LENGTH_LONG).show();
                }

                // Check if both password match
                if (!password.equals(confirmPassword)) {
                    error = true;
                    Toast.makeText(getApplicationContext(), "Lozinke se ne poklapaju", Toast.LENGTH_LONG).show();
                }

                if ( !error) {
                    // Save the Data in Database
                    User user = new User(firstName, lastName, password, email);
                    //loginDataBaseAdapter.insertEntry(email, password);
                    myDatabase.userDao().insertUser(user);
                    Toast.makeText(getApplicationContext(), "Uspešno kreiran korisnički nalog", Toast.LENGTH_LONG).show();
                    Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(i);
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //Closing The Database
        //myDatabase.close();
    }
}
