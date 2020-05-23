package com.notarealcompany.arun.readmymusic;

import java.util.ArrayList;

/**
 * MusicTuningProfile contains the tuning properties of an instrument. Users can add tuning notes
 * and their corresponding tuning frequencies. There is a method to access these tuning frequencies
 * on request.
 *
 * @author Arun B.
 * @version 1.1, 24/3/20
 */
public class MusicTuningProfile {
    private String instrumentName;
    private ArrayList<Note> tuningNotes = new ArrayList<Note>();

    /**
     * Sole constructor. It creates a music tuning profile for an instrument. The tuning notes and
     * corresponding tuning notes must be added using <code>addTuningNote()</code>.
     *
     * @param instrumentName    a string that initializes the instrument name of the tuning profile
     */
    public MusicTuningProfile(String instrumentName) {
        this.instrumentName = instrumentName;
    }

    /**
     * Replaces the name of the instrument for the tuning profile. It replaces the name set up by
     * the constructor.
     *
     * @param newInstrumentName a string that represents and instrument name that replaces the
     *                          existing name on the profile
     */
    public void changeInstrumentName(String newInstrumentName) {
        instrumentName = newInstrumentName;
    }

    /**
     * Adds a tuning note and corresponding frequency to the instrument profile.
     *
     * @param newNote           a note object that contains the name and frequency of the note
     */
    public void addTuningNote(Note newNote)
    {
        tuningNotes.add(newNote);
    }

    /**
     * This method obtains the tuning frequency of a given tuning note.
     *
     * @param requestedNote     string that represents the requested note. Type "C#"
     *                          if the frequency for C# is required or "Bf" if a B-flat tuning
     *                          frequency is required
     * @return                  -1, if the operation was unsuccessful, which occurs if the tuning
     *                          note does not exist
     *                          integer, frequency of the requested note
     */
    public int getTuningFrequency(String requestedNote)
    {
        Note n;
        int i = 0;
        while(!((n=tuningNotes.get(i)).getNoteName().equals(requestedNote)))
        {
            if (i>tuningNotes.size())
                return -1;
            ++i;
        }
        return tuningNotes.get(i).getNoteFrequency();
    }
}