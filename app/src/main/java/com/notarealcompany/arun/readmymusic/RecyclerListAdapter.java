package com.notarealcompany.arun.readmymusic;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

/**
 * This class handles the display of list objects with RecyclerView. It uses TextView to display the
 * names of the elements on the list.
 *
 * @author Arun B
 * @version 1.0, 30/4/20
 */
public class RecyclerListAdapter extends RecyclerView.Adapter<RecyclerListAdapter.MyViewHolder> {
    /* Dataset to be displayed */
    private String[] mDataset;

    /**
     * This class handles the display of the elements with TextView on the list.
     *
     * @author Arun B.
     * @version 1.0, 30/4/20
     */
    public static class MyViewHolder extends RecyclerView.ViewHolder
    {
        /* TextView object that contains element of list */
        public TextView textView;
        /* Sole constructor. Finds TextView for each row. */
        public MyViewHolder(View v)
        {
            super(v);
            textView = (TextView) v.findViewById(R.id.rowName);
        }
    }

    /* Sole constructor. Identifies data to be displayed on the list */
    public RecyclerListAdapter(String[] dataset) { mDataset = dataset; }

    /**
     * Required override. Creates the layout for the list
     *
     * @param parent    Handled by Android
     * @param viewType  Handled by Android
     * @return          MyViewHolder object
     */
    @Override
    public RecyclerListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View v = (View) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view_row, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    /**
     * Required override. Sets text of element
     *
     * @param holder    Handled by Android
     * @param position  Handled by Android
     */
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position)
    {
        holder.textView.setText(mDataset[position]);
    }

    /**
     * Returns the length of the dataset to be displayed.
     *
     * @return  int, length of dataset
     */
    @Override
    public int getItemCount() { return mDataset.length; }
}