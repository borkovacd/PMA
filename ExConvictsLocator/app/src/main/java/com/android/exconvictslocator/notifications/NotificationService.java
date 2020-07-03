package com.android.exconvictslocator.notifications;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.preference.PreferenceManager;

import com.android.exconvictslocator.MyDatabase;
import com.android.exconvictslocator.R;
import com.android.exconvictslocator.entities.ExConvict;
import com.android.exconvictslocator.entities.Report;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class NotificationService extends Service {
    public static final String NOTIFICATION_CHANNEL_ID = "10001";
    private final static String default_notification_channel_id = "default";
    Timer timer;
    TimerTask timerTask;
    String TAG = "OLGA";
    int Your_X_SECS = 60;

    private MyDatabase myDatabase = MyDatabase.getDatabase(this.getApplication()) ;;
    private List<ExConvict> exConvicts ;
    private List<Report> reportsByExConvict ;
    private  List<Report> reports ;

    double lat ;
    double lan ;

    double latUser = 45.2523492 ;
    double lanUser = 19.7960865;

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "onStartCommand");
        super.onStartCommand(intent, flags, startId);
        startTimer();
        //sendNotification();
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        Log.e(TAG, "onCreate");
    }

    @Override
    public void onDestroy() {
        Log.e(TAG, "onDestroy");
        //stopTimerTask();
        super.onDestroy();
    }

    //we are going to use a handler to be able to run in our TimerTask
    final Handler handler = new Handler();

    public void sendNotification() {

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        String distance_radius_string = sharedPref.getString("distance_radius", "2");
        int distance_radius = Integer.parseInt(distance_radius_string);
        Log.d("RESTTASK", "Distanca (INT): " + distance_radius);

        exConvicts = myDatabase.exConvictDao().getExConvicts();
        reports = myDatabase.reportDao().findAllReports();
        for (ExConvict exConvict: exConvicts) {

            reportsByExConvict = myDatabase.reportDao().findReportsByExConvict(exConvict.getId());

            if (reportsByExConvict.size() != 0 || reportsByExConvict != null) {
                lat = reportsByExConvict.get(0).getLat() ;
                lan = reportsByExConvict.get(0).getLang() ;

                LatLng lld1 = new LatLng(lat, lan);
                LatLng lld2 = new LatLng(latUser, lanUser);
                Double distance = distance(lat, lan, latUser, lanUser);
                Log.d(TAG, "Distance in kilometers " + distance);

                if (distance < distance_radius) {
                    Log.d(TAG, "BLIZU SU! ");

                }
                else {
                    Log.d(TAG, "NISU BLIZU ! ");

                }

            }

        }
        /*
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
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
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                createNotification();
            }

            @Override
            public void onProviderDisabled(String provider) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onProviderEnabled(String provider) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onStatusChanged(String provider, int status,
                                        Bundle extras) {
                // TODO Auto-generated method stub
            }
        });

        NotificationCompat.Builder notifyBuilder = getNotificationBuilder();
        mNotifyManager.notify(NOTIFICATION_ID, notifyBuilder.build());
        */
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
    }

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