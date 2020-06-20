package com.notarealcompany.arun.readmymusic;

import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import java.util.ArrayList;

/**
 * PlaybackMusicProject uses the music notes in the form of an ArrayList that is passed upon setup
 * to play it back.
 *
 *      The notes that this fragment accepts comes in the form of String arrays and reads the first
 * character of the array as the note to be played. Playback can be controlled using the
 * displayed buttons.
 *
 *      By default, this playback routine plays audio back at 60 BPM.
 *
 * @author Arun B.
 * @version 1.0, 19/6/20
 */
public class PlaybackMusicProject extends Fragment {
    /* Notes for playback */
    private ArrayList<String> playbackNotes = new ArrayList<>();

    /* Unique key to receive fragment arguments */
    private static final String NOTE_KEY = "playback_notes";

    /* Controls playback of audio */
    private boolean play = false;
    private boolean endSound = false;

    /* Default tempo is 60 BPM */
    private int tempoInBPM = 60;

    /* For controlling playback and display */
    private MediaPlayer mp;
    private View inflatedView;

    /**
     * Required empty public constructor.
     */
    public PlaybackMusicProject() {
    }

    /**
     * This function is used to create a new instance of this fragment, which must be initially set
     *      up with the music notes sent as an ArrayList of Strings.
     *
     * This activity will read the first character of each element of the ArrayList as the musical
     *      note to be played. As such, accidental notes will not be understood (for now).
     *
     * @param playbackNotes ArrayList<String>, music notes
     *
     * @return A new instance of fragment PlaybackMusicProject.
     */
    public static PlaybackMusicProject newInstance(ArrayList<String> playbackNotes) {
        PlaybackMusicProject fragment = new PlaybackMusicProject();
        Bundle args = new Bundle();
        args.putSerializable(NOTE_KEY, playbackNotes);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Receives the music notes for playback.
     *
     * @param savedInstanceState    Handled by Android
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.playbackNotes = (ArrayList<String>) getArguments().getSerializable(NOTE_KEY);
        }
    }

    /**
     * Creates the layout for the music playback screen, including the play/pause button.
     *
     * @param inflater              Handled by Android
     * @param container             Handled by Android
     * @param savedInstanceState    Handled by Android
     * @return                      Handled by Android
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        inflatedView = inflater.inflate(R.layout.fragment_playback_music_project, container,
                false);
        Log.d("ARUNS_DEBUG", "size of input note array: " + playbackNotes.size());
        final ImageButton playbackBtn = (ImageButton) inflatedView.findViewById(R.id.playbackButton);
        if (endSound) {
            endSound = false;
            playbackBtn.setImageResource(R.drawable.playback_button);
        }
        playbackBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                play = !play;
                if (play)
                {
                    playbackBtn.setImageResource(R.drawable.stop_btn);
                    endSound = false;
                    playNotes();
                }
                else
                {
                    stopPlayback();
                    playbackBtn.setImageResource(R.drawable.playback_button);
                }
            }
        });
        return inflatedView;
    }

    /**
     * Plays music notes using Android's MediaPlayer. It plays the notes at 60 BPM (default
     *      playback tempo).
     */
    private void playNotes()
    {
        long millis_per_beat = 60000/tempoInBPM;
        new CountDownTimer(playbackNotes.size()*millis_per_beat, millis_per_beat)
        {
            int counter=0;

            /**
             * Plays a note at every beat, which is scheduled by the CountDownTimer.
             *
             * @param millisUntilFinished   long, milliseconds until the countdown is complete
             */
            public void onTick(long millisUntilFinished)
            {
                char fileName = playbackNotes.get(counter).toLowerCase().charAt(0);
                Log.d("ARUNS_DEBUG", "Playback audio file name: " + fileName);
                int resId = getActivity().getResources().getIdentifier(String.valueOf(fileName), "raw",
                        getActivity().getPackageName());
                if (endSound)
                {
                    this.cancel();
                }
                mp = MediaPlayer.create(getActivity(), resId);
                mp.start();
                mp.setOnSeekCompleteListener(new MediaPlayer.OnSeekCompleteListener() {
                    @Override
                    public void onSeekComplete(MediaPlayer mp) { mp.release(); }
                });
                ++counter;
            }

            /**
             * Stops playback and cancels the CountDownTimer.
             */
            public void onFinish() {
                stopPlayback();
                this.cancel();
            }
        }.start();
    }

    /**
     * This function stops the media playback, if there is any. It then resets the button to display
     *      the "Play" button.
     */
    private void stopPlayback()
    {
        if (mp!=null && mp.isPlaying())
            mp.stop();
        endSound = true;
        final ImageButton playbackBtn = (ImageButton) inflatedView.findViewById(R.id.playbackButton);
        playbackBtn.setImageResource(R.drawable.playback_button);
        play = false;
    }
}