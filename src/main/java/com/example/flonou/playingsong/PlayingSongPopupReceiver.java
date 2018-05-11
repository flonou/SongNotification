package com.example.flonou.playingsong;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;

public class PlayingSongPopupReceiver extends PlayingSongBaseReceiver {

    @Override
    public void onSongPlayingReceive(Context context, String artist, String album, String track, Uri albumArtUri) {
        Intent intent = new Intent(context,PopupActivity.class);

        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        //intent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.putExtra("artist", artist);
        intent.putExtra("album", album);
        intent.putExtra("track", track);
        intent.putExtra("albumArt", albumArtUri != null ? albumArtUri.toString() : "");


        context.startActivity(intent);


    }
}
