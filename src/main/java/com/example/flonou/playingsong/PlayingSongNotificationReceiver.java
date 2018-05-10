package com.example.flonou.playingsong;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.widget.Toast;

public class PlayingSongNotificationReceiver extends PlayingSongBaseReceiver {

    @Override
    protected void onNotPlayingReceive(Context context) {
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.cancel(1);
    }

    @Override
    public void onSongPlayingReceive(Context context, String artist, String album, String track, Uri albumArtUri) {
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        Toast.makeText(context,track,Toast.LENGTH_SHORT).show();
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, "My_Channel")
                .setSmallIcon(R.drawable.ic_notifications_black_24dp)
                .setContentTitle(track)
                .setContentText(artist + " - " + album)

                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        if (albumArtUri != null)
            mBuilder.setLargeIcon(PlayingSongBaseService.getAlbumart(context,albumArtUri));

        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(1, mBuilder.build());
    }
}
