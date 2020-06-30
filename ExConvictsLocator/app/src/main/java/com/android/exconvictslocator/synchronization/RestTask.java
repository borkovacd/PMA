package com.android.exconvictslocator.synchronization;

import android.util.Log;

import com.android.exconvictslocator.entities.ExConvict;
import com.android.exconvictslocator.entities.Report;
import com.android.exconvictslocator.entities.User;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

public class RestTask {

    protected ExConvict[] getExConvicts(String... uri) {
        final String url = uri[0];
        RestTemplate restTemplate = new RestTemplate();
        try {
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
            HttpEntity<String> entity = new HttpEntity<String>(headers);
            Log.d("RESTTASK", "Pre exchange metode");
            ResponseEntity<ExConvict[]> response = restTemplate.exchange(url, HttpMethod.GET, entity, ExConvict[].class);
            Log.d("RESTTASK", "Posle exchange metode");
            ExConvict[] exConvicts = response.getBody();
            for(ExConvict tempExconvict: exConvicts) {
                Log.d("RESTTASK", "Vracen ex-convict sa imenom: " + tempExconvict.getFirstName());
            }
            return exConvicts;
        } catch (Exception ex) {
            String message = ex.getMessage();
            return null;
        }
    }

    protected User[] getUsers(String... uri) {
        final String url = uri[0];
        RestTemplate restTemplate = new RestTemplate();
        try {
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            HttpHeaders headers = new HttpHeaders();
            HttpEntity<String> entity = new HttpEntity<String>(headers);
            //Log.d("RESTTASK", "Pre exchange metode");
            ResponseEntity<User[]> response = restTemplate.exchange(url, HttpMethod.GET, entity, User[].class);
            //Log.d("RESTTASK", "Posle exchange metode");
            User[] users = response.getBody();
            for(User tempUser: users) {
                Log.d("RESTTASK", "Vracen user sa emailom: " + tempUser.getEmail());
            }
            return users;
        } catch (Exception ex) {
            String message = ex.getMessage();
            return null;
        }
    }

    protected Report[] getReports(String... uri) {
        final String url = uri[0];
        RestTemplate restTemplate = new RestTemplate();
        try {
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            HttpHeaders headers = new HttpHeaders();
            HttpEntity<String> entity = new HttpEntity<String>(headers);
            ResponseEntity<Report[]> response = restTemplate.exchange(url, HttpMethod.GET, entity, Report[].class);
            Report[] reports = response.getBody();
            for(Report tempReport: reports) {
                Log.d("RESTTASK", "Vracen report sa lokacijom: " + tempReport.getLocation());
            }
            return reports;
        } catch (Exception ex) {
            String message = ex.getMessage();
            return null;
        }
    }
}
