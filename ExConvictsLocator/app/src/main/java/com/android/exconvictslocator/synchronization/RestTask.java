package com.android.exconvictslocator.synchronization;

import android.util.Log;

import com.android.exconvictslocator.entities.ExConvict;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

public class RestTask {

    // Preuzimace se zapravo List<ExConvicts>
    // Samo neka primer metoda cisto da isprobam
    protected ExConvict getExConvicts(String... uri) {
        final String url = uri[0];
        RestTemplate restTemplate = new RestTemplate();
        try {
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            HttpHeaders headers = new HttpHeaders();
            HttpEntity<String> entity = new HttpEntity<String>(headers);
            Log.d("RESTTASK", "Pre exchange metode");
            ResponseEntity<ExConvict> response = restTemplate.exchange(url, HttpMethod.GET, entity, ExConvict.class);
            Log.d("RESTTASK", "Posle exchange metode");
            ExConvict exConvict = response.getBody();
            Log.d("RESTTASK", "Vracen ex-convict sa imenom: " + exConvict.getFirstName());
            return exConvict;
        } catch (Exception ex) {
            String message = ex.getMessage();
            return null;
        }
    }
}
