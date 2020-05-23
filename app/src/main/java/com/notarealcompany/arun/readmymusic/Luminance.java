package com.notarealcompany.arun.readmymusic;

import android.graphics.Color;

/**
 * Luminance contains methods that analyze a colour and determine its relative luminance. It is
 * used for image processing primarily, to convert individual pixels into black and white depending
 * on its colour.
 *
 * <p>
 *     This class is based on Katherine Lee's "Luminance" class in her "Sheet Music Reader" project
 * (https://github.com/klee97/Sheet-Music-Reader).
 * </p>
 *
 * @author Arun B
 * @version 1.1, 2/4/20
 */
public class Luminance {
    //threshold to be considered significant enough to be different
    private static final double lumThreshold = 200.0;

    /**
     * This internal method calculates the monochrome luminance of a colour. It computes the
     * monochrome luminance of a colour using the NTSC formula to convert the colour to grayscale.
     *
     * @param colour    integer (Android colour), the Android colour integer is encoded with rgb
     *                  values
     * @return          double, monochrome luminance of the colour between 0.0 and 255.0
     */
    private static double intensity(int colour)
    {
        int r = Color.red(colour);
        int g = Color.green(colour);
        int b = Color.blue(colour);

        return .299*r + .587*g + .114*b;    //from Princeton
    }

    /**
     * Compares two monochrome luminances of two colours and identifies whether it is significant.
     *
     * @param a integer (Android colour) the first Android colour to be compared. RGB values are
     *          encoded within Android colours.
     * @param b integer (Android colour) the other Android colour. RGB values are encoded within
     *          these Android colours
     * @return  <code>true</code> if the monochrome luminance is significant
     *          <code>false</code> if the monochrome luminance is insignificant
     */
    public static boolean compare(int a, int b) {
        return Math.abs(intensity(a) - intensity(b)) >= lumThreshold;
    }
}
