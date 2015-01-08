package org.huntingtonrobotics.frcrecyclerushpitscouter;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
        setHasOptionsMenu(true);    //to tell fm to recieve a call for onCreateOptionsMenu()

        getActivity().setTitle(R.string.teams_title);
        mTeams = TeamLab.get(getActivity()).getTeams();

        TeamAdapter adapter = new TeamAdapter(mTeams);
        setListAdapter(adapter);
        setRetainInstance(true);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState){
        View v = super.onCreateView(inflater, parent, savedInstanceState);

        ListView listView = (ListView)v.findViewById(android.R.id.list);
        registerForContextMenu(listView);

        return v;
    }


    //team from teamlist is clicked
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        //use team adapter
        Team t = ((TeamAdapter)getListAdapter()).getItem(position);
        //log to tell which team was clicked
        Log.d(TAG, "Team #" + t.getTeamNum() + " was clicked.");

        //Start TeamPagerActivity with this team
        Intent i = new Intent(getActivity(), TeamPagerActivity.class);
        //get the team id and put it in an extra
        i.putExtra(TeamFragment.EXTRA_TEAM_ID, t.getID());
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
            //for the list view
            TextView titleTextView = (TextView)convertView.findViewById(R.id.team_list_item_titleTextView);
            titleTextView.setText("Team #"+ t.getTeamNum());


            //TODO add views to list items here

            return convertView;
        }
    }

    //Reload team list onResume
    @Override
    public void onResume(){
        super.onResume();
        ((TeamAdapter)getListAdapter()).notifyDataSetChanged();
    }
    //---Reload team list onResume

    //inflate the options menu
    @Override
    public void onCreateOptionsMenu(Menu menu,MenuInflater inflater){
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_team_list, menu);
    }
    //---inflate the options menu

    //respond to menu selection
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.menu_item_new_team:
                Team team = new Team();
                TeamLab.get(getActivity()).addTeam(team);
                Intent i = new Intent(getActivity(), TeamPagerActivity.class);
                i.putExtra(TeamFragment.EXTRA_TEAM_ID, team.getID());
                startActivityForResult(i, 0);
                return true;
                //TODO set subtitle
            case R.id.menu_item_send_teams:
                //TODO open dialog
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}