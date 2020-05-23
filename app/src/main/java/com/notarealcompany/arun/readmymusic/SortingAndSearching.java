package com.notarealcompany.arun.readmymusic;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;

/**
 * SortingAndSearching contains functions that can sort through and search through ArrayLists of
 * Strings. It include selection sort, bubble sort, linear search and binary search.
 *
 * @author Arun B
 * @version 1.0, 30/4/20
 */
public class SortingAndSearching {
    /**
     * This method uses selection sort to sort an ArrayList object of Strings
     *
     * @param stringArray   String ArrayList, object to be sorted using selection sort
     */
    public static void selectionSortByAlpha(ArrayList<String> stringArray)
    {
        /* used to find current minimum when looping */
        int indexOfAlphaMin;

        /* temporarily used to store values for sorting */
        String tempString;

        int length = stringArray.size();

        for (int i = 0; i < length-1; ++i)
        {
            indexOfAlphaMin = i;
            for (int j = i+1; j < length; ++j)
            {
                if (stringArray.get(j).compareTo(stringArray.get(indexOfAlphaMin)) < 0)
                    indexOfAlphaMin = j;
            }

            tempString = stringArray.get(i);
            stringArray.set(i, stringArray.get(indexOfAlphaMin));
            stringArray.set(indexOfAlphaMin, tempString);
        }

        return;
    }

    /**
     * This method uses bubble sort to sort an ArrayList object of Strings.
     *
     * @param list  ArrayList String, object to be sorted with bubble sort
     */
    public static void bubbleSortByAlpha(ArrayList<String> list)
    {
        String temp;
        int l = list.size();
        for (int k = 0; k < l-1; ++k)
            for (int i = 0; i < l-k-1; ++i)
                if(list.get(i).compareTo(list.get(i+1)) > 0)
                {
                    temp = list.get(i);
                    list.set(i, list.get(i+1));
                    list.set(i+1, temp);
                }
        return;
    }

    /**
     * This method uses binary search to find the index of the search term in a String ArrayList.
     * This method will sort the object prior to searching for the term.
     *
     * @param key           String, search term
     * @param searchList    String ArrayList, list object to be searched
     * @return              int, index of search term in ArrayList
     *                      -1, if the term doesn't exist within the list
     */
    public static int binarySearchName(String key, ArrayList<String> searchList)
    {
        Collections.sort(searchList);
        int low = 0;                    //lower-bound index of currently used array
        int high = searchList.size()-1; //current upper-bound index of array

        while(low <= high)
        {
            Log.d("ARUNS_DEBUG", "Value of low: " + low + "Value of high: " + high);
            int mid = (int) (low+high)/2 ;
            if(searchList.get(mid).compareTo(key) < 0)
                low = mid+1;
            else if (searchList.get(mid).compareTo(key) > 0)
                high = mid-1;
            else
                return mid;
        }

        return -1;
    }

    /**
     * This method uses linear search to find the index of the search term in a list object.
     *
     * @param key           String, search term
     * @param searchList    ArrayList of Strings, list to be searched in
     * @return              int, the index of the search term
     *                      -1, if the search term cannot be found within the list
     */
    public static int linearSearchName(String key, ArrayList<String> searchList)
    {
        for (int i = 0; i < searchList.size(); ++i)
            if (searchList.get(i).compareTo(key) == 0)
                return i;
        return -1;
    }

    /**
     * This method uses the binary search algorithm to find the index of a search term. It assumes
     * that the input is already sorted
     *
     * @param key           String, search term
     * @param searchList    ArrayList of Strings, sorted list of strings
     * @return              int, index of search term
     *                      -1, if the search cannot find the search-term in the list
     */
    public static int binarySearchNameNoSort(String key, ArrayList<String> searchList)
    {
        int low = 0;
        int high = searchList.size()-1;

        while(low <= high)
        {
            Log.d("ARUNS_DEBUG", "Value of low: " + low + "Value of high: " + high);
            int mid = (int) (low+high)/2 ;
            if(searchList.get(mid).compareTo(key) < 0)
                low = mid+1;
            else if (searchList.get(mid).compareTo(key) > 0)
                high = mid-1;
            else
                return mid;
        }

        return -1;
    }
}
