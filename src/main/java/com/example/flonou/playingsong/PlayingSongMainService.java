package com.example.flonou.playingsong;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.Log;

public class PlayingSongMainService extends Service {
    public PlayingSongMainService() {


    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onStart(Intent intent, int startid)
    {
        Log.d("SongNotif", "main service started");
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean notificationsOn = preferences.getBoolean("notification_switch", false);
        if (notificationsOn)
        {
            Intent msgIntent = new Intent(this, PlayingSongNotificationService.class);
            //msgIntent.putExtra(SimpleIntentService.PARAM_IN_MSG, strInputMsg);
            startService(msgIntent);
        }

        boolean popupOn = preferences.getBoolean("popup_switch", false);
        if (popupOn)
        {
            Intent msgIntent = new Intent(this, PlayingSongPopupService.class);
            //msgIntent.putExtra(SimpleIntentService.PARAM_IN_MSG, strInputMsg);
            startService(msgIntent);
        }
    }
}
