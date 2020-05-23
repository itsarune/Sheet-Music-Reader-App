package com.notarealcompany.arun.readmymusic;

import android.graphics.Bitmap;
import android.util.Log;

import java.util.ArrayList;
import java.util.Stack;

/**
 * NoteBlobFinder is a class that finds clusters of pixels that is optimized to find clusters of
 *      circles (like note-heads on a sheet music).
 *
 * @author Arun B.
 * @version 1.0, 21/5/20
 */
public class NoteBlobFinder extends BlobFinderTemplate {

    /** {@inheritDoc} */
    public NoteBlobFinder(Bitmap b) {
        super(b);
    }

    /** {@inheritDoc} */
    @Override
    protected void dfs(int x, int y, Cluster note) {
        if (x <= 0 || y <= 0 || x >= width || y >= height || !toVisit[x][y])
            return;
        note.add(x,y);
        toVisit[x][y] = false;
        dfs(x+1, y, note);
        dfs(x, y+1, note);
        dfs(x-1, y, note);
        dfs(x, y-1, note);
    }

    /**
     * This method identifies large pixel-cluster masses and presumes them to be note-heads. It
     *      returns these pixel clusters as an array.
     *
     * @param sigSize   int, size of pixel cluster to be considered as "significant" as noteheads
     * @param minY      int, the lower-bound y-limit to look for note-heads
     * @param maxY      int, the upper-bound y-limit to look for note-heads
     *
     * @return          ArrayList of Cluster objects, representing the clusters of note-head pixels
     */
    @Override
    public ArrayList<Cluster> getClusters(int sigSize, int minY, int maxY) {
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
                    dfs(x, y, temp);
                    Log.d("ARUNS_DEBUG", "Size of potential note: " + temp.getMass());
                    if (temp.getMass() >= sigSize)
                    {
                        bStack.push(temp);
                        ++size;
                    }
                }
        ArrayList<Cluster> array = new ArrayList<>(bStack);
        return array;
    }
}