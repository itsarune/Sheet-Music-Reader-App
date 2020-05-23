package com.notarealcompany.arun.readmymusic;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;

import java.util.ArrayList;
import java.util.Stack;

/**
 * This class is used for image-processing, finding note-heads as well as the lines of the staff. It
 * finds "blobs" of pixels and uses the Cluster class to record their locations.
 *
 * <p>
 *     This class is based on the "BlobFinder" class of Katrina Lee's "Sheet Music Reader" project
 * (https://github.com/klee97/Sheet-Music-Reader).
 * </p>
 * @author Arun B.
 * @version 1.1, 2/4/20
 */
public class BlobFinder {
    private Bitmap image;
    private int height;
    private int width;
    private boolean[][] toVisit;

    /**
     * Sole constructor. It creates a BlobFinder instance with a bitmap to be used for processing.
     *
     * @param b bitmap, to be used by the object for image-processing
     */
    BlobFinder(Bitmap b)
    {
        image = b;
        height = b.getHeight();
        width = b.getWidth();
        toVisit = new boolean[width][height];
    }

    /**
     * This function is internally used to convert the image to pure black-and-white to make
     * image-processing simpler.
     *
     *<p>
     *     Note-heads and lines are displayed as white on a black background and the locations of
     * these pixels are noted so that they could be re-visited later.
     *</p>
     *
     */
    private void populate()
    {
        for(int x = 0; x < width; ++x)
            for(int y = 0; y < height; ++y)
            {
                if(image.getPixel(x,y) == Color.WHITE)
                    toVisit[x][y] = true;
            }
    }

    /**
     * Uses "Depth First Search" in order to find and record clusters of pixels. It recursively adds
     * nearby white pixels to the cluster until no more can be found.
     *
     * <p>
     *     This function is optimized for finding staff lines, so it only will look in the first
     * 0-10 columns of the bitmap.
     * </p>
     *
     * @param x         int, the x-location of the pixel to be analyzed
     * @param y         int, the y-location of the pixel to be analyzed
     * @param cluster   Cluster object, adds nearby pixels to this Cluster object
     */
    private void dfsLine(int x, int y, Cluster cluster)
    {
        if (y>height || x > width || x > 10 || x < 0 || y < 0 || !toVisit[x][y])
            return;
        cluster.add(x, y);
        toVisit[x][y] = false;
        dfsLine(x+1, y, cluster);
        dfsLine(x-1, y, cluster);
        dfsLine(x, y+1, cluster);
        dfsLine(x, y-1, cluster);
    }

    /**
     * This method used "Depth-First Search" to identify note-heads through clusters of pixels.
     * It recursively adds nearby white pixels (which are presumably notes if the image processing
     * worked). It adds these pixels to a cluster object.
     *
     * <p>
     *     This method will only work if the bitmap is stripped of noise like staff lines and note
     * stems.
     * </p>
     *
     * @param x     int, x-coordinate of the pixel
     * @param y     int, y-coordinate of the pixel
     * @param note  cluster object, updates this object with nearby pixels to identify clusters
     */
    private void dfsNoteHeads (int x, int y, Cluster note)
    {
        if (x <= 0 || y <= 0 || x >= width || y >= height || !toVisit[x][y])
            return;
        note.add(x,y);
        toVisit[x][y] = false;
        dfsNoteHeads(x+1, y, note);
        dfsNoteHeads(x, y+1, note);
        dfsNoteHeads(x-1, y, note);
        dfsNoteHeads(x, y-1, note);
    }

    /**
     * This method obtains the locations of staff lines on a bitmap and returns it as an array of
     * pixel clusters.
     *
     * @return  Cluster object ArrayList, an array of pixel-clusters of the locations of horizontal
     *              staff lines
     */
    public ArrayList<Cluster> getLines() {
        populate();
        int size = 0;
        int pixels = 0;
        ArrayList<Cluster> bStack = new ArrayList<Cluster>();

        for (int y = 0; y < height; ++y) {
            for (int x = 0; x < 10; ++x) {
                if (toVisit[x][y]) {
                    ++pixels;
                    Cluster temp = new Cluster();
                    dfsLine(x, y, temp);
                    int m = temp.getMinY();
                    if (temp.getMass() >= 50) {
                        bStack.add(temp);
                    }
                    ++size;
                }
            }
        }
        return bStack;
    }

    /**
     * This method identifies large pixel-cluster masses and presumes them to be note-heads. It
     * returns these pixel clusters as an array.
     *
     * @param sigSize   number of pixels to be considered significant enough to be considered as a
     *                      note
     * @param minY      the lower-bound y-limit to look for note-heads
     * @param maxY      the upper-bound y-limit to look for note-heads
     * @return          cluster array, an array of pixel clusters that are presumed to be note-heads
     */
    public Cluster[] getBeads (int sigSize, int minY, int maxY)
    {
        populate();
        int size=0;
        int pixels=0;
        Stack<Cluster> bStack = new Stack<Cluster>();
        for (int y = minY; y < maxY; ++y)
            for (int x = 0; x < width; ++x)
                if (toVisit[x][y])
                {
                    ++pixels;
                    Cluster temp = new Cluster();
                    dfsNoteHeads(x, y, temp);
                    if (temp.getMass() >= sigSize)
                    {
                        bStack.push(temp);
                        ++size;
                    }
                }
        Cluster[] array = new Cluster[size];
        bStack.toArray(array);
        return array;
    }
}