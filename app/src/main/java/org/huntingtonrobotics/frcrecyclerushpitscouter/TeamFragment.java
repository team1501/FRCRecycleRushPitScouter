package org.huntingtonrobotics.frcrecyclerushpitscouter;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.Camera;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;

import java.util.UUID;

/**
 * Created by 2015H_000 on 1/6/2015.
 */
public class TeamFragment extends Fragment {
    private static final String TAG = "TeamListFragment";
    public static final String EXTRA_TEAM_ID = "org.huntingtonrobotics.frcrecyclerush.team_id";

    private static final int REQUEST_PHOTO =1;
    private static final String DIALOG_IMAGE = "image";

    private Team mTeam;
    private EditText mTeamNum;
    private ImageButton mPhotoButton;
    private ImageView mPhotoView;

    //auto
    private EditText mAutoTotes;
    private EditText mAutoContainers;

    private RadioButton mAutoStartPos1;
    private RadioButton mAutoStartPos2;
    private RadioButton mAutoStartPos3;
    private RadioButton mAutoStartPosA;


    //fragment is created
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        //retrieve the extra and fetch the team
        UUID teamId = (UUID)getArguments().getSerializable(EXTRA_TEAM_ID);
        mTeam = TeamLab.get(getActivity()).getTeam(teamId);

        setHasOptionsMenu(true);
    }
    //---fragment is created


    //inflates the view
    @TargetApi(11)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_team, parent, false);

        //turn on Up button

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){

            //no parent no carret
            if (NavUtils.getParentActivityName(getActivity())==null) {
                getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
            }
        }

        //PUT TEAM INFO VIEWS HERE

        //Team Number Edit Text
        mTeamNum = (EditText)v.findViewById(R.id.teamNum);
        mTeamNum.setText(""+mTeam.getTeamNum());
        mTeamNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //left blank
            }

            //user changes text
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                //use try catch in case nothing is in text view
                try {
                    //saves text after converting CS to integer
                    mTeam.setTeamNum(Integer.parseInt(s.toString()));
                } catch (Exception e) {
                    //exception is thrown so setTeamNum to 0 so program can carry on
                    Log.d(TAG, "ERROR: " + e);
                    mTeam.setTeamNum(0);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                //also left blank
            }
        });

        //photo button
        mPhotoButton = (ImageButton)v.findViewById(R.id.team_imageButton);
        mPhotoButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent i = new Intent(getActivity(), RobotCameraActivity.class);
                Log.d(TAG,"Photo button clicked");
                startActivityForResult(i, REQUEST_PHOTO);
            }
        });

        mPhotoView = (ImageView)v.findViewById(R.id.team_imageView);
        showPhoto();
        mPhotoView.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Photo p = mTeam.getPhoto();
                if(p==null){
                    return;
                }

                FragmentManager fm = getActivity().getSupportFragmentManager();
                String path = getActivity().getFileStreamPath(p.getFileName()).getAbsolutePath();
                ImageFragment.newInstance(path).show(fm, DIALOG_IMAGE);
            }
        });

        //if camera is not avilable, disable camera functionality
        PackageManager pm = getActivity().getPackageManager();
        boolean hasACamera = pm.hasSystemFeature(PackageManager.FEATURE_CAMERA) || pm.hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT)
                || (Build.VERSION.SDK_INT>= Build.VERSION_CODES.GINGERBREAD&& Camera.getNumberOfCameras() > 0);
        if(!hasACamera){
            mPhotoButton.setEnabled(false);
        }

        //PUT AUTO VIEWS HERE

        mAutoTotes = (EditText)v.findViewById(R.id.auto_totes);
        mAutoTotes.setFilters(new InputFilter[]{new InputFilterMinMax(0, 3, getActivity().getApplicationContext())});

        mAutoContainers = (EditText)v.findViewById(R.id.auto_containers);
        mAutoContainers.setFilters(new InputFilter[]{ new InputFilterMinMax(0, 3,getActivity().getApplicationContext())});




        //PUT COOPERTITION VIEWS HERE

        //PUT TELEOP VIEWS HERE


        return v;
    }
    //---inflates the view


    //shows the photo
    private void showPhoto(){
        //(Re)set the image buttons image based on your photo
        Photo p = mTeam.getPhoto();
        BitmapDrawable b = null;
        if (p != null){
            String path = getActivity().getFileStreamPath(p.getFileName()).getAbsolutePath();
            b = PictureUtils.getScaledDrawable(getActivity(), path);
        }
        mPhotoView.setImageDrawable(b);
    }
    //---shows the photo in photoview

    //Load the image
    @Override
    public void onStart(){
        super.onStart();
        showPhoto();
    }
    //---Load the image

    //unload the image
    @Override
    public void onStop(){
        super.onStop();
        PictureUtils.cleanImageView(mPhotoView);
    }
    //---unload the image

    //attach extra argument to team fragment
    public static TeamFragment newInstance(UUID teamId){
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_TEAM_ID, teamId);

        TeamFragment fragment = new TeamFragment();
        fragment.setArguments(args);

        return fragment;
    }
    //---attach extra argument to team fragment


    //retrieve files from extras
    @Override
    public void onActivityResult(int requestCode,int resultCode, Intent data){
        if (resultCode!= Activity.RESULT_OK) return;

        if(requestCode == REQUEST_PHOTO){
            //create a new Photo objet and attach it to the team
            String filename = data.getStringExtra(RobotCameraFragment.EXTRA_PHOTO_FILENAME);
            if(filename!=null){
                Log.i(TAG,"filename: " + filename);

                //handle a new photo
                Photo p = new Photo(filename);
                mTeam.setPhoto(p);
                showPhoto();
                Log.i(TAG, "Team: " + mTeam.getTeamNum() + "has a photo");
            }
        }
    }


    //saves teams to filesystem onPause()
    @Override
    public void onPause(){
        super.onPause();
        TeamLab.get(getActivity()).saveTeams();
    }
    //---saves teams to filesystem onPause()


   //Respond to menu items being clicked
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                if (NavUtils.getParentActivityName(getActivity())!=null){
                    NavUtils.navigateUpFromSameTask(getActivity());
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    //---Respond to menu items being clicked
}
