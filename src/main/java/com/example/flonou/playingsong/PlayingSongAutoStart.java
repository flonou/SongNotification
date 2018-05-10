package com.example.flonou.playingsong;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.Log;

public class PlayingSongAutoStart extends BroadcastReceiver  {
    public PlayingSongAutoStart() {
        Log.d("SongNotif", "application started");


    }


    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            Intent serviceIntent = new Intent(context, PlayingSongMainService.class);
            context.startService(serviceIntent);
        }
    }
}
