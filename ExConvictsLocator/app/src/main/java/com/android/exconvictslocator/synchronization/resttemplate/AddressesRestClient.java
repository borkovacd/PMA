package com.android.exconvictslocator.synchronization.resttemplate;

import com.android.exconvictslocator.synchronization.ServerIPConfig;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

public class AddressesRestClient {
    private static String ip = ServerIPConfig.getIp_address();
   //private static final String BASE_URL = "http://192.168.0.13:8080/api/addresses";
    private static final String BASE_URL = "http://" + ip + ":8080/api/addresses";

    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void get(String url, AsyncHttpResponseHandler responseHandler) {
        client.get(BASE_URL, responseHandler);
    }


}