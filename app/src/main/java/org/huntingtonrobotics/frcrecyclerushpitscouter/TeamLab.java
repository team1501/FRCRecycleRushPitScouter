package org.huntingtonrobotics.frcrecyclerushpitscouter;


import android.content.Context;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by 2015H_000 on 1/6/2015.
 * Singleton TeamLab only allows one instance of itself to be created
 * TeamLab stores team list to keep team data no matter what happens with act frags and their lifecycles
 */
public class TeamLab {
    private ArrayList<Team> mTeams;

    private static TeamLab sTeamLab;    //'s'TeamLab is static
    private Context mAppContext;

    private TeamLab(Context appContext){
        mAppContext = appContext;
        mTeams = new ArrayList<Team>();

        //Generate a list of random teams for now
        for (int i = 0; i <100; i++){
            Team t = new Team();
            t.setTeamNum(i);
            mTeams.add(t);
        }

    }

    public static TeamLab get(Context c){
        if (sTeamLab == null){
            sTeamLab = new TeamLab(c.getApplicationContext());
        }
        return sTeamLab;
    }

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

}
