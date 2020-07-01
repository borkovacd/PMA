package com.android.exconvictslocator.synchronization.resttemplate;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.android.exconvictslocator.MyDatabase;
import com.android.exconvictslocator.entities.ExConvictReport;
import com.android.exconvictslocator.entities.Report;
import com.android.exconvictslocator.repositories.impl.ExConvictRepository;
import com.android.exconvictslocator.repositories.impl.ReportRepository;
import com.android.exconvictslocator.repositories.impl.UserRepository;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.List;

public class RestTaskReports extends AsyncTask<String, Void, ResponseEntity<Report[]>> {

    private Context context;
    private ReportRepository reportRepository;
    private ExConvictRepository exConvictRepository;
    private List<ExConvictReport> exConvictsReports;

    public RestTaskReports(Context applicationContext) {
        this.context = applicationContext;
    }

    @Override
    protected ResponseEntity<Report[]> doInBackground(String... uri) {
        final String url = uri[0];
        RestTemplate restTemplate = new RestTemplate();
        try {
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            HttpHeaders headers = new HttpHeaders();
            HttpEntity<String> entity = new HttpEntity<String>(headers);
            ResponseEntity<Report[]> response = restTemplate.exchange(url, HttpMethod.GET, entity, Report[].class);
            return response;
        } catch (Exception ex) {
            String message = ex.getMessage();
            Log.d("RESTTASK", "Error: " + message);
            return null;
        }
    }


    @Override
    protected void onPostExecute(ResponseEntity<Report[]> result) {
        Report[] reports = result.getBody();
        for (Report tempReport : reports) {
            Log.d("RESTTASK", "Vracen report sa lokacijom: " + tempReport.getLocation());
        }
        MyDatabase db =  MyDatabase.getDatabase(context);
        exConvictRepository = ExConvictRepository.getInstance(db.exConvictDao());
        reportRepository = ReportRepository.getInstance(db.reportDao());
        exConvictsReports = exConvictRepository.getExConvictReports();
        for(Report report: reports) {
            reportRepository.insertReport(report);
        }
        Log.d("RESTTASK", "SINHRONIZOVANE PRIJAVE LOKACIJE");
    }

}
