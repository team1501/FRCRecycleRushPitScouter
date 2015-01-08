package org.huntingtonrobotics.frcrecyclerushpitscouter;

import android.annotation.TargetApi;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.util.UUID;

/**
 * Created by 2015H_000 on 1/6/2015.
 */
public class TeamFragment extends Fragment {
    private static final String TAG = "TeamListFragment";
    public static final String EXTRA_TEAM_ID = "org.huntingtonrobotics.frcrecyclerush.team_id";

    private Team mTeam;
    private EditText mTeamNum;

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
                     }catch (Exception e) {
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

        //PUT AUTO VIEWS HERE

        //PUT COOPERTITION VIEWS HERE

        //PUT TELEOP VIEWS HERE

        return v;
    }
    //---inflates the view

    //attach extra argument to team fragment
    public static TeamFragment newInstance(UUID teamId){
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_TEAM_ID, teamId);

        TeamFragment fragment = new TeamFragment();
        fragment.setArguments(args);

        return fragment;
    }
    //---attach extra argument to team fragment


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
