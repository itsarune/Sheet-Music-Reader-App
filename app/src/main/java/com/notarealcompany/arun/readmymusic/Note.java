package com.notarealcompany.arun.readmymusic;

/**
 * Note defines a musical note with a tuning name and a corresponding frequency.
 *
 * @author Arun B
 * @version 1.1, 24/3/20
 */
public class Note {
    private String noteName;
    private int noteFrequency;

    /**
     * Sole constructor. User must define note name and corresponding frequency when calling the
     * object
     * @param noteName      name of note as a string. For accidentals, it is preferred that the note
     *                      name is followed by "#" or "f". For instance, the note name for B-flat
     *                      would be "Bf".
     * @param noteFrequency integer frequency of the note in Hz
     */
    public Note (String noteName, int noteFrequency)
    {
        this.noteName = noteName;
        this.noteFrequency = noteFrequency;
    }

    /**
     * Obtains the name of the note as a string.
     *
     * @return  name of the note as a string
     */
    public String getNoteName() { return noteName;    }

    /**
     * Obtains the corresponding frequency of the note as an integer.
     *
     * @return  integer frequency that corresponds to the note
     */
    public int getNoteFrequency() { return noteFrequency; }
}