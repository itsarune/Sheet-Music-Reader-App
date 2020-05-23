package com.notarealcompany.arun.readmymusic.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.notarealcompany.arun.readmymusic.CreateMusicFragment;
import com.notarealcompany.arun.readmymusic.MusicDatabaseManager;
import com.notarealcompany.arun.readmymusic.R;
import com.notarealcompany.arun.readmymusic.RecyclerListAdapter;
import com.notarealcompany.arun.readmymusic.SortingAndSearching;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * This BlankFragment object is a test object that creates a layout that allows uses to select
 * whether to create or import a new project. Then, if a user wants to import an existing object, it
 * displays a list of existing projects.
 *
 * @author Arun B.
 * @version 1.2, 22/5/20
 */
public class BlankFragment extends Fragment {
    /* Music Database Object */
    private MusicDatabaseManager db;

    /* Handles layout management */
    private LayoutInflater mInflater;
    private ViewGroup mView;

    /* Required empty public constructor */
    public BlankFragment() {
    }

    /**
     * Creates a new instance of this fragment using the provided parameters.
     *
     * @return A new instance of fragment BlankFragment.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Required override. Creates the layout on creating this Fragment object. It creates two
     * buttons that allows users to select whether they want to create or import a new project.
     *
     * @param inflater              LayoutInflater, handled by Android
     * @param container             ViewGroup, handled by Android
     * @param savedInstanceState    Bundle, handled by Android
     * @return                      View, layout of the fragment
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mInflater = inflater;
        mView = container;

        // Inflate the layout for this fragment
        View view = mInflater.inflate(R.layout.fragment_blank, container, false);

        /* Find the "Create New Music" button */
        Button createMusicBtn = (Button) view.findViewById(R.id.createProject);
        createMusicBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                /*ragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, screenTwo);
                fragmentTransaction.commit();*/
                changeToScreenTwo();
            }
        });

        /* Find import existing project button */
        Button importProject = (Button) view.findViewById(R.id.importProject);
        importProject.setOnClickListener(new View.OnClickListener() {
            /**
             * Required override. Display project list on Android.
             *
             * @param v View v, handled by Android
             */
            @Override
            public void onClick(View v) {
                showDatabaseList();
            }
        });
        return view;
    }

    /**
     * Changes fragment to the CreateMusicFragment if the user chooses to create their own music
     * digitizer project
     */
    private void changeToScreenTwo()
    {
        CreateMusicFragment screenTwo = new CreateMusicFragment();
        FragmentTransaction fragmentTransaction;
        fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, screenTwo);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    /**
     * This method displays the list of projects that exists in the database on the Android screen.
     * The displayed project list is sorted alphabetically. The function immediately pops-up a
     * message and returns if the number of objects within the database is 0.
     */
    private void showDatabaseList()
    {
        db = new MusicDatabaseManager(getActivity());
        if (db.getDatabaseRowCount()==0) {
            Toast.makeText(getActivity(),
                    "No projects stored in database", Toast.LENGTH_LONG).show();
            return;
        }
        Log.d("ARUNS_DEBUG","Number of Projects: " + db.getDatabaseRowCount());
        mView.removeAllViews();
        View view = mInflater.inflate(R.layout.import_projects_layout, mView, false);
        mView.addView(view);
        RecyclerView rvList = (RecyclerView) mView.findViewById(R.id.projectList);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rvList.setLayoutManager(layoutManager);

        ArrayList<String> projects = new ArrayList<>(Arrays.asList(db.getExistingProjectNames()));
        //SortingAndSearching.selectionSortByAlpha(projects);
        Collections.sort(projects, String.CASE_INSENSITIVE_ORDER);
        String[] projectsAsArray = new String[projects.size()];
        projects.toArray(projectsAsArray);
        //Arrays.sort(projectsAsArray);
        //Log.d("ARUNS_DEBUG", "Length of Array: " + projects.length);
        RecyclerListAdapter mAdapter = new RecyclerListAdapter(projectsAsArray);
        rvList.setAdapter(mAdapter);
        db.closeDatabase();
    }
}