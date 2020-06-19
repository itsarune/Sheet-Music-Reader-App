package com.notarealcompany.arun.readmymusic;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.CountDownTimer;

import java.util.TimerTask;

public class PlaybackTask extends TimerTask {
    Context context;

    @Override
    public void run() { playTick(); }

    public void setup(Context context)
    {
        this.context = context;
    }

    private void playTick()
    {
        final MediaPlayer mp = MediaPlayer.create(context, R.raw.cymbal_crash);
        mp.start();
        mp.setOnSeekCompleteListener(new MediaPlayer.OnSeekCompleteListener()
                                     {
                                         @Override
                                         public void onSeekComplete(MediaPlayer mp)
                                         {
                                             mp.release();
                                         }
                                     }
        );
    }
}
