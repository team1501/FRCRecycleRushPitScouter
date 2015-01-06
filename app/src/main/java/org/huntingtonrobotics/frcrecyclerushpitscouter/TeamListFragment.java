package org.huntingtonrobotics.frcrecyclerushpitscouter;

import android.os.Bundle;
import android.support.v4.app.ListFragment;

import java.util.ArrayList;

/**
 * Created by 2015H_000 on 1/6/2015.
 */
public class TeamListFragment extends ListFragment{
    private ArrayList<Team> mTeams;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        getActivity().setTitle(R.string.teams_title);
        mTeams = TeamLab.get(getActivity()).getTeams();

    }


}
