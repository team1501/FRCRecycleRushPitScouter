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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

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
    private EditText mAutoProgs;
    private CheckBox mAutoMove;
    private EditText mAutoTotes;
    private EditText mAutoContainers;
    private EditText mAutoHeight;
    private CheckBox mAutoMoveTote;
    private RadioGroup mAutoPos;

    //teleop


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
        final View v = inflater.inflate(R.layout.fragment_team, parent, false);

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

        //photo view
        mPhotoView = (ImageView)v.findViewById(R.id.team_imageView);
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

        mAutoProgs = (EditText)v.findViewById(R.id.auto_programs);
        mAutoProgs.setText(""+mTeam.getAutoProgs());
        mAutoProgs.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //left blank
            }

            //user changes text of auto progs
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                //use try catch in case nothing is in text view
                try {
                    //saves text after converting CS to integer
                    mTeam.setAutoProgs(Integer.parseInt(s.toString()));
                } catch (Exception e) {
                    //exception is thrown so setAutoProgs to 0 so program can carry on
                    Log.d(TAG, "ERROR: " + e);
                    mTeam.setAutoProgs(0);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                //also left blank
            }
        });


        mAutoMove = (CheckBox)v.findViewById(R.id.auto_move);
        mAutoMove.setChecked(mTeam.isAutoMove());
        mAutoMove.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mTeam.setAutoMove(true);
                } else if (!isChecked) {
                    mTeam.setAutoMove(false);
                } else {

                }
            }
        });


        mAutoTotes = (EditText)v.findViewById(R.id.auto_totes);
        //set filter to only allow numbers 0-3
        mAutoTotes.setFilters(new InputFilter[]{new InputFilterMinMax(0, 3, getActivity().getApplicationContext())});
        mAutoTotes.setText(""+mTeam.getAutoTotes());
        mAutoTotes.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //left blank
            }

            //user changes text of auto progs
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                //use try catch in case nothing is in text view
                try {
                    //saves text after converting CS to integer
                     mTeam.setAutoTotes(Integer.parseInt(s.toString()));
                } catch (Exception e) {
                    //exception is thrown so setAutoTotes to 0 so program can carry on
                    Log.d(TAG, "ERROR: " + e);
                    mTeam.setAutoTotes(0);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                //also left blank
            }
        });


        mAutoContainers = (EditText)v.findViewById(R.id.auto_containers);
        //set filter to only allow numbers 0-3
        mAutoContainers.setFilters(new InputFilter[]{ new InputFilterMinMax(0, 3,getActivity().getApplicationContext())});
        mAutoContainers.setText(""+mTeam.getAutoContainers());
        mAutoContainers.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //left blank
            }

            //user changes text of auto progs
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                //use try catch in case nothing is in text view
                try {
                    //saves text after converting CS to integer
                    mTeam.setAutoContainers(Integer.parseInt(s.toString()));
                } catch (Exception e) {
                    //exception is thrown so setAutoContainers to 0 so program can carry on
                    Log.d(TAG, "ERROR: " + e);
                    mTeam.setAutoContainers(0);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                //also left blank
            }
        });

        /*deleted from auto
        mAutoHeight = (EditText)v.findViewById(R.id.auto_tote_height);
        //set filter to only allow numbers 0-6
        mAutoHeight.setFilters(new InputFilter[]{ new InputFilterMinMax(0, 6,getActivity().getApplicationContext())});
        mAutoHeight.setText(""+mTeam.getAutoHeight());
        mAutoHeight.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //left blank
            }

            //user changes text of auto height
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                //use try catch in case nothing is in text view
                try {
                    //saves text after converting CS to integer
                    mTeam.setAutoHeight(Integer.parseInt(s.toString()));
                } catch (Exception e) {
                    //exception is thrown so setAutoHeight to 0 so program can carry on
                    Log.d(TAG, "ERROR: " + e);
                    mTeam.setAutoHeight(0);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                //also left blank
            }
        });
        */


        mAutoMoveTote = (CheckBox)v.findViewById(R.id.auto_move_tote_stack);
        mAutoMoveTote.setChecked(mTeam.isAutoMoveTote());
        mAutoMoveTote.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                if(isChecked){
                    mTeam.setAutoMoveTote(true);
                }else if (!isChecked){
                    mTeam.setAutoMoveTote(false);
                }else{

                }
            }
        });


        mAutoPos = (RadioGroup)v.findViewById(R.id.startPosRadioGroup);
        int ap = mTeam.getAutoPos();
        switch (ap) {
            case 1:
                mAutoPos.check(R.id.autoStartPos1);
                break;
            case 2:
                mAutoPos.check(R.id.autoStartPos2);
                break;
            case 3:
                mAutoPos.check(R.id.autoStartPos3);
                break;
            case 0:
                mAutoPos.check(R.id.autoStartPosA);
                break;
        }
        mAutoPos.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // find which radio button is selected

                switch (checkedId) {
                    case R.id.autoStartPos1:
                        mTeam.setAutoPos(1);
                        break;
                    case R.id.autoStartPos2:
                        mTeam.setAutoPos(2);
                        break;
                    case R.id.autoStartPos3:
                        mTeam.setAutoPos(3);
                        break;
                    case R.id.autoStartPosA:
                        mTeam.setAutoPos(0);
                        break;

                }
            }

        });




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
