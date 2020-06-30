package com.android.exconvictslocator.synchronization;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SyncResult;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.android.exconvictslocator.MyDatabase;
import com.android.exconvictslocator.entities.ExConvict;
import com.android.exconvictslocator.entities.ExConvictReport;
import com.android.exconvictslocator.entities.Report;
import com.android.exconvictslocator.entities.User;
import com.android.exconvictslocator.repositories.impl.ExConvictRepository;
import com.android.exconvictslocator.repositories.impl.ReportRepository;
import com.android.exconvictslocator.repositories.impl.UserRepository;

import java.math.BigInteger;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.ByteOrder;
import java.util.Enumeration;
import java.util.List;

/**
 * Handle the transfer of data between a server and an
 * app, using the Android sync adapter framework.
 */
public class SyncAdapter extends AbstractThreadedSyncAdapter {

    private static final String TAG = SyncAdapter.class.getSimpleName();

    // Global variables
    // Define a variable to contain a content resolver instance
    ContentResolver contentResolver;

    private ExConvictRepository exConvictRepository;
    private UserRepository userRepository;
    private ReportRepository reportRepository;
    private List<ExConvictReport> exConvictsReports;

    /**
     * Set up the sync adapter
     */
    public SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        /*
         * If your app uses a content resolver, get an instance of it
         * from the incoming Context
         */
        contentResolver = context.getContentResolver();
    }

    /**
     * Set up the sync adapter. This form of the
     * constructor maintains compatibility with Android 3.0
     * and later platform versions
     */
    public SyncAdapter(
            Context context,
            boolean autoInitialize,
            boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
        /*
         * If your app uses a content resolver, get an instance of it
         * from the incoming Context
         */
        contentResolver = context.getContentResolver();
    }

    /*
     * Specify the code you want to run in the sync adapter. The entire
     * sync adapter runs in a background thread, so you don't have to set
     * up your own background processing.
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onPerformSync(
            Account account,
            Bundle extras,
            String authority,
            ContentProviderClient provider,
            SyncResult syncResult) {

        Log.i(TAG, "onPerformSync() was called");

        // !!!!!
        //API 21 RADI, API 25 RADI, API 29 NE RADI!!!

        // TODO 1 -> Pronaći način za automatsko detektovanje ip adrese interneta
        // TODO 1 -> Potencijalno reseno ako su telefon i lap konektovani na istu wifi mrezu, otkomentarisati
        //Isprobati sa telefonom
        String ip = wifiIpAddress(this.getContext().getApplicationContext());
        Log.d("RESTTASK", ip);
        //String ip_address  = getIpAddress();
        //Log.d("RESTTASK", ip_address);


        final String uri = "http://192.168.0.71:8080/api/exConvicts";
        //final String uri = "http://" + ip + ":8080/api/exConvicts";
        ExConvict[] exConvicts = new RestTask().getExConvicts(uri);
        final String uri2 = "http://192.168.0.71:8080/api/users";
        //final String uri2 = "http://" + ip + ":8080/api/users";
        User[] users = new RestTask().getUsers(uri2);
        final String uri3 = "http://192.168.0.71:8080/api/reports";

        //final String uri3 = "http://" + ip + ":8080/api/reports";
        Report[] reports = new RestTask().getReports(uri3);
        Log.d("RESTTASK", "Rezultat (exConvicts) : " + exConvicts.length);
        Log.d("RESTTASK", "Rezultat (users) : " + users.length);
        Log.d("RESTTASK", "Rezultat (reports) : " + reports.length);
        // TODO 2 -> Nakon preuzimanje svih osuđenika popuniti bazu na telefonu

        MyDatabase db =  MyDatabase.getDatabase(this.getContext());
        exConvictRepository = ExConvictRepository.getInstance(db.exConvictDao());
        reportRepository = ReportRepository.getInstance(db.reportDao());
        userRepository = UserRepository.getInstance(db.userDao());
        exConvictsReports = exConvictRepository.getExConvictReports();

        for(ExConvict exConvict: exConvicts) {
            exConvictRepository.insertExConvict(exConvict);
        }
        for(User user: users) {
            userRepository.insertUser(user);
        }
        for(Report report: reports) {
            reportRepository.insertReport(report);
        }

        Log.d("RESTTASK", "Kraj");

        // TODO 3 -> Provera da li se desi konflikt u bazi ili update

        //TODO 4 -> Provera da li ako je zika zikic bio na id-u 2 a na serveru se pojavi pera peric na tom id-u dodje do update-a

        //TODO 5 -> A reports?


        /*
         * Put the data transfer code here.
         */
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    protected String wifiIpAddress(Context context) {
        WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        int ipAddress = wifiManager.getConnectionInfo().getIpAddress();

        // Convert little-endian to big-endianif needed
        if (ByteOrder.nativeOrder().equals(ByteOrder.LITTLE_ENDIAN)) {
            ipAddress = Integer.reverseBytes(ipAddress);
        }

        byte[] ipByteArray = BigInteger.valueOf(ipAddress).toByteArray();

        String ipAddressString;
        try {
            ipAddressString = InetAddress.getByAddress(ipByteArray).getHostAddress();
        } catch (UnknownHostException ex) {
            Log.e("WIFIIP", "Unable to get host address.");
            ipAddressString = null;
        }

        return ipAddressString;
    }

    private String getIpAddress() {
        String ip = "";
        try {
            Enumeration<NetworkInterface> enumNetworkInterfaces = NetworkInterface
                    .getNetworkInterfaces();
            while (enumNetworkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = enumNetworkInterfaces
                        .nextElement();
                Enumeration<InetAddress> enumInetAddress = networkInterface
                        .getInetAddresses();
                while (enumInetAddress.hasMoreElements()) {
                    InetAddress inetAddress = enumInetAddress.nextElement();

                    if (inetAddress.isSiteLocalAddress()) {
                        ip += "SiteLocalAddress: "
                                + inetAddress.getHostAddress() + "\n";
                    }
                }
            }

        } catch (SocketException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            ip += "Something Wrong! " + e.toString() + "\n";
        }
        return ip;
    }
}
