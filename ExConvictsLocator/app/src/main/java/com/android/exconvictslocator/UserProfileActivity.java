package com.android.exconvictslocator;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.android.exconvictslocator.entities.User;
import com.android.exconvictslocator.repositories.impl.UserRepository;

import org.mindrot.jbcrypt.BCrypt;

import java.util.HashMap;

public class UserProfileActivity extends MainActivity {

    private DrawerLayout mDrawer;

    private MyDatabase myDatabase;
    private UserRepository userRepository;

    private MutableLiveData<User> user2 = new MutableLiveData<>();

    private String emailUser;

    private EditText etFirstName;
    private EditText etLastName;

    private EditText etOldPassword;
    private EditText etNewPassword;
    private EditText etRepeatPassword;

    Button btnSave ;

    private boolean passwordChanged = false;
    private boolean validationOk = false;

    SessionManagement sessionManagement;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_user_profile, null, false);
        mDrawer = (DrawerLayout) findViewById(R.id.drawer);
        mDrawer.addView(contentView, 0);


        sessionManagement = new SessionManagement(getApplicationContext());
        sessionManagement.checkLogin();
        // get user data from session
        HashMap<String, String> user = sessionManagement.getUserDetails();
        // email
        emailUser = user.get(SessionManagement.KEY_EMAIL);

        myDatabase = MyDatabase.getDatabase(this.getApplication());
        userRepository = UserRepository.getInstance(myDatabase.userDao());

        etFirstName = findViewById(R.id.et_firstName);
        etLastName = findViewById(R.id.et_lastName);
        etOldPassword = findViewById(R.id.et_old_password);
        etNewPassword = findViewById(R.id.et_new_password);
        etRepeatPassword = findViewById(R.id.et_repeat_password);

        final ProfileTask task = new ProfileTask();
        task.execute(emailUser);

        user2.observe((LifecycleOwner) this.getLifecycle(), new Observer<User>() {
            @Override
            public void onChanged(User user) {
                etFirstName.setText(String.valueOf(user.getFirstName()),TextView.BufferType.EDITABLE );
                etLastName.setText(String.valueOf(user.getLastName()),TextView.BufferType.EDITABLE );
            }
        });

        btnSave = findViewById(R.id.btn_save);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // pracim String promenljivu za svaki EditText
                String profileFirstName = etFirstName.getText().toString();
                String profileLastName = etLastName.getText().toString();
                String profileOldPass = etOldPassword.getText().toString();
                String profileNewPass = etNewPassword.getText().toString();
                String profileRepeatPass = etRepeatPassword.getText().toString();


                // da li se poklapa OldPassword sa njegovom lozinkom kojom se logovao (iz baze)
                if(BCrypt.checkpw(profileOldPass, user2.getValue().getPassword())){
                    validationOk = true; // globalna boolean promenljiva
                }

                // validacije za prazno polje
                if(profileFirstName.equals("")){
                    Toast.makeText(getApplicationContext(), "Neophodno je uneti ime!.", Toast.LENGTH_LONG).show();
                    return;
                }

                if(profileLastName.equals("")){
                    Toast.makeText(getApplicationContext(), "Neophodno je uneti prezime!.", Toast.LENGTH_LONG).show();
                    return;
                }

                // ako je bilo sta ukucano u oldPassword (tacno ili netacno) svakako da ga obrise
                if(!profileOldPass.equals(""))
                    passwordChanged = true;

                // ukoliko hoce da promeni sifru i ukoliko je uneo ispravnu (duzu od 5 znakova)
                if(!(profileNewPass.equals("")) && profileNewPass.length()>=4){
                    if(validationOk) { // da li je dobra stara sifra
                        // postavlja nova polja

                        if (profileNewPass.equals(profileRepeatPass)) {
                            user2.getValue().setPassword(BCrypt.hashpw(profileNewPass, BCrypt.gensalt())); // kriptovanje sifre
                            user2.getValue().setFirstName(profileFirstName);
                            user2.getValue().setLastName(profileLastName);
                            validationOk = false; // za sledeci krug, da bi opet provere prolazio
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "Unete lozinke se ne poklapaju!.", Toast.LENGTH_LONG).show();
                        }
                    }
                    else { // ukoliko nije ukucan dobar oldPassword
                        Toast.makeText(getApplicationContext(), "Uneta je neispravna stara lozinka!.", Toast.LENGTH_LONG).show();
                        return;
                    }
                }
                else if(profileNewPass.equals("") || profileRepeatPass.equals("")) { // nije hteo da menja pass zapravo
                    user2.getValue().setFirstName(profileFirstName);
                    user2.getValue().setLastName(profileLastName);
                }
                else { // uneo pass manji od 4
                    Toast.makeText(getApplicationContext(), "Uneta lozinka nije dovoljno dugacka!.", Toast.LENGTH_LONG).show();
                    return;
                }

                // pravi se opet novi task -> UPISIVANJE U BAZU
                UpdateProfileTask task1 = new UpdateProfileTask();
                task1.execute(user2.getValue()); // saljem celog user-a (getValue jer je Mutable)
            }
        });


    }

    private class ProfileTask extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... strings) {

            User userPom = userRepository.findUserByEmail(strings[0]);
            user2.postValue(userPom);

            return null;
        }
    }


    private class UpdateProfileTask extends  AsyncTask<User, Void, Void>{
        @Override
        protected Void doInBackground(User... users) {
            userRepository.updateUser(users[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(passwordChanged){ // ukoliko je menjao lozinku, zelim da obrisem sadrzaj tih polja
                Toast.makeText(getApplicationContext(), "Uspesno je izmenjen profil!.", Toast.LENGTH_LONG).show();
                etOldPassword.getText().clear();
                etNewPassword.getText().clear();
                etRepeatPassword.getText().clear();
            }
            else
                Toast.makeText(getApplicationContext(), "Uspesno je izmenjen profil!.", Toast.LENGTH_LONG).show();
        }
    }
}
