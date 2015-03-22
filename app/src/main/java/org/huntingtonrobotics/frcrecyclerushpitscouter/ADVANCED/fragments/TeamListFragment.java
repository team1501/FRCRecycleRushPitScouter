package org.huntingtonrobotics.frcrecyclerushpitscouter.ADVANCED.fragments;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.huntingtonrobotics.frcrecyclerushpitscouter.ADVANCED.TeamLab;
import org.huntingtonrobotics.frcrecyclerushpitscouter.ADVANCED.activities.TeamPagerActivity;
import org.huntingtonrobotics.frcrecyclerushpitscouter.ADVANCED.activities.SendActivity;
import org.huntingtonrobotics.frcrecyclerushpitscouter.ADVANCED.robotPhoto.Photo;
import org.huntingtonrobotics.frcrecyclerushpitscouter.ADVANCED.robotPhoto.PictureUtils;
import org.huntingtonrobotics.frcrecyclerushpitscouter.R;
import org.huntingtonrobotics.frcrecyclerushpitscouter.MODIFY.Team;
import org.huntingtonrobotics.frcrecyclerushpitscouter.MODIFY.TeamFragment;

import java.util.ArrayList;

/**
 * Created by 2015H_000 on 1/6/2015.
 */
public class TeamListFragment extends ListFragment {
    private static final String TAG = "TeamListFragment";
    Team mTeam;
    ImageView imageView;
    private ArrayList<Team> mTeams;

    //bubble sort teams
    public static void bubbleSort(ArrayList<Team> list) {
        Team team;
        if (list.size() > 1) // check if the number of teams is larger than 1
        {
            for (int x = 0; x < list.size(); x++) // bubble sort outer loop
            {

                for (int i = 0; i < list.size() - x - 1; i++) {
                    if (list.get(i).compareTo(list.get(i + 1)) > 0) {
                        team = list.get(i);
                        list.set(i, list.get(i + 1));
                        list.set(i + 1, team);
                    }
                }
            }
        }

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);    //to tell fm to recieve a call for onCreateOptionsMenu()

        getActivity().setTitle(R.string.teams_title);
        loadListTeams();
        setRetainInstance(true);
    }

    @TargetApi(11)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, parent, savedInstanceState);

        ListView listView = (ListView) v.findViewById(android.R.id.list);

        //set choice mode
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            //use floating context menus on Froyo and Gingerbread
            registerForContextMenu(listView);
        } else {
            //use contextual action bar on Honeycomb and above
            listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);

            //Multichoicelistener for deleting teams

            listView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
                @Override
                public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
                    //not used
                }

                //ActionMode.Callback methods
                @Override
                public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                    MenuInflater inflater = mode.getMenuInflater();
                    inflater.inflate(R.menu.team_list_item_context, menu);
                    return true;
                }

                @Override
                public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                    return false;
                    //not used
                }

                @Override
                public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.menu_item_delete_team:
                            TeamAdapter adapter = (TeamAdapter) getListAdapter();
                            TeamLab teamLab = TeamLab.get(getActivity());
                            for (int i = adapter.getCount() - 1; i >= 0; i--) {
                                if (getListView().isItemChecked(i)) {
                                    teamLab.deleteTeam(adapter.getItem(i));
                                }
                            }
                            mode.finish();
                            adapter.notifyDataSetChanged();
                            teamLab.saveTeams();
                            return true;

                        default:
                            return false;
                    }
                }
                @Override
                public void onDestroyActionMode(ActionMode mode) {
                    //not used
                }
            });
        }

        return v;
    }

    //team from teamlist is clicked
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        //use team adapter
        Team t = ((TeamAdapter) getListAdapter()).getItem(position);
        //log to tell which team was clicked
        Log.d(TAG, "Team #" + t.getTeamNum() + " was clicked.");
        //Start TeamPagerActivity with this team
        Intent i = new Intent(getActivity(), TeamPagerActivity.class);
        //get the team id and put it in an extra
        i.putExtra(TeamFragment.EXTRA_TEAM_ID, t.getID());
        startActivity(i);
    }

    private void loadListTeams() {
        mTeams = TeamLab.get(getActivity()).getTeams();

        bubbleSort(mTeams);
        TeamAdapter adapter = new TeamAdapter(mTeams);
        setListAdapter(adapter);
    }

    //Reload team list onResume
    @Override
    public void onResume() {
        super.onResume();
        ((TeamAdapter) getListAdapter()).notifyDataSetChanged();
        loadListTeams();
    }
    //---Reload team list onResume

    //inflate the options menu
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_team_list, menu);
    }
    //---inflate the options menu

    //respond to menu selection
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
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
                Intent i2 = new Intent(getActivity(), SendActivity.class);
                startActivity(i2);
                return true;
            case R.id.menu_item_info:
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.rrpitscout.wordpress.com"));
                startActivity(browserIntent);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //inflate the menu resource and use it to populate the context menu
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getActivity().getMenuInflater().inflate(R.menu.team_list_item_context, menu);
    }

    //Listen for context menu item selection
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int position = info.position;
        TeamAdapter adapter = (TeamAdapter) getListAdapter();
        Team team = adapter.getItem(position);

        switch (item.getItemId()) {
            case R.id.menu_item_delete_team:

        }

        return super.onContextItemSelected(item);
    }

    //load picture
    //shows the photo
    private void showPhoto(Team team) {
        //(Re)set the image buttons image based on your photo

        Photo p = team.getPhoto();
        BitmapDrawable b = null;
        if (p != null) {
            String path = getActivity().getFileStreamPath(p.getFileName()).getAbsolutePath();
            b = PictureUtils.getScaledDrawable(getActivity(), path);
        }
        imageView.setImageDrawable(b);
    }

    //Load the image
    @Override
    public void onStart() {
        super.onStart();
        loadListTeams();
    }
    //---shows the photo in photoview

    //unload the image
    @Override
    public void onStop() {
        super.onStop();
        try {
            PictureUtils.cleanImageView(imageView);
        } catch (Exception e) {

        }
    }
    //---Load the image

    //hooks up dataset of crimes
    private class TeamAdapter extends ArrayAdapter<Team> {

        public TeamAdapter(ArrayList<Team> teams) {
            super(getActivity(), 0, teams);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //inflate a view if none is given
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater().inflate(R.layout.list_item_team, null);
            }

            //Configure the view for the team
            Team t = getItem(position);
            //for the list view
            TextView titleTextView = (TextView) convertView.findViewById(R.id.team_list_item_titleTextView);
            titleTextView.setText("" + t.getTeamNum());



            //TODO add views to list items here

            return convertView;
        }
    }
    //---unload the image
}



