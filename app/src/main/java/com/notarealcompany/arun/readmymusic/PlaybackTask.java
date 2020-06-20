package com.notarealcompany.arun.readmymusic;

import android.content.Context;
import android.media.MediaPlayer;
import java.util.TimerTask;

/**
 * PlaybackTask is a helper class for the Metronome. It is a task that can be scheduled to play a
 *      cymbal crash at regular intervals.
 *
 * Before using PlaybackTask, you must ensure you call <code>setup()</code> before running the task.
 *      It only needs to be set-up once to associate an application context with the program.
 *
 * @author Arun B.
 * @version 1.0, 19/6/20
 */
public class PlaybackTask extends TimerTask {
    /* Current application context */
    Context context;

    /**
     * Plays the metronome tick whenever the task is schedule to run.
     */
    @Override
    public void run() { playTick(); }

    /**
     * Associates the application context to the PlaybackTask (required for MediaPlayer).
     *
     *      This function must be run prior to starting the task, or else the playback will result
     * in errors.
     *
     * @param context   Context, current application context
     */
    void setup(Context context)
    {
        this.context = context;
    }

    /**
     * Plays the metronome click using Android's MediaPlayer. It releases resources after it is done
     *      playing the tick.
     */
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
