package org.huntingtonrobotics.frcrecyclerushpitscouter.ADVANCED;


import android.content.Context;
import android.util.Log;

import org.huntingtonrobotics.frcrecyclerushpitscouter.MODIFY.Team;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by 2015H_000 on 1/6/2015.
 * Singleton TeamLab only allows one instance of itself to be created
 * TeamLab stores team list to keep team data no matter what happens with act frags and their lifecycles
 */
public class TeamLab {
    private static final String TAG = "TeamLab";
    private static final String FILENAME = "teams.json";

    private ArrayList<Team> mTeams;
    private FRCRecycleRushPitScouterJSONSerializer mSerializer;

    private static TeamLab sTeamLab;    //'s'TeamLab is static
    private Context mAppContext;

    private TeamLab(Context appContext){
        mAppContext = appContext;
        mTeams = new ArrayList<Team>();
        mSerializer = new FRCRecycleRushPitScouterJSONSerializer(mAppContext, FILENAME);

        //loads teams
        try{
            mTeams = mSerializer.loadTeams();
        }catch (Exception e){
            mTeams = new ArrayList<Team>();
            Log.e(TAG, "ERROR loading teams: ", e);
        }
    }

    public static TeamLab get(Context c){
        if (sTeamLab == null){
            sTeamLab = new TeamLab(c.getApplicationContext());
        }
        return sTeamLab;
    }

    //add team to teams
    public void addTeam(Team t){
        mTeams.add(t);
    }
    //---add team to teams

    //remove team from teams
    public void deleteTeam(Team t){
        mTeams.remove(t);
    }

    //save changes
    public boolean saveTeams(){
        try{
            mSerializer.saveTeams(mTeams);
            Log.d(TAG, "teams saved to file");
            return true;
        } catch (Exception e){
            Log.e(TAG, "ERROR saving teams: ", e);
            return false;
        }
    }
    //---save changes


    //getter for mTeams (returns an array of all the teams)
    public ArrayList<Team> getTeams(){
        return mTeams;
    }
    //---getter for mTeams

    //getter for team (returns a specific team)
    public Team getTeam(UUID id){
        for (Team t : mTeams){
            if (t.getID().equals(id))
                return t;
        }
        return null;
    }
    //---getter for team (returns a specific team)

    //getter for team (returns a specific team)
    public Team getTeamByNum(int num){
        for (Team t : mTeams){
            if (t.getTeamNum() == num) {
                return t;
            }
        }
        return null;
    }
    //---getter for team (returns a specific team)

}
