package com.android.exconvictslocator.notifications;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.preference.PreferenceManager;

import com.android.exconvictslocator.ListOfExConvicts;
import com.android.exconvictslocator.MyDatabase;
import com.android.exconvictslocator.R;
import com.android.exconvictslocator.entities.ExConvict;
import com.android.exconvictslocator.entities.Report;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class NotificationService extends Service {

    // *** NOTIFICATIONS ***
    // Every notification channel must be associated with an ID that is unique within your package.
    private static final String PRIMARY_CHANNEL_ID = "primary_notification_channel";
    private NotificationManager mNotifyManager;
    private static final int NOTIFICATION_ID = 0;

    //public static final String NOTIFICATION_CHANNEL_ID = "10001";
    //private final static String default_notification_channel_id = "default";

    Timer timer;
    TimerTask timerTask;

    String NOTIFICATION_TAG = "NOTIFICATION_TAG";

    int Your_X_SECS = 60;

    private MyDatabase myDatabase;
    private List<ExConvict> exConvicts;
    private List<Report> reportsByExConvict;
    private List<Report> reports;

    double lat;
    double lan;

    double latUser = 45.2523492;
    double lanUser = 19.7960865;

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(NOTIFICATION_TAG, "onStartCommand");
        myDatabase = MyDatabase.getDatabase(this.getApplication());
        super.onStartCommand(intent, flags, startId);
        startTimer();
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        Log.d(NOTIFICATION_TAG, "onCreate");
        createNotificationChannel(); //!!!
    }

    @Override
    public void onDestroy() {
        Log.d(NOTIFICATION_TAG, "Pozvan onDestroy u okviru servisa!");
        stopTimerTask();
        super.onDestroy();
        stopSelf();
    }

    //we are going to use a handler to be able to run in our TimerTask
    final Handler handler = new Handler();

    public void sendNotification() {

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        String distance_radius_string = sharedPref.getString("distance_radius", "2");
        int distance_radius = Integer.parseInt(distance_radius_string);
/*
        try {
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            Location current = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if(current != null){
                Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                List<android.location.Address> addresses = geocoder.getFromLocation(current.getLatitude(), current.getLongitude(),1);
                double longitude = addresses.get(0).getLongitude();
                double latitude = addresses.get(0).getLatitude();
                String address = addresses.get(0).getAddressLine(0);

                lanUser = longitude;
                Log.d(TAG, "LAN: " + lanUser);
                latUser = latitude;
                Log.d(TAG, "LAT: " + latUser);
            }else{
                //locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, UpdateLocationActivity.this);


            }
        }catch (Exception e) {
            System.out.println(e.getStackTrace());
        }
*/
        exConvicts = myDatabase.exConvictDao().getExConvicts();
        reports = myDatabase.reportDao().findAllReports();
        if (exConvicts.size() != 0) {
            
            for (ExConvict exConvict : exConvicts) {

                reportsByExConvict = myDatabase.reportDao().findReportsByExConvict(exConvict.getId());

                if (reportsByExConvict.size() != 0) {
                    Log.d(NOTIFICATION_TAG, "Datum poslednje prijavljene lokacije:  " + reportsByExConvict.get(0).getDate());
                    lat = reportsByExConvict.get(0).getLat();
                    lan = reportsByExConvict.get(0).getLang();

                    LatLng lld1 = new LatLng(lat, lan);
                    LatLng lld2 = new LatLng(latUser, lanUser);
                    Double distance = distance(lat, lan, latUser, lanUser);
                    distance = Math.round(distance * 100.0) / 100.0;
                    Log.d(NOTIFICATION_TAG, "Distance in kilometers " + distance);

                    if (distance <= distance_radius) {
                        Log.d(NOTIFICATION_TAG, "BLIZU SU! ");
                        //createNotification();
                        NotificationCompat.Builder notifyBuilder = getNotificationBuilder(exConvict, distance);
                        mNotifyManager.notify(NOTIFICATION_ID, notifyBuilder.build());
                        //stopSelf(); //valjda ovo nista ne radi, ne brisem dok ne proverim!
                    } else {
                        Log.d(NOTIFICATION_TAG, "NISU BLIZU ! ");
                    }

                }

            }
        }
    }

    public void startTimer() {
        timer = new Timer();
        initializeTimerTask();
        timer.schedule(timerTask, 5000, Your_X_SECS * 1000); //
    }

    public void stopTimerTask() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    public void initializeTimerTask() {
        timerTask = new TimerTask() {
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        sendNotification();
                    }
                });
            }
        };
    }

    private NotificationCompat.Builder getNotificationBuilder(ExConvict exConvict, Double distance) {
        Intent notificationIntent = new Intent(this, ListOfExConvicts.class);
        PendingIntent notificationPendingIntent =
                PendingIntent.getActivity(this, NOTIFICATION_ID, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder notifyBuilder = new NotificationCompat.Builder(this, PRIMARY_CHANNEL_ID)
                .setContentTitle("UPOZORENJE!")
                .setContentText("Na udaljenosti od " + distance + "km  nalazi se bivši osuđenik " + exConvict.getFirstName() + " " + exConvict.getLastName())
                .setStyle(new NotificationCompat.BigTextStyle().bigText("Na udaljenosti od " + distance + "km nalazi se bivši osuđenik " + exConvict.getFirstName() + " " + exConvict.getLastName()))
                .setSmallIcon(R.drawable.ic_warning)
                .setContentIntent(notificationPendingIntent)
                .setAutoCancel(true) //Setting auto-cancel to true closes the notification when user taps on it.
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setDefaults(NotificationCompat.DEFAULT_ALL);
        return notifyBuilder;
    }

    public void createNotificationChannel() {
        mNotifyManager = (NotificationManager)
                getSystemService(NOTIFICATION_SERVICE);
        // Because notification channels are only available in API 26 and higher,
        // adding a condition to check for the device's API version.
        if (android.os.Build.VERSION.SDK_INT >=
                android.os.Build.VERSION_CODES.O) {
            // The name is displayed under notification Categories in the device's user-visible Settings app.
            NotificationChannel notificationChannel = new NotificationChannel(PRIMARY_CHANNEL_ID,
                    "Ex-Convicts Notifications", NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setDescription("Notification from Ex-Convicts Locator");
            mNotifyManager.createNotificationChannel(notificationChannel);
        }
    }

    /*
    private void createNotification() {
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), default_notification_channel_id);
        mBuilder.setContentTitle("My Notification");
        mBuilder.setContentText("Notification Listener Service Example");
        mBuilder.setTicker("Notification Listener Service Example");
        mBuilder.setSmallIcon(R.drawable.ic_launcher_foreground);
        mBuilder.setAutoCancel(true);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "NOTIFICATION_CHANNEL_NAME", importance);
            mBuilder.setChannelId(NOTIFICATION_CHANNEL_ID);
            assert mNotificationManager != null;
            mNotificationManager.createNotificationChannel(notificationChannel);
        }
        assert mNotificationManager != null;
        mNotificationManager.notify((int) System.currentTimeMillis(), mBuilder.build());
    }*/

    private double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (dist);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }
}