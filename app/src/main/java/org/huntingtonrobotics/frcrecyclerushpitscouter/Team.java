package org.huntingtonrobotics.frcrecyclerushpitscouter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.UUID;


/**
 * Created by 2015H_000 on 1/5/2015.
 */
public class Team {

    private static final String JSON_ID = "id";
    private static final String JSON_TEAM_NUM = "teamNum";

    //inclde other things to save here


    private UUID mID;
    private int mTeamNum;




    //Constructor for Team
    public Team(){
        //generate unique identifier
        mID = UUID.randomUUID();
    }
    //---Constructor for Team

    //puts teams to JSON format to be put in JSON file
    public JSONObject toJSON() throws JSONException{
        JSONObject json = new JSONObject();
        json.put(JSON_ID, mID.toString());
        json.put(JSON_TEAM_NUM, mTeamNum);
        return json;
    }
    //---puts teams to JSON format to be put in JSON file



    @Override
    public String toString() {
        return "Team #" + mTeamNum;
    }


    //Getter and Setter for mID
    public UUID getID() {
        return mID;
    }

    public void setID(UUID ID) {
        mID = ID;
    }
    //---Getter and Setter for mID


    //Getter and Setter for mTeamNum
    public int getTeamNum() {
        return mTeamNum;
    }

    public void setTeamNum(int teamNum) {
        mTeamNum = teamNum;
    }

    //---Getter and Setter for mTeamNum



}
