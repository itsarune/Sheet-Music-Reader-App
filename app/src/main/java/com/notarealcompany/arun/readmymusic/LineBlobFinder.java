package com.notarealcompany.arun.readmymusic;

import android.graphics.Bitmap;
import android.util.Log;

import java.util.ArrayList;

/**
 * LineBlobFinder is a class that finds blobs of pixels that is optimized in find lines (like the
 *      lines on a staff).
 *
 * @author Arun B.
 * @version 1.0, 21/5/20
 */
public class LineBlobFinder extends BlobFinderTemplate {
    LineBlobFinder(Bitmap b)
    {
        super(b);
    }

    /** {@inheritDoc} */
    @Override
    protected void dfs(int x, int y, Cluster cluster) {
        if (y>height || x > width || x > 10 || x < 0 || y < 0 || !toVisit[x][y])
            return;
        cluster.add(x, y);
        toVisit[x][y] = false;
        dfs(x+1, y, cluster);
        dfs(x-1, y, cluster);
        dfs(x, y+1, cluster);
        dfs(x, y-1, cluster);
    }

    /**
     * This method obtains the locations of staff lines on a bitmap and returns it as an array of
     * pixel clusters.
     *
     * @param sigSize   int, size of pixel to be considered "significant" enough to be a line
     * @param minY      int, any integer (unused in function)
     * @param maxY      int, any integer (unused in function)
     * @return          Cluster object ArrayList, an array of pixel-clusters of the locations of
     *                      horizontal staff lines
     */
    @Override
    public ArrayList<Cluster> getClusters(int sigSize, int minY, int maxY) {
        populate();
        int size = 0;
        int pixels = 0;
        ArrayList<Cluster> bStack = new ArrayList<Cluster>();

        for (int y = 0; y < height; ++y) {
            for (int x = 0; x < 10; ++x) {
                if (toVisit[x][y]) {
                    ++pixels;
                    Cluster temp = new Cluster();
                    dfs(x, y, temp);
                    Log.d("ARUNS_DEBUG", "Size of pixel array: " + temp.getMass());
                    if (temp.getMass() >= sigSize) {
                        bStack.add(temp);
                    }
                    ++size;
                }
            }
        }
        return bStack;
    }

}
