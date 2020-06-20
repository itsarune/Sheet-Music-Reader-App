package com.notarealcompany.arun.readmymusic;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Arrays;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import static android.app.Activity.RESULT_OK;

/**
 * A Fragment class that opens when the user clicks on a button to create a new project. It creates
 * a new DigitalizedMusic object and saves it to the database.
 *
 * @author Arun B.
 * @version 1.1, 1/5/20
 */
public class CreateMusicFragment extends Fragment {
    //Success code associated with a loaded image from the gallery
    private static int RESULT_LOAD_IMAGE = 1;

    //New project created
    DigitalizedMusic musicProject;

    /* Controls layout of the screen */
    private LayoutInflater mInflater;
    private ViewGroup mView;

    /**
     * Sole constructor.
     */
    public CreateMusicFragment() {
        // Required empty public constructor
    }

    /**
     * Required Android override
     *
     * @param savedInstanceState    Bundle, handled by Android
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Required Android override. Contains the functions that pertain to the behaviour of the
     *  screen.
     *
     * <p>
     *     It creates a new DigitalizedMusic object and allows the users to select an image from the
     * Gallery or take a picture with their Camera.
     * </p>
     *
     * @param inflater              xml, defines the layout of the fragment
     * @param container             Container object, the Container in which this fragment will be
     *                                  displayed
     * @param savedInstanceState    Bundle, handled by Android
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.digmusic_fragment_one, container, false);
        mInflater = inflater;
        mView = container;

        musicProject = new DigitalizedMusic();
        //musicProject.setMusicImage(BitmapFactory.decodeResource(getResources(), R.drawable.test));
        //saveToDB();
        /*MusicDatabaseManager dbManager = new MusicDatabaseManager(getActivity());
        dbManager.insertNewProject(musicProject);
        dbManager.closeDatabase();*/
        //Log.d("ARUNS_DEBUG", dbManager.getExistingProjects().toString());

        Button getGalleryBtn = (Button) view.findViewById(R.id.getPicture);
        getGalleryBtn.setOnClickListener(new View.OnClickListener() {
            /**
             * Button click override that opens the Gallery and allows users to select a music sheet
             *      photo.
             *
             * @param v View, handled by Android
             */
            @Override
            public void onClick(View v) {
                Intent galleryImage = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                galleryImage.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
                galleryImage.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                galleryImage.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                startActivityForResult(galleryImage, RESULT_LOAD_IMAGE);
                namingMusic();
            }
        });
        // Inflate the layout for this fragment
        return view;
    }

    /**
     * Handles obtained image after user selects one from the Gallery.
     *
     * @param requestCode   int, handled by Android
     * @param resultCode    int, handled by Android
     * @param data          Intent, handled by Android
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getActivity().getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            Bitmap imageBitmap = BitmapFactory.decodeFile(picturePath);
            if (musicProject.setMusicImage(imageBitmap)==1)
                Toast.makeText(getActivity(), "Image Analysis Successful!", Toast.LENGTH_LONG)
                        .show();
            else
                Toast.makeText(getActivity(), "Image Analysis Unsucessful!", Toast.LENGTH_LONG)
                        .show();
        }
    }

    /**
     * This method displays the layout and takes input to rename the DigitalizedMusic object. It
     * checks whether the name is unique by checking the database and saves the object to the
     * database. Then, displays a pop-up message depending on the status of the check.
     *
     * If the name was unique and the operation was successful, the new object is added to the
     * database.
     */
    public void namingMusic()
    {
        mView.removeAllViews();
        View view = mInflater.inflate(R.layout.fragment_rename_music, mView, false);
        mView.addView(view);

        /* Find button to rename music */
        Button renameBtn = (Button) view.findViewById(R.id.renameMusicBtn);

        renameBtn.setOnClickListener(new View.OnClickListener()
        {
            /**
             * Checks whether the name inputted by the user is unique by verifying that the name
             * does not exist in the database, using linear search.
             */
            public void onClick(View view)
            {
                Log.d("ARUNS_DEBUG", "Successfully registered a click!");
               EditText mEdit = (EditText) mView.findViewById(R.id.editText);
               String prospectiveName = mEdit.getText().toString();
               MusicDatabaseManager db = new MusicDatabaseManager(getActivity());
               ArrayList<String> listOfExistingProjects = new ArrayList<String>
                       (Arrays.asList(db.getExistingProjectNames()));
               if(SortingAndSearching.linearSearchName(prospectiveName, listOfExistingProjects)
                       == -1)
               {
                   musicProject.changeFileName(prospectiveName);
                   Toast.makeText(getActivity(), "Successfully Named", Toast.LENGTH_LONG)
                           .show();
                   db.insertNewProject(musicProject);
                   db.closeDatabase();
                   PlaybackMusicProject playbackScreen = PlaybackMusicProject.newInstance(
                           musicProject.getNotes());
                   FragmentTransaction fm = getActivity().getSupportFragmentManager().beginTransaction();
                   fm.replace(R.id.fragment_container, playbackScreen);
                   fm.commit();
               } else
               {
                   Toast.makeText(getActivity(), "File with that name already exists!",
                           Toast.LENGTH_LONG).show();
               }


            }
        });
    }
}