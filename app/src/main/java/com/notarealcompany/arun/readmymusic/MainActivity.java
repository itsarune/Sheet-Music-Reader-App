package com.notarealcompany.arun.readmymusic;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

/**
 * This is the Main Activity Class. It contains UI, methods and processes for the digital tuner
 * screen on the Android app. It creates floating tabs at the bottom of the screen to access other
 * activities.
 *
 * @author Arun B.
 * @version 1.1, 30/4/20
 */
public class MainActivity extends AppCompatActivity {
    public BlankFragment fragment;
    FragmentTransaction fragmentTransaction;
    FragmentManager fragmentManager;

    /**
     * Creates the music digitizer activity. It creates a fragment that provides users the choice
     * to create a new project or import an existing one from the database.
     *
     * @param savedInstanceState
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageView music = (ImageView) findViewById(R.id.imageView);
        music.setVisibility(View.INVISIBLE);

        /**
         * Computational Complexity Analysis Code
         */
        /*ArrayList listOfStrings = new ArrayList<String>();
        try (BufferedReader br = new BufferedReader((
                new InputStreamReader(getAssets().open("cca.txt")))))
        {
            String line = "";
            while((line = br.readLine()) != null)
            {
                listOfStrings.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        Collections.sort(listOfStrings);
        Log.d("CCA", "Sorted: " + listOfStrings);
        long startTime = System.currentTimeMillis();
        if (SortingAndSearching.binarySearchNameNoSort("blahh",
                listOfStrings) != -1)
            Log.d("CCA", "Found a match!");
        else
            Log.d("CCA", "Didn't find anything!");
        long endTime = System.currentTimeMillis();

        Log.d("CCA", "Time taken: " + (endTime-startTime));*/

        //FragmentManager f = getSupportFragmentManager();
        //FragmentTransaction fragmentTransaction = f.beginTransaction();

        //MusicDatabaseManager db = new MusicDatabaseManager(this);
        //DigitalizedMusic importedProject = db.importProject(1);
        //Log.d("ARUNS_DEBUG", "Name of imported object: " + importedProject.getNameOfMusic());
        //db.closeDatabase();

        fragment = new BlankFragment();
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragment_container, fragment);
        fragmentTransaction.commit();

        createTabs();

        //Fragment fragment = new ExampleFragment();
        //fragmentTransaction.add(R.id.get_music, fragment);
        /*DigitalizedMusic t = new DigitalizedMusic();
        t.setMusicImage(BitmapFactory.decodeResource(getResources(), R.drawable.test));

        ImageView i = (ImageView) findViewById(R.id.imageView);
        Bitmap bm = t.getImageOfMusic();
        i.setImageBitmap(bm);*/
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
