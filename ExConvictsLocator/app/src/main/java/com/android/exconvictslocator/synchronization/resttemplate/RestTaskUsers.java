package com.android.exconvictslocator.synchronization.resttemplate;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.android.exconvictslocator.MyDatabase;
import com.android.exconvictslocator.entities.User;
import com.android.exconvictslocator.repositories.impl.UserRepository;
import com.android.exconvictslocator.synchronization.ServerIPConfig;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

public class RestTaskUsers extends AsyncTask<String, Void, ResponseEntity<User[]>> {

    private Context context;
    private UserRepository userRepository;

    public RestTaskUsers(Context applicationContext) {
        this.context = applicationContext;
    }

    @Override
    protected ResponseEntity<User[]> doInBackground(String... uri) {
        final String url = uri[0];
        RestTemplate restTemplate = new RestTemplate();
        try {
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            HttpHeaders headers = new HttpHeaders();
            HttpEntity<String> entity = new HttpEntity<String>(headers);
            //Log.d("RESTTASK", "Pre exchange metode");
            ResponseEntity<User[]> response = restTemplate.exchange(url, HttpMethod.GET, entity, User[].class);
            return response;
        } catch (Exception ex) {
            String message = ex.getMessage();
            Log.d("RESTTASK", "Error: " + message);
            return null;
        }
    }

    @Override
    protected void onPostExecute(ResponseEntity<User[]> result) {
        //Log.d("RESTTASK", "Posle exchange metode");
        User[] users = result.getBody();
        for(User tempUser: users) {
            Log.d("RESTTASK", "Vracen user sa emailom: " + tempUser.getEmail());
        }
        MyDatabase db =  MyDatabase.getDatabase(context);
        userRepository = UserRepository.getInstance(db.userDao());
        for(User user: users) {
            userRepository.insertUser(user);
        }
        Log.d("RESTTASK", "SINHRONIZOVANI KORISNICI");
        String ip_address = ServerIPConfig.getIp_address();
        final String uri3 = "http://" + ip_address + ":8080/api/reports";
        new RestTaskReports(context).execute(uri3);
    }
}
