package org.huntingtonrobotics.frcrecyclerushpitscouter;

import java.util.UUID;


/**
 * Created by 2015H_000 on 1/5/2015.
 */
public class Team {

    private UUID mID;
    private int mTeamNum;




    //Constructor for Team
    public Team(){
        //generate unique identifier
        mID = UUID.randomUUID();
    }
    //---Constructor for Team

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
