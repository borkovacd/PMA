package com.android.exconvictslocator.synchronization;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.android.exconvictslocator.MainActivity;
import com.android.exconvictslocator.util.NetworkStateTools;

public class SyncReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        int status = NetworkStateTools.getConnectivityStatus(context);

        if (intent.getAction().equals(MainActivity.SYNC_DATA)) {
            if(status != 0 ) {
                Toast.makeText(context, "Povezani ste na internet", Toast.LENGTH_SHORT).show();
            }

        }
    }
}
