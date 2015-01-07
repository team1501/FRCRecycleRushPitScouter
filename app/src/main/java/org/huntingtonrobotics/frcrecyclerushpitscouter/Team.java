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

    public void setID(UUID mID) {
        this.mID = mID;
    }
    //---Getter and Setter for mID


    //Setter for mTeamNum
    public void setTeamNum(int mTeamNum) {
        this.mTeamNum = mTeamNum;
    }
    //---Setter for mTeamNum


}
