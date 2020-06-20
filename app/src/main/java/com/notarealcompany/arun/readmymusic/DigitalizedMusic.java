package com.notarealcompany.arun.readmymusic;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Log;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

import static com.notarealcompany.arun.readmymusic.Luminance.compare;

/**
 * Stores the user's digitalized music. It contains information about the digital image, the name
 * and musical information like key signature and time signature. It contains methods that allow
 * the user to manipulate and use their digitalized music.
 *
 * <p>
 *      The image processing converts the image to black and white pixels and uses the Cluster class
 * to identify pixel clusters of lines and note-heads. It uses this information to identify the
 * relative location of notes and determine the name of the note.
 * </p>
 *
 * <p>
 *     This version of the class is intended to be serializable to that it can be stored into a
 * database. It contains functions to "flatten" the object.
 * </p>
 *
 * <p>
 *      The methods that are related to image processing are modified from Katrina Lee's "Sheet
 * Music Reader" project (https://github.com/klee97/Sheet-Music-Reader).
 * </p>
 *
 * @author Arun B.
 * @version 2.2, 19/6/20
 */
public class DigitalizedMusic implements Serializable {
    private String nameOfMusic;     //project name
    private String[] notesOnStaff;  //the notes of a standard staff
    private Bitmap imageOfMusic;    //image processed by program
    private int picHeight;          //height of the music sheet image
    private int picWidth;           //width of the music sheet image
    private boolean[][] toVisit;    //records visited pixels in the music sheet image

    //notes of the music from the music sheet image
    private ArrayList<String> musicNotes = new ArrayList<String>();
    private String keySignature;    //key signature of the music
    private String timeSignature;   //time signature of the music
    private byte[] imgByteArray;    //used internally to convert image to a byte array

    /**
     * Sole constructor. Invoked by user to create a default, uninitialized music sheet with an
     * untitled name (which should be changed) in a standard C key in 4/4 time.
     */
    public DigitalizedMusic() {
        nameOfMusic = "Untitled";
        keySignature = "C";
        timeSignature = "4/4";
        //noteList = new String[13];
        musicNotes = new ArrayList<String>();
        notesOnStaff = new String[]{"Au", "Gu", "Fu", "Eu", "Du", "Cu", "B", "A", "G", "F", "E", "D", "C"};
        //database = new DigitalMusicHelper(getApplicationActivity()).getWritableDatabase();//
    }

    /**
     * Connects the image of the music sheet to the digitalized music object. It then calls
     * <code>processMusic()</code> to attempt to convert the image to playable notes.
     *
     * @param image a bitmap (JPEG/PNG/GIF) of the sheet music to be analyzed
     */
    public void setMusicImage(Bitmap image) {
        imageOfMusic = image.copy(Bitmap.Config.ARGB_8888, true);
        picHeight = image.getHeight();
        picWidth = image.getWidth();
        toVisit = new boolean[picHeight][picWidth];
        processMusic();
    }

    public String getNameOfMusic() {    return nameOfMusic;    }

    /**
     * Internally used to remove vertical lines of note stems. It aids in processing the music and
     * ensures note-heads can be found accurately. It works by identifying small horizontal strips
     * of white pixels at a given height which is inferred to be note stems and converts them to
     * black pixels.
     *
     * <p>
     *  This function is based on code from Katrina Lee's
     * "Sheet Music Reader" project (https://github.com/klee97/Sheet-Music-Reader).
     * </p>
     *
     * @param image a bitmap that contains the sheet music
     * @return      a bitmap that contains the sheet music without the note stems
     */
    private static Bitmap removeStems(Bitmap image)
    {
        int pixelCount;
        for (int y = 0; y < image.getHeight(); ++y)
        {
            pixelCount = 0;
            for (int x = 0; x < image.getWidth(); ++x) {
                if (image.getPixel(x, y) == Color.WHITE) { ++pixelCount; }
                else
                {
                    if (pixelCount>0 && pixelCount < 12)
                        while (pixelCount > 0)
                        {
                            image.setPixel(x-pixelCount, y, Color.BLACK);
                            --pixelCount;
                        }
                    else
                    {
                        pixelCount = 0;
                    }
                }
            }
        }
        return image;
    }

    /**
     * Changes the name of the digital music file (does not internally change the name of the
     * object). It is recommended that the file name is changed from the default "Untitled".
     *
     * @param newFileName   a string to replace the name of the digitalized music sheet
     */
    public void changeFileName(String newFileName) {
        /*MusicDatabaseManager dbManager = new MusicDatabaseManager(context);

        ArrayList<String> allProjects = new ArrayList<String>(
                Arrays.asList(dbManager.getExistingProjectNames()));
        dbManager.closeDatabase();*/

        /*if (SortingAndSearching.binarySearchName(newFileName, allProjects) == -1)
        {*/
            nameOfMusic = newFileName;
        /*    return 1;
        } else
            return 0;*/
    }

    /**
     * Used internally by other methods. This method analyzes the digital music bitmap and updates
     * the music notes array with the names of the notes of music.
     *
     * <p>
     * This function modifies code from Katrina Lee' "Sheet Music Reader" project
     * (https://github.com/klee97/Sheet-Music-Reader).
     * </p>
     *
     * @return  -1 if the music processing completed unsuccessfully. This result occurs when the
     *              image lacks clarity and the system cannot interpret it.
     *           1 if the processing completed successfully
     */
    private int processMusic() {
        try
        {
            int sigPixelSize = 7;
            //convert picture to black & white
            for (int x = 0; x < picWidth; ++x)
                for (int y = 0; y < picHeight; ++y) {
                    int pixel = imageOfMusic.getPixel(x, y);
                    if (compare(pixel, Color.BLACK)) {
                        imageOfMusic.setPixel(x, y, Color.BLACK);
                    } else {
                        imageOfMusic.setPixel(x, y, Color.WHITE);
                    }
                }
            LineBlobFinder lineFind = new LineBlobFinder(imageOfMusic);
            ArrayList<Cluster> lines = lineFind.getClusters(20, 0, 0);
            int sets = lines.size() / 5;
            imageOfMusic = removeStems(imageOfMusic);
            Log.d("T", "# of staves: " + lines.size());
            double centres[] = new double[13 * sets];
            int j = 0;
            double avgHalf;
            //Log.d("T", "What is average half?" + avgHalf);
            for (int l = 0; l < lines.size(); l += 5) {
                avgHalf = ((double) (lines.get(l + 1).getMinY() - lines.get(l).getMaxY())) / 2.0;
                Log.d("T", "value of l: " + l);
                centres[j] = lines.get(l).getMinY() - avgHalf * 2;
                ++j;
                centres[j] = lines.get(l).getMinY() - avgHalf;
                ++j;

                for (int i = 0; i < 5; ++i) {
                    centres[j] = lines.get(l + i).avgY();
                    ++j;
                    centres[j] = centres[j - 1] + avgHalf;
                    ++j;
                    //imageOfMusic.setPixel(lines.get(l + i).maxX, lines.get(l + i).maxY, Color.RED);
                    //imageOfMusic.setPixel(lines.get(l + i).minX, lines.get(l + i).minY, Color.GREEN);
                }
                centres[j] = lines.get(l + 4).getMaxY() + 2 * avgHalf;
                ++j;
                removeLines(lines);
                NoteBlobFinder noteFind = new NoteBlobFinder(imageOfMusic);

                int min = lines.get(l).getMinY() - (int) avgHalf * 2;
                if (min < 0) {
                    min = 0;
                }
                int max = lines.get(l + 4).getMaxY() + (int) avgHalf * 2;
                if (max > picHeight) {
                    max = picHeight;
                }
                ArrayList<Cluster> notes = noteFind.getClusters(sigPixelSize,
                        min, max);
                Cluster.bubbleSortX(notes);
                Log.d("T", "# of notes: " + notes.size());
                for (int i = 0; i < notes.size(); i++)
                    musicNotes.add(notesOnStaff[findLine(notes.get(i), centres, 13 * l / 5)]);
                Log.d("T", "check " + (l - 5));
            }
            Log.d("ARUNS_DEBUG", "notes?" + musicNotes);
            return 1;
        } catch (Exception e) {
            return -1;
        }
    }

    /**
     * This internal function removes the horizontal lines from <code>imageOfMusic</code> using the
     * pixel clusters of lines. For every x-coordinate of the line, this function checks whether
     * there is white-pixel activity above-or-below the line (so as to not interfere with musical
     * notes) and then converts to black if no activity is found.
     *
     * <p>
     * This function modifies code from Katrina Lee's "Sheet Music Reader" project
     * (https://github.com/klee97/Sheet-Music-Reader).
     * </p>
     *
     * @param lines         clusters of pixels of horizontal lines across an image
     */
    private void removeLines(ArrayList<Cluster> lines)
    {
        int max, min;
        for (int i = 0; i < lines.size(); ++i)
        {
            max = lines.get(i).getMaxY();
            min = lines.get(i).getMinY();
            for (int x = 0; x < picWidth; ++x)
            {
                if (imageOfMusic.getPixel(x, max+3)!=Color.WHITE && imageOfMusic.getPixel(x,
                        min-3) != Color.WHITE)
                    for (int y = min-1; y <= max+1; ++y)
                        imageOfMusic.setPixel(x, y, Color.BLACK);
            }
        }
    }

    /**
     * Gets the bitmap of the modified image of the sheet music used by the object.
     *
     * @return  bitmap of modified, processed sheet music
     */
    public Bitmap getImageOfMusic() { return imageOfMusic; }

    /**
     * This method changes the default time-signature to a user-defined time signature. The time
     * signature must be entered manually as it will not be recognized during processing.
     *
     * <p>
     * After adjusting the time signature, this method will call <code>processMusic()</code> to
     * re-process the music for the new time signature.
     * </p>
     *
     * @param newTimeSignature  a string that corresponds to either "4/4" for 4/4 time, or "5/8"
     *      *                              for 5/8 tim, or "5/4" for 5/4 time
     * @return  1, if the music processing completed successfully
     *          -1, if the music process completed unsuccessfully. Likely occurs if there is not
     *              digital image set by <code>setMusicImage()</code> or if the music processing by
     *              <code>processMusic()</code> completed unsuccessfully.
     */
    public int changeTimeSignature(String newTimeSignature)
    {
        timeSignature = newTimeSignature;
        if (processMusic() == -1)
            return -1;
        else
            return 1;
    }

    /**
     * This method changes the default key signature from "C" to a user-defined signature.
     *
     * @param newKeySignature   a string that corresponds to the new key. The format should be as
     *                          follows: [NOTE NAME (in capitals)][Accidentals (if any):"#"/"f"
     *                          corresponding to sharp or flat]. For example, a B-flat major scale
     *                          would be expressed as "Bf"
     */
    public void changeKeySignature(String newKeySignature)
    {
        keySignature = newKeySignature;
    }

    /**
     * This function is used internally to analyze the sheet music image. It returns the name of the
     * note in the C treble-scale according to its relative position to the staff. It returns an
     * integer that corresponds to its location on the staff.
     *
     * <p>
     *     Currently, this function will only work accurately for notes that are within one ledger
     *     up or down from the standard five-ledger staff.
     * </p>
     *
     * <p>
     *     This function modifies code from Katrina Lee's "Sheet Music Reader" project
     *     (https://github.com/klee97/Sheet-Music-Reader).
     * </p>
     *
     * @param note      the pixel cluster of a note-head
     * @param centres   array double, array that contains the pixel-centres of the lines and spaces
     *                  of the music bitmap
     * @param centrePos integer, representing the starting position of the staff within all the
     *                  lines of music on the music bitmap
     * @return          integer, between 0-13, where 0 represents one ledger above the staff, while
     *                  1 represents the space above the staff and 13 representing one ledger under
     *                  the staff
     */
    private static int findLine(Cluster note, double[] centres, int centrePos)
    {
        double min = Math.abs(note.avgY() - centres[centrePos]);
        double temp = 0;
        int index = 0;
        for (int i = 0; i < 13; i++)
        {
            temp = Math.abs(note.avgY() - centres[i+centrePos]);
            if (temp < min)
            {
                min = temp;
                index = i;
            }
            ++i;
        }
        return index;
    }

    /**
     * Used internally by MusicDatabaseManager. It compresses the object and writes Object details
     * to the output stream. This allows the data to be stored within the database and later
     * re-created when required.
     *
     * <p>
     *     It converts the Bitmap into a byte-array to be stored.
     * </p>
     *
     * @param out           Output stream, to write object details to
     *
     * @throws IOException  Must be handled by user; occurs during error during object writing
     */
    private void writeObject(java.io.ObjectOutputStream out) throws IOException
    {
        out.writeObject(nameOfMusic);
        out.writeObject(notesOnStaff);
        out.writeInt(picHeight);
        out.writeInt(picWidth);
        out.writeObject(toVisit);
        out.writeObject(musicNotes);
        out.writeObject(keySignature);
        out.writeObject(timeSignature);

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        imageOfMusic.compress(Bitmap.CompressFormat.PNG, 100, stream);
        imgByteArray = stream.toByteArray();

        out.writeInt(imgByteArray.length);
        out.writeObject(imgByteArray);
    }

    /**
     * Used internally by MusicDatabaseManager
     *
     * <p>
     *     Reconstructs object from the binary data stored in the database. It reconstructs
     * Bitmap from byte-array data.
     * </p>
     *
     * @param in                        Input stream of data from database
     * @throws IOException              Occurs during input error. It could occur if the stored data
     *                                      is from an object version that is older than the current
     *                                      version
     * @throws ClassNotFoundException   User should handle exception
     */
    private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException
    {
        nameOfMusic = (String)in.readObject();
        notesOnStaff = (String[]) in.readObject();
        picHeight = in.readInt();
        picWidth = in.readInt();
        toVisit = (boolean[][]) in.readObject();
        musicNotes = (ArrayList<String>) in.readObject();
        keySignature = (String) in.readObject();
        timeSignature = (String) in.readObject();
        int imgByteLength = in.readInt();
        imgByteArray = (byte[]) in.readObject();
        imageOfMusic = BitmapFactory.decodeByteArray(imgByteArray,0, imgByteArray.length);
    }

    /**
     * Obtains the music notes associated with the digitalized music project.
     *
     *      The returned ArrayList will be of the form strings consisting of one or
     * two characters. Letters like "C", "D", "F" represents the lower-half of the treble cleff
     * staff while "Cu", "Du", "Gu" represent the upper-half.
     *
     *      Ensure that an image has been attached to this music project or else this function will
     * return an empty list.
     *
     * @return  ArrayList<String>, music notes in the form of "C" or "Du" to denote their location
     *              in the bottom- or top-half of the treble cleff staff.
     */
    public ArrayList<String> getNotes() { return musicNotes; }
}