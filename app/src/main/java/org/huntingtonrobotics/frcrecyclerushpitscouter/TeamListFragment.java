package org.huntingtonrobotics.frcrecyclerushpitscouter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by 2015H_000 on 1/6/2015.
 */
public class TeamListFragment extends ListFragment {
    private ArrayList<Team> mTeams;
    private static final String TAG = "TeamListFragment";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle(R.string.teams_title);
        mTeams = TeamLab.get(getActivity()).getTeams();

        TeamAdapter adapter = new TeamAdapter(mTeams);
        setListAdapter(adapter);
    }

    //team from teamlist is clicked
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        //use team adapter
        Team t = ((TeamAdapter)getListAdapter()).getItem(position);
        //log to tell which team was clicked
        Log.d(TAG, "Team #" + t.getTeamNum() + " was clicked.");

        //Start TeamActivity
        Intent i = new Intent(getActivity(), TeamActivity.class);
        startActivity(i);
    }

    //hooks up dataset of crimes
    private class TeamAdapter extends ArrayAdapter<Team>{

        public TeamAdapter(ArrayList<Team> teams){
            super(getActivity(), 0, teams);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            //inflate a view if none is given
            if(convertView == null){
                convertView = getActivity().getLayoutInflater().inflate(R.layout.list_item_team, null);
            }

            //Configure the view for the team
            Team t = getItem(position);

            TextView titleTextView = (TextView)convertView.findViewById(R.id.team_list_item_titleTextView);
            titleTextView.setText("Team #"+ t.getTeamNum());


            //TODO add views to list items here

            return convertView;
        }
    }
}