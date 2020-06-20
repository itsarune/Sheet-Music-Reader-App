package com.notarealcompany.arun.readmymusic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

/**
 * The Tuner Activity is an interface where users can tune their instrument according to their
 * instrument's music tuning profile and receive feedback for their instrument's tuning using their
 * phone's microphone.
 *
 *      This Activity is still in-progress
 *
 * @author Arun B.
 * @version 1.0, 19/6/20
 */
public class Tuner extends AppCompatActivity {

    /**
     * Creates the layout of the Tuner Activity.
     *
     * @param savedInstanceState    previous state, handled by Android
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tuner);
        createTabs();
    }

    /**
     * This method creates ImageButtons that float at the bottom of the screen for navigation
     * between activity screens.
     */
    private void createTabs()
    {
        ImageButton metronomeBtn = (ImageButton) findViewById(R.id.metronomeButton);
        metronomeBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(getApplicationContext(), Metronome.class);
                startActivity(i);
            }
        });

        ImageButton digBtn = (ImageButton) findViewById(R.id.digMusicBtn);
        digBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
            }
        });
    }
}
