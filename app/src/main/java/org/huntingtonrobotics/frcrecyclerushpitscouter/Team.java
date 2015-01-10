package org.huntingtonrobotics.frcrecyclerushpitscouter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.UUID;


/**
 * Created by 2015H_000 on 1/5/2015.
 */
public class Team {

    private static final String JSON_ID = "id";
    private static final String JSON_TEAM_NUM = "teamNum";
    private static final String JSON_PHOTO = "photo";

    //include other things to save here


    private UUID mID;
    private int mTeamNum;
    private Photo mPhoto;




    //Constructor for Team
    public Team(){
        //generate unique identifier
        mID = UUID.randomUUID();
    }
    //---Constructor for Team

    //constructor that accepts JSON object
    public Team(JSONObject json) throws JSONException{
        mID = UUID.fromString(json.getString(JSON_ID));
        if (json.has(JSON_TEAM_NUM)){
            mTeamNum = json.getInt(JSON_TEAM_NUM);
        }else if (json.has(JSON_PHOTO)){
            mPhoto = new Photo(json.getJSONObject(JSON_PHOTO));
        }

    }
    //---constructor that accepts JSON object


    //puts teams to JSON format to be put in JSON file
    public JSONObject toJSON() throws JSONException{
        JSONObject json = new JSONObject();
        json.put(JSON_ID, mID.toString());
        json.put(JSON_TEAM_NUM, mTeamNum);
        if (mPhoto!=null){
            json.put(JSON_PHOTO, mPhoto.toJSON());
        }
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


    //Getter and Setter for mPhoto
    public Photo getPhoto() {
        return mPhoto;
    }

    public void setPhoto(Photo p) {
        this.mPhoto = p;
    }
    //---Getter and Setter for mPhoto
}
