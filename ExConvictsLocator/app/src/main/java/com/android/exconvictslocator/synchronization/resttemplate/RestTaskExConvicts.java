package com.android.exconvictslocator.synchronization.resttemplate;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.android.exconvictslocator.MyDatabase;
import com.android.exconvictslocator.R;
import com.android.exconvictslocator.entities.ExConvict;
import com.android.exconvictslocator.repositories.impl.ExConvictRepository;
import com.android.exconvictslocator.synchronization.ServerIPConfig;
import com.android.exconvictslocator.synchronization.SyncAdapter;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

//Async call kako bi se ispostovalo pravilo i sklonilo sa UI threada
public class RestTaskExConvicts extends AsyncTask<String, Void, ResponseEntity<ExConvict[]>> {

    private Context context;
    private ExConvictRepository exConvictRepository;

    public RestTaskExConvicts(Context applicationContext) {
        this.context = applicationContext;
    }

    @Override
    public ResponseEntity<ExConvict[]> doInBackground(String... uri) {
        final String url = uri[0];
        Log.d("RESTTASK", uri[0]);
        RestTemplate restTemplate = new RestTemplate();
        try {
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
            HttpEntity<String> entity = new HttpEntity<String>(headers);
            ResponseEntity<ExConvict[]> response = restTemplate.exchange(url, HttpMethod.GET, entity, ExConvict[].class);
            ExConvict[] exConvicts = response.getBody();
            MyDatabase db =  MyDatabase.getDatabase(context);
            exConvictRepository = ExConvictRepository.getInstance(db.exConvictDao());
            db.clearAllTables();
            for(ExConvict exConvict: exConvicts) {
                if(exConvict.getId() == 1)
                    exConvict.setPhoto(R.drawable.img1);
                if(exConvict.getId() == 2)
                    exConvict.setPhoto(R.drawable.img2);
                if(exConvict.getId() == 3)
                    exConvict.setPhoto(R.drawable.img3);
                exConvictRepository.insertExConvict(exConvict);
            }
            return response;
        } catch (Exception ex) {
            String message = ex.getMessage();
            Log.d("RESTTASK", "Error: " + message);
            return null;
        }
    }

    //Ova metoda se izvrsava kada se izvrsi async task
    @Override
    protected void onPostExecute(ResponseEntity<ExConvict[]> result) {
        Log.d("RESTTASK", "SINHRONIZOVANI BIVSI OSUDJENICI");
        String ip_address = ServerIPConfig.getIp_address();
        final String uri2 = "http://" + ip_address + ":8080/api/users";
        new RestTaskUsers(context).execute(uri2);

    }
}
