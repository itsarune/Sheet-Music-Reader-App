package com.notarealcompany.arun.readmymusic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class Metronome extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_metronome);
        createTabs();
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
                Intent i = new Intent(getApplicationContext(), Tuner.class);
                startActivity(i);
            }
        });
    }
}
