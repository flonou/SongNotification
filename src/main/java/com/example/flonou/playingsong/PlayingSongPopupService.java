package com.example.flonou.playingsong;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.content.IntentFilter;
import android.util.Log;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class PlayingSongPopupService extends PlayingSongBaseService {

    public PlayingSongPopupService() {
        super(new PlayingSongPopupReceiver());
    }


    @Override
    public void onStart(Intent intent, int startid) {
        super.onStart(intent, startid);
        Log.d("SongNotif", "popup service started");
    }


}
