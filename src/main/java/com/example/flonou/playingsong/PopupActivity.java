package com.example.flonou.playingsong;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class PopupActivity extends Activity {

    PopupTimer timer = null;

    public ProgressBar progressBar = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup);
        Boolean stop = getIntent().getExtras().getBoolean("stop");
        if(stop) {
            return;
        }

        createPopup();

        countDown();
    }

    void createPopup()
    {
        String artist = getIntent().getStringExtra("artist");
        String album = getIntent().getStringExtra("album");
        String track = getIntent().getStringExtra("track");
        String albumArtUriString = getIntent().getStringExtra("albumArt");
        Uri albumArtUri = !albumArtUriString.equals("")? Uri.parse(albumArtUriString) : null;
        Log.d("SongNotif", "popup created : " + artist);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        int position = preferences.getInt("edit_seek_bar_height", 1);


        int height = (int)(dm.heightPixels*0.15);
        int width = (int)(dm.widthPixels*0.7);

        if (isLandscape())
        {
            Log.d("SongNotif", "is landscape");
            height = (int)(dm.widthPixels*0.15);
            width = (int)(dm.heightPixels*0.7);
        }

        Log.d("SongNotif", "height : " + height + "   width : " + width);
        getWindow().setLayout(width,height);

        WindowManager.LayoutParams params = getWindow().getAttributes();

        params.gravity = Gravity.TOP;
        params.x = 0;

        int availableSpace = dm.heightPixels-height;

        params.y = availableSpace*position / 100;
        getWindow().setAttributes(params);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        // clicks go through
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
        //getWindow().setTransitionBackgroundFadeDuration(10);
        GradientDrawable shape =  new GradientDrawable();
        shape.setColor(Color.WHITE);
        shape.setCornerRadius( 32 );

        int alpha = preferences.getInt("edit_seek_bar_transparency", 1);

        shape.setAlpha(alpha);
        getWindow().setBackgroundDrawable(shape);
        this.setFinishOnTouchOutside(true);

        AppCompatTextView textViewTrack = new AppCompatTextView(this);
        textViewTrack.setTypeface(null, Typeface.BOLD);
        textViewTrack.setMaxLines(2);
        textViewTrack.setMinLines(1);
        TextViewCompat.setAutoSizeTextTypeWithDefaults(textViewTrack, TextViewCompat.AUTO_SIZE_TEXT_TYPE_UNIFORM);
        textViewTrack.setText(track);
        textViewTrack.setTextColor(Color.BLACK);
        textViewTrack.setId(1);

        AppCompatTextView textViewArtist = new AppCompatTextView(this);
        textViewArtist.setText(artist);
        textViewArtist.setMaxLines(2);
        textViewArtist.setMinLines(1);
        TextViewCompat.setAutoSizeTextTypeWithDefaults(textViewArtist, TextViewCompat.AUTO_SIZE_TEXT_TYPE_UNIFORM);
        textViewArtist.setTextColor(Color.DKGRAY);
        textViewArtist.setId(2);

        AppCompatTextView textViewAlbum = new AppCompatTextView(this);
        textViewAlbum.setTypeface(null, Typeface.ITALIC);
        textViewAlbum.setText(album);
        textViewAlbum.setMaxLines(2);
        textViewAlbum.setMinLines(1);
        TextViewCompat.setAutoSizeTextTypeWithDefaults(textViewAlbum, TextViewCompat.AUTO_SIZE_TEXT_TYPE_UNIFORM);
        textViewAlbum.setTextColor(Color.DKGRAY);
        textViewAlbum.setId(3);

        ImageView imageView = null;
        LinearLayout myLayout = null;
        if (albumArtUri != null) {
            imageView = new ImageView(this);
            imageView.setId(4);
            imageView.setImageURI(albumArtUri);
            myLayout = new LinearLayout(this);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.gravity = Gravity.RIGHT;
            myLayout.setLayoutParams(layoutParams);

            LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(height,height);
            imageView.setLayoutParams(layoutParams2);

            myLayout.addView(imageView);

        }

        progressBar = new ProgressBar(this,null, android.R.attr.progressBarStyleHorizontal);
        progressBar.setIndeterminate(true);
        progressBar.setVisibility(View.VISIBLE);
       /* LinearLayout firstLayout = new LinearLayout(this);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(5,5,5,5);
        layoutParams.gravity = Gravity.LEFT;
        layoutParams.setLayoutDirection(LinearLayout.HORIZONTAL);
        firstLayout.setLayoutParams(layoutParams);*/

        RelativeLayout firstLayout = new RelativeLayout(this);
        /*
        TextView tv = new TextView(this);
        tv.setText("Hello, World");

        TextView tv2 = new TextView(this);
        tv2.setText("Hello, World 2");*/


        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams
                (RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        lp.setMargins(10, 10, 10,10);
        if (myLayout != null) {
            firstLayout.addView(myLayout, lp);
            //firstLayout.addView(tv, lp);
/*
        GradientDrawable border = new GradientDrawable();
        border.setColor(0xFFFFFFFF); //white background
        border.setStroke(1, 0xFF000000); //black border with full opacity
            imageView.setBackground(border);*/
        /*GradientDrawable border2 = new GradientDrawable();
        border2.setColor(0xFFFFFFFF); //white background
        border2.setStroke(1, 0xFF990000); //black border with full opacity
        imageView.setBackground(border2);*/
        }
        RelativeLayout.LayoutParams lp2 = new RelativeLayout.LayoutParams
                (width-height-20, height/2 - 10);
        lp2.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        lp2.setMargins(10, 10, 10,0);
        firstLayout.addView(textViewTrack, lp2);

      /*  GradientDrawable border2 = new GradientDrawable();
        border2.setColor(0xFFFFFFFF); //white background
        border2.setStroke(1, 0xFF990000); //black border with full opacity
        textViewTrack.setBackground(border2);
*/
        RelativeLayout.LayoutParams lp3 = new RelativeLayout.LayoutParams
                (width-height-20, height/4);
        lp3.addRule(RelativeLayout.BELOW,textViewTrack.getId());
        lp3.setMargins(10, -10, 10,0);
        firstLayout.addView(textViewArtist, lp3);

        /*GradientDrawable border3 = new GradientDrawable();
        border3.setColor(0xFFFFFFFF); //white background
        border3.setStroke(1, 0xFF990000); //black border with full opacity
        textViewArtist.setBackground(border3);
*/
        RelativeLayout.LayoutParams lp4 = new RelativeLayout.LayoutParams
                (width-height-20, height/4 - 15);
        lp4.addRule(RelativeLayout.BELOW,textViewArtist.getId());
        lp4.setMargins(10, 0, 10,0);
        firstLayout.addView(textViewAlbum, lp4);
/*
        GradientDrawable border4 = new GradientDrawable();
        border4.setColor(0xFFFFFFFF); //white background
        border4.setStroke(1, 0xFF990000); //black border with full opacity
        textViewAlbum.setBackground(border4);*/

        RelativeLayout.LayoutParams lp0 = new RelativeLayout.LayoutParams
                (width, 5);
        lp0.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        lp0.setMargins(32, 0, 32,0);
        firstLayout.addView(progressBar, lp0);

        setContentView(firstLayout);
}

    public boolean isLandscape()
    {
        return getResources().getConfiguration().orientation == android.content.res.Configuration.ORIENTATION_LANDSCAPE;
    }

    public static boolean isXLargeScreen(Context context) {
        return (context.getResources().getConfiguration().screenLayout
        & android.content.res.Configuration.SCREENLAYOUT_SIZE_MASK)
        >= android.content.res.Configuration.SCREENLAYOUT_SIZE_XLARGE;
    }

   /* @Override
public void onConfigurationChanged(Configuration newConfig) {
    super.onConfigurationChanged(newConfig);

    // Checks the orientation of the screen
    if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
        Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show();
    } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
        Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show();
    }
}*/

    @Override
    protected void onNewIntent(Intent intent)
    {
        super.onNewIntent(intent);
        Log.d("SongNotif", "got new intent");
        Boolean stop = intent.getExtras().getBoolean("stop");
        if(stop)
        {
            Log.d("SongNotif", "end of popup");
            this.finish();
        }
        if (timer != null)
            Log.d("SongNotif", "got new intent : " + timer.elapsedTime);
        // filter new intents if this was lees thant 100ms ago
        if (timer == null || timer.elapsedTime > 100)
        {
            setIntent(intent);
            createPopup();
            countDown();
        }
    }

    public void countDown() {
        if (timer != null)
            timer.cancel();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        int popupTime = preferences.getInt("edit_seek_bar", 1);
        timer = new PopupTimer(this, popupTime * 100);
        timer.start();
    }

    class PopupTimer extends CountDownTimer
    {
        Activity mActivity;
        int time = 0;
        ProgressBar progressBar = null;
        public boolean isRunning = false;
        public long elapsedTime = 0;

        public PopupTimer(PopupActivity activity, int millisec)
        {
            super(millisec,100);
            mActivity = activity;
            time = millisec;
            progressBar = activity.progressBar;
            if(progressBar != null) {
                progressBar.setMax(time);
                //progressBar.setProgress(time);
                progressBar.setProgress(time);
                progressBar.setIndeterminate(false);
            }
            Log.d("SongNotif", "starting popup timer");
        }

        public void onTick(long millisUntilFinished) {
            isRunning = true;
            elapsedTime = time - millisUntilFinished;
            int seconds = (int) (millisUntilFinished / 1000) % 60;
            //int minutes = (int) ((millisUntilFinished / (1000*60)) % 60);
            //int hours   = (int) ((millisUntilFinished / (1000*60*60)) % 24);
            //String my_new_str =((((((((( (pad(hours)+":"+pad(minutes)+":"+pad(seconds)));
            //timer.setText( my_new_str);
            float pvalue = (float) (millisUntilFinished * 100) / (60000 * 30);
            //myprogressbar.setProgress((int)(Math.round(pvalue)));
            if(progressBar != null)
            {
                //int value = (int)((time-millisUntilFinished)/100);
                int value = (int)((millisUntilFinished));
                //Log.d("SongNotif", ""+value);
                progressBar.setProgress(value);
                //progressBar.setProgress((int)millisUntilFinished/100);
            }
        }

        public void onFinish() {
            //timer.setText("Completed");
            //goToResult();
            isRunning = false;
            Log.d("SongNotif", "end of popup due to timer");
            mActivity.finish();
        }
    }

}
