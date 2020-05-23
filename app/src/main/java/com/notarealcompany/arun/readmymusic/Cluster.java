package com.notarealcompany.arun.readmymusic;

import java.util.ArrayList;

/**
 * This class identifies pixel clusters, recording information about its minimum and maximum x and y
 * coordinates as well as the size of the cluster. It can be used for image processing, to identify
 * clusters of pixels.
 *
 * <p>
 *     This class is based on the "Note" class of Katrina Lee's "Sheet Music Reader" project
 * (https://github.com/klee97/Sheet-Music-Reader).
 * </p>
 *
 * @author Arun B
 * @version 1.3, 28/4/20
 */
public class Cluster {
    /* Describes properties of the pixel-clusters */
    private int mass;
    private int maxX;
    private int minX;
    private int maxY;
    private int minY;

    /**
     * Sole constructor. Creates a pixel cluster, resetting all instance variables to equal 0.
     */
    Cluster() {
        mass = 0;
        maxX = 0;
        minX = 0;
        maxY = 0;
        minY = 0;
    }

    /**
     * Adds a pixel to the cluster. It modifies the size of the cluster and updates the max/min x &
     * y coordinate bounds.
     *
     * @param inX   integer, x-coordinate of the pixel
     * @param inY   integer, y-coordinate of the pixel
     */
    public void add(int inX, int inY)
    {
        if (minY == 0 || inY < minY)
            minY = inY;
        if (minX == 0 || inX < minX)
            minX = inX;
        if (inX > maxX)
            maxX = inX;
        if (inY > maxY)
            maxY = inY;
        ++mass;
    }

    /**
     * Gets the average y-coordinate of the pixel cluster.
     *
     * @return  double, the average y-value coordinate of the cluster
     */
    public double avgY() { return minY + ((double) (maxY-minY))/2; }

    public int getMass() { return mass; }

    /**
     * Gets the average x-coordinate of the pixel cluster.
     *
     * @return  double, the average x-value coordinate of the cluster
     */
    public double avgX() { return minX + ((double) (maxX-minX))/2; }

    /**
     * Gets the minimum y-value coordinate of the pixel cluster.
     *
     * @return  double, the minimum y-value coordinate of the cluster
     */
    public int getMinY() { return minY; }

    /**
     * Gets the maximum y-value coordinate of the pixel cluster.
     *
     * @return  double, the maximum y-value coordinate of the cluster
     */
    public int getMaxY() { return maxY; }

    /**
     * Sort the x-coordinates of a pixel clusters in an array to order them from average
     * x-coordinate position. It ensures that the music is read from left-to-right.
     *
     * @param notes an array of note clusters in an unorganized order
     * @return      an array of note clusters in the order of average x position
     */
    public static void bubbleSortX(ArrayList<Cluster> notes)
    {
        for (int i = (notes.size() - 1); i >= 0; --i)
            for(int j = 1; j <= i; ++j)
                if(notes.get(j-1).avgX() > notes.get(j).avgX())
                {
                    Cluster temp = notes.get(j-1);
                    notes.set(j-1, notes.get(j));
                    notes.set(j, temp);
                }
    }
}
