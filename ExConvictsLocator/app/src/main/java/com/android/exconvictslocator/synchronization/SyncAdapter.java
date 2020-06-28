package com.android.exconvictslocator.synchronization;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;
import android.util.Log;

import com.android.exconvictslocator.MyDatabase;
import com.android.exconvictslocator.entities.ExConvict;
import com.android.exconvictslocator.repositories.impl.ExConvictRepository;

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
        final String uri = "http://10.5.50.253:8080/api/exConvicts";
        ExConvict[] exConvicts = new RestTask().getExConvicts(uri);
        Log.d("RESTTASK", "Rezultat: " + exConvicts.length);
        // TODO 2 -> Nakon preuzimanje svih osuđenika popuniti bazu na telefonu

        MyDatabase db =  MyDatabase.getDatabase(this.getContext());
        exConvictRepository = ExConvictRepository.getInstance(db.exConvictDao());

        for(ExConvict exConvict: exConvicts) {
            exConvictRepository.insertExConvict(exConvict);
        }

        // TODO 3 -> Provera da li se desi konflikt u bazi ili update

        //TODO 4 -> Provera da li ako je zika zikic bio na id-u 2 a na serveru se pojavi pera peric na tom id-u dodje do update-a

        //TODO 5 -> A reports?


        /*
         * Put the data transfer code here.
         */
    }
}
