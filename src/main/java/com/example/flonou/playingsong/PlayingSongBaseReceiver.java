package com.example.flonou.playingsong;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.widget.Toast;

import java.io.FileDescriptor;

public class PlayingSongBaseReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        Boolean playing = intent.getBooleanExtra("playing", false);

        for (String key : intent.getExtras().keySet()) {
            Log.v("SongNotif", key + ": " + intent.getExtras().get(key));
        }

        String action = intent.getAction();

        // wtf sony ?
        if (action.equals("com.sonyericsson.music.playbackcontrol.ACTION_TRACK_STARTED"))
            playing = true;
        if (playing)
            onPlayingReceive(context);
        else {
            onNotPlayingReceive(context);
            return;
        }



        String cmd = intent.getStringExtra("command");
        Log.v("SongNotif", action + " / " + cmd);
        String artist = null;
        if (intent.hasExtra("artist"))
            artist = intent.getStringExtra("artist");
        else if (intent.hasExtra("ARTIST_NAME"))
            artist = intent.getStringExtra("ARTIST_NAME");
        String album = null;
        if (intent.hasExtra("album"))
            album = intent.getStringExtra("album");
        if (intent.hasExtra("ALBUM_NAME"))
            album = intent.getStringExtra("ALBUM_NAME");
        String track = null;
        if (intent.hasExtra("track"))
            track = intent.getStringExtra("track");
        if (intent.hasExtra("TRACK_NAME"))
            track = intent.getStringExtra("TRACK_NAME");

           /* if (!playing)
            {
                return;
            }*/

        Log.v("SongNotif",artist+":"+album+":"+track);

        long album_id = -1;

        intent.getLongExtra("album_id",0);
        if (intent.hasExtra("ALBUM_ID"))
            album_id = intent.getLongExtra("ALBUM_ID",-1);

        if (album_id <= 0) {
            album_id = getAlbumId(context, artist, album, track);
        }
        Uri albumArtUri = null;
        if (album_id > 0)
            albumArtUri = getAlbumArtUri(album_id);

        onSongPlayingReceive(context, artist, album, track, albumArtUri);

    }

    protected void onPlayingReceive(Context context) {
    }

    protected void onNotPlayingReceive(Context context) {
    }

    protected void onSongPlayingReceive(Context context, String artist, String album, String track, Uri albumArtUri) {

    }

    public Long getAlbumId(Context context, String artist, String album, String track) {
        Long albumid = 0L;
        ContentResolver contentResolver = context.getContentResolver();


        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

        String[] projection = new String[]{
                /*  MediaStore.Audio.Media._ID,
                  MediaStore.Audio.Media.ARTIST,
                  MediaStore.Audio.Media.TITLE,
                  MediaStore.Audio.Media.ALBUM,
                  MediaStore.Audio.Media.DURATION,
                  MediaStore.Audio.Media.DATA,*/
                MediaStore.Audio.Media.ALBUM_ID};

        Log.v("SongNotif",artist+" - " +  album + " - " + track);

        // trying to find with the song name in particular
        String selection = MediaStore.Audio.Media.IS_MUSIC + "!= 0" +
                (artist != null ? " AND " + MediaStore.Audio.Media.ARTIST + "=?" : "") +
                (album != null ? " AND " + MediaStore.Audio.Media.ALBUM + "=?" : "") +
                " AND " + MediaStore.Audio.Media.TITLE + "=?";

        String[] arguments = new String[3];
        int i = 0;
        if (artist != null)
        {
            arguments[i] = artist;
            i++;
        }
        if (album != null)
        {
            arguments[i] = album;
            i++;
        }
        arguments[i] = track;

        Cursor cursor = null;
        try
        {
            cursor = contentResolver.query(uri, projection, selection, arguments, null);

            //cursor = contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null, null);

            // we found what we were looking for
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToNext();
                String album_id = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));
                albumid = Long.parseLong(album_id);
            }
            // else try with approximation (for VLC)
            else {
                Log.v("SongNotif", "Could not find with the song name. Trying without it");
                // close previous cursor
                cursor.close();
                selection = MediaStore.Audio.Media.IS_MUSIC + "!= 0" +
                        (artist != null ? " AND " + MediaStore.Audio.Media.ARTIST + "=?" : "") +
                        (album != null ? " AND " + MediaStore.Audio.Media.ALBUM + "=?" : "");

                arguments = new String[2];
                i = 0;
                if (artist != null) {
                    arguments[i] = artist;
                    i++;
                }
                if (album != null) {
                    arguments[i] = album;
                    i++;
                }
                // ignore the title for now
                cursor = contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, selection, arguments, null);
                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToNext();
                    String album_id = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));
                    albumid = Long.parseLong(album_id);
                }
            }
        } catch (Exception e)
            {

            }
            finally
            {
                if (cursor!= null)
                cursor.close();
            }


        return albumid;
    }

    public Uri getAlbumArtUri(Long album_id){
        final Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");
        Uri uri = ContentUris.withAppendedId(sArtworkUri, album_id);
        return uri;
    }



}
