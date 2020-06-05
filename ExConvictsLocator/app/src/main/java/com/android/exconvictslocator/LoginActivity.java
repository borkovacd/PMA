package com.android.exconvictslocator;

import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.exconvictslocator.entities.User;
import com.android.exconvictslocator.repositories.impl.UserRepository;

import org.mindrot.jbcrypt.BCrypt;

import java.util.HashMap;
import java.util.List;

public class LoginActivity extends MainActivity {

    private DrawerLayout mDrawer;

    EditText etEmail, etPassword;
    TextView tvRegister;
    Button btnLogin;

    private MyDatabase myDatabase;
    private UserRepository userRepository;

    // Session Management Class
    SessionManagement sessionManagement;

    // Alert Dialog Manager
    AlertDialogManager alert = new AlertDialogManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_login, null, false);
        mDrawer = (DrawerLayout) findViewById(R.id.drawer);
        mDrawer.addView(contentView, 0);

        // Session Management class instance
        sessionManagement = new SessionManagement(getApplicationContext());
        //Toast.makeText(getApplicationContext(), "User Login Status: " + sessionManagement.isLoggedIn(), Toast.LENGTH_LONG).show();


        myDatabase = MyDatabase.getDatabase(this.getApplication());
        userRepository = UserRepository.getInstance(myDatabase.userDao());
        List<User> registeredUsers = userRepository.getUsers();

        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        btnLogin = findViewById(R.id.btn_login);
        tvRegister = findViewById(R.id.tv_register);

        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(i);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etEmail.getText().toString();
                String password = etPassword.getText().toString();

                boolean notExistingEmail = true;
                // Check if any of the fields is empty
                if (email.equals("") || password.equals("")) {
                    //alert.showAlertDialog(LoginActivity.this, "Login failed..", "Please enter username and password", false);
                    Toast.makeText(getApplicationContext(), "Polje ne može biti prazno", Toast.LENGTH_LONG).show();
                } else {
                    for (User user : registeredUsers) {
                        if (user.getEmail().equals(email)) {
                            //Pronadjen user sa unetim email-om
                            notExistingEmail = false;
                            if(BCrypt.checkpw(password, user.getPassword())) {
                                //alert.showAlertDialog(LoginActivity.this, "Login successful!", "You are now logged in", true);
                                Toast.makeText(getApplicationContext(), "Uspešno ste se ulogovali.", Toast.LENGTH_LONG).show();
                                // Creating user login session
                                sessionManagement.createLoginSession(user.getEmail());
                                // Starting MainActivity
                                Intent i = new Intent(getApplicationContext(), ListOfExConvicts.class);
                                startActivity(i);
                                finish();
                            } else {
                                //alert.showAlertDialog(LoginActivity.this, "Login failed..", "Username/Password is incorrect", false);
                                Toast.makeText(getApplicationContext(), "Neispravan email ili lozinka.", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                    if (notExistingEmail) {
                        //alert.showAlertDialog(LoginActivity.this, "Login failed..", "Username/Password is incorrect", false);
                        Toast.makeText(getApplicationContext(), "Neispravan email ili lozinka.", Toast.LENGTH_LONG).show();
                    }
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
