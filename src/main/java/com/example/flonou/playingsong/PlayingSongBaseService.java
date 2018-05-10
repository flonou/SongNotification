package com.example.flonou.playingsong;

import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Intent;
import android.content.Context;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.widget.Toast;

import java.io.FileDescriptor;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class PlayingSongBaseService extends IntentService {

    public static Bitmap getAlbumart(Context context, Uri albumArtUri){
        Bitmap bm = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        try{
            ParcelFileDescriptor pfd = context.getContentResolver().openFileDescriptor(albumArtUri, "r");
            if (pfd != null){
                FileDescriptor fd = pfd.getFileDescriptor();
                bm = BitmapFactory.decodeFileDescriptor(fd, null, options);
                pfd = null;
                fd = null;
            }
        } catch(Error ee){}
        catch (Exception e) {}
        return bm;
    }


    private BroadcastReceiver mReceiver = new PlayingSongNotificationReceiver();

    public PlayingSongBaseService(BroadcastReceiver receiver) {

        super("PlayingSongBaseService");
        mReceiver = receiver;
    }


    @Override
    public void onStart(Intent intent, int startid) {

        Log.d("SongNotif", "base service started");

        IntentFilter iF = new IntentFilter();

        // Read action when music player changed current song
        // I just try it with stock music player form android

        // stock music player
        iF.addAction("com.android.music.metachanged");
        iF.addAction("com.android.music.playstatechanged");

        // MIUI music player
        iF.addAction("com.miui.player.metachanged");

        // HTC music player
        iF.addAction("com.htc.music.metachanged");

        // WinAmp
        iF.addAction("com.nullsoft.winamp.metachanged");

        // MyTouch4G
        iF.addAction("com.real.IMP.metachanged");

        // Sony
        iF.addAction("com.sonyericsson.music.metachanged");
        iF.addAction("com.sonyericsson.music.playstatechanged");
        iF.addAction("com.sonyericsson.music.playbackcontrol.ACTION_TRACK_STARTED");
        iF.addAction("com.sonyericsson.music.playbackcontrol.ACTION_PAUSED");
        iF.addAction("com.sonyericsson.music.playbackcontrol.ACTION_PLAYBACK_PLAY");

        // xiaomi
        iF.addAction("soundbar.music.metachanged");

        // Spotify
        iF.addAction("com.spotify.music.metadatachanged");

        registerReceiver(mReceiver, iF);

    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

    }


}
