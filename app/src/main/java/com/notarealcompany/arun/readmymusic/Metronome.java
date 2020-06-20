package com.notarealcompany.arun.readmymusic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import java.util.Timer;

/**
 * This Metronome Activity classes contains the functions and methods in presenting and using the
 *      metronome feature in the application.
 *
 * In this activity, users can use right and left arrows to increment and decrease the tempo
 *      respectively by increments of two. The current tempo will be displayed on-screen. Currently,
 *      this activity only supports 4/4 time and will have a cymbal-crash every beat.
 *
 * The metronome will start playing immediately after this activity is opened.
 *
 * @author Arun B.
 * @version 1.0, 19/6/20
 */
public class Metronome extends AppCompatActivity {
    /* tempo of the metronome */
    private int tempo = 140;

    /* timers used to control media playback */
    private Timer tickTimer = new Timer();
    private PlaybackTask tickTimerTask = new PlaybackTask();

    /**
     * Creates the layout for the metronome activity. It creates the buttons that can control the
     *      tempo and updates the tempo display on-screen.
     *
     * @param savedInstanceState    Bundle, Handled by Android
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_metronome);
        createTabs();
        final TextView tempoDisplay = (TextView) findViewById(R.id.bpmTextView);
        tempoDisplay.setText(String.valueOf(tempo));
        play();
        ImageButton decreaseTempo = (ImageButton) findViewById(R.id.left_arrow_btn);
        decreaseTempo.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                stop();
                setTempo(tempo-2);
                tempoDisplay.setText(String.valueOf(tempo));
                play();
            }
        });
        ImageButton increaseTempo = (ImageButton) findViewById(R.id.right_arrow_btn);
        increaseTempo.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                stop();
                setTempo(tempo+2);
                tempoDisplay.setText(String.valueOf(tempo));
                play();
            }
        });
    }

    /**
     * This method creates ImageButtons that float at the bottom of the screen for navigation
     * between activity screens.
     */
    private void createTabs()
    {
        ImageButton digBtn = (ImageButton) findViewById(R.id.digMusicBtn);
        digBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                stop();
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
            }
        });

        ImageButton tunerBtn = (ImageButton) findViewById(R.id.tunerButton);
        tunerBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                stop();
                Intent i = new Intent(getApplicationContext(), Tuner.class);
                startActivity(i);
            }
        });
    }

    /**
     * Sets the new tempo for the metronome activity.
     *
     * @param newTempo  int, in BPM
     */
    public void setTempo(int newTempo) { tempo = newTempo; }

    /**
     * Schedules the metronome ticks to be played at the tempo requested.
     */
    private void play()
    {
        tickTimerTask.setup(this);
        tickTimer.schedule(tickTimerTask, 0, 60000 / tempo);
    }

    /**
     * Stops the current scheduled metronome ticks.
     */
    private void stop()
    {
        tickTimerTask.cancel();
        tickTimer.cancel();
        tickTimer = new Timer();
        tickTimerTask = new PlaybackTask();
    }
}

