package com.notarealcompany.arun.readmymusic;

import android.graphics.Bitmap;
import android.graphics.Color;

import java.util.ArrayList;

/**
 * BlobFinderTemplate creates the template for creating an image-processing blob finder. It finds
 *      blobs of pixels. The abstract functions <code>dfs()</code> and <code>getClusters</code>
 *      must be implemented to both search for nearby pixels and set up the type of object being
 *      found.
 *
 * @author Arun B.
 * @version 1.0, 21/5/20
 */
public abstract class BlobFinderTemplate {
    Bitmap image;     //image of music sheet
    int height;       //height of image
    int width;        //pixel width of image
    boolean[][] toVisit; //visited pixel of image during analysis

    /**
     * Sole constructor. Sets the bitmap image to be analyzed and sets the picture height and width
     *      variables.
     *
     * @param b     Bitmap, music sheet that will be analyzed
     */
    BlobFinderTemplate(Bitmap b)
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
    void populate()
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
     * @param x         int, the x-location of the pixel to be analyzed
     * @param y         int, the y-location of the pixel to be analyzed
     * @param c         Cluster object, adds nearby pixels to this Cluster object
     */
    protected abstract void dfs(int x, int y, Cluster c);

    public abstract ArrayList<Cluster> getClusters(int sigSize, int minY, int maxY);
}
